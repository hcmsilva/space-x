package com.hcms.spacex.ui.adapters.adapterutils

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.hcms.spacex.viewmodels.LaunchItemViewModel
import kotlinx.android.synthetic.main.launch_item_row.view.*

class LaunchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(model: LaunchItemViewModel) {
        itemView.missionNameTextView.text = model.missionName
        itemView.dateAtTimeTextView.text = model.dayAtTime
        itemView.nameTypeTextView.text = model.rocketNameType
        itemView.daysSinceFromTextView.text = model.daysSinceFromLaunch
        itemView.todayMinusLaunchDateTextView.text = model.todayMinusLaunchDateInDays
    }
}
