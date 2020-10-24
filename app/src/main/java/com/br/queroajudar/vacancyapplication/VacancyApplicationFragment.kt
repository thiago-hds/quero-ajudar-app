package com.br.queroajudar.vacancyapplication

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.br.queroajudar.R
import com.br.queroajudar.databinding.FragmentVacancyApplicationBinding
import com.br.queroajudar.network.ResultWrapper
import com.br.queroajudar.util.showNetworkErrorMessage
import com.br.queroajudar.util.showToast
import com.br.queroajudar.vacancies.MainActivity
import timber.log.Timber
import javax.inject.Inject

class VacancyApplicationFragment: Fragment(){

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel : VacancyApplicationViewModel

    lateinit var binding : FragmentVacancyApplicationBinding

    private val args: VacancyApplicationFragmentArgs by navArgs()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).mainComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this, viewModelFactory)[VacancyApplicationViewModel::class.java]
        viewModel.vacancy = args.vacancy

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_vacancy_application, container, false
        )

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.applicationActive = viewModel.vacancy.application != null

        setupListeners()

        return binding.root
    }

    private fun setupListeners(){
        binding.btnConfirm.setOnClickListener{
            Timber.i("btnRegister click event")

            val result = viewModel.applyForVacancyOrCancelApplication()
            result.observe(viewLifecycleOwner, Observer { result ->
                Timber.i("user change observed $result")
                binding.overlayNetworkStatus.isUserAction = true
                binding.overlayNetworkStatus.result = result


                when (result){
                    is ResultWrapper.GenericError ->
                        result.error?.let {
                            viewModel.vacancyApplicationData.setApiValidationErrors(it.errors)
                        }
                    is ResultWrapper.NetworkError ->
                        showNetworkErrorMessage(context)
                    is ResultWrapper.Success -> {
                        showToast(R.string.vacancyApplication_applicationSent, context)
                        goToVacancyDetailsFragment()
                    }
                }
            })
        }

        binding.btnCancel.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun goToVacancyDetailsFragment(){
        val action =
            VacancyApplicationFragmentDirections
                .actionVacancyApplicationFragmentToVacancyDetailsFragment(viewModel.vacancy.id)
        findNavController().navigate(action)
    }
}