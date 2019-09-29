package com.darkfuturestudios.martincostamyfitnesspalproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ArticleAdapter: RecyclerView.Adapter<ArticleAdapter.ArticleHolder>() {

    private var articles: List<Article> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.article_item, parent, false)
        return ArticleHolder(itemView)
    }

    override fun onBindViewHolder(holder: ArticleHolder, position: Int) {
        val currentArticle = articles.get(position)
        //holder.imageViewThumbnail.setImageBitmap(bitmap)
        holder.textViewHeadline.text = "TODO"
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    fun setArticles(articles: List<Article>) {
        this.articles = articles
        notifyDataSetChanged()
    }


    class ArticleHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imageViewThumbnail: ImageView
        val textViewHeadline: TextView

        init {
            imageViewThumbnail = itemView.findViewById(R.id.imageViewThumbnail)
            textViewHeadline = itemView.findViewById(R.id.textViewHeadline)
        }
    }
}