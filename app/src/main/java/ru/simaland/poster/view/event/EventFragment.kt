package ru.simaland.poster.view.event

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toFile
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ru.simaland.poster.R
import ru.simaland.poster.databinding.FragmentEventBinding
import ru.simaland.poster.model.Event
import ru.simaland.poster.state.EventsState
import ru.simaland.poster.util.*
import ru.simaland.poster.viewmodel.EventViewModel

@AndroidEntryPoint
class EventFragment : Fragment() {

    private lateinit var binding: FragmentEventBinding
    private val viewModel: EventViewModel by viewModels(::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentEventBinding.inflate(inflater)

        viewModel.dataState.observe(viewLifecycleOwner) {
            when (it) {
                is EventsState.Loading -> {
                    binding.progressBar.isVisible = true
                }
                is EventsState.Error -> {
                    Snackbar.make(
                        binding.root,
                        it.code.getInfo(),
                        Snackbar.LENGTH_LONG
                    ).show()
                    binding.progressBar.isVisible = false
                }
                is EventsState.Success -> {
                    binding.progressBar.isVisible = false
                }
            }
        }

        viewModel.currentEvent.observe(viewLifecycleOwner) { event ->
            if (event == null) {
                return@observe
            }

            viewModel.loadSpeakers(event.speakerIds)
            with(binding) {
                attachmentImageView.loadImg(
                    event.attachment?.url ?: "",
                    R.drawable.ic_baseline_error_outline_24
                )
                contentEditText.setText(event.content)
                authorTextView.text = event.author
                dateTimeTextView.text = event.datetime.parseToStringDate()
                typeTextView.text = event.type
            }
        }

        val adapter = SpeakersAdapter()
        viewModel.speakers.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.editMode.observe(viewLifecycleOwner) { isEditMode ->
            if (isEditMode) {
                binding.contentEditText.isEnabled = true
                binding.contentEditText.requestFocus()
                binding.saveFab.visibility = View.VISIBLE
                binding.attachmentCardView.isClickable = true
                binding.changeFab.visibility = View.GONE
                binding.speakersAdd.visibility = View.VISIBLE

            } else {
                binding.contentEditText.isEnabled = false
                binding.saveFab.visibility = View.GONE
                binding.attachmentCardView.isClickable = false
                binding.changeFab.visibility = View.VISIBLE
                binding.speakersAdd.visibility = View.GONE
            }
        }

        viewModel.photo.observe(viewLifecycleOwner) { photoModel ->
            binding.attachmentImageView.setImageURI(photoModel.uri)
        }

        binding.typeTextView.setOnClickListener {
            if (viewModel.isEditMode()) {
                val builder = AlertDialog.Builder(requireContext()).apply {
                    setTitle(R.string.choose_event_type)
                    setItems(R.array.choose_event_types) { _, which ->
                        when (which) {
                            0 -> binding.typeTextView.setText(R.string.event_type_online)
                            1 -> binding.typeTextView.setText(R.string.event_type_offline)
                        }
                    }
                }
                builder.show()
            }
        }

        binding.speakersAdd.setOnClickListener {
            findNavController().navigate(R.id.action_eventFragment_to_usersFragment)
        }

        binding.attachmentCardView.setOnClickListener {
            if (viewModel.isEditMode())
                this.buildPhotoMenu().show()
        }

        binding.changeFab.setOnClickListener {
            viewModel.changeEditMode(true)
        }

        binding.saveFab.setOnClickListener {
            viewModel.save(
                Event(
                    content = binding.contentEditText.text.toString(),
                    type = binding.typeTextView.text.toString()
                )
            )
        }

        requireActivity().onBackPressedDispatcher.addCallback {
            viewModel.onBackPressed()
            findNavController().navigateUp()
        }

        binding.speakersRecyclerView.adapter = adapter
        binding.speakersRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        return binding.root

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