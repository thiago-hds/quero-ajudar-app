package com.br.queroajudar.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController


import com.br.queroajudar.R
import com.br.queroajudar.databinding.FragmentStartBinding
import com.br.queroajudar.profile.ProfileFragmentDirections

class StartFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentStartBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_start, container, false
        )

        binding.btnRegister.setOnClickListener {
//            findNavController().navigate(
//                StartFragmentDirections.actionStartFragmentToRegisterDataFragment(null)
//            )
            findNavController().navigate(
                R.id.action_startFragment_to_registerDataFragment
            )
        }

        binding.btnLogin.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.action_startFragment_to_loginFragment
            )

        )
        return binding.root
    }
}
