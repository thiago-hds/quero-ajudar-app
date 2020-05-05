package com.br.queroajudar.view.fragments

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
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.selection.*
import androidx.recyclerview.widget.RecyclerView

import com.br.queroajudar.R
import com.br.queroajudar.databinding.FragmentVacanciesBinding
import com.br.queroajudar.network.ResultWrapper
import com.br.queroajudar.util.enable
import com.br.queroajudar.util.toggleViewExpansion2
import com.br.queroajudar.util.toggleViewRotation0to180
import com.br.queroajudar.view.HomeActivity
import com.br.queroajudar.view.adapters.*
import com.br.queroajudar.viewmodel.VacanciesViewModel
import kotlinx.android.synthetic.main.vacancies_filter_layout.view.*
import okhttp3.internal.toImmutableList
import timber.log.Timber
import javax.inject.Inject

class VacanciesFragment : Fragment() {

//    private val viewModel: VacanciesViewModel by lazy {
//        ViewModelProvider(this).get(VacanciesViewModel::class.java)
//    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel : VacanciesViewModel

    lateinit var binding : FragmentVacanciesBinding

    var isCauseFilterExpanded = false
    var isSkillFilterExpanded = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as HomeActivity).homeComponent.inject(this)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this, viewModelFactory)[VacanciesViewModel::class.java]

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_vacancies, container, false
        )

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setupVacanciesList()
        setupOrderBySpinner()
        setupFilters()

        setListeners()

        return binding.root
    }



    private fun setupVacanciesList(){

        val adapter = VacancyAdapter(VacancyClickListener { vacancyId ->
            Toast.makeText(context, "$vacancyId", Toast.LENGTH_LONG).show()
            viewModel.onVacancyClicked()
        })

        binding.rvVacancies.adapter = adapter

        viewModel.vacancies.observe(viewLifecycleOwner, Observer { vacanciesPagedList ->
            Timber.i("vacancies observed $vacanciesPagedList")
            adapter.submitList(vacanciesPagedList)
        })

        viewModel.organizations.observe(viewLifecycleOwner, Observer { result ->
            Timber.i("organizations list changed $result")
            when(result){
                is ResultWrapper.Success -> result.value.data?.let { adapter.setOrganizations(it) }
                is ResultWrapper.GenericError -> { /*TODO*/}
                is ResultWrapper.NetworkError -> {/*TODO*/}
            }
        })

//        viewModel.vacanciesSize.observe(viewLifecycleOwner,Observer{ size ->
//            Timber.i("vacancies size changed $size")
//            showEmptyList(size == 0)
//        })

        viewModel.vacanciesLoadAfterApiStatus.observe(viewLifecycleOwner, Observer {
            Timber.i("getVacanciesStatus changed $it")
            adapter.setApiStatus(it)
        })
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
            when(result){
                is ResultWrapper.Success -> { result.value.data?.let{causeAdapter.submitList(it)}}
                is ResultWrapper.Loading -> {}
                is ResultWrapper.NetworkError -> {}
                is ResultWrapper.GenericError -> {}
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

//        viewModel.skills.observe(viewLifecycleOwner, Observer { skillList ->
//            Timber.i("Skill list changed. Size is ${skillList.size}")
//            skillList?.let {
//                skillAdapter.submitList(it.toImmutableList())
//                Timber.i("Skill list submitted")
//            }
//        })

        viewModel.skills.observe(viewLifecycleOwner, Observer { result ->
            when(result){
                is ResultWrapper.Success -> { result.value.data?.let{skillAdapter.submitList(it)}}
                is ResultWrapper.Loading -> {}
                is ResultWrapper.NetworkError -> {}
                is ResultWrapper.GenericError -> {}
            }
        })

    }

    private fun setListeners(){
        binding.vacanciesBtnFilters.setOnClickListener {
            binding.vacanciesDlFilters.openDrawer(GravityCompat.END)
        }

        binding.vacanciesDlFilters.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(newState: Int) {}

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}

            override fun onDrawerClosed(drawerView: View) {}

            override fun onDrawerOpened(drawerView: View) {
                viewModel.onDrawerOpened()
            }

        })

//        binding.vacanciesDlFilters.addDrawerListener(VacancyDrawerListener {
//            viewModel.onDrawerOpened()
//        })

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

        binding.vacanciesOverlayLoading.btnTryAgain.setOnClickListener {
            viewModel.onTryAgainClicked()
        }
    }

    private fun expandOrCollapseFilterLayout(ivArrow: ImageView, listLayout:View,
                                             isExpanded:Boolean) : Boolean {
        toggleViewRotation0to180(ivArrow, isExpanded)

        toggleViewExpansion2(listLayout,isExpanded)

        return !isExpanded
    }


    private fun setupSelectionTracker(recyclerView:RecyclerView,
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
                        Timber.tag("QA.VacanciesFragment")
                            .i("selection observer: ${selectionTracker.selection}")
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
}

//class VacancyDrawerListener(val onDrawerOpened : () -> Unit) : DrawerLayout.SimpleDrawerListener(){
//    override fun onDrawerOpened(drawerView: View) {
//        super.onDrawerOpened(drawerView)
//        onDrawerOpened()
//    }
//}