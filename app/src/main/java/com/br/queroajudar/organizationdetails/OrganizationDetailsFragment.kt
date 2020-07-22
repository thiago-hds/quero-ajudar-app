package com.br.queroajudar.organizationdetails

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.br.queroajudar.R
import com.br.queroajudar.categories.CategoryAdapter
import com.br.queroajudar.databinding.FragmentOrganizationDetailsBinding
import com.br.queroajudar.network.ResultWrapper
import com.br.queroajudar.vacancies.HomeActivity
import com.br.queroajudar.vacancies.VacanciesFragmentDirections
import com.br.queroajudar.vacancies.VacancyAdapter
import com.br.queroajudar.vacancies.VacancyClickListener
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import timber.log.Timber
import javax.inject.Inject

class OrganizationDetailsFragment : Fragment() {

    private val TAB_INFORMATION = 0
    private val TAB_VACANCIES   = 1

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel : OrganizationDetailsViewModel

    lateinit var binding : FragmentOrganizationDetailsBinding

    private val args: OrganizationDetailsFragmentArgs by navArgs()

    lateinit var menu: Menu

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as HomeActivity).homeComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this, viewModelFactory)[OrganizationDetailsViewModel::class.java]
        viewModel.id = args.id

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_organization_details, container, false
        )

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setHasOptionsMenu(true)

        setupOrganizationDetails()
        setupVacanciesList()
        setupListeners()

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_vacancy_details,menu)
        this.menu = menu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean  = when (item.itemId) {
        R.id.favorite_vacancy -> {
            val response = viewModel.favoriteOrganization()
            item.isEnabled = false
            response.observe(viewLifecycleOwner, Observer {result ->
                Timber.i("favoriteVacancy response $result")
                if(result is ResultWrapper.Success){
                    val strId: Int
                    val iconId: Int
                    if (result.value){
                        strId = R.string.vacancyDetails_favorite_success
                        iconId = R.drawable.ic_favorite_24dp
                    }
                    else{
                        strId = R.string.vacancyDetails_unfavorite_success
                        iconId = R.drawable.ic_baseline_favorite_border_24
                    }
                    item.setIcon(iconId)

                    Snackbar.make(
                        binding.root, strId,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                else if(result is ResultWrapper.NetworkError || result is ResultWrapper.GenericError){
                    Toast.makeText(
                        activity, R.string.vacancyDetails_favorite_error, Toast.LENGTH_SHORT
                    ).show()
                }
                item.isEnabled = true
            })
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }


    private fun setupOrganizationDetails(){
        binding.rvCauses.layoutManager = GridLayoutManager(activity,2)

        val causeAdapter = CategoryAdapter()

        binding.rvCauses.adapter = causeAdapter

        viewModel.organization.observe(viewLifecycleOwner, Observer { organizationResult ->
            Timber.i("organization change observed $organizationResult")
            binding.overlayNetworkStatus.result = organizationResult
            if(organizationResult is ResultWrapper.Success) {
                val organization = organizationResult.value
                binding.organization = organization

                organization.causes?.let { causeAdapter.submitList(it) }

                if(organization.causes?.size == 0){
                    binding.tvLabelCauses.visibility = View.GONE
                    binding.rvCauses.visibility = View.GONE
                }

                if(organization.favourited){
                    menu.findItem(R.id.favorite_vacancy).setIcon(R.drawable.ic_favorite_24dp)
                }
                menu.findItem(R.id.favorite_vacancy).isVisible = true

            }
        })
    }

    private fun setupVacanciesList(){

        val adapter =
            VacancyAdapter(VacancyClickListener { vacancyId ->
                val action
                        = VacanciesFragmentDirections.actionVacanciesFragmentToVacancyDetailsFragment(vacancyId)
                findNavController().navigate(action)
            })

        binding.rvVacancies.adapter = adapter

        viewModel.vacancies.observe(viewLifecycleOwner, Observer { vacanciesPagedList ->
            Timber.i("vacancies change observed $vacanciesPagedList")
            adapter.submitList(vacanciesPagedList)
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

//        viewModel.vacanciesLoadInitialResultWrapper.observe(viewLifecycleOwner, Observer { result ->
//            Timber.i("vacanciesLoadInitialResultWrapper change observed $result")
//            binding.overlayNetworkStatus.result = result
//        })
//
//        viewModel.vacanciesLoadAfterResultWrapper.observe(viewLifecycleOwner, Observer { result ->
//            Timber.i("vacanciesLoadAfterResultWrapper change observed $result")
//            adapter.setResultWrapper(result)
//        })
    }

    private fun setupListeners(){
        binding.overlayNetworkStatus.btnTryAgain.setOnClickListener {
            // TODO
        }

        binding.tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {}

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabSelected(tab: TabLayout.Tab?) {
                Timber.i("Tab selected ${tab?.position}")
                if(tab?.position == TAB_INFORMATION){
                    binding.layoutInfo.visibility = View.VISIBLE
                    binding.layoutVacancies.visibility = View.GONE
                }
                else if (tab?.position == TAB_VACANCIES){
                    binding.layoutVacancies.visibility = View.VISIBLE
                    binding.layoutInfo.visibility = View.GONE
                }
            }

        })

    }
}