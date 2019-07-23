package com.furkanaskin.app.podpocket.ui.profile.favorites

import androidx.lifecycle.MutableLiveData
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.core.Resource
import com.furkanaskin.app.podpocket.core.Status
import com.furkanaskin.app.podpocket.service.response.Podcasts
import com.uber.autodispose.autoDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 * Created by Furkan on 2019-05-18
 */

class FavoritesViewModel : BaseViewModel() {

    val episodesLiveData = MutableLiveData<Resource<Podcasts>>()

    fun getEpisodes(id: String) {
        baseApi.getPodcastById(id)
                .subscribeOn(Schedulers.io())
                .map { Resource.success(it) }
                .onErrorReturn { Resource.error(it) }
                .doOnSubscribe { progressLiveData.postValue(true) }
                .doOnTerminate { progressLiveData.postValue(false) }
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(this)
                .subscribe {
                    when (it?.status) {
                        Status.SUCCESS -> episodesLiveData.postValue(it)
                        Status.LOADING -> ""
                        Status.ERROR -> Timber.e(it.error)
                    }
                }
    }
}