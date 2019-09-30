package com.darkfuturestudios.martincostamyfitnesspalproject.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.darkfuturestudios.martincostamyfitnesspalproject.Article
import com.darkfuturestudios.martincostamyfitnesspalproject.ArticleRepository

class MainViewModel : ViewModel() {

    private val repository: ArticleRepository
    private val articles: LiveData<List<Article>>?

    init {
        repository = ArticleRepository()
        articles = repository.getAll()
    }

    fun insert(article: Article) {
        repository.insert(article)
    }

    fun update(article: Article) {
        repository.update(article)
    }

    fun delete(article: Article) {
        repository.delete(article)
    }

    fun deleteAll() {
        repository.deleteAll()
    }

    fun getAll(): LiveData<List<Article>>? {
        return articles
    }}
