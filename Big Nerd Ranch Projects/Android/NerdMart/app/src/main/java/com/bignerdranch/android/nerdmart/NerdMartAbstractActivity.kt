package com.bignerdranch.android.nerdmart

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import com.bignerdranch.android.nerdmart.databinding.ActivityNerdmartAbstractBinding
import com.bignerdranch.android.nerdmart.model.service.NerdMartServiceManager
import com.bignerdranch.android.nerdmart.viewmodel.NerdMartViewModel
import com.bignerdranch.android.nerdmartservice.service.payload.Cart
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

abstract class NerdMartAbstractActivity : DaggerAppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()

    @Inject
    lateinit var nerdMartServiceManager: NerdMartServiceManager

    @Inject
    lateinit var nerdMartViewModel: NerdMartViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nerdmart_abstract)
        val binding: ActivityNerdmartAbstractBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_nerdmart_abstract)
        binding.logoutButtonClickListener = View.OnClickListener { signout() }
        binding.nerdMartViewModel = nerdMartViewModel
        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.abstract_nerdmart_activity_fragment_frame, getFragment())
                    .add(binding.abstractNerdmartActivityFragmentFrame.id, getFragment())
                    .commit()
        }
    }

    fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    private fun signout() {
        addDisposable(nerdMartServiceManager
                .signout()
                .subscribe {
                    val intent = Intent(this, LoginActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    }
                    startActivity(intent)
                })
    }

    fun updateCartStatus(cart: Cart) {
        nerdMartViewModel.updateCartStatus(cart)
    }

    protected abstract fun getFragment(): Fragment
}
