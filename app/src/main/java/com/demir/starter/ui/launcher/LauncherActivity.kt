package com.demir.starter.ui.launcher

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.demir.starter.R
import com.demir.starter.ui.authorization.LoginActivity
import com.demir.starter.ui.authorization.RegisterActivity


class LauncherActivity : AppCompatActivity() {
    private val buttonLogin get() = findViewById<Button>(R.id.buttonLogin)
    private val buttonRegister get() = findViewById<Button>(R.id.buttonRegister)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_launcher)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        buttonLogin.setOnClickListener {
            navigateToLogin()
        }
        buttonRegister.setOnClickListener {
            navigateToRegister()
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}
