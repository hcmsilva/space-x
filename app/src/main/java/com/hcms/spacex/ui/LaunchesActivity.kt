package com.hcms.spacex.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.hcms.spacex.R
import com.hcms.spacex.repo.local.domain.LaunchItemDomain
import com.hcms.spacex.ui.adapters.LaunchesAdapter
import com.hcms.spacex.ui.utils.ResourceProvider
import com.hcms.spacex.viewmodels.CompanyInfoViewModel
import com.hcms.spacex.viewmodels.LaunchesViewModel
import com.hcms.spacex.viewmodels.mappers.toLaunchItemList
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_launches.*
import javax.inject.Inject

@AndroidEntryPoint
class LaunchesActivity : AppCompatActivity() {
    private val companyInfoViewModel: CompanyInfoViewModel by viewModels()
    private val launchesViewModel: LaunchesViewModel by viewModels()

    private val companyInfoObserver =
        Observer<String> { companyInfo -> company_info.text = companyInfo }
    private val launchesObserver =
        Observer<List<LaunchItemDomain>> { launchesAdapter.renewList(it.toLaunchItemList(resProvider)) }

    @Inject
    lateinit var launchesAdapter: LaunchesAdapter

    @Inject
    lateinit var filterFragment: FilterFragment

    @Inject
    lateinit var resProvider: ResourceProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launches)

        launches_list.layoutManager = LinearLayoutManager(this)
        launches_list.adapter = launchesAdapter

        observeViewModels()

        filter_button.setOnClickListener {
            filterFragment.show(supportFragmentManager, "filterfragment")
        }
    }

    private fun observeViewModels() {
        companyInfoViewModel.companyInfo.observe(this, companyInfoObserver)
        launchesViewModel.launchList.observe(this, launchesObserver)
    }
}

