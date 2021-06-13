package com.bignerdranch.android.nerdmart

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bignerdranch.android.nerdmart.databinding.FragmentLoginBinding

class LoginFragment : NerdMartAbstractFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val fragmentLoginBinding = DataBindingUtil.inflate<FragmentLoginBinding>(
                inflater, R.layout.fragment_login, container, false)

        fragmentLoginBinding.setLoginButtonClickListener {
            val username = fragmentLoginBinding.fragmentLoginUsername.text.toString()
            val password = fragmentLoginBinding.fragmentLoginPassword.text.toString()
            addDisposable(nerdMartServiceManager
                    .authenticate(username, password)
                    .showLoadingDialog()
                    .subscribe { authenticated ->
                        val message = if (authenticated) {
                            R.string.auth_success_toast
                        } else {
                            R.string.auth_failure_toast
                        }
                        Toast.makeText(context,
                                message,
                                Toast.LENGTH_SHORT).show()
                        if (authenticated) {
                            val intent = ProductsActivity.newIntent(context!!).apply {
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                        or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            }
                            startActivity(intent)
                            activity?.finish()
                        }
                    })
        }
        return fragmentLoginBinding.root
    }
}
