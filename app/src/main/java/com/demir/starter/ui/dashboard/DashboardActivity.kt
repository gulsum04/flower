package com.demir.starter.ui.dashboard

import android.os.Bundle
import androidx.annotation.IdRes
import com.demir.starter.R
import com.demir.starter.data.Basket
import com.demir.starter.data.Product
import com.demir.starter.ui.BaseActivity
import com.demir.starter.ui.dashboard.account.AccountFragment
import com.demir.starter.ui.dashboard.basket.BasketFragment
import com.demir.starter.ui.dashboard.home.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class DashboardActivity : BaseActivity() {

    private val bottomNavigationView get() = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

    private val homeFragment by lazy { HomeFragment() }
    private val basketFragment by lazy { BasketFragment() }
    private val accountFragment by lazy { AccountFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_dashboard)

        bottomNavigationView.setOnItemSelectedListener { item ->
            showDashboardPage(item.itemId)
            true
        }
        showDashboardPage(R.id.home)
    }

    private fun showDashboardPage(@IdRes menuId: Int) {
        val targetFragment = when (menuId) {
            R.id.home -> homeFragment
            R.id.basket -> basketFragment
            R.id.account -> accountFragment
            else -> throw IllegalArgumentException("Page is not supported!")
        }

        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .setCustomAnimations(
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
            .replace(R.id.frameLayout, targetFragment)
            .commitNowAllowingStateLoss()
    }

    fun addToBasket(product: Product) {
        Basket.addToBasket(product)
        bottomNavigationView.getOrCreateBadge(R.id.basket).number = Basket.addedProducts.size

        showMessage(
            product.label + " sepete eklendi!",
        )
        val basketFragment =
            supportFragmentManager.findFragmentByTag(BasketFragment::class.java.simpleName) as? BasketFragment
        basketFragment?.listBasket()
    }

    fun removeFromBasket(product: Product) {
        Basket.removeFromBasket(product)
        bottomNavigationView.getOrCreateBadge(R.id.basket).number = Basket.addedProducts.size

        showMessage(product.label + " sepetten çıkarıldı!")

        val basketFragment =
            supportFragmentManager.findFragmentByTag(BasketFragment::class.java.simpleName) as? BasketFragment
        basketFragment?.listBasket()
    }
}

