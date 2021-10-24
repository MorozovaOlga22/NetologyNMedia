package ru.netology.nmedia

import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.activities.PostBindingObject
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: OnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        PostBindingObject.bindCardPost(post, binding, onInteractionListener)
        binding.root.setOnClickListener {
            onInteractionListener.onShowOnePost(post)
        }
    }
}