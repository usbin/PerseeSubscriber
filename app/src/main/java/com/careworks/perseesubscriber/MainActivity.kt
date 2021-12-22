package com.careworks.perseesubscriber

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.careworks.perseesubscriber.databinding.ActivityMainBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding;
    private lateinit var database : FirebaseDatabase;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkPermission()
        binding.btnSetting.setOnClickListener {
            startActivity(Intent(this, SettingActivity::class.java))
        }
        binding.btnMainUsercodeConfirm.setOnClickListener{
            val userCode = binding.tvMainUsercode.text;
            database = Firebase.database("https://persee-63395-default-rtdb.firebaseio.com/");
            //user table 전부 읽어서, value가 userCode인 게 있다면 subscribe에 등록.
//            val userRef = database.getReference("user");
            database.getReference("user").get().let{
                it.addOnSuccessListener { dataSnapshot ->
                    for(user in dataSnapshot.children){
                        if(userCode.toString() == user.value.toString()){
                            //shared preference에 있으면 그거, 없으면 새로 생성

                            val myCode = getMyCode();
                            writeSubscribe(userCode.toString(), myCode);
                            startActivity(Intent(this, SecondActivity::class.java))

                        }
                    }

                }
                it.addOnFailureListener { exception ->
                    Log.d("TEST", "db user 읽기 실패 $exception")
                    Toast.makeText(this, "네트워크 설정을 확인해주세요", Toast.LENGTH_SHORT)
                    finish()
                }

                //없다면 alert("없는 usercode")
                Log.d("TEST", "button 클릭됨!")
            }
        }
    }
    private fun writeSubscribe(userCode: String, myCode: String){
        val subscribeRef = database.getReference("subscribe")
        subscribeRef.child(userCode).child(myCode).setValue(true);
    }
    fun getRandomString(length: Int) : String {
        val charset = ('A'..'Z');

        return List(length) { charset.random() }
            .joinToString("")
    }
    private fun getMyCode(): String{
        val sharedPreference = getPreferences(0)
        var myCode = sharedPreference.getString("my_code", "")
        Log.d("TEST", "myCode : $myCode")
        if(myCode!!.isEmpty()){
            myCode = getRandomString(6)
            sharedPreference.edit().putString("my_code", myCode).apply()
        }
        return myCode
    }
    private fun checkPermission(){
        //권한 체크
        val arrayPermission = arrayListOf<String>(
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.ACCESS_WIFI_STATE)
        val arrayNotGranted = arrayListOf<String>()
        for(permission in arrayPermission){
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                arrayNotGranted.add(permission);
            }
        }
        //하나라도 허용되지 않은 권한이 있으면 리스트를 넘겨 요청
        if(arrayNotGranted.size > 0){
            ActivityCompat.requestPermissions(this, arrayNotGranted.toTypedArray(),
                PERMISSION_REQUEST_CODE
            )
        }
        else{
            //허용된 상태일 때의 동작
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            //요청을 거부했을 때
            PERMISSION_REQUEST_CODE ->{
                for(index in grantResults.indices){
                    grantResults[index].takeIf{ it == PackageManager.PERMISSION_GRANTED}?.apply{
                        //권한 허용 눌렀을 때의 동작
                    }
                    grantResults[index].takeIf{ it == PackageManager.PERMISSION_DENIED}?.apply{
                        //권한 거부 눌렀을 때의 동작
                        Log.d("TEST", "권한 거부됨. 종료함.")
                        //finish()
                    }
                }
            }
        }
    }
    companion object{
        const val PERMISSION_REQUEST_CODE = 100;
    }
}