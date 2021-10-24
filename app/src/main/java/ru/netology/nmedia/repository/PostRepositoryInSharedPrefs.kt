package ru.netology.nmedia.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.dto.Post
import java.lang.RuntimeException

class PostRepositoryInSharedPrefs(context: Context) : PostRepository {
    private val prefs = context.getSharedPreferences("repo", Context.MODE_PRIVATE)
    private val key = "posts"

    private val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type

    private val emptyPost = Post(
        id = 0,
        author = "",
        content = "",
        published = "",
    )

    private var nextId = 1L

    private val data = MutableLiveData(emptyList<Post>())

    init {
        prefs.getString(key, null)?.let { postsJson ->
            try {
                val posts: List<Post> = gson.fromJson(postsJson, type)
                data.value = posts
                nextId = (posts.map { post -> post.id }.maxOrNull() ?: 0L) + 1L
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun get(): LiveData<List<Post>> = data

    override fun likeById(id: Long) {
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
        val updater = { post: Post ->
            if (post.id == id)
                post.copy(repostsCount = post.repostsCount + 1)
            else
                post
        }
        updatePost(updater)
    }

    override fun removeById(id: Long) {
        val posts = getPostsFromLiveData()
        val updatedPosts = posts.filter { post -> post.id != id }
        data.value = updatedPosts
        sync()
    }

    override fun save(postId: Long, newContent: String) {
        if (postId == 0L) {
            val newPost = emptyPost.copy(
                id = nextId++,
                author = "Me",
                published = "now",
                content = newContent
            )
            val posts = listOf(newPost) + getPostsFromLiveData()
            data.value = posts
            sync()
            return
        }

        val updater = { oldPost: Post ->
            if (oldPost.id == postId)
                oldPost.copy(content = newContent)
            else
                oldPost
        }
        updatePost(updater)
    }

    override fun getById(postId: Long) =
        getPostsFromLiveData().find { post -> post.id == postId }

    private fun sync() {
        prefs.edit().apply {
            val postsJson = gson.toJson(data.value)
            putString(key, postsJson)
            apply()
        }
    }

    private fun updatePost(updater: (Post) -> Post) {
        val posts = getPostsFromLiveData()
        val updatedPosts = posts.map(updater)
        data.value = updatedPosts
        sync()
    }

    private fun getPostsFromLiveData() =
        data.value ?: throw RuntimeException("Data not specified")
}