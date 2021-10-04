package ru.simaland.poster.view.auth

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toFile
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ru.simaland.poster.R
import ru.simaland.poster.databinding.FragmentRegistrationBinding
import ru.simaland.poster.state.AuthState
import ru.simaland.poster.util.buildPhotoMenu
import ru.simaland.poster.util.cameraRequestCode
import ru.simaland.poster.util.photoRequestCode
import ru.simaland.poster.viewmodel.AuthViewModel

@AndroidEntryPoint
class RegistrationFragment : Fragment() {

    private val viewModel: AuthViewModel by viewModels()
    private lateinit var binding: FragmentRegistrationBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentRegistrationBinding.inflate(inflater)

        with(binding) {

            avatarCardView.setOnClickListener {
                this@RegistrationFragment.buildPhotoMenu().show()
            }

            createButton.setOnClickListener {
                viewModel.register(
                    nameTextInputEditText.text.toString(),
                    loginTextInputEditText.text.toString(),
                    passwordTextInputEditText.text.toString()
                )
            }

            viewModel.state.observe(viewLifecycleOwner) {
                when (it) {
                    is AuthState.Loading -> {
                        progressBar.isVisible = true
                    }
                    is AuthState.Error -> {
                        Snackbar.make(
                            binding.root,
                            R.string.registration_error,
                            Snackbar.LENGTH_LONG
                        ).show()
                        progressBar.isVisible = false
                    }
                    is AuthState.Success -> {
                        findNavController().apply {
                            navigate(graph.startDestination)
                        }
                        progressBar.isVisible = false
                    }
                }
            }

            viewModel.photo.observe(viewLifecycleOwner) { photoModel ->
                avatarImageView.setImageURI(photoModel.uri)
            }

            return binding.root
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode == ImagePicker.RESULT_ERROR) {
            Snackbar.make(binding.root, ImagePicker.getError(data), Snackbar.LENGTH_SHORT)
                .show()

            return
        }

        if (resultCode == Activity.RESULT_OK && (requestCode == cameraRequestCode || requestCode == photoRequestCode)) {
            val uri = data?.data
            val file = uri?.toFile()
            viewModel.changePhoto(uri, file)
        }
    }
}

