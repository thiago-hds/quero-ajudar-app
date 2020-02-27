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
import com.br.queroajudar.viewmodel.RegisterViewModel

/**
 * A simple [Fragment] subclass.
 */
class RegisterDataFragment : Fragment() {

    private lateinit var registerViewModel : RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerViewModel = activity?.run{
            ViewModelProvider(this).get(RegisterViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentRegisterDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_register_data, container, false)


        binding.viewModel = registerViewModel


        return binding.root
    }
}
