package com.hcms.spacex.ui

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hcms.spacex.R
import com.hcms.spacex.viewmodels.IFilterable
import com.hcms.spacex.viewmodels.LaunchesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dialog_filter_layout_simple.*
import kotlinx.android.synthetic.main.dialog_filter_layout_simple.view.*
import javax.inject.Inject

@AndroidEntryPoint
class FilterFragment @Inject constructor() : BottomSheetDialogFragment() {
    private val launchesViewModel: LaunchesViewModel by activityViewModels()
    private lateinit var filterable: IFilterable

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        filterable = launchesViewModel
        val view = inflater.inflate(R.layout.dialog_filter_layout_simple, container, false)
        setFilterClickListeners(view)
        return view
    }

    private fun setFilterClickListeners(view: View) {
        view.clear.setOnClickListener {
            view.filter_success_switch.isEnabled = false
            filterable.resetFilters()
            dismiss()
        }

        view.editTextDate.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                filterResults()
                return@setOnEditorActionListener true
            }
            false
        }
    }

    private fun filterResults() {
        filterable.filter(
            year = editTextDate.text.toString(),
            filterSuccess = filter_success_switch.isEnabled,
            reversed = false
        )
    }

    override fun onDismiss(dialog: DialogInterface) {
        filterResults()
        super.onDismiss(dialog)
    }

}