package com.hcms.spacex.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.hcms.spacex.R
import com.hcms.spacex.ui.adapters.LaunchesAdapter
import com.hcms.spacex.ui.utils.ResourceProvider
import com.hcms.spacex.viewmodels.CompanyInfoViewModel
import com.hcms.spacex.viewmodels.LaunchesViewModel
import com.hcms.spacex.viewmodels.mappers.toLaunchItemList
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_launches.*

@AndroidEntryPoint
class LaunchesActivity : AppCompatActivity() {
    private val companyInfoViewModel: CompanyInfoViewModel by viewModels()
    private val launchesViewModel: LaunchesViewModel by viewModels()
    private lateinit var launchesAdapter: LaunchesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launches)

        launches_list.layoutManager = LinearLayoutManager(this)
        launchesAdapter = LaunchesAdapter(this)
        launches_list.adapter = launchesAdapter

        companyInfoViewModel.companyInfo.observe(
            this,
            Observer { companyInfo -> company_info.text = companyInfo })

        launchesViewModel.launchList.observe(
            this, Observer { newLaunchData ->
                launchesAdapter.updateList(
                    newLaunchData.toLaunchItemList(
                        ResourceProvider(applicationContext)
                    )
                )
            }
        )
    }
}

