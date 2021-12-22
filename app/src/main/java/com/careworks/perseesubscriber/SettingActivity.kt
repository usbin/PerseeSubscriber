package com.careworks.perseesubscriber

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.careworks.perseesubscriber.databinding.ActivitySettingBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SettingActivity: AppCompatActivity() {
    private lateinit var binding : ActivitySettingBinding;
    private lateinit var database : FirebaseDatabase;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Firebase.database("https://persee-63395-default-rtdb.firebaseio.com/");
        /* DB 초기화 버튼 */
        binding.btnInitDb.setOnClickListener {
            //DB의 subscribe, manual_setting을 삭제
            database.getReference("manual_setting").removeValue();
            database.getReference("subscribe").removeValue();
        }

        /* 넘어짐 완료 버튼 */
        binding.btnFinCheckFall.setOnClickListener {
            //db의 manual_setting 하위의 fall_checked를 true로 설정.
            database.getReference("manual_setting").child("fall_checked").setValue(true);
        }

    }
}