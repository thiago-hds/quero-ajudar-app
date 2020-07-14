package com.br.queroajudar.vacancydetails

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager

import com.br.queroajudar.R
import com.br.queroajudar.categories.CategoryAdapter
import com.br.queroajudar.databinding.FragmentVacancyDetailsBinding
import com.br.queroajudar.network.ResultWrapper
import com.br.queroajudar.util.Constants.RECURRENT
import com.br.queroajudar.util.Constants.UNIQUE_EVENT
import com.br.queroajudar.vacancies.HomeActivity
import com.br.queroajudar.vacancies.VacanciesViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.vacancies_filter_layout.view.*
import timber.log.Timber
import javax.inject.Inject

class VacancyDetailsFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel : VacancyDetailsViewModel

    lateinit var binding : FragmentVacancyDetailsBinding

    private val args: VacancyDetailsFragmentArgs by navArgs()

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
        inflater?.inflate(R.menu.vacancy_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean  = when (item.itemId) {
        R.id.favorite_vacancy -> {
            val response = viewModel.favoriteVacancy()
            response.observe(viewLifecycleOwner, Observer {result ->
                if(result is ResultWrapper.Success){
                    Snackbar.make(binding.root, R.string.fragment_favorites_label, Snackbar.LENGTH_SHORT)
                    //TODO mudar cor do botao de favoritar
                }
                else{
                    Toast.makeText(activity, R.string.app_name, Toast.LENGTH_SHORT)
                }
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

        val causeAdapter = CategoryAdapter()
        val skillAdapter = CategoryAdapter()

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

            }
        })
    }

    private fun setupListeners(){
        binding.overlayNetworkStatus.btnTryAgain.setOnClickListener {
            // TODO
        }
    }
}
