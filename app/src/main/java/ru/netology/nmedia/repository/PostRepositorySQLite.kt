package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post

class PostRepositorySQLite(private val dao: PostDao) : PostRepository {
    private val emptyPost = Post(
        id = 0,
        author = "",
        content = "",
        published = "",
    )

    private val data = MutableLiveData(emptyList<Post>())

    init {
        data.value = dao.getAll()
    }

    override fun get(): LiveData<List<Post>> = data

    override fun likeById(id: Long) {
        dao.likeById(id)
        val updater = { post: Post ->
            if (post.id == id)
                post.copy(
                    likesCount = if (post.likedByMe) post.likesCount - 1 else post.likesCount + 1,
                    likedByMe = !post.likedByMe
                )
            else
                post
        }
        updatePost(updater)
    }

    override fun repostById(id: Long) {
        dao.repostById(id)
        val updater = { post: Post ->
            if (post.id == id)
                post.copy(repostsCount = post.repostsCount + 1)
            else
                post
        }
        updatePost(updater)
    }

    override fun removeById(id: Long) {
        dao.removeById(id)
        val posts = getPostsFromLiveData()
        val updatedPosts = posts.filter { post -> post.id != id }
        data.value = updatedPosts
    }

    override fun save(postId: Long, newContent: String) {
        if (postId == 0L) {
            val newPost = emptyPost.copy(
                author = "Me",
                published = "now",
                content = newContent
            )
            val saved = dao.save(newPost)
            val posts = listOf(saved) + getPostsFromLiveData()
            data.value = posts
            return
        }

        val updater = { oldPost: Post ->
            if (oldPost.id == postId) {
                dao.save(oldPost.copy(content = newContent))
            } else {
                oldPost
            }
        }
        updatePost(updater)
    }

    override fun getById(postId: Long) =
        getPostsFromLiveData().find { post -> post.id == postId }

    private fun updatePost(updater: (Post) -> Post) {
        val posts = getPostsFromLiveData()
        val updatedPosts = posts.map(updater)
        data.value = updatedPosts
    }

    private fun getPostsFromLiveData() =
        data.value ?: throw RuntimeException("Data not specified")
}