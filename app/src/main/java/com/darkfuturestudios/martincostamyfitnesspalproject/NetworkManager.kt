package com.darkfuturestudios.martincostamyfitnesspalproject

import android.content.Context
import android.net.Uri
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class NetworkManager(val context: Context) {

    private val queue: RequestQueue


    companion object {
        fun newInstance(context: Context) = NetworkManager(context)

        const val TAG = "NetworkManager"
    }

    init {
        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(context)
    }

    fun sendRequest(pageNum: Int?, query: String?) {
        val json = JSONObject()
        //val url = context.getString(R.string.http_request_url)
        val apiKey = context.getString(R.string.api_key)

        val beginDate = "20190926"
        val endDate = "20190930"

        val builder = Uri.Builder()
            .scheme("https")
            .authority("api.nytimes.com")
            .appendPath("svc")
            .appendPath("search")
            .appendPath("v2")
            .appendPath("articlesearch.json")
            .appendQueryParameter("begin_date", beginDate)
            .appendQueryParameter("end_date", endDate)
            //.appendQueryParameter("page", "$pageNum")
            //.appendQueryParameter("")
            //.appendQueryParameter("")
            //.appendQueryParameter("q", "House Democrats Tread")
            .appendQueryParameter("api-key", apiKey)

        if (pageNum != null) {
            builder.appendQueryParameter("page", "$pageNum")
        }

        if (query != null) {
            builder.appendQueryParameter("q", query)
        }

        val url = builder.build().toString()

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                parseArticlesJSON(response)
                //textView.text = "Response: %s".format(response.toString())
            },
            Response.ErrorListener { error ->
                // TODO: Handle error
            }
        ).setTag(context)

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)
    }

    fun parseArticlesJSON(response: JSONObject) {
        Log.d(TAG, "parse")

        val articles = response.getJSONObject("response").getJSONArray("docs")

        for(i in 0 until articles.length()) {
            val article: JSONObject = articles.get(i) as JSONObject
            val id: String = article.get("_id") as String
            val headline: String = article.getJSONObject("headline").get("main") as String

            var thumbnailUrl = ""

            if (article.getJSONArray("multimedia").length() > 0) {
                val multimedia: JSONObject = article.getJSONArray("multimedia")[0] as JSONObject
                thumbnailUrl = multimedia.get("url") as String
            }

            val newArticle = Article(id, headline, thumbnailUrl)

            ArticleRepository.singleton.insert(newArticle)

            //Log.d(TAG, "$headline")
        }
    }

    fun shutdown() {
        queue.cancelAll(context)
    }
}