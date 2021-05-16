package com.hcms.spacex.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hcms.spacex.R
import com.hcms.spacex.viewmodels.LaunchesViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FilterFragment @Inject constructor() : BottomSheetDialogFragment() {
    private val launchesViewModel: LaunchesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_filter_layout_simple, container, false)

        return view
    }

    override fun onResume() {
        super.onResume()
        Log.d("aaaa", "I am VM on frag: " + launchesViewModel.hashCode().toString())
    }
}