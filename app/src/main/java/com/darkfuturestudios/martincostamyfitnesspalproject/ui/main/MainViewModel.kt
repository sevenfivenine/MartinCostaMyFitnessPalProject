package com.darkfuturestudios.martincostamyfitnesspalproject.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.darkfuturestudios.martincostamyfitnesspalproject.Article
import com.darkfuturestudios.martincostamyfitnesspalproject.ArticleRepository

class MainViewModel : ViewModel() {

    private val repository: ArticleRepository
    private val articles: MutableLiveData<MutableList<Article>>?

    init {
        repository = ArticleRepository.singleton
        articles = this.repository.getAll()
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

    fun getAll(): MutableLiveData<MutableList<Article>>? {
        return articles
    }
}
