package ru.simaland.poster.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import ru.simaland.poster.R
import ru.simaland.poster.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMainBinding.inflate(inflater)

        binding.navigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.profile -> {
                    binding.fragmentContainer.findNavController().navigate(R.id.profileFragment)
                    true
                }
                R.id.jobs -> {
                    true
                }
                R.id.posts -> {
                    true
                }
                R.id.events -> {
                    binding.fragmentContainer.findNavController().navigate(R.id.eventFragment)
                    true
                }
                else -> throw Exception("Unknown item menu")
            }
        }

        return binding.root
    }
}