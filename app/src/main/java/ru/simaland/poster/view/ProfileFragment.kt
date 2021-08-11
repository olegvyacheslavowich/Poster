package ru.simaland.poster.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import ru.simaland.poster.BuildConfig
import ru.simaland.poster.R
import ru.simaland.poster.databinding.FragmentProfileBinding
import ru.simaland.poster.db.AppDatabase
import ru.simaland.poster.db.entity.toDto
import ru.simaland.poster.util.loadImg
import ru.simaland.poster.viewmodel.AuthViewModel

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentProfileBinding.inflate(inflater)
        val viewModel: AuthViewModel by viewModels()

        with(binding) {
            viewModel.getCurrentUser()
            viewModel.currentUser.observe(viewLifecycleOwner) { user ->
                nameTextView.text = user.name
                avatarImageView.loadImg(user.avatar)
            }

            logoutButton.setOnClickListener {
                viewModel.logout()
                findNavController().navigate(R.id.action_profileFragment_to_mainActivity)
            }
        }
        return binding.root
    }
}
