package com.br.queroajudar.register


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.br.queroajudar.R
import com.br.queroajudar.databinding.FragmentRegisterBinding
import com.br.queroajudar.network.ResultWrapper
import com.br.queroajudar.util.showNetworkErrorMessage
import timber.log.Timber
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class RegisterFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel : RegisterViewModel
    lateinit var binding : FragmentRegisterBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).mainComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this, viewModelFactory)[RegisterViewModel::class.java]

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_register, container, false
        )
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setupListeners()

        return binding.root
    }

    private fun setupListeners(){
        binding.btnRegister.setOnClickListener{
            Timber.i("btnRegister click event")

            val userResult = viewModel.register()
            userResult.observe(viewLifecycleOwner, Observer { result ->
                Timber.i("user change observed $result")
                binding.overlayNetworkStatus.isUserAction = true
                binding.overlayNetworkStatus.result = result


                when (result){
                    is ResultWrapper.GenericError ->
                        result.error?.let {viewModel.registerData.setApiValidationErrors(it.errors)}
                    is ResultWrapper.NetworkError ->
                        showNetworkErrorMessage(context)
                    is ResultWrapper.Success -> {
                        result.value.token?.let { it1 -> viewModel.saveApiToken(it1) }
                        goToHomeActivity()
                    }
                }
            })
        }
    }

    private fun goToHomeActivity(){
        findNavController().navigate(R.id.action_registerDataFragment_to_homeActivity)
    }
}
