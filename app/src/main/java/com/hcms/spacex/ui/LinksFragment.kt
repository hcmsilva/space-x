package com.hcms.spacex.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hcms.spacex.R
import com.hcms.spacex.ui.utils.UrlManager
import com.hcms.spacex.viewmodels.LaunchItemViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.external_links_layout.view.*
import javax.inject.Inject


@AndroidEntryPoint
class LinksFragment @Inject constructor(private val urlManager: UrlManager) :
    BottomSheetDialogFragment() {
    private var launchItem: LaunchItemViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.external_links_layout, container, false)

        val videoLink = launchItem?.videoLink
        val ctx = context


        if (ctx != null && videoLink != null) {
            view.imageButtonVideo.visibility = VISIBLE
            view.imageButtonVideo.setOnClickListener {
                urlManager.watchYoutubeVideo(ctx, videoLink)
            }
        } else {
            view.imageButtonVideo.visibility = GONE
        }

        val wiki = launchItem?.wikipedia
        if (ctx != null && wiki != null) {
            view.imageButtonWiki.visibility = VISIBLE
            view.imageButtonWiki.setOnClickListener { urlManager.navigateToWiki(ctx, wiki) }
        } else {
            view.imageButtonWiki.visibility = GONE
        }

        return view
    }

    fun setModel(item: LaunchItemViewModel) {
        launchItem = item
    }

}
