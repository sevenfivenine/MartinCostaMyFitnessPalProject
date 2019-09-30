package com.darkfuturestudios.martincostamyfitnesspalproject.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.darkfuturestudios.martincostamyfitnesspalproject.*
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private var networkManager: NetworkManager? = null

    private lateinit var viewModel: MainViewModel

    private var pagesLoaded: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val recyclerView = recyclerViewArticles
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        val adapter = ArticleAdapter()
        adapter.setOnBottomReachedListener(object : ArticleAdapter.OnBottomReachedListener {

            // Pagination
            // Load next page, add it to articles list
            override fun onBottomReached(position: Int) {
                networkManager?.sendRequest(pagesLoaded)
                pagesLoaded++
            }

        })

        recyclerView.adapter = adapter

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        viewModel.getAll()?.observe(this, object : Observer<List<Article>> {

            override fun onChanged(articles: List<Article>?) {
                // update recyclerview
                if (articles != null) {
                    adapter.setArticles(articles)
                }
            }

        })
    }

    override fun onResume() {
        super.onResume()

        ArticleRepository.singleton.deleteAll()
        networkManager?.sendRequest(pagesLoaded)
        pagesLoaded++
    }

    fun setNetworkManager(networkManager: NetworkManager?) {
        this.networkManager = networkManager
    }

}
