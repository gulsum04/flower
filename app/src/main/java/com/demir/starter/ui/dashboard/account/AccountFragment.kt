package com.demir.starter.ui.dashboard.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.doOnPreDraw
import com.demir.starter.R
import com.demir.starter.data.UserProfile
import com.demir.starter.manager.FirebaseAuthManager
import com.demir.starter.manager.FirestoreManager
import com.demir.starter.ui.BaseFragment
import com.demir.starter.ui.authorization.LoginActivity
import com.google.android.material.textfield.TextInputLayout
import kotlin.math.sqrt


class AccountFragment : BaseFragment() {
    private val userProfile get() = FirebaseAuthManager.currentUser
    private val userId get() = userProfile?.email

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_account, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateUIWithUserProfile()

        // Çıkış yapma butonuna tıklama olayı
        view.findViewById<View>(R.id.buttonLogout).setOnClickListener {
            FirebaseAuthManager.logout()
            showMessage("Çıkış yapıldı!")

            navigateToLogin()
        }


        val emailInput = view.findViewById<TextInputLayout>(R.id.inputLayoutEmail)
        val username = view.findViewById<TextInputLayout>(R.id.inputLayoutUsername)
        val phone = view.findViewById<TextInputLayout>(R.id.inputLayoutPhone)
        emailInput.isEnabled = false
        username.isEnabled = false
        phone.isEnabled = false

        view.findViewById<View>(R.id.buttonSaveProfile).setOnClickListener {
            it as Button
            if (it.text == "Güncelle") {
                username.isEnabled = true
                phone.isEnabled = true
                it.text = "Kaydet"
            } else {
                username.isEnabled = false
                phone.isEnabled = false
                it.text = "Güncelle"


                val updatedProfile = UserProfile().apply {
                    this.email = FirebaseAuthManager.currentUser!!.email
                    this.fullName = username.editText!!.text.toString()
                    this.phoneNumber = phone.editText!!.text.toString()
                }
                FirebaseAuthManager.currentUser = updatedProfile
                saveUserProfileToFirestore()
            }
        }

        // Çıkış yapma butonuna tıklama olayı
        view.findViewById<View>(R.id.buttonLogout).setOnClickListener {
            FirebaseAuthManager.logout()
            showMessage("Çıkış yapıldı!")

            navigateToLogin()
        }


        val cardView = view.findViewById<View>(R.id.cardView)
        cardView.doOnPreDraw {
            val radius =
                ((cardView.width.toFloat() * cardView.width.toFloat()) +
                        (cardView.height.toFloat() * cardView.height.toFloat())).let {
                    sqrt(it)
                }

            ViewAnimationUtils.createCircularReveal(
                cardView,
                0,
                0,
                0f,
                radius
            ).apply {
                duration = 1000
            }.start()
        }

    }


    // UI'yı kullanıcı profili verileri ile güncelleme
    private fun updateUIWithUserProfile() {
        view?.findViewById<TextInputLayout>(R.id.inputLayoutEmail)?.editText!!.setText(userProfile?.email)
        view?.findViewById<TextInputLayout>(R.id.inputLayoutUsername)?.editText!!.setText(
            userProfile?.fullName
        )
        view?.findViewById<TextInputLayout>(R.id.inputLayoutPhone)?.editText!!.setText(userProfile?.phoneNumber)
    }

    // Kullanıcı profili verilerini Firestore'a kaydetme
    private fun saveUserProfileToFirestore() {
        FirestoreManager.updateUserProfile(userId!!, userProfile!!)
    }

    // Giriş ekranına yönlendirme
    private fun navigateToLogin() {
        val intent = Intent(requireContext(), LoginActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
    }
}