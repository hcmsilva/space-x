package com.hcms.spacex.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hcms.spacex.R
import com.hcms.spacex.ui.adapters.adapterutils.LaunchViewHolder
import com.hcms.spacex.viewmodels.LaunchItemViewModel
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class LaunchesAdapter @Inject constructor(
    @ActivityContext context: Context,
    private val customClickListener: OnLaunchItemClickListener
) :
    RecyclerView.Adapter<LaunchViewHolder>() {
    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private val data: MutableList<LaunchItemViewModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaunchViewHolder =
        LaunchViewHolder(mInflater.inflate(R.layout.launch_item_row, parent, false))

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: LaunchViewHolder, position: Int) =
        holder.bind(data[position], customClickListener)

    fun renewList(launches: List<LaunchItemViewModel>) {
        data.clear()
        data.addAll(launches)
        notifyDataSetChanged()
    }

    interface OnLaunchItemClickListener {
        fun onItemClick(item: LaunchItemViewModel)
    }
}