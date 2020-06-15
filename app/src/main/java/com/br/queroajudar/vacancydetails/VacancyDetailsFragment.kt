package com.br.queroajudar.vacancydetails

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs

import com.br.queroajudar.R
import com.br.queroajudar.categories.CategoryAdapter
import com.br.queroajudar.databinding.FragmentVacancyDetailsBinding
import com.br.queroajudar.network.ResultWrapper
import com.br.queroajudar.vacancies.HomeActivity
import com.br.queroajudar.vacancies.VacanciesViewModel
import kotlinx.android.synthetic.main.vacancies_filter_layout.view.*
import timber.log.Timber
import javax.inject.Inject

class VacancyDetailsFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel : VacancyDetailsViewModel

    lateinit var binding : FragmentVacancyDetailsBinding

    private val args: VacancyDetailsFragmentArgs by navArgs()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as HomeActivity).homeComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this, viewModelFactory)[VacancyDetailsViewModel::class.java]
        viewModel.id = args.id

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_vacancy_details, container, false
        )

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setupVacancyDetails()

        return binding.root
    }

    fun setupVacancyDetails(){

        val causeAdapter = CategoryAdapter()
        val skillAdapter = CategoryAdapter()

        binding.rvCauses.adapter = causeAdapter
        binding.rvSkills.adapter = skillAdapter


        viewModel.vacancy.observe(viewLifecycleOwner, Observer { vacancyResult ->
            Timber.i("vacancy change observed $vacancyResult")
            if(vacancyResult is ResultWrapper.Success) {
                binding.vacancy = vacancyResult.value
                //vacancyResult.value. //TODO adicionar habilidades de causas em seus recyclerviews

            }
        })


    }

}
