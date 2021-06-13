package com.bignerdranch.android.nerdmart

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bignerdranch.android.nerdmart.model.service.NerdMartServiceManager
import dagger.android.support.DaggerFragment
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

abstract class NerdMartAbstractFragment : DaggerFragment() {

    @Inject
    lateinit var nerdMartServiceManager: NerdMartServiceManager
    private val compositeDisposable = CompositeDisposable()
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        setupLoadingDialog()

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }

    fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    private fun setupLoadingDialog() {
        progressDialog = ProgressDialog(context).apply {
            isIndeterminate = true
            setMessage(getString(R.string.loading_text))
        }
    }
    protected fun <T> Observable<T>.showLoadingDialog(): Observable<T> {
        return doOnSubscribe { _ -> progressDialog.show() }
                .doOnComplete {
                    if (progressDialog.isShowing) {
                        progressDialog.dismiss()
                    }
                }
    }
}
