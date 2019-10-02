package com.darkfuturestudios.martincostamyfitnesspalproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso


class ArticleAdapter :
    RecyclerView.Adapter<ArticleAdapter.ArticleHolder>() {

    private var listener: RecyclerViewClickListener? = null
    private var articles: List<Article> = mutableListOf()
    private var onBottomReachedListener: OnBottomReachedListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.article_item, parent, false)
        return ArticleHolder(itemView, this.listener!!)
    }

    override fun onBindViewHolder(holder: ArticleHolder, position: Int) {
        val currentArticle = articles[position]
        holder.textViewHeadline.text = currentArticle.headline

        if (currentArticle.thumbnailUrl != null && currentArticle.thumbnailUrl != "") {
            val imageUrl = "https://www.nytimes.com/" + currentArticle.thumbnailUrl

            Picasso.get().load(imageUrl).into(holder.imageViewThumbnail)
        } else {
            holder.imageViewThumbnail.visibility = View.GONE
        }

        if (position == articles.size - 1) {
            onBottomReachedListener?.onBottomReached(position)
        }
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    fun setArticles(articles: List<Article>) {
        this.articles = articles
        notifyDataSetChanged()
    }

    class ArticleHolder(itemView: View, val listener: RecyclerViewClickListener) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val imageViewThumbnail: ImageView = itemView.findViewById(R.id.imageViewThumbnail)
        val textViewHeadline: TextView = itemView.findViewById(R.id.textViewHeadline)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (v != null) {
                listener.onClick(v, adapterPosition)
            }
        }
    }

    fun setOnBottomReachedListener(onBottomReachedListener: OnBottomReachedListener) {
        this.onBottomReachedListener = onBottomReachedListener
    }

    fun getArticles(): List<Article> {
        return articles
    }

    fun setOnClickListener(listener: RecyclerViewClickListener) {
        this.listener = listener
    }

    interface OnBottomReachedListener {
        fun onBottomReached(position: Int)
    }

    interface RecyclerViewClickListener {
        fun onClick(view: View, position: Int)
    }

}