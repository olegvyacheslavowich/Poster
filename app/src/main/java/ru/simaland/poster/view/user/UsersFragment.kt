package ru.simaland.poster.view.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import ru.simaland.poster.R
import ru.simaland.poster.databinding.CardUserBinding
import ru.simaland.poster.databinding.FragmentUserBinding

class UsersFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentUserBinding.inflate(inflater)

        binding.usersRecyclerView.adapter = UserAdapter()
        binding.usersRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        return binding.root
    }
}