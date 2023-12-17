package com.demir.starter.ui.authorization

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatImageButton
import com.demir.starter.R
import com.demir.starter.manager.FirebaseAuthManager
import com.demir.starter.ui.BaseActivity
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textfield.TextInputLayout


class RegisterActivity : BaseActivity() {
    private lateinit var oneTapClient: SignInClient
    private lateinit var signUpRequest: BeginSignInRequest
    private lateinit var activityResultLauncher: ActivityResultLauncher<IntentSenderRequest>

    private val inputLayoutEmail get() = findViewById<TextInputLayout>(R.id.inputLayoutEmail)
    private val inputLayoutPassword get() = findViewById<TextInputLayout>(R.id.inputLayoutPassword)
    private val buttonStartRegister get() = findViewById<Button>(R.id.buttonStartRegister)
    private val imageButton get() = findViewById<AppCompatImageButton>(R.id.imageButton)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register)

        buttonStartRegister.setOnClickListener {
            startRegister()
        }

        imageButton.setOnClickListener {
            signInUsingGoogle()
        }

        oneTapClient = Identity.getSignInClient(this)
        signUpRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(getString(R.string.web_client_id))
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .build()

        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        )
        { result: ActivityResult? ->
            if (result?.resultCode == Activity.RESULT_OK) {
                try {
                    val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
                    val idToken = credential.googleIdToken
                    when {
                        idToken != null -> {
                            val email = credential.id
                            inputLayoutEmail.editText!!.setText(email)
                            showMessage("Şifrenizi girip devam edebilirsiniz.")
                        }
                    }
                } catch (e: ApiException) {
                    showMessage(e.message)
                }
            }
        }
    }

    private fun startRegister() {
        val email = inputLayoutEmail.editText!!.text
        if (email.isNullOrBlank()) {
            inputLayoutEmail.error = getString(R.string.message_empty_input_warning)
            return
        }
        val password = inputLayoutPassword.editText!!.text
        if (password.isNullOrBlank()) {
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
        FirebaseAuthManager.registerUser(email.toString(), password.toString()) { e ->
            if (e != null) {
                showMessage(e.message.toString())
            } else {
                showMessage(getString(R.string.message_user_registered))
                navigateToLogin()
            }
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
    }


    private fun signInUsingGoogle() {
        oneTapClient.beginSignIn(signUpRequest)
            .addOnSuccessListener(this@RegisterActivity) { result ->
                val intentSenderRequest =
                    IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                activityResultLauncher.launch(intentSenderRequest)
            }
            .addOnFailureListener(this@RegisterActivity) {
                showMessage(it.message.toString())
            }
    }
}
