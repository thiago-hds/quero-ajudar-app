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
import com.br.queroajudar.register.AuthenticationActivity
import com.br.queroajudar.util.QueroAjudarPreferences
import com.br.queroajudar.util.saveApiToken
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
        (activity as AuthenticationActivity).authenticationComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_login, container, false
        )

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setupListeners()

        return binding.root
    }

    private fun setupListeners(){
        binding.btnEnter.setOnClickListener {
            val user = viewModel.doLogin()
            user?.observe(viewLifecycleOwner, Observer{ result ->
                binding.overlayNetworkStatus.isUserAction = true
                binding.overlayNetworkStatus.result = result

                if (result is ResultWrapper.GenericError){
                    result.error?.errors?.let { viewModel.showErrors(it) }
                }
                else if(result is ResultWrapper.Success){
                    result.value.token?.let { token ->
                        saveApiToken(token)
                        Timber.i("token: ${QueroAjudarPreferences.apiToken}")

                        goToMainActivity()
                    }
                }
            })
        }
    }

    private fun goToMainActivity(){
        findNavController().navigate(
            R.id.action_loginFragment_to_mainActivity
        )
    }
}
