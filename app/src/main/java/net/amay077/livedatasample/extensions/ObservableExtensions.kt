package net.amay077.livedatasample.extensions

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

/**
 * Observable<T> を LiveData<T> に変換
 */
fun <T> Observable<T>.toLiveData() : LiveData<T> {

    return object : MutableLiveData<T>() {
        var disposable : Disposable? = null;

        // ライフサイクルがActiveになったときに購読開始
        override fun onActive() {
            super.onActive()

            // Observable -> LiveData
            disposable = this@toLiveData.subscribe({
                this.postValue(it)
            })
        }

        // ライフサイクルが非Activeになったら購読停止
        override fun onInactive() {
            disposable?.dispose();
            super.onInactive()
        }
    }
}
