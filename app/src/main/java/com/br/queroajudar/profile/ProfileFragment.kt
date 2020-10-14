package com.br.queroajudar.profile

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager

import com.br.queroajudar.R
import com.br.queroajudar.categories.CategoryAdapter
import com.br.queroajudar.data.User
import com.br.queroajudar.databinding.FragmentProfileBinding
import com.br.queroajudar.databinding.FragmentVacancyDetailsBinding
import com.br.queroajudar.network.ResultWrapper
import com.br.queroajudar.vacancies.HomeActivity
import com.br.queroajudar.vacancydetails.VacancyDetailsViewModel
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var skillAdapter: CategoryAdapter
    @Inject
    lateinit var causeAdapter: CategoryAdapter

    private lateinit var viewModel : ProfileViewModel
    lateinit var binding : FragmentProfileBinding
    lateinit var menu: Menu

    lateinit var user: User

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as HomeActivity).homeComponent.inject(this)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_profile,menu)
        this.menu = menu
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this, viewModelFactory)[ProfileViewModel::class.java]

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_profile, container, false
        )

        binding.lifecycleOwner = this
        binding.rvCauses.layoutManager = GridLayoutManager(activity,2)
        binding.rvCauses.adapter = causeAdapter
        binding.rvSkills.layoutManager = GridLayoutManager(activity,2)
        binding.rvSkills.adapter = skillAdapter

        setHasOptionsMenu(true)
        setupObservers()
        setupListeners()

        return binding.root
    }

    private fun setupObservers(){
        viewModel.profile.observe(viewLifecycleOwner, Observer { result ->
            binding.overlayNetworkStatus.result = result
            if(result is ResultWrapper.Success){
                user = result.value
                binding.user = user
                user.causes?.let {
                    causeAdapter.submitList(it)
                    if(it.isEmpty()){
                        binding.rvCauses.visibility = View.GONE
                        binding.tvCausesEmpty.visibility = View.VISIBLE
                    }
                }
                user.skills?.let {
                    skillAdapter.submitList(it)
                    if(it.isEmpty()){
                        binding.rvSkills.visibility = View.GONE
                        binding.tvSkillsEmpty.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    private fun setupListeners(){
        binding.btnEditProfile.setOnClickListener { goToRegisterFragment() }
    }

    private fun goToRegisterFragment(){
        val action =
            ProfileFragmentDirections.actionProfileFragmentToRegisterFragment(user)
        findNavController().navigate(action)
    }

}
