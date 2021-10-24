package ru.netology.nmedia.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.PostViewModel
import ru.netology.nmedia.R
import ru.netology.nmedia.activities.NewPostFragment.Companion.longArg
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.databinding.FragmentOnePostBinding

class OnePostFragment : Fragment() {
    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentOnePostBinding.inflate(
            inflater,
            container,
            false
        )

        val postId = arguments?.longArg ?: throw RuntimeException("Can't get post id")
        val cardPostBinding = CardPostBinding.inflate(inflater, binding.root, true)

        val onInteractionListener =
            PostBindingObject.getOnInteractionListener(
                viewModel,
                findNavController(),
                R.id.action_onePostFragment_to_newPostFragment
            )

        viewModel.data.observe(viewLifecycleOwner) {
            val post = viewModel.getById(postId)
            if (post == null) {
                findNavController().navigateUp()
            } else {
                PostBindingObject.bindCardPost(post, cardPostBinding, onInteractionListener)
            }
        }

        return binding.root
    }
}