package com.hcms.spacex.model

import androidx.lifecycle.*
import com.hcms.spacex.repo.ILaunchesRepo
import com.hcms.spacex.repo.local.domain.LaunchItemDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class LaunchesViewModel @Inject constructor(private val repo: ILaunchesRepo) : ViewModel() {

    private val _launchList = MutableLiveData<List<LaunchItemDomain>>()
    val launchList: LiveData<List<LaunchItemDomain>> = _launchList

    private val compositeDisposable = CompositeDisposable()

    init {
        loadLaunchesData()
    }

    internal fun loadLaunchesData() {
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
    }


    private fun processResult(result: List<LaunchItemDomain>) {
        _launchList.postValue(result)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onClear() {
        compositeDisposable.dispose()
    }
}