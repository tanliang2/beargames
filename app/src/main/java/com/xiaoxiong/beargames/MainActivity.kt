package com.xiaoxiong.beargames

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xiaoxiong.beargames.util.AppManager

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppManager.addActivity(this)
        BaseWebActivity.startWebActivity(this,GAME_HOME_URL)
    }

    companion object {
        val GAME_HOME_URL = "http://121.37.3.69"
    }
}