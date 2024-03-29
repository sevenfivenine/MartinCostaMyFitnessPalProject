package com.darkfuturestudios.martincostamyfitnesspalproject.ui.main

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
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
    private var query: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)

        val recyclerView = recyclerViewArticles
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        val adapter = ArticleAdapter()

        val listener = object : ArticleAdapter.RecyclerViewClickListener {
            override fun onClick(view: View, position: Int) {
                val article = adapter.getArticles()[position]

                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.container, DetailsFragment.newInstance(article))
                    ?.addToBackStack(null)
                    ?.commit()
            }
        }

        adapter.setOnClickListener(listener)

        adapter.setOnBottomReachedListener(object : ArticleAdapter.OnBottomReachedListener {
            // Pagination
            // Load next page, add it to articles list
            override fun onBottomReached(position: Int) {
                networkManager?.sendRequest(query)
            }

        })

        recyclerView.adapter = adapter

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        viewModel.getAll()?.observe(this, object : Observer<List<Article>> {

            override fun onChanged(articles: List<Article>?) {
                // update RecyclerView
                if (articles != null) {
                    adapter.setArticles(articles)
                }
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        menu.clear()
        inflater.inflate(R.menu.options_menu, menu)
        val searchView = SearchView((activity as MainActivity).supportActionBar!!.themedContext)

        menu.findItem(R.id.search)?.apply {
            setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW or MenuItem.SHOW_AS_ACTION_IF_ROOM)
            actionView = searchView

            setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
                override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                    return true
                }

                override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                    ArticleRepository.singleton.deleteAll()

                    // Go back to showing all articles
                    query = null
                    networkManager?.prevQuery = null
                    networkManager?.pageToLoad = 0
                    networkManager?.sendRequest(null)

                    onCreateOptionsMenu(menu, inflater)

                    return true
                }

            })

            // If we are coming back from a DetailsFragment where we had already entered a query, reflect this
            if (query != null) {
                expandActionView()
                searchView.setQuery(query, false)
                searchView.clearFocus()
            }
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                ArticleRepository.singleton.deleteAll()
                this@MainFragment.query = query
                networkManager?.sendRequest(query)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }

    override fun onResume() {
        super.onResume()

        // If we are coming back from DetailsFragment when the user had input a query, resume viewing that query
        if (networkManager?.prevQuery != null) {
            query = networkManager?.prevQuery
            networkManager?.sendRequest(query)
        }

        // Otherwise, just show all articles
        else {
            networkManager?.sendRequest(null)
        }
    }

    fun setNetworkManager(networkManager: NetworkManager?) {
        this.networkManager = networkManager
    }

}
