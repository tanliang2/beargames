package com.xiaoxiong.beargames

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        BaseWebActivity.startWebActivity(this,GAME_HOME_URL)
    }

    companion object {
        val GAME_HOME_URL = "http://121.37.3.69"
    }
}