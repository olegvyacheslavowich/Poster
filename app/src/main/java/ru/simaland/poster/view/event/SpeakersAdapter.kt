package ru.simaland.poster.view.event

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.simaland.poster.R
import ru.simaland.poster.databinding.CardSpeakerBinding
import ru.simaland.poster.model.User
import ru.simaland.poster.util.loadImg

class SpeakersAdapter :
    ListAdapter<User, SpeakersAdapter.SpeakersViewHolder>(SpeakersItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpeakersViewHolder {
        val binding = CardSpeakerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SpeakersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SpeakersViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    class SpeakersViewHolder(private val binding: CardSpeakerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {
            with(binding) {
                user.avatar?.let {
                    avatarImageView.loadImg(it, R.drawable.ic_baseline_person_outline_24)
                }
                nameTextView.text = user.name
            }
        }
    }

    class SpeakersItemCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            if (oldItem::class != newItem::class) {
                return false
            }

            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }

    }
}


