package com.br.queroajudar.favorites

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
import com.br.queroajudar.databinding.FragmentFavoriteVacanciesBinding
import com.br.queroajudar.vacancies.*
import timber.log.Timber
import javax.inject.Inject

class FavoriteVacanciesFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel : FavoriteVacanciesViewModel

    lateinit var binding: FragmentFavoriteVacanciesBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).mainComponent.inject(this)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, viewModelFactory)[FavoriteVacanciesViewModel::class.java]


        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_favorite_vacancies, container, false
        )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setupVacanciesList()

        return binding.root
    }

    private fun setupVacanciesList(){

        val adapter = VacancyAdapter(VacancyClickListener { vacancyId ->
                val action= FavoriteVacanciesFragmentDirections
                    .actionFavoriteVacanciesFragmentToVacancyDetailsFragment(vacancyId)
                findNavController().navigate(action)
            },
            VacancyOrganizationClickListener { organizationId ->
                val action= FavoriteVacanciesFragmentDirections
                    .actionFavoriteVacanciesFragmentToOrganizationDetailsFragment(organizationId)
                findNavController().navigate(action)
            })

        binding.rvVacancies.adapter = adapter

        viewModel.vacancies.observe(viewLifecycleOwner, Observer { vacanciesPagedList ->
            Timber.i("vacancies change observed $vacanciesPagedList")
            adapter.submitList(vacanciesPagedList)
        })

        viewModel.vacanciesLoadAfterResultWrapper.observe(viewLifecycleOwner, Observer { result ->
            Timber.i("vacanciesLoadAfterResultWrapper change observed $result")
            adapter.setResultWrapper(result)
        })

        viewModel.vacanciesLoadInitialResultWrapper.observe(viewLifecycleOwner, Observer { result ->
            Timber.i("vacanciesLoadInitialResultWrapper change observed $result")
            //binding.overlayNetworkStatus.result = result
            adapter.setResultWrapper(result)
        })
    }

}