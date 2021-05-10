package com.pedrobneto.giphyapp.extensions

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

fun <T, R> Single<T>.call(
    transformation: (T) -> R,
    liveData: MutableResponseLiveData<R>,
): Disposable = observeOn(AndroidSchedulers.mainThread())
    .subscribeOn(Schedulers.io())
    .subscribe({ liveData.postData(transformation(it)) }, { liveData.postError(it) })
