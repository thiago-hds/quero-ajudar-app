package com.br.queroajudar.view.fragments


import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.br.queroajudar.R
import com.br.queroajudar.databinding.FragmentLoginBinding
import com.br.queroajudar.network.QueroAjudarApiStatus
import com.br.queroajudar.viewmodel.LoginViewModel

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {

    private val loginViewModel: LoginViewModel by lazy {
        ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    lateinit var binding : FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = loginViewModel

        setupLoadingProgressBarVisibility()

        return binding.root
    }

    private fun setupLoadingProgressBarVisibility(){
//        loginViewModel.apiStatus.observe(viewLifecycleOwner, Observer<QueroAjudarApiStatus>{status ->
//            Log.i("QueroAjudar", "ApiStatus changed")
//
//            if(status == QueroAjudarApiStatus.LOADING){
//                showLoadingOverlay()
//            }
//            else {
//                hideLoadingOverlay()
//
//                if(status == QueroAjudarApiStatus.NETWORK_ERROR){
//                    showNetworkErrorMessage()
//                }
//            }
//        })
    }

    private fun showLoadingOverlay(){
        //binding.loginOverlayLoading.visibility = View.VISIBLE
    }
    private fun hideLoadingOverlay(){
        //binding.loginOverlayLoading.visibility = View.GONE
    }

    private fun showNetworkErrorMessage(){
        Toast.makeText(context, R.string.error_connection, Toast.LENGTH_LONG).show()
    }

}
