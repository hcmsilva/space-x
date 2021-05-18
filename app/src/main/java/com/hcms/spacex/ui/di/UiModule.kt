package com.hcms.spacex.ui.di

import android.content.Context
import com.hcms.spacex.ui.LaunchesActivity
import com.hcms.spacex.ui.LinksFragment
import com.hcms.spacex.ui.adapters.LaunchesAdapter
import com.hcms.spacex.viewmodels.LaunchItemViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext

@Module
@InstallIn(ActivityComponent::class)
object UiModule {
    @Provides
    fun provideLaunchItemClickListener(
        @ActivityContext launchesContext: Context,
        frag: LinksFragment
    ): LaunchesAdapter.OnLaunchItemClickListener {
        return object : LaunchesAdapter.OnLaunchItemClickListener {
            override fun onItemClick(item: LaunchItemViewModel) {
                val fm = (launchesContext as LaunchesActivity).supportFragmentManager
                if (item.wikipedia != null || item.videoLink != null) {
                    frag.setModel(item)
                    frag.show(fm, "")
                }
            }
        }
    }
}