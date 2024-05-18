package com.example.umc_thread

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.umc_thread.databinding.ActivityMainBinding
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding

    private var started = false // 시작 되었는지 체크
    private var paused = false // 일시정지
    private var startTime = 0L
    private var elapsedTime = 0L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val handler = Handler(mainLooper)

        // 시작버튼 클릭 (타이머 시작)
        binding.startButton.setOnClickListener {
            if (!started){
                started = true
                paused = false
                startTime = System.currentTimeMillis() - elapsedTime


                // 스레드 실행 (타이머를 위한 쓰레드)
                thread(start=true){
                    while (started){
                        if (!paused){

                            elapsedTime = System.currentTimeMillis() - startTime
                            val totalSeconds = elapsedTime / 1000
                            val milliseconds = (elapsedTime % 1000) / 10

                            val minute = String.format("%02d", totalSeconds / 60) // 분
                            val second = String.format("%02d", totalSeconds % 60) // 초
                            val millis = String.format("%02d", milliseconds) // 밀리초
                            handler.post{
                                binding.timerTextView.text = "$minute:$second:$millis"
                            }
                            // 10ms 지연
                            Thread.sleep(10)
                        }
                    }

                    // 00:00:00으로 타이머 초기화
                    handler.post{
                        binding.timerTextView.text = "00:00:00"
                    }
                }
            } else {
                paused = false
                startTime = System.currentTimeMillis() - elapsedTime
            }

            // pause 버튼을 보이도록 설정
            binding.pauseButton.visibility = android.view.View.VISIBLE
        }

        // 정지 버튼 클릭 시 타이머 일시정지
        binding.pauseButton.setOnClickListener {
            if (started) {
                paused = true
                // pause 버튼을 안 보이도록 설정
                binding.pauseButton.visibility = android.view.View.GONE
            }
        }

        // 종료 버튼 클릭 시 타이머 종료
        binding.endButton.setOnClickListener {
            started = false
            paused = false
            elapsedTime = 0L
            // pause 버튼을 안 보이도록 설정
            binding.pauseButton.visibility = android.view.View.GONE
        }
    }
}