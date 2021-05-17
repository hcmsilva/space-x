package com.hcms.spacex.viewmodels

import androidx.lifecycle.*
import com.hcms.spacex.R
import com.hcms.spacex.repo.ICompanyInfoRepo
import com.hcms.spacex.repo.local.domain.CompanyInfoDomain
import com.hcms.spacex.ui.utils.CountingIdlingResourceSingleton
import com.hcms.spacex.ui.utils.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class CompanyInfoViewModel @Inject constructor(
    private val repo: ICompanyInfoRepo,
    private val resourceProvider: ResourceProvider
) : ViewModel() {
    private val _companyInfo = MutableLiveData<String>("")
    val companyInfo: LiveData<String> = _companyInfo

    private val compositeDisposable = CompositeDisposable()

    init {
        loadCompanyData()
    }

    internal fun loadCompanyData() {
        CountingIdlingResourceSingleton.incrementCompanyInfo()
        compositeDisposable.addAll(
            repo.getCompanyInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { success -> processSuccess(success) },
                    { error -> processError(error) }
                )
        )
    }

    private fun processError(error: Throwable?) {
        error?.printStackTrace()
        CountingIdlingResourceSingleton.decrementCompanyInfo()
    }


    private fun processSuccess(result: List<CompanyInfoDomain>) {
        _companyInfo.postValue(result.first().toCompanyInfoString())
        CountingIdlingResourceSingleton.decrementCompanyInfo()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onClear() {
        compositeDisposable.dispose()
    }

    private fun CompanyInfoDomain.toCompanyInfoString(): String =
        String.format(
            format = resourceProvider.getString(R.string.company_info_template),
            this.name,
            this.founder.toString(),
            this.founded,
            this.employees,
            this.launchSites,
            this.valuation
        )

}