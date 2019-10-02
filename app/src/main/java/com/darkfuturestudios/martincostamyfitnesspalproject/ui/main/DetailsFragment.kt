package com.darkfuturestudios.martincostamyfitnesspalproject.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.darkfuturestudios.martincostamyfitnesspalproject.Article
import com.darkfuturestudios.martincostamyfitnesspalproject.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.details_fragment.*


class DetailsFragment(val article: Article) : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance(article: Article) = DetailsFragment(article)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.details_fragment, container, false)
    }

    override fun onResume() {
        super.onResume()

        val fab: FloatingActionButton? = activity?.findViewById(R.id.fabShare)
        fab?.setOnClickListener {
            shareArticle()
        }

        if (article.thumbnailUrl != null) {
            val imageUrl = "https://www.nytimes.com/" + article.thumbnailUrl

            Picasso.get().load(imageUrl).into(imageViewThumbnailDetails)
        }

        if (article.byline != null) {
            textViewBylineDetails.text = article.byline
        }

        if (article.firstPara != null) {
            textViewFirstParaDetails.text = article.firstPara
        }

        textViewHeadlineDetails.text = article.headline
    }

    fun shareArticle() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, article.url)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

}
