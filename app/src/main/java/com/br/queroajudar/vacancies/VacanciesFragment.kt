package com.br.queroajudar.vacancies

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.selection.*
import androidx.recyclerview.widget.RecyclerView

import com.br.queroajudar.R
import com.br.queroajudar.categories.CategoryAdapter
import com.br.queroajudar.categories.CategoryDetailsLookup
import com.br.queroajudar.categories.CategoryItemKeyProvider
import com.br.queroajudar.databinding.FragmentVacanciesBinding
import com.br.queroajudar.network.ResultWrapper
import com.br.queroajudar.register.MainActivity
import com.br.queroajudar.util.enable
import com.br.queroajudar.util.toggleViewExpansion2
import com.br.queroajudar.util.toggleViewRotation0to180
import kotlinx.android.synthetic.main.vacancies_filter_layout.view.*
import timber.log.Timber
import javax.inject.Inject

/**
 * Fragment que representa a tela de listagem de vagas
 *
 */

class VacanciesFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel : VacanciesViewModel

    lateinit var binding : FragmentVacanciesBinding

    private var isCauseFilterExpanded = false
    private var isSkillFilterExpanded = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as HomeActivity).homeComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //TODO colocar condicao
        goToCausesFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this, viewModelFactory)[VacanciesViewModel::class.java]


        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_vacancies, container, false
        )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setupVacanciesList()
        setupOrderBySpinner()
        setupFilters()
        setupListeners()

        return binding.root
    }

    private fun setupVacanciesList(){

        val adapter =
            VacancyAdapter(VacancyClickListener { vacancyId ->
                val action
                    = VacanciesFragmentDirections.actionVacanciesFragmentToVacancyDetailsFragment(vacancyId)
                findNavController().navigate(action)
            })

        binding.rvVacancies.adapter = adapter

        viewModel.vacancies.observe(viewLifecycleOwner, Observer { vacanciesPagedList ->
            Timber.i("vacancies change observed $vacanciesPagedList")
            adapter.submitList(vacanciesPagedList)
        })

//        viewModel.organizations.observe(viewLifecycleOwner, Observer { result ->
//            Timber.i("organizations change observed $result")
//            if(result is ResultWrapper.Success) {
//                adapter.setOrganizations(result.value)
//            }
//        })

//        viewModel.vacanciesSize.observe(viewLifecycleOwner,Observer{ size ->
//            Timber.i("vacanciesSize change observed: $size")
//            showEmptyList(size == 0)
//        })

        viewModel.vacanciesLoadInitialResultWrapper.observe(viewLifecycleOwner, Observer { result ->
            Timber.i("vacanciesLoadInitialResultWrapper change observed $result")
            binding.overlayNetworkStatus.result = result
        })

        viewModel.vacanciesLoadAfterResultWrapper.observe(viewLifecycleOwner, Observer { result ->
            Timber.i("vacanciesLoadAfterResultWrapper change observed $result")
            adapter.setResultWrapper(result)
        })
    }

    private fun setupOrderBySpinner(){
        context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.orderBy_array,
                R.layout.dropdown_menu_item
            ).also { adapter->
                adapter.setDropDownViewResource(R.layout.dropdown_menu_item)
                binding.vacanciesSpinnerOrderBy.adapter = adapter
            }
        }
    }

    private fun setupFilters(){
        // Filtro de Causas
        val causeAdapter = CategoryAdapter()
        binding.vacanciesFilterLayout.causesListLayout.causes_recyclerView.adapter = causeAdapter

        val causeTracker = setupSelectionTracker(
            binding.vacanciesFilterLayout.causesListLayout.causes_recyclerView,
            "causeSelection"
        ) {selectedCauses -> viewModel.onCauseItemSelected(selectedCauses)}
        causeAdapter.tracker = causeTracker

        viewModel.causes.observe(viewLifecycleOwner, Observer { result ->
            Timber.i("causes change observed $result")
            if(result is ResultWrapper.Success){
                result.value?.let{causeAdapter.submitList(it)}
            }
        })

        // Filtro de Habilidades
        val skillAdapter = CategoryAdapter()
        binding.vacanciesFilterLayout.skillsListLayout.skills_recyclerView.adapter = skillAdapter

        val skillTracker = setupSelectionTracker(
            binding.vacanciesFilterLayout.skillsListLayout.skills_recyclerView,
            "skillSelection"
        ) {selectedSkills -> viewModel.onSkillItemSelected(selectedSkills)}
        skillAdapter.tracker = skillTracker

        viewModel.skills.observe(viewLifecycleOwner, Observer { result ->
            Timber.i("skills change observed $result")
            if(result is ResultWrapper.Success){
                result.value?.let{ skillAdapter.submitList(it) }
            }
        })
    }

    private fun setupListeners(){
        binding.vacanciesBtnFilters.setOnClickListener {
            binding.vacanciesDlFilters.openDrawer(GravityCompat.END)
        }

        binding.vacanciesFilterLayout.layoutExpandCauseFilter.setOnClickListener {
            isCauseFilterExpanded = expandOrCollapseFilterLayout(
                binding.vacanciesFilterLayout.ivExpandCauseFilterArrow,
                binding.vacanciesFilterLayout.causesListLayout,
                isCauseFilterExpanded)
        }

        binding.vacanciesFilterLayout.layoutExpandSkillFilter.setOnClickListener {
            isSkillFilterExpanded = expandOrCollapseFilterLayout(
                binding.vacanciesFilterLayout.ivExpandSkillFilterArrow,
                binding.vacanciesFilterLayout.skillsListLayout,
                isSkillFilterExpanded)
        }

        binding.overlayNetworkStatus.btnTryAgain.setOnClickListener {
            viewModel.onTryAgainClicked()
        }
    }

    private fun expandOrCollapseFilterLayout(
        ivArrow: ImageView, listLayout:View, isExpanded:Boolean
    ) : Boolean {

        toggleViewRotation0to180(ivArrow, isExpanded)
        toggleViewExpansion2(listLayout,isExpanded)
        return !isExpanded
    }


    private fun setupSelectionTracker(
        recyclerView:RecyclerView,
        selectionId:String,
        onItemSelected : (items:List<Int>) -> Unit
    ): SelectionTracker<Long>? {

        val selectionTracker = SelectionTracker.Builder(
            selectionId,
            recyclerView,
            CategoryItemKeyProvider(recyclerView),
            CategoryDetailsLookup(recyclerView),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()

        selectionTracker.enable()

        selectionTracker?.addObserver(
            object : SelectionTracker.SelectionObserver<Long>() {
                override fun onSelectionChanged() {
                    super.onSelectionChanged()
                    if (selectionTracker.selection.size() > 0) {
                        Timber.i("selection observer: ${selectionTracker.selection}")
                        val items = selectionTracker.selection.mapNotNull{
                            if(it >= 0) it.toInt() else null
                        }
                        onItemSelected(items)
                    }
                }
            }
        )
        return selectionTracker
    }

    private fun showEmptyList(isEmpty : Boolean) {
        if (isEmpty) {
            binding.rvVacancies.visibility = View.INVISIBLE
            binding.tvRvVacanciesEmpty.visibility = View.VISIBLE
        } else {
            binding.rvVacancies.visibility = View.VISIBLE
            binding.tvRvVacanciesEmpty.visibility = View.GONE
        }
    }

    private fun goToCausesFragment(){
        findNavController().navigate(R.id.action_vacanciesFragment_to_causesFragment)
    }
}