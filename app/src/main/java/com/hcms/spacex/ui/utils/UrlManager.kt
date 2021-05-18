package com.hcms.spacex.ui.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
import javax.inject.Inject

class UrlManager @Inject constructor() {

    fun navigateToWiki(context: Context, wikiUrl: String) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(wikiUrl)
        ContextCompat.startActivity(context, i, null)
    }

    fun watchYoutubeVideo(context: Context, url: String) {
        val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        val webIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(url)
        )
        try {
            context.startActivity(appIntent)
        } catch (ex: ActivityNotFoundException) {
            context.startActivity(webIntent)
        }
    }
}