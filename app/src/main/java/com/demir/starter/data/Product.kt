package com.demir.starter.data

import java.util.Locale

class Product {
    val label: String = ""
    val image: String = ""
    val category: String = ""

    val price: Double = 0.0

    val productCategory: ProductCategory
        get() = ProductCategory.entries.first {
            it.name.lowercase(Locale.ROOT) == category
        }
}