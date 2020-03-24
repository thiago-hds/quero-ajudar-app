package com.br.queroajudar.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager

import com.br.queroajudar.R
import com.br.queroajudar.databinding.FragmentVacanciesBinding
import com.br.queroajudar.view.adapters.*
import com.br.queroajudar.viewmodel.VacanciesViewModel
import okhttp3.internal.toImmutableList
import timber.log.Timber

class VacanciesFragment : Fragment() {

    private val viewModel: VacanciesViewModel by lazy {
        ViewModelProvider(this).get(VacanciesViewModel::class.java)
    }

    lateinit var binding : FragmentVacanciesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_vacancies, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        //setApiStatusObserver()
        setListeners()
        setupVacanciesList()
        setupFilters()

        return binding.root
    }

    private fun setListeners(){

        binding.vacanciesBtnFiters.setOnClickListener {
            binding.vacanciesDlFilters.openDrawer(GravityCompat.END)
        }

        val drawerListener = VacancyDrawerListener {
            Toast.makeText(context, "Drawer opened", Toast.LENGTH_LONG).show()
            viewModel.onDrawerOpened()
        }

        binding.vacanciesDlFilters.addDrawerListener(drawerListener)

    }

    private fun setupVacanciesList(){

        val adapter = VacancyAdapter(VacancyClickListener { vacancyId ->
            Toast.makeText(context, "$vacancyId", Toast.LENGTH_LONG).show()
            viewModel.onVacancyClicked()
        })



        val scrollListener = VacancyListScrollListener({
                viewModel.onListScrollReachEnd()
            },
            binding.vacanciesRecyclerView.layoutManager as LinearLayoutManager
        )

        binding.vacanciesRecyclerView.adapter = adapter
        binding.vacanciesRecyclerView.addOnScrollListener(scrollListener)

        viewModel.vacancies.observe(viewLifecycleOwner, Observer { vacancyList ->
            vacancyList?.let{
                adapter.submitList(it)
            }
        })
    }

    private fun setupFilters(){
        // Lista de Causas




        val causeAdapter = CauseAdapter(/*CauseClickListener { causeId ->
            Toast.makeText(context, "$causeId", Toast.LENGTH_LONG).show()
            viewModel.onCauseFilterItemClicked(causeId)
        }*/)

        binding.vacanciesFilterLayout.vacanciesRvCauses.adapter = causeAdapter

        var causeTracker = SelectionTracker.Builder<Long>(
            "mySelection",
            binding.vacanciesFilterLayout.vacanciesRvCauses,
            StableIdKeyProvider(binding.vacanciesFilterLayout.vacanciesRvCauses),
            CauseDetailsLookup(binding.vacanciesFilterLayout.vacanciesRvCauses),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()

        causeAdapter.tracker = causeTracker



        viewModel.causes.observe(viewLifecycleOwner, Observer { causeList ->
            Timber.tag("QueroAjudar.VacFrag").i("Cause list changed. Size is ${causeList.size}")
            causeList?.let{
                causeAdapter.submitList(it.toImmutableList())
                Timber.tag("QueroAjudar.VacFrag").i("Cause list submitted")
            }
        })



        // Lista de Habilidades
        val skillAdapter = SkillAdapter(SkillClickListener { skillId ->
            Toast.makeText(context, "$skillId", Toast.LENGTH_LONG).show()
        })

        binding.vacanciesFilterLayout.vacanciesRvSkills.adapter = skillAdapter

        viewModel.skills.observe(viewLifecycleOwner, Observer { skillList ->
            Timber.tag("QueroAjudar.VacFrag").i("Skill list changed. Size is ${skillList.size}")
            skillList?.let{
                skillAdapter.submitList(it.toList())
            }
        })
    }

    private fun showLoadingOverlay(){ binding.isLoadingProgressVisible = true}
    private fun hideLoadingOverlay(){binding.isLoadingProgressVisible = false}

    private fun showNetworkErrorMessage(){
        Toast.makeText(context, R.string.error_connection, Toast.LENGTH_LONG).show()
    }
}

class VacancyDrawerListener(val onDrawerOpened : () -> Unit) : DrawerLayout.SimpleDrawerListener(){
    override fun onDrawerOpened(drawerView: View) {
        super.onDrawerOpened(drawerView)

        onDrawerOpened()
    }
}


