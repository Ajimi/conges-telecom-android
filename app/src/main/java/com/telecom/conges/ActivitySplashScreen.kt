package com.telecom.conges

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rbddevs.splashy.Splashy
import com.telecom.conges.ui.login.LoginActivity
import com.telecom.conges.ui.main.MainActivity
import com.telecom.conges.ui.main.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ActivitySplashScreen : AppCompatActivity() {
    val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        setSplashy()
        Splashy.onComplete(object : Splashy.OnComplete {
            override fun onComplete() {
                if (mainViewModel.isLoggedIn()) {
                    startActivity(MainActivity.starterIntent(this@ActivitySplashScreen))
                } else {
                    startActivity(LoginActivity.starterIntent(this@ActivitySplashScreen))
                }
            }
        })
    }

    fun setSplashy() {
        Splashy(this)         // For JAVA : new Splashy(this)
            .setLogo(R.drawable.logo_tt)
            .setTitle("TUNISIE TELECOM")
            .setTitleColor("#FFFFFF")
            .setSubTitle("La vie est émotions")
            .setProgressColor(R.color.blue_100)
            .setBackgroundResource(R.color.white)
            .setFullScreen(true)
            .setAnimation(Splashy.Animation.GLOW_LOGO_TITLE)
            .setTime(5000)
            .show()
    }

}
