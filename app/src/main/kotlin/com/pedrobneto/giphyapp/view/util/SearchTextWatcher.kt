package com.pedrobneto.giphyapp.view.util

import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher

private const val DELAY = 500L

class SearchTextWatcher(private val onSearch: (String) -> Unit) : TextWatcher {
    private val handler = Handler(Looper.getMainLooper())
    private var workRunnable: Runnable? = null

    private fun stopRunning() {
        if (workRunnable != null) {
            handler.removeCallbacks(workRunnable!!)
            workRunnable = null
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        //Do nothing
    }

    override fun afterTextChanged(s: Editable?) {
        //Do nothing
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        stopRunning()
        workRunnable = Runnable { onSearch.invoke(s.toString()) }
        handler.postDelayed(workRunnable!!, DELAY)
    }
}