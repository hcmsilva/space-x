package com.hcms.spacex.ui.utils

import android.content.Context
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ResourceProvider @Inject constructor(@ApplicationContext private val applicationContext: Context) {

    fun getString(@StringRes resId: Int) = applicationContext.getString(resId)
}