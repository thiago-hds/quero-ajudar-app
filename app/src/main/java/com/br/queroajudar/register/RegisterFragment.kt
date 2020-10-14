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
import com.br.queroajudar.databinding.FragmentRegisterBinding
import com.br.queroajudar.network.ResultWrapper
import com.br.queroajudar.util.Constants.EDIT_MODE
import com.br.queroajudar.util.Constants.REGISTER_MODE
import com.br.queroajudar.util.showNetworkErrorMessage
import com.br.queroajudar.vacancies.HomeActivity
import com.br.queroajudar.vacancydetails.VacancyDetailsFragmentArgs
import com.google.android.material.snackbar.Snackbar
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

    private val args: RegisterFragmentArgs by navArgs()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val act = activity
        if(act is AuthenticationActivity){
            act.authenticationComponent.inject(this)
        }
        else if(act is HomeActivity){
            act.homeComponent.inject(this)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this, viewModelFactory)[RegisterViewModel::class.java]

        args.user?.let{
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
            Timber.i("btnRegister click event")

            val userResult = viewModel.sendData()
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
                        if(viewModel.mode == REGISTER_MODE) {
                            result.value.token?.let { it1 -> viewModel.saveApiToken(it1) }
                            goToSelectCausesFragment()
                        }
                        else if(viewModel.mode == EDIT_MODE){
                            Snackbar.make(
                                binding.root, R.string.register_profile_edit_success,
                                Snackbar.LENGTH_SHORT
                            ).show()
                            goToProfileFragment()
                        }
                    }
                }
            })
        }
    }

    private fun goToSelectCausesFragment(){
        findNavController().navigate(R.id.action_registerDataFragment_to_selectCategoriesFragment2)
    }

    private fun goToProfileFragment(){
        findNavController().navigate(R.id.action_registerFragment_to_profileFragment)
    }

}
