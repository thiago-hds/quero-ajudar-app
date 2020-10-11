package com.br.queroajudar.profile

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider

import com.br.queroajudar.R
import com.br.queroajudar.databinding.FragmentProfileBinding
import com.br.queroajudar.databinding.FragmentVacancyDetailsBinding
import com.br.queroajudar.vacancies.HomeActivity
import com.br.queroajudar.vacancydetails.VacancyDetailsViewModel
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel : ProfileViewModel

    lateinit var binding : FragmentProfileBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as HomeActivity).homeComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        viewModel = ViewModelProvider(this, viewModelFactory)[ProfileViewModel::class.java]

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_vacancy_details, container, false
        )

        binding.lifecycleOwner = this
        //binding.viewModel = viewModel

        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

}
