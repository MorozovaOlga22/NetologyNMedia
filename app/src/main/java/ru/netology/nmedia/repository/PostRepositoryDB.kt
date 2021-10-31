package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entities.PostEntity

class PostRepositoryDB(private val dao: PostDao) : PostRepository {
    private val emptyPost = Post(
        id = 0,
        author = "",
        content = "",
        published = "",
    )

    override fun get(): LiveData<List<Post>> = Transformations.map(dao.getAll()) { list ->
        list.map {
            Post(
                it.id,
                it.author,
                it.content,
                it.published,
                it.likedByMe,
                it.likesCount,
                it.repostsCount,
                it.viewingsCount,
                it.videoUrl
            )
        }
    }

    override fun likeById(id: Long) {
        dao.likeById(id)
    }

    override fun repostById(id: Long) {
        dao.repostById(id)
    }

    override fun removeById(id: Long) {
        dao.removeById(id)
    }

    override fun save(postId: Long, newContent: String) {
        val post: Post =
            if (postId == 0L) {
                emptyPost.copy(
                    author = "Me",
                    published = "now",
                    content = newContent
                )
            } else {
                getById(postId).copy(content = newContent)
            }

        dao.save(PostEntity.fromDto(post))
    }

    override fun getById(postId: Long) =
        dao.getById(postId).toDto()
}