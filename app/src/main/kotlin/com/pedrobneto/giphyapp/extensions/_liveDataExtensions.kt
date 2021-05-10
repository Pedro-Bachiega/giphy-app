package com.pedrobneto.giphyapp.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.pedrobneto.giphyapp.entities.DataResult

typealias ResponseLiveData<T> = LiveData<DataResult<T>>
typealias MutableResponseLiveData<T> = MutableLiveData<DataResult<T>>

fun <T> MutableResponseLiveData<T>.postLoading(isLoading: Boolean = true) = apply {
    postValue(DataResult(null, isLoading, null))
}

fun <T> MutableResponseLiveData<T>.postData(data: T?) = apply {
    postValue(DataResult(data, false, null))
}

fun <T> MutableResponseLiveData<T>.postError(throwable: Throwable) = apply {
    postValue(DataResult(null, false, throwable))
}

fun <T> LiveData<T>.observeData(viewLifecycleOwner: LifecycleOwner, observer: (T) -> Unit) = apply {
    observe(viewLifecycleOwner, Observer(observer))
}
