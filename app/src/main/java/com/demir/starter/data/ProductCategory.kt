package com.demir.starter.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.demir.starter.R

enum class ProductCategory(
    @StringRes val nameResId: Int,
    @DrawableRes val iconResId: Int

) {
    FOOD(R.string.label_category_food, R.drawable.ic_food),
    BIRTHDAY(R.string.label_category_birthday, R.drawable.ic_birthday),
    HOUSE(R.string.label_category_plant, R.drawable.ic_homee),
    ROSE(R.string.label_category_rose, R.drawable.ic_rose);
}