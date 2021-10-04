package ru.simaland.poster.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import dagger.hilt.android.AndroidEntryPoint
import ru.simaland.poster.R
import ru.simaland.poster.databinding.FragmentMainBinding
import ru.simaland.poster.viewmodel.AuthViewModel

@AndroidEntryPoint
class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMainBinding.inflate(inflater)
        val viewModel: AuthViewModel by viewModels()

        val bottomNavigationView = binding.navigationView
        val navController =
            (childFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment).navController
        NavigationUI.setupWithNavController(bottomNavigationView, navController)

        viewModel.authData.observe(viewLifecycleOwner) {
            if (it == null || it.id == 0 || it.token == "") {
                findNavController().navigate(R.id.action_mainFragment_to_loginFragment)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback {
            requireActivity().finish()
        }
        return binding.root
    }
}