package com.darkfuturestudios.martincostamyfitnesspalproject

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.darkfuturestudios.martincostamyfitnesspalproject.ArticleAdapter.OnBottomReachedListener



class ArticleAdapter(private val listener: RecyclerViewClickListener) :
    RecyclerView.Adapter<ArticleAdapter.ArticleHolder>() {

    private var articles: List<Article> = mutableListOf()
    private var onBottomReachedListener: OnBottomReachedListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.article_item, parent, false)
        return ArticleHolder(itemView, this.listener!!)
    }

    override fun onBindViewHolder(holder: ArticleHolder, position: Int) {
        val currentArticle = articles.get(position)
        //holder.imageViewThumbnail.setImageBitmap(bitmap)
        holder.textViewHeadline.text = currentArticle.headline

        if (currentArticle.thumbnailUrl != null) {
            val imageUrl = "https://www.nytimes.com/" + currentArticle.thumbnailUrl

            Picasso.get().load(imageUrl).into(holder.imageViewThumbnail)
        }

        if (position == articles.size - 1){
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


    class ArticleHolder(itemView: View, val listener: RecyclerViewClickListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val imageViewThumbnail: ImageView
        val textViewHeadline: TextView


        init {
            imageViewThumbnail = itemView.findViewById(R.id.imageViewThumbnail)
            textViewHeadline = itemView.findViewById(R.id.textViewHeadline)
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

    interface OnBottomReachedListener {

        fun onBottomReached(position: Int)

    }

    interface RecyclerViewClickListener {
        fun onClick(view: View, position: Int)
    }
}