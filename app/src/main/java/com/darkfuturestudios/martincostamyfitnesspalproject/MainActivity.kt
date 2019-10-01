package com.darkfuturestudios.martincostamyfitnesspalproject

import android.content.BroadcastReceiver
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.darkfuturestudios.martincostamyfitnesspalproject.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    private var networkManager: NetworkManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)

        networkManager = NetworkManager.newInstance(this)

        if (fragment is MainFragment) {
            fragment.setNetworkManager(networkManager)
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()

        networkManager?.shutdown()
    }
}
