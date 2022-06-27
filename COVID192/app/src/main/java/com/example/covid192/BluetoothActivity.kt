package com.example.covid192

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView

class BluetoothActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth)

        supportActionBar?.hide()

        val verify = 1
        val nextBluetooth: Button = findViewById(R.id.next_bluetooth)
        nextBluetooth.setOnClickListener {
            if(verify ==1){
                val intent = Intent(this@BluetoothActivity, AmbienteActivity::class.java)
                startActivity(intent)
            }else if(verify == 0){
                val window = PopupWindow(this)
                val view = layoutInflater.inflate(R.layout.activity_next_bluetooth, null)
                window.contentView = view
                Handler().postDelayed({
                    window.dismiss()
                }, 3000)
                window.showAsDropDown(nextBluetooth)
            }
        }

        val backBluetooth: ImageView = findViewById(R.id.back_bluetooth)
        backBluetooth.setOnClickListener {
            val intent = Intent(this@BluetoothActivity, AlertActivity::class.java)
            startActivity(intent)
        }

        val infoPopUp: ImageView = findViewById(R.id.info_bluetooth)
        infoPopUp.setOnClickListener {
            val window = PopupWindow(this)
            val view = layoutInflater.inflate(R.layout.activity_info_bluetooth, null)
            window.contentView = view
            Handler().postDelayed({
                window.dismiss()
            }, 5000)
            window.showAsDropDown(infoPopUp)
        }

        val btConected: TextView = findViewById(R.id.roboBt)
        btConected.setOnClickListener {
            val window = PopupWindow(this)
            val view = layoutInflater.inflate(R.layout.activity_connected, null)
            window.contentView = view
            Handler().postDelayed({
                window.dismiss()
            }, 2000)
            window.showAsDropDown(btConected)
        }
    }
}