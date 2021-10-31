package ru.netology.nmedia.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean,
    val likesCount: Int = 0,
    val repostsCount: Int = 0,
    val viewingsCount: Int = 0,
    val videoUrl: String? = null
) {
    fun toDto() = Post(
        id,
        author,
        content,
        published,
        likedByMe,
        likesCount,
        repostsCount,
        viewingsCount,
        videoUrl
    )

    companion object {
        fun fromDto(dto: Post) =
            PostEntity(
                dto.id, dto.author, dto.content, dto.published,
                dto.likedByMe, dto.likesCount, dto.repostsCount, dto.viewingsCount, dto.videoUrl
            )
    }
}