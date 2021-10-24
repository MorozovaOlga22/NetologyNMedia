package ru.netology.nmedia.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.navigation.NavController
import ru.netology.nmedia.OnInteractionListener
import ru.netology.nmedia.PostViewModel
import ru.netology.nmedia.R
import ru.netology.nmedia.activities.NewPostFragment.Companion.longArg
import ru.netology.nmedia.activities.NewPostFragment.Companion.textArg
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post

object PostBindingObject {
    fun getOnInteractionListener(
        viewModel: PostViewModel,
        navController: NavController,
        newPostFragmentNavId: Int
    ): OnInteractionListener =
        object : OnInteractionListener {
            override fun onLike(post: Post) =
                viewModel.likeById(post.id)

            override fun onRepost(post: Post) =
                viewModel.repostById(post.id)

            override fun onEdit(post: Post) {
                navController.navigate(
                    newPostFragmentNavId,
                    Bundle().apply {
                        longArg = post.id
                        textArg = post.content
                    })
            }

            override fun onRemove(post: Post) =
                viewModel.removeById(post.id)

            override fun onShowOnePost(post: Post) {
                navController.navigate(
                    R.id.action_feedFragment_to_onePostFragment,
                    Bundle().apply {
                        longArg = post.id
                    })
            }
        }

    fun bindCardPost(
        post: Post,
        binding: CardPostBinding,
        onInteractionListener: OnInteractionListener
    ) {
        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content

            like.isChecked = post.likedByMe
            like.text = ru.netology.nmedia.utils.PostService.getCountText(post.likesCount)

            repost.text = ru.netology.nmedia.utils.PostService.getCountText(post.repostsCount)
            viewing.text = ru.netology.nmedia.utils.PostService.getCountText(post.viewingsCount)

            like.setOnClickListener {
                onInteractionListener.onLike(post)
            }
            repost.setOnClickListener {
                onInteractionListener.onRepost(post)
            }

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onInteractionListener.onRemove(post)
                                true
                            }
                            R.id.edit -> {
                                onInteractionListener.onEdit(post)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }

            if (post.videoUrl.isNullOrBlank()) {
                videoGroup.visibility = View.GONE
            } else {
                videoGroup.visibility = View.VISIBLE

                val videoClickListener: (View) -> Context = { view ->
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        android.net.Uri.parse(post.videoUrl)
                    )
                    view.context.apply {
                        val shareIntent =
                            Intent.createChooser(
                                intent,
                                getString(R.string.chooser_play_video)
                            )
                        startActivity(shareIntent)
                    }
                }

                videoButton.setOnClickListener { view -> videoClickListener(view) }
                videoPicture.setOnClickListener { view -> videoClickListener(view) }
            }
        }
    }
}