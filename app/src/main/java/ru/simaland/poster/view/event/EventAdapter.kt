package ru.simaland.poster.view.event

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.simaland.poster.R
import ru.simaland.poster.databinding.CardEventBinding
import ru.simaland.poster.model.Event
import ru.simaland.poster.util.loadImg

interface OnInteractionListener {
    fun onClicked(event: Event)
}


class EventAdapter(private val onInteractionListener: OnInteractionListener) :
    ListAdapter<Event, EventAdapter.EventViewHolder>(EventDiffCallback()) {
    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = CardEventBinding.inflate(LayoutInflater.from(parent.context))
        return EventViewHolder(binding, onInteractionListener)
    }

    class EventViewHolder(
        private val binding: CardEventBinding,
        private val onInteractionListener: OnInteractionListener
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event) {

            with(binding) {
                contentTextView.text = event.content
                if (event.link != "") {
                    linkTextView.text = event.link
                    linkTextView.visibility = View.VISIBLE
                }
                attachmentImageView.loadImg(
                    event.attachment?.url ?: "",
                    R.drawable.ic_baseline_error_outline_24
                )
                avatarImageView.loadImg(
                    event.authorAvatar,
                    R.drawable.ic_baseline_person_24
                )
                attachmentImageView.setOnClickListener {
                    onInteractionListener.onClicked(event)
                }

            }
        }
    }

    class EventDiffCallback : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            if (oldItem::class != newItem::class) {
                return false
            }
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem == newItem
        }

    }
}

