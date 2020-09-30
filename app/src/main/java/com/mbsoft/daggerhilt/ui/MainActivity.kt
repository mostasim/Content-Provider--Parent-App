package com.mbsoft.daggerhilt.ui

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.mbsoft.daggerhilt.R
import com.mbsoft.daggerhilt.db.model.User
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random


class MainActivity : AppCompatActivity() {
    lateinit var email: String
    lateinit var password: String
    private val mainViewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btLogin.setOnClickListener {
            email = etEmail.text.toString()
            password = etPassword.text.toString()

            if (email.length > 0 && password.length > 0) {
                val user = User(Random.nextInt(), email, password)
                mainViewModel.insertUser(user)
                etEmail.text.clear()
                etPassword.text.clear()
                openOtherApp()
            }

            /*
            //Share data using deep linking
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("app://open.my.app?email=$email&password=$password")
            startActivity(intent)
            */


        }

        btOpenApp.setOnClickListener {
            openOtherApp()
        }
        mainViewModel.getUserData().observe(this, {
            textView.text = it.toString()
            Log.e("User", it.toString())
            if (it.isNotEmpty()) {
                btOpenApp.visibility = View.VISIBLE
            } else {
                btOpenApp.visibility = View.GONE
            }
        })
        btClear.setOnClickListener {
            mainViewModel.clearData()
        }

    }


    /*
    * open another app if its already launch then bring it backs to top and if its not then it will start fresh launch
    * */
    private fun openOtherApp() {
        val intent = Intent()
        intent.component =
            ComponentName("com.mbsoft.intentexample", "com.mbsoft.intentexample.MainActivity")
        intent.action = Intent.ACTION_MAIN
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

}