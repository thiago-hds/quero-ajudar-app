package com.br.queroajudar.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.selection.*
import androidx.recyclerview.widget.RecyclerView

import com.br.queroajudar.R
import com.br.queroajudar.databinding.FragmentVacanciesBinding
import com.br.queroajudar.util.enable
import com.br.queroajudar.util.toggleViewExpansion2
import com.br.queroajudar.util.toggleViewRotation0to180
import com.br.queroajudar.view.adapters.*
import com.br.queroajudar.viewmodel.VacanciesViewModel
import kotlinx.android.synthetic.main.vacancies_filter_layout.view.*
import okhttp3.internal.toImmutableList
import timber.log.Timber

class VacanciesFragment : Fragment() {

    private val viewModel: VacanciesViewModel by lazy {
        ViewModelProvider(this).get(VacanciesViewModel::class.java)
    }

    lateinit var binding : FragmentVacanciesBinding

    var isCauseFilterExpanded = false
    var isSkillFilterExpanded = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_vacancies, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        //setApiStatusObserver()

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


//        val scrollListener = VacancyListScrollListener({
//                viewModel.onListScrollReachEnd()
//            },
//            binding.vacanciesRecyclerView.layoutManager as LinearLayoutManager
//        )

        binding.rvVacancies.adapter = adapter


//        binding.vacanciesOverlayLoading.loadingOverlayApiStatus = viewModel.getVacanciesStatus.value
//        binding.vacanciesRecyclerView.addOnScrollListener(scrollListener)

//        viewModel.vacancies.observe(viewLifecycleOwner, Observer { vacancyList ->
//            Timber.tag("QA.VacanciesFragment").i("vacancies list changed")
//            vacancyList?.let{
//                adapter.submitList(it.toImmutableList())
//            }
//        })


        viewModel.vacancies.observe(viewLifecycleOwner, Observer { vacanciesPagedList ->
            Timber.tag("QA.VacanciesFragment").i("vacancies observed $vacanciesPagedList")
            adapter.submitList(vacanciesPagedList)
        })

        viewModel.vacanciesSize.observe(viewLifecycleOwner,Observer{ size ->
            Timber.tag("QA.VacanciesFragment").i("vacancies size changed $size")
            showEmptyList(size == 0)
        })

        viewModel.vacanciesLoadAfterApiStatus.observe(viewLifecycleOwner, Observer {
            Timber.tag("QA.VacanciesFragment").i("getVacanciesStatus changed $it")
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
                //binding.vacanciesSpinnerOrderBy.setText(R.string.vacancies_btn_recommended)
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

        viewModel.causes.observe(viewLifecycleOwner, Observer { causeList ->
            Timber.tag("QueroAjudar.VacFrag").i("Cause list changed. Size is ${causeList.size}")
            causeList?.let {
                causeAdapter.submitList(it.toImmutableList())
                Timber.tag("QueroAjudar.VacFrag").i("Cause list submitted")
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

        viewModel.skills.observe(viewLifecycleOwner, Observer { skillList ->
            Timber.tag("QueroAjudar.VacFrag").i("Skill list changed. Size is ${skillList.size}")
            skillList?.let {
                skillAdapter.submitList(it.toImmutableList())
                Timber.tag("QueroAjudar.VacFrag").i("Skill list submitted")
            }
        })

    }

    private fun setListeners(){
        binding.vacanciesBtnFilters.setOnClickListener {
            binding.vacanciesDlFilters.openDrawer(GravityCompat.END)
        }

        binding.vacanciesDlFilters.addDrawerListener(VacancyDrawerListener {
            viewModel.onDrawerOpened()
        })

        binding.vacanciesFilterLayout.layoutExpandCauseFilter.setOnClickListener {
            isCauseFilterExpanded = expandOrCollapseFilterLayout(
                binding.vacanciesFilterLayout.ivExpandCauseFilterArrow,
                binding.vacanciesFilterLayout.causesListLayout,
                isCauseFilterExpanded,binding.vacanciesFilterLayout.tvSelectedCauses)
        }

        binding.vacanciesFilterLayout.layoutExpandSkillFilter.setOnClickListener {
            isSkillFilterExpanded = expandOrCollapseFilterLayout(
                binding.vacanciesFilterLayout.ivExpandSkillFilterArrow,
                binding.vacanciesFilterLayout.skillsListLayout,
                isSkillFilterExpanded,binding.vacanciesFilterLayout.tvSelectedSkills)
        }

        binding.vacanciesOverlayLoading.btnTryAgain.setOnClickListener {
            viewModel.onTryAgainClicked()
        }
    }

    private fun expandOrCollapseFilterLayout(ivArrow: ImageView, listLayout:View,
                                             isExpanded:Boolean, tvCategoriesSelected : TextView
    ) : Boolean {
        toggleViewRotation0to180(ivArrow, isExpanded)

        toggleViewExpansion2(listLayout,isExpanded)

//        if(isExpanded){
//            tvCategoriesSelected.visibility = View.VISIBLE
//        }
//        else{
//            tvCategoriesSelected.visibility = View.INVISIBLE
//        }

        return !isExpanded
    }


    private fun setupSelectionTracker(recyclerView:RecyclerView, selectionId:String,
                                      onItemSelected : (items:List<Int>) -> Unit): SelectionTracker<Long>? {
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


