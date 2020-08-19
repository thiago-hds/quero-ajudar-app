package com.br.queroajudar.login


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.br.queroajudar.R
import com.br.queroajudar.databinding.FragmentLoginBinding
import com.br.queroajudar.network.ResultWrapper
import com.br.queroajudar.register.MainActivity
import com.br.queroajudar.util.QueroAjudarPreferences
import timber.log.Timber
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel : LoginViewModel

    lateinit var binding : FragmentLoginBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).mainComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setupLoadingProgressBarVisibility()
        setupListeners()

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

    private fun setupListeners(){
        binding.loginBtnEnter.setOnClickListener {
            val user = viewModel.doLogin()
            user?.observe(viewLifecycleOwner, Observer{ status ->
                if (status is ResultWrapper.GenericError){
                    status.error?.errors?.let { viewModel.showErrors(it) }
                }
                else if(status is ResultWrapper.Success){
                    status.value.token?.let { token ->
                        viewModel.saveToken(token)
                        Timber.i("token: ${QueroAjudarPreferences.apiToken}")

                        val action
                                = LoginFragmentDirections.actionLoginFragmentToHomeActivity()
                        findNavController().navigate(action)
                    }
                }
            })
        }

    }

}
