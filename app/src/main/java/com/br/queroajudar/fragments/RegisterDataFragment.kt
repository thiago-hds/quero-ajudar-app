package com.br.queroajudar.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import com.br.queroajudar.R
import com.br.queroajudar.databinding.FragmentRegisterDataBinding

/**
 * A simple [Fragment] subclass.
 */
class RegisterDataFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentRegisterDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_register_data, container, false)
        return binding.root
    }


}
