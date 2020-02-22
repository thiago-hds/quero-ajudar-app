package com.br.queroajudar.view.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider

import com.br.queroajudar.R
import com.br.queroajudar.databinding.FragmentRegisterDataBinding
import com.br.queroajudar.viewmodel.RegisterDataViewModel

/**
 * A simple [Fragment] subclass.
 */
class RegisterDataFragment : Fragment() {
    private val viewModel: RegisterDataViewModel by lazy {
        ViewModelProvider(this).get(RegisterDataViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentRegisterDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_register_data, container, false)


        binding.viewModel = viewModel

        viewModel.test();

        return binding.root
    }


}
