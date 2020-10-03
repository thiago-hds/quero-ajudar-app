package com.br.queroajudar.vacancydetails

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager

import com.br.queroajudar.R
import com.br.queroajudar.categories.CategoryAdapter
import com.br.queroajudar.databinding.FragmentVacancyDetailsBinding
import com.br.queroajudar.network.ResultWrapper
import com.br.queroajudar.util.Constants.RECURRENT
import com.br.queroajudar.vacancies.HomeActivity
import com.br.queroajudar.vacancies.VacanciesFragmentDirections
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
import javax.inject.Inject


class VacancyDetailsFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var skillAdapter: CategoryAdapter
    @Inject
    lateinit var causeAdapter: CategoryAdapter


    private lateinit var viewModel : VacancyDetailsViewModel

    lateinit var binding : FragmentVacancyDetailsBinding

    private val args: VacancyDetailsFragmentArgs by navArgs()

    lateinit var menu: Menu

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as HomeActivity).homeComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this, viewModelFactory)[VacancyDetailsViewModel::class.java]
        viewModel.id = args.id

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_vacancy_details, container, false
        )

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setHasOptionsMenu(true)

        setupVacancyDetails()
        setupListeners()

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_vacancy_details,menu)
        this.menu = menu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean  = when (item.itemId) {
        R.id.favorite_vacancy -> {
            val response = viewModel.favoriteVacancy()
            item.isEnabled = false
            response.observe(viewLifecycleOwner, Observer {result ->
                Timber.i("favoriteVacancy response $result")
                if(result is ResultWrapper.Success){
                    val strId: Int
                    val iconId: Int
                    if (result.value){
                        strId = R.string.vacancyDetails_favorite_success
                        iconId = R.drawable.ic_favorite_24dp
                    }
                    else{
                        strId = R.string.vacancyDetails_unfavorite_success
                        iconId = R.drawable.ic_baseline_favorite_border_24
                    }
                    item.setIcon(iconId)

                    Snackbar.make(
                        binding.root, strId,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                else if(result is ResultWrapper.NetworkError || result is ResultWrapper.GenericError){
                    Toast.makeText(
                        activity, R.string.vacancyDetails_favorite_error, Toast.LENGTH_SHORT
                    ).show()
                }
                item.isEnabled = true
            })
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }


    private fun setupVacancyDetails(){
        binding.rvCauses.layoutManager = GridLayoutManager(activity,2)
        binding.rvSkills.layoutManager = GridLayoutManager(activity,2)


        binding.rvCauses.adapter = causeAdapter
        binding.rvSkills.adapter = skillAdapter

        viewModel.vacancy.observe(viewLifecycleOwner, Observer { vacancyResult ->
            Timber.i("vacancy change observed $vacancyResult")
            binding.overlayNetworkStatus.result = vacancyResult
            if(vacancyResult is ResultWrapper.Success) {
                val vacancy = vacancyResult.value
                binding.vacancy = vacancy

                vacancy.causes?.let { causeAdapter.submitList(it) }
                vacancy.skills?.let { skillAdapter.submitList(it) }

                if(vacancy.causes?.size > 0){
                    binding.tvLabelCauses.visibility = View.GONE
                    binding.rvCauses.visibility = View.GONE
                }

                if(vacancy.skills?.size > 0){
                    binding.tvLabelSkills.visibility = View.GONE
                    binding.rvSkills.visibility = View.GONE
                }

                if(vacancy.type == RECURRENT){
                    binding.ivDate.visibility = View.GONE
                    binding.tvDate.visibility = View.GONE
                }
                else{
                    binding.tvFrequency.visibility = View.GONE
                }

                if(vacancy.favorite){
                    menu.findItem(R.id.favorite_vacancy).setIcon(R.drawable.ic_favorite_24dp)
                }
                menu.findItem(R.id.favorite_vacancy).isVisible = true

                if(vacancy.application != null && vacancy.application.status == 1){
                    binding.btnApply.backgroundTintList = context?.let {
                        ContextCompat.getColorStateList(
                            it, R.color.colorRed
                        )
                    }
                    binding.btnApply.text = getString(R.string.vacancyDetails_btnApplyCancel)
                }
                else if (vacancy.application != null){
                    binding.btnApply.isEnabled = false
                }

                binding.btnOrganization.setOnClickListener {
                    val action =
                        VacancyDetailsFragmentDirections
                            .actionVacancyDetailsFragmentToOrganizationDetailsFragment(
                                vacancy.organization.id
                            )
                    findNavController().navigate(action)
                }

                binding.btnApply.setOnClickListener {
                    val action =
                        VacancyDetailsFragmentDirections
                            .actionVacancyDetailsFragmentToVacancyApplicationFragment(
                                vacancy
                            )
                    findNavController().navigate(action)
                }
            }
        })
    }

    private fun setupListeners(){
        binding.overlayNetworkStatus.btnTryAgain.setOnClickListener {
            // TODO
        }
    }
}
