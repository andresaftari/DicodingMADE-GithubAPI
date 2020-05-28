package com.example.lastprojectmade.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lastprojectmade.MainActivity
import com.example.lastprojectmade.R
import com.example.lastprojectmade.notification.NotificationReceiver
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {
    private lateinit var mainActivity: MainActivity
    private lateinit var alarmReceiver: NotificationReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        mainActivity = MainActivity()
        alarmReceiver = NotificationReceiver()

        val message = "Don't forget to return to this app :)"

        btn_onNotif.setOnClickListener {
            alarmReceiver.setRepeatingAlarm(this, message)
        }

        btn_offNotif.setOnClickListener {
            alarmReceiver.setCancel(this)
        }
    }
}
