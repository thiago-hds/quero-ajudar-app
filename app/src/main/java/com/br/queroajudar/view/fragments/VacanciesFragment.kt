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
import androidx.recyclerview.widget.LinearLayoutManager

import com.br.queroajudar.R
import com.br.queroajudar.databinding.FragmentVacanciesBinding
import com.br.queroajudar.view.adapters.*
import com.br.queroajudar.viewmodel.VacanciesViewModel
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
                adapter.submitList(vacancyList)
            }
        })
    }

    private fun setupFilters(){
        val adapter = CauseAdapter(CauseClickListener { causeId ->
            Toast.makeText(context, "$causeId", Toast.LENGTH_LONG).show()
        })

        binding.vacanciesFilterLayout.vacanciesRvCauses.adapter = adapter

        viewModel.causes.observe(viewLifecycleOwner, Observer { causeList ->
            causeList?.let{
                adapter.submitList(causeList)
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


