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
import com.br.queroajudar.databinding.FragmentFavoriteOrganizationsBinding
import com.br.queroajudar.organizations.OrganizationClickListener
import com.br.queroajudar.organizations.OrganizationsAdapter
import com.br.queroajudar.vacancies.HomeActivity
import com.br.queroajudar.vacancies.VacanciesViewModel
import com.br.queroajudar.vacancies.VacancyAdapter
import com.br.queroajudar.vacancies.VacancyClickListener
import timber.log.Timber
import javax.inject.Inject

class FavoriteOrganizationsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel : FavoriteOrganizationsViewModel

    lateinit var binding: FragmentFavoriteOrganizationsBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as HomeActivity).homeComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this, viewModelFactory)[FavoriteOrganizationsViewModel::class.java]


        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_favorite_organizations, container, false
        )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setupOrganizationsList()

        return binding.root
    }

    private fun setupOrganizationsList(){

        val adapter = OrganizationsAdapter(OrganizationClickListener { organizationId ->
//            val action= FavoriteVacanciesFragmentDirections
//                .actionFavoriteVacanciesFragmentToVacancyDetailsFragment(organizationId)
//            findNavController().navigate(action)
        })

        binding.rvOrganizations.adapter = adapter

        viewModel.organizations.observe(viewLifecycleOwner, Observer { organizationsPagedList ->
            Timber.i("organizations change observed $organizationsPagedList")
            adapter.submitList(organizationsPagedList)
        })

//        viewModel.organizations.observe(viewLifecycleOwner, Observer { result ->
//            Timber.i("organizations change observed $result")
//            if(result is ResultWrapper.Success) {
//                adapter.setOrganizations(result.value)
//            }
//        })

//        viewModel.vacanciesSize.observe(viewLifecycleOwner,Observer{ size ->
//            Timber.i("vacanciesSize change observed: $size")
//            showEmptyList(size == 0)
//        })

        viewModel.organizationsLoadInitialResultWrapper.observe(viewLifecycleOwner, Observer { result ->
            Timber.i("organizationsLoadInitialResultWrapper change observed $result")
            //binding.overlayNetworkStatus.result = result
        })

        viewModel.organizationsLoadAfterResultWrapper.observe(viewLifecycleOwner, Observer { result ->
            Timber.i("organizationsLoadAfterResultWrapper change observed $result")
            adapter.setResultWrapper(result)
        })
    }
}