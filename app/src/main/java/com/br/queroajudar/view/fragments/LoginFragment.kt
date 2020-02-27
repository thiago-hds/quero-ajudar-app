package com.br.queroajudar.view.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider

import com.br.queroajudar.R
import com.br.queroajudar.databinding.FragmentLoginBinding
import com.br.queroajudar.viewmodel.LoginViewModel

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {

    val loginViewModel: LoginViewModel by lazy {
        ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentLoginBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = loginViewModel

        return binding.root
    }


}
