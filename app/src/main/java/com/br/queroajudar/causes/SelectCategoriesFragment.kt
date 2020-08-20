package com.br.queroajudar.causes

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy

import com.br.queroajudar.R
import com.br.queroajudar.categories.CategoryAdapter
import com.br.queroajudar.categories.CategoryDetailsLookup
import com.br.queroajudar.categories.CategoryItemKeyProvider
import com.br.queroajudar.databinding.FragmentSelectCategoriesBinding
import com.br.queroajudar.network.ResultWrapper
import com.br.queroajudar.util.enable
import com.br.queroajudar.vacancies.HomeActivity
import com.br.queroajudar.vacancydetails.VacancyDetailsFragmentArgs
import timber.log.Timber
import javax.inject.Inject

class SelectCategoriesFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var adapter: CategoryAdapter

    lateinit var viewModel : SelectCategoriesViewModel
    private lateinit var binding : FragmentSelectCategoriesBinding

    private val args: SelectCategoriesFragmentArgs by navArgs()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as HomeActivity).homeComponent.inject(this)
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this, viewModelFactory)[SelectCategoriesViewModel::class.java]
        viewModel.categoryType = args.categoryType

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_select_categories, container, false
        )

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setupCategoriesList()
        setupObservers()

        return binding.root
    }

    private fun setupCategoriesList(){
        val recyclerView = binding.categoryRecyclerView

        recyclerView.adapter = adapter

        val selectionTracker = SelectionTracker.Builder(
            "categorySelection",
            recyclerView,
            CategoryItemKeyProvider(recyclerView),
            CategoryDetailsLookup(recyclerView),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()

        selectionTracker.enable()

        adapter.tracker = selectionTracker
    }

    private fun setupObservers(){
        viewModel.categories?.observe(viewLifecycleOwner, Observer { result ->
            Timber.i("categories change observed $result")
            binding.overlayNetworkStatus.result = result

            if(result is ResultWrapper.Success) {
                adapter.submitList(result.value)
            }
        })
    }

}
