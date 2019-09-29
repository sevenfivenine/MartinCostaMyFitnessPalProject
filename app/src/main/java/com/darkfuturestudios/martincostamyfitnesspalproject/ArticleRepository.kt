package com.darkfuturestudios.martincostamyfitnesspalproject

import android.os.AsyncTask
import androidx.lifecycle.LiveData

class ArticleRepository {

    companion object {

        private class InsertAsyncTask : AsyncTask<Article, Void, Unit>() {

            override fun doInBackground(vararg articles: Article?) {
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

            }

        }

    }

    private var articles: LiveData<List<Article>>? = null

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

    fun getAll(): LiveData<List<Article>>? {
        return articles
    }
}