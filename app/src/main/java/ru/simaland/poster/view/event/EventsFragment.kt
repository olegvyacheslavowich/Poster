package ru.simaland.poster.view.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import ru.simaland.poster.R
import ru.simaland.poster.databinding.FragmentEventsBinding
import ru.simaland.poster.model.Event
import ru.simaland.poster.state.EventsState
import ru.simaland.poster.viewmodel.EventViewModel

@AndroidEntryPoint
class EventsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentEventsBinding.inflate(inflater)
        val viewModel: EventViewModel by viewModels(::requireParentFragment)

        val adapter = EventAdapter(object : OnInteractionListener {
            override fun onClicked(event: Event) {
                viewModel.onEventClicked(event)
            }
        })

        lifecycleScope.launchWhenCreated {
            viewModel.data.collect { events ->
                adapter.submitList(events)
            }
        }

        viewModel.dataState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is EventsState.Success -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                }
                is EventsState.Loading -> {
                    binding.swipeRefreshLayout.isRefreshing = true
                }
                is EventsState.Error -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    Snackbar.make(
                        binding.root,
                        state.error.toString(),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }

        viewModel.currentEvent.observe(viewLifecycleOwner) { event ->
            if (event == null) {
                return@observe
            }
            findNavController().navigate(
                R.id.action_eventsFragment_to_eventFragment
            )
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadLast()
        }

        binding.addFab.setOnClickListener {
            viewModel.addEvent()
        }

        binding.eventsRecyclerView.adapter = adapter

        return binding.root
    }

}