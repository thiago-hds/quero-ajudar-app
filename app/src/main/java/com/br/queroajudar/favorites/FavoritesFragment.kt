package com.br.queroajudar.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2

import com.br.queroajudar.R
import com.br.queroajudar.databinding.FragmentFavoritesBinding
import com.google.android.material.tabs.TabLayoutMediator


class FavoritesFragment : Fragment() {

    private lateinit var binding : FragmentFavoritesBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var favoritesAdapter: FavoritesCollectionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_favorites, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        favoritesAdapter = FavoritesCollectionAdapter(this)
        viewPager = binding.pager
        viewPager.adapter = favoritesAdapter

        val tabLayout = binding.tabLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = if (position == 0){
                getString(R.string.favoriteVacancies_title)
            } else {
                getString(R.string.favoriteOrganizations_title)
            }
        }.attach()
    }
}

class FavoritesCollectionAdapter(fragment: Fragment): FragmentStateAdapter(fragment){
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0){
            FavoriteVacanciesFragment()
        } else{
            FavoriteOrganizationsFragment()
        }
    }
}
