package com.telecom.conges.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.afollestad.materialdialogs.MaterialDialog
import com.esprit.core.extensions.observeUIState
import com.telecom.conges.ui.main.MainActivity
import com.telecom.conges.R
import com.telecom.conges.extensions.toast
import com.telecom.conges.util.DialogWidget
import com.telecom.conges.util.EventObserver
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    val loginViewModel: LoginViewModel by viewModel()
    val dialogWidget: DialogWidget = DialogWidget()
    lateinit var dialog: MaterialDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        dialog = dialogWidget.invoke(this, null, "Connection...", true)
        handleLogin()
        loginViewModel.navigateToMainAction.observe(this, EventObserver {
            if (it) {
                startActivity(MainActivity.starterIntent(this@LoginActivity))
            }
        })

        loginViewModel.dialogState.observe(this, EventObserver {
            if (it) {
                dialog.show()
            } else {
                dialog.hide()
            }
        })

        loginViewModel.uiState.observe(this, Observer {
            val uiModel = it ?: return@Observer
            observeUIState(uiModel, {}, {
                toast(it)
            }, {})
        })
    }

    private fun handleLogin() {
        signin1.setOnClickListener {
            loginViewModel.login(
                username = username.text.toString().trim(),
                password = password.text.toString().trim(),
//                hasNetwork = hasNetworkConnection()
                hasNetwork = true
            )
        }
    }
}
