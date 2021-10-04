package ru.simaland.poster.view.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.simaland.poster.R
import ru.simaland.poster.databinding.FragmentProfileBinding
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
                user.avatar?.let {
                    avatarImageView.loadImg(user.avatar, R.drawable.ic_baseline_person_24)
                }

            }

            logoutButton.setOnClickListener {
                viewModel.logout()
            }
        }
        return binding.root
    }
}
