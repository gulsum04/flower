package com.demir.starter.ui.authorization

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.demir.starter.R
import com.demir.starter.manager.FirebaseAuthManager
import com.demir.starter.manager.FirestoreManager
import com.demir.starter.ui.BaseActivity
import com.demir.starter.ui.dashboard.DashboardActivity
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : BaseActivity() {

    private val inputLayoutUsername get() = findViewById<TextInputLayout>(R.id.inputLayoutUsername)
    private val inputLayoutPassword get() = findViewById<TextInputLayout>(R.id.inputLayoutPassword)
    private val buttonStartLogin get() = findViewById<Button>(R.id.buttonStartLogin)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        buttonStartLogin.setOnClickListener {
            startLogin()
        }
    }

    private fun startLogin() {
        val userName = inputLayoutUsername.editText!!.text.toString()
        if (userName.isBlank()) {
            inputLayoutUsername.error = getString(R.string.message_empty_input_warning)
            return
        }

        val password = inputLayoutPassword.editText!!.text.toString()
        if (password.isBlank()) {
            inputLayoutPassword.error = getString(R.string.message_empty_input_warning)
            return
        }
        if (password.length < 6) {
            inputLayoutPassword.error = getString(R.string.message_short_input_warning)
            return
        }
        if (password.contains(" ")) {
            inputLayoutPassword.error = getString(R.string.message_space_input_warning)
            return
        }
        loginUser(userName, password)
    }

    private fun loginUser(email: String, password: String) {
        FirebaseAuthManager.loginUser(email, password) { e ->
            if (e != null) {
                showMessage(e.message.toString())
            } else {
                showMessage("Giriş Başarılı!")

                AlertDialog.Builder(this@LoginActivity)
                    .setMessage("Giriş yapılıyor...")
                    .show()

                window.decorView.postDelayed({
                    navigateToDashboard()
                    finish()
                }, 1000)
            }
        }
    }

    private fun navigateToDashboard() {
        FirestoreManager.getFlowers {
            if (it.isNullOrEmpty()) {
                showMessage("Ürünler çekilemedi!")
            } else {
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
            }
        }
    }
}