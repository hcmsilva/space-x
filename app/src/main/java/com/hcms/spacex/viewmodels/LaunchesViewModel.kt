package com.hcms.spacex.viewmodels

import androidx.lifecycle.*
import com.hcms.spacex.repo.ILaunchesRepo
import com.hcms.spacex.repo.local.domain.LaunchItemDomain
import com.hcms.spacex.ui.utils.CountingIdlingResourceSingleton
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class LaunchesViewModel @Inject constructor(private val repo: ILaunchesRepo) : ViewModel(),
    IFilterable {

    internal val fullList = mutableListOf<LaunchItemDomain>()
    private val _launchList = MutableLiveData<List<LaunchItemDomain>>()
    val launchList: LiveData<List<LaunchItemDomain>> = _launchList

    private val compositeDisposable = CompositeDisposable()

    init {
        loadLaunchesData()
    }

    internal fun loadLaunchesData() {
        CountingIdlingResourceSingleton.incrementAllLaunches()
        compositeDisposable.addAll(
            repo.getAllLaunches()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result -> processResult(result) },
                    { error -> processError(error) }
                )
        )
    }

    private fun processError(error: Throwable?) {
        error?.printStackTrace()
        CountingIdlingResourceSingleton.countingIdlingResAllLaunches.decrement()
    }

    private fun processResult(result: List<LaunchItemDomain>) {
        val tmpList = result.sortedBy { it.launchYear }
        fullList.clear()
        fullList.addAll(tmpList)
        _launchList.postValue(fullList)
        CountingIdlingResourceSingleton.decrementAllLaunches()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onClear() {
        compositeDisposable.dispose()
    }

    override fun resetFilters() {
        _launchList.postValue(fullList)
    }

    override fun filter(year: String, filterSuccess: Boolean, reversed: Boolean) {
        var filteredList: List<LaunchItemDomain> = fullList.toList()

        if (reversed)
            filteredList = filteredList.asReversed()
        if (filterSuccess)
            filteredList = filteredList.filter { it.launchSuccess == true }
        if (year.isNotBlank())
            filteredList = filteredList.filter { it.launchYear == year }

        _launchList.postValue(filteredList)
    }
}

interface IFilterable {
    fun resetFilters()
    fun filter(year: String, filterSuccess: Boolean, reversed: Boolean)
}