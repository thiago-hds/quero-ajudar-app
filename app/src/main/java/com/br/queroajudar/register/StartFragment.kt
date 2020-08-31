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
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController


import com.br.queroajudar.R
import com.br.queroajudar.databinding.FragmentStartBinding
import com.br.queroajudar.login.LoginFragmentDirections
import com.br.queroajudar.network.ResultWrapper
import com.br.queroajudar.profile.ProfileViewModel
import com.br.queroajudar.util.QueroAjudarPreferences
import com.br.queroajudar.vacancies.HomeActivity
import com.br.queroajudar.vacancydetails.VacancyDetailsViewModel
import timber.log.Timber
import javax.inject.Inject


class StartFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentStartBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_start, container, false)

        binding.startBtnRegister.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_startFragment_to_registerDataFragment)
        )

        binding.startBtnlogin.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_startFragment_to_loginFragment)
        )

        return binding.root
    }
}
