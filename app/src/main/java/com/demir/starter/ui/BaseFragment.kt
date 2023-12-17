package com.demir.starter.ui

import android.widget.Toast
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {
    protected fun showMessage(message: CharSequence?) {
        message ?: return
        Toast.makeText(
            requireActivity(),
            message,
            Toast.LENGTH_SHORT
        ).show()
    }
}