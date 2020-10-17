package com.br.queroajudar.register

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
import androidx.navigation.fragment.navArgs

import com.br.queroajudar.R
import com.br.queroajudar.data.User
import com.br.queroajudar.databinding.FragmentRegisterBinding
import com.br.queroajudar.network.ResultWrapper
import com.br.queroajudar.util.Constants.EDIT_MODE
import com.br.queroajudar.util.Constants.REGISTER_MODE
import com.br.queroajudar.util.saveApiToken
import com.br.queroajudar.util.showNetworkErrorMessage
import com.br.queroajudar.vacancies.MainActivity
import com.br.queroajudar.util.hideKeyboard
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
import javax.inject.Inject

class RegisterFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel : RegisterViewModel
    lateinit var binding : FragmentRegisterBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val act = activity
        if(act is AuthenticationActivity){
            act.authenticationComponent.inject(this)
        }
        else if(act is MainActivity){
            act.mainComponent.inject(this)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this, viewModelFactory)[RegisterViewModel::class.java]

        val user = arguments?.getParcelable<User>("user")
        user?.let{
            viewModel.mode = EDIT_MODE
            viewModel.setUserData(it)
        }

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
            hideKeyboard()
            Timber.i("btnRegister click event")

            val userResult = viewModel.sendData()
            userResult.observe(viewLifecycleOwner, Observer { result ->
                Timber.i("user change observed $result")
                binding.overlayNetworkStatus.isUserAction = true
                binding.overlayNetworkStatus.result = result


                when (result){
                    is ResultWrapper.GenericError ->
                        result.error?.let {
                            viewModel.registerData.setApiValidationErrors(it.errors)
                        }
                    is ResultWrapper.NetworkError ->
                        showNetworkErrorMessage(context)
                    is ResultWrapper.Success -> {
                        val message = if(viewModel.mode == REGISTER_MODE){
                            R.string.register_success
                        } else{
                            R.string.register_profile_edit_success
                        }

                        Snackbar.make(
                            binding.root, message,
                            Snackbar.LENGTH_SHORT
                        ).show()

                        if(viewModel.mode == REGISTER_MODE) {
                            result.value.token?.let { token -> saveApiToken(token) }
                            goToSelectCausesFragment()
                        }
                        else if(viewModel.mode == EDIT_MODE){
                            goToProfileFragment()
                        }
                    }
                }
            })
        }
    }



    private fun goToSelectCausesFragment(){
        findNavController().navigate(
            R.id.action_registerDataFragment_to_selectCategoriesFragment2
        )
    }

    private fun goToProfileFragment(){
        findNavController().navigate(
            R.id.action_registerFragment_to_profileFragment
        )
    }
}
