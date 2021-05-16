package com.hcms.spacex.ui.adapters.adapterutils

import androidx.recyclerview.widget.DiffUtil
import com.hcms.spacex.viewmodels.LaunchItemViewModel

class LaunchesDiffCallback(
    private val oldLaunches: List<LaunchItemViewModel>,
    private val newLaunches: List<LaunchItemViewModel>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int =
        oldLaunches.size

    override fun getNewListSize(): Int =
        newLaunches.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldLaunches[oldItemPosition] == newLaunches[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldLaunches[oldItemPosition].isVisualRepresentationTheSame(newLaunches[newItemPosition])

}