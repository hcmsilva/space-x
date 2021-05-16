package com.hcms.spacex.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hcms.spacex.R
import com.hcms.spacex.ui.adapters.LaunchesAdapter.LaunchViewHolder
import com.hcms.spacex.viewmodels.LaunchItemViewModel
import kotlinx.android.synthetic.main.launch_item_row.view.*

class LaunchesAdapter(context: Context) : RecyclerView.Adapter<LaunchViewHolder>() {
    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private val data: MutableList<LaunchItemViewModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaunchViewHolder =
        LaunchViewHolder(mInflater.inflate(R.layout.launch_item_row, parent, false))

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: LaunchViewHolder, position: Int) =
        holder.bind(data[position])

    fun updateList(launches: List<LaunchItemViewModel>) {
        //TODO: Debug flickr issue using diff utils
//        val diffResult = DiffUtil.calculateDiff(LaunchesDiffCallback(data, launches))
//        diffResult.dispatchUpdatesTo(this)
        data.clear()
        data.addAll(launches)
        notifyDataSetChanged()
    }

    class LaunchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(model: LaunchItemViewModel) {
            itemView.missionNameTextView.text = model.missionName
            itemView.dateAtTimeTextView.text = model.dayAtTime
            itemView.nameTypeTextView.text = model.rocketNameType
            itemView.daysSinceFromTextView.text = model.daysSinceFromLaunch
            itemView.todayMinusLaunchDateTextView.text = model.todayMinusLaunchDateInDays
        }
    }

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

}