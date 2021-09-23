package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "21 мая в 18:36",
            likesCount = 25
        )

        updateView(binding, post)

        with(binding) {
            likesImage.setOnClickListener {
                PostService.like(post)
                updateView(this, post)
            }
            repostsImage.setOnClickListener {
                PostService.repost(post)
                updateView(this, post)
            }
        }
    }

    private fun updateView(binding: ActivityMainBinding, post: Post) {
        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content

            val likesImageResource =
                if (post.likedByMe) R.drawable.ic_liked_24 else R.drawable.ic_likes_24
            likesImage.setImageResource(likesImageResource)

            likesText.text = PostService.getCountText(post.likesCount)
            repostsText.text = PostService.getCountText(post.repostsCount)
            viewingsText.text = PostService.getCountText(post.viewingsCount)
        }
    }
}