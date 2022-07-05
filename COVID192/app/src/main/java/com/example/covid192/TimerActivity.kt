package com.example.covid192

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView

class TimerActivity : AppCompatActivity() {
    private var contando = false
    private var time = 60000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        supportActionBar?.hide()

        val backTimer: ImageView = findViewById(R.id.back_timer)
        backTimer.setOnClickListener {
            val intent = Intent(this@TimerActivity, AmbienteActivity::class.java)
            startActivity(intent)
        }

        val infoPopUp: ImageView = findViewById(R.id.info_timer)
        infoPopUp.setOnClickListener {
            val window = PopupWindow(this)
            val view = layoutInflater.inflate(R.layout.activity_info_timer, null)
            window.contentView = view
            Handler().postDelayed({
                window.dismiss()
            }, 5000)
            window.showAsDropDown(infoPopUp)
        }

        val start: Button = findViewById(R.id.startStopButton)
        start.setOnClickListener {
            if (contando == false) {
                start.text = "encerrar"
                val clock: TextView = findViewById(R.id.timeTV)
                clock.textSize = 70F
                val timer = object : CountDownTimer(time.toLong(), 1000) {
                    override fun onTick(i: Long) {
                        val seg = i / 1000
                        clock.text = "${seg}"
                    }

                    override fun onFinish() {
                        clock.textSize = 20F
                        clock.text = "Finalizado!"
                    }
                }

                contando = true
                timer.start()

            } else {
                val intent = Intent(this@TimerActivity, AlertActivity::class.java)
                startActivity(intent)
            }

        }

    }
}