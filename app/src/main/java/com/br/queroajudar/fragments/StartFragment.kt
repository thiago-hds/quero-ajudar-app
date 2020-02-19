package com.br.queroajudar.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation


import com.br.queroajudar.R
import com.br.queroajudar.databinding.FragmentStartBinding


class StartFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentStartBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_start, container, false)

        binding.btnMainRegister.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_startFragment_to_registerDataFragment)
        )

        binding.btnMainLogin.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_startFragment_to_loginFragment)
        )

        return binding.root
    }

}
