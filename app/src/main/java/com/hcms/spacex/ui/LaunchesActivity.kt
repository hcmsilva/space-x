package com.hcms.spacex.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.hcms.spacex.R
import com.hcms.spacex.repo.IRepo
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@AndroidEntryPoint
class LaunchesActivity : AppCompatActivity() {
    private val companyViewModel: CompanyViewModel by viewModels()
    private val launchesViewModel: LaunchesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launches)

        checkVM()
    }

    private fun checkVM() {
        Log.d("activity", "ViewModel HashCode? ${companyViewModel.hashCode()}")
        Log.d("activity", "ViewModel HashCode? ${launchesViewModel.hashCode()}")
    }
}

@HiltViewModel
class LaunchesViewModel @Inject constructor(val repo: IRepo) : ViewModel() {

}

@HiltViewModel
class CompanyViewModel @Inject constructor(val repo: IRepo) : ViewModel() {

}
