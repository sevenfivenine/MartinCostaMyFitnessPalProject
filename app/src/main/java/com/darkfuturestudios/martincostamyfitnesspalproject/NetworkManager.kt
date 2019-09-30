package com.darkfuturestudios.martincostamyfitnesspalproject

import android.content.Context
import android.net.Uri
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
    }

    init {
        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(context)
    }

    fun sendRequest() {
        val json = JSONObject()
        //val url = context.getString(R.string.http_request_url)
        val apiKey = context.getString(R.string.api_key)

        val beginDate = "20190926"
        val endDate = "20190928"

        val builder = Uri.Builder()
            .scheme("https")
            .authority("api.nytimes.com")
            .appendPath("svc")
            .appendPath("search")
            .appendPath("v2")
            .appendPath("articlesearch.json")
            .appendQueryParameter("begin_date", beginDate)
            .appendQueryParameter("end_date", endDate)
            //.appendQueryParameter("")
            //.appendQueryParameter("")
            //.appendQueryParameter("q", query)
            .appendQueryParameter("api-key", apiKey)

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

    }

    fun shutdown() {
        queue.cancelAll(context)
    }
}