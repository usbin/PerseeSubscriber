package com.careworks.perseesubscriber

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.careworks.perseesubscriber.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySecondBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root);
//        binding.btnSetting.setOnClickListener {
//            startActivity(Intent(this, SettingActivity::class.java))
//        }
    }
}