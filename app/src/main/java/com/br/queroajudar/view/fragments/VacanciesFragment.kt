package com.br.queroajudar.view.fragments

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.br.queroajudar.R
import com.br.queroajudar.databinding.FragmentVacanciesBinding
import com.br.queroajudar.network.QueroAjudarApiStatus
import com.br.queroajudar.view.adapters.VacancyAdapter
import com.br.queroajudar.view.adapters.VacancyClickListener
import com.br.queroajudar.view.adapters.VacancyListScrollListener
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

        //setApiStatusObserver()
        setListeners()
        setupVacanciesList()

        return binding.root
    }

    private fun setListeners(){
        binding.vacanciesBtnFiters.setOnClickListener {
            binding.vacanciesDlFilters.openDrawer(GravityCompat.END)
        }
    }

    private fun setupVacanciesList(){
        val adapter = VacancyAdapter(VacancyClickListener { vacancyId ->
            Toast.makeText(context, "$vacancyId", Toast.LENGTH_LONG).show()
            viewModel.onVacancyClicked()
        })

        val scrollListener = VacancyListScrollListener({
                viewModel.onListEndReached()
            },
            binding.vacanciesRecyclerView.layoutManager as LinearLayoutManager
        )

        binding.vacanciesRecyclerView.adapter = adapter
        binding.vacanciesRecyclerView.addOnScrollListener(scrollListener)

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


