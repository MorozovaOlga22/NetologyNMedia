package ru.netology.nmedia.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.utils.AndroidUtils
import ru.netology.nmedia.PostViewModel
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.utils.LongArg
import ru.netology.nmedia.utils.StringArg

class NewPostFragment : Fragment() {
    companion object {
        var Bundle.longArg: Long? by LongArg
        var Bundle.textArg: String? by StringArg
    }

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentNewPostBinding.inflate(
            inflater,
            container,
            false
        )

        val postId = arguments?.longArg ?: 0L

        val oldContent = arguments?.textArg
        if (!oldContent.isNullOrBlank()) {
            binding.oldText.text = oldContent
            binding.edit.setText(oldContent)
            binding.editCancelGroup.visibility = View.VISIBLE
        }
        binding.edit.requestFocus()
        binding.ok.setOnClickListener {
            if (!binding.edit.text.isNullOrBlank()) {
                val content = binding.edit.text.toString()
                viewModel.save(postId, content)
            }
            AndroidUtils.hideKeyboard(requireView())
            findNavController().navigateUp()
        }
        binding.cancelEdit.setOnClickListener {
            AndroidUtils.hideKeyboard(requireView())
            findNavController().navigateUp()
        }

        return binding.root
    }
}