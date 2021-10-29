package ru.netology.nmedia

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositorySQLite

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositorySQLite(
        AppDb.getInstance(application).postDao
    )
    val data: LiveData<List<Post>> = repository.get()

    fun likeById(id: Long) = repository.likeById(id)
    fun repostById(id: Long) = repository.repostById(id)
    fun removeById(id: Long) = repository.removeById(id)

    fun save(postId: Long, newContent: String) = repository.save(postId, newContent)
    fun getById(postId: Long) = repository.getById(postId)
}