package com.br.queroajudar.causes

import android.content.Context
import android.os.Bundle
import android.view.ContextMenu
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider

import com.br.queroajudar.R
import com.br.queroajudar.databinding.FragmentCausesBinding
import com.br.queroajudar.databinding.FragmentRegisterBinding
import com.br.queroajudar.register.MainActivity
import com.br.queroajudar.register.RegisterViewModel
import com.br.queroajudar.vacancies.HomeActivity
import javax.inject.Inject

class CausesFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel : CausesViewModel
    private lateinit var binding : FragmentCausesBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as HomeActivity).homeComponent.inject(this)
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_causes, container, false)

        //binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setupCauseList()
        return binding.root
    }

    private fun setupCauseList(){

    }

}
