package com.darkfuturestudios.martincostamyfitnesspalproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.darkfuturestudios.martincostamyfitnesspalproject.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    private lateinit var networkManager: NetworkManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }

    override fun onResume() {
        super.onResume()

        networkManager = NetworkManager.newInstance(this)
    }

    override fun onStop() {
        super.onStop()

        networkManager.shutdown()
    }
}
