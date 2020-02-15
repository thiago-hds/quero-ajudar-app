package com.br.queroajudar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn_main_register).setOnClickListener {
            startRegisterActivity()
        }
        findViewById<Button>(R.id.btn_main_login).setOnClickListener {
            startLoginActivity()
        }
    }

    private  fun startRegisterActivity(){
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    private fun startLoginActivity(){
        startActivity(Intent(this, LoginActivity::class.java))
    }
}
