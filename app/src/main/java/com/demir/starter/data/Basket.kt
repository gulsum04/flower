package com.demir.starter.data

import java.math.BigDecimal

object Basket {
    val addedProducts = hashMapOf<Product, Int>()

    val totalAmount: BigDecimal
        get() {
            return addedProducts.toList().sumOf {
                it.first.price * it.second
            }.let {
                BigDecimal.valueOf(it).apply {
                    setScale(2)
                }
            }
        }

    fun clear() {
        addedProducts.clear()
    }

    fun addToBasket(product: Product) {
        val existingItem = addedProducts.getOrPut(product) { 0 }
        addedProducts[product] = existingItem + 1
    }

    fun removeFromBasket(product: Product) {
        val existingItem = addedProducts.getOrPut(product) { 0 }
        if (existingItem > 1) {
            addedProducts[product] = existingItem - 1
        } else {
            addedProducts.remove(product)
        }
    }

    fun getProductSize(product: Product): Int {
        return addedProducts[product] ?: 0
    }
}