package ru.simaland.poster.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ru.simaland.poster.R
import ru.simaland.poster.databinding.FragmentLoginBinding
import ru.simaland.poster.state.AuthState
import ru.simaland.poster.util.getInfo
import ru.simaland.poster.viewmodel.AuthViewModel

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var loginClicked = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel: AuthViewModel by viewModels()
        val binding = FragmentLoginBinding.inflate(inflater)

        with(binding) {
            loginButton.setOnClickListener {
                if (loginClicked) {
                    viewModel.login(
                        loginEditText.text.toString(),
                        passwordEditText.text.toString()
                    )
                } else {
                    loginClicked = true
                    loginEditText.animate()
                        .alpha(1.0f)
                        .setDuration(700)
                        .start()
                    passwordEditText.animate()
                        .alpha(1.0f)
                        .setDuration(700)
                        .start()
                }
            }

            viewModel.authData.observe(viewLifecycleOwner) {
                if (it == null || it.id == 0 || it.token == "") {
                    return@observe
                }
                findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
            }

            viewModel.state.observe(viewLifecycleOwner) {
                when (it) {
                    is AuthState.Loading -> {
                        progressBar.isVisible = true
                    }
                    is AuthState.Error -> {
                        Snackbar.make(
                            binding.root,
                            it.code.getInfo(),
                            Snackbar.LENGTH_LONG
                        ).show()
                        progressBar.isVisible = false
                    }
                    is AuthState.Success -> {
                        progressBar.isVisible = false
                    }
                }
            }

            registrationButton.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
            }
        }

        return binding.root
    }

}