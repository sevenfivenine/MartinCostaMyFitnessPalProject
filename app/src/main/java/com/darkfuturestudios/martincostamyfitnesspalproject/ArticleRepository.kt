package com.darkfuturestudios.martincostamyfitnesspalproject

import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData

class ArticleRepository {

    companion object {

        val singleton = ArticleRepository()

        private class InsertAsyncTask : AsyncTask<Article, Void, Unit>() {

            override fun doInBackground(vararg articles: Article?) {
                if (articles.isNotEmpty() && articles[0] is Article) {
                    val articlesList = singleton.articles?.value
                    articlesList?.add(articles[0]!!)
                    singleton.articles?.postValue(articlesList)
                }

            }

        }

        private class UpdateAsyncTask : AsyncTask<Article, Void, Unit>() {

            override fun doInBackground(vararg articles: Article?) {

            }

        }

        private class DeleteAsyncTask : AsyncTask<Article, Void, Unit>() {

            override fun doInBackground(vararg articles: Article?) {

            }

        }

        private class DeleteAllAsyncTask : AsyncTask<Void, Void, Unit>() {

            override fun doInBackground(vararg params: Void?) {
                singleton.articles.postValue(mutableListOf())
            }

        }

    }

    private var articles: MutableLiveData<MutableList<Article>> = MutableLiveData()

    init {
        articles.postValue(mutableListOf())
    }

    fun insert(article: Article) {
        InsertAsyncTask().execute(article)
    }

    fun update(article: Article) {
        UpdateAsyncTask().execute(article)
    }

    fun delete(article: Article) {
        DeleteAsyncTask().execute(article)
    }

    fun deleteAll() {
        DeleteAllAsyncTask().execute()
    }

    fun getAll(): MutableLiveData<MutableList<Article>>? {
        return articles
    }

}