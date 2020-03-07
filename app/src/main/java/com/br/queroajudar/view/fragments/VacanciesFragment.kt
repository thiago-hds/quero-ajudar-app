package com.br.queroajudar.view.fragments

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
import com.br.queroajudar.databinding.FragmentVacanciesBinding
import com.br.queroajudar.network.QueroAjudarApiStatus
import com.br.queroajudar.view.adapters.VacancyAdapter
import com.br.queroajudar.viewmodel.VacanciesViewModel
import timber.log.Timber

class VacanciesFragment : Fragment() {

    private val viewModel: VacanciesViewModel by lazy {
        ViewModelProvider(this).get(VacanciesViewModel::class.java)
    }

    lateinit var binding : FragmentVacanciesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_vacancies, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setApiStatusObserver()
        setupVacanciesList()

        return binding.root
    }

    private fun setupVacanciesList(){
        val adapter = VacancyAdapter()
        binding.vacanciesRecyclerView.adapter = adapter

        viewModel.vacancies.observe(viewLifecycleOwner, Observer { vacancyList ->
            vacancyList?.let{
                adapter.submitList(vacancyList)
            }
        })
    }

    private fun setApiStatusObserver(){
        viewModel.apiStatus.observe(viewLifecycleOwner, Observer<QueroAjudarApiStatus>{ status ->
            Timber.i("API status changed")

            if(status == QueroAjudarApiStatus.LOADING){
                showLoadingOverlay()
            }
            else {
                hideLoadingOverlay()

                if(status == QueroAjudarApiStatus.NETWORK_ERROR){
                    showNetworkErrorMessage()
                }
            }
        })
    }

    private fun showLoadingOverlay(){ binding.isLoadingProgressVisible = true}
    private fun hideLoadingOverlay(){binding.isLoadingProgressVisible = false}

    private fun showNetworkErrorMessage(){
        Toast.makeText(context, R.string.error_connection, Toast.LENGTH_LONG).show()
    }


}


