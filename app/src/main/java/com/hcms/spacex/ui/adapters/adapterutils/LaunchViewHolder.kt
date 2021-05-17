package com.hcms.spacex.ui.adapters.adapterutils

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.hcms.spacex.viewmodels.LaunchItemViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.launch_item_row.view.*

class LaunchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(model: LaunchItemViewModel) {
        itemView.missionNameTextView.text = model.missionName
        itemView.dateAtTimeTextView.text = model.dayAtTime
        itemView.nameTypeTextView.text = model.rocketNameType
        itemView.daysSinceFromTextView.text = model.daysSinceFromLaunch
        itemView.todayMinusLaunchDateTextView.text = model.todayMinusLaunchDateInDays
        Picasso.get().load(model.missionPatchSmall).into(itemView.mission_patch_iv)
        Picasso.get().load(getSuccessResId(model)).into(itemView.success_iv)
        itemView.success_iv.tag = model.launchSuccess
        itemView.mission_patch_iv.tag = model.missionPatchSmall
    }

    private fun getSuccessResId(model: LaunchItemViewModel): Int {
        return if (model.launchSuccess == true) {
            android.R.drawable.checkbox_on_background
        } else
            android.R.drawable.checkbox_off_background
    }

}
