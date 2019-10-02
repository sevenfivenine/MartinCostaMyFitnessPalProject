package com.darkfuturestudios.martincostamyfitnesspalproject

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class NetworkManager(val context: Context) {

    private val queue: RequestQueue


    companion object {
        fun newInstance(context: Context) = NetworkManager(context)

        const val TAG = "NetworkManager"
        const val RESPONSE_CODE_UNAUTHORIZED = 401
        const val RESPONSE_CODE_LIMIT_REACHED = 429
    }

    init {
        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(context)
    }

    fun sendRequest(pageNum: Int?, query: String?) {
        // Check if internet is connected
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true

        if (!isConnected) {
            val noConnectionBuilder: AlertDialog.Builder? = context.let {
                AlertDialog.Builder(it)
            }

            noConnectionBuilder?.setMessage(R.string.dialog_message_no_connection)
                ?.setTitle(R.string.dialog_title_no_connection)
                ?.setNeutralButton(R.string.ok, null)

            val dialog: AlertDialog? = noConnectionBuilder?.create()

            dialog?.show()

            return
        }

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
            .appendQueryParameter("fq", "source:(\"The New York Times\")")
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
            },
            Response.ErrorListener { error ->
                val responseCode = error.networkResponse.statusCode

                if (responseCode == RESPONSE_CODE_UNAUTHORIZED) {
                    val unauthorizedBuilder: AlertDialog.Builder? = context.let {
                        AlertDialog.Builder(it)
                    }

                    unauthorizedBuilder?.setMessage(R.string.dialog_message_unauthorized)
                        ?.setTitle(R.string.dialog_title_unauthorized)
                        ?.setNeutralButton(R.string.ok, null)

                    val dialog: AlertDialog? = unauthorizedBuilder?.create()

                    dialog?.show()
                }

                else if (responseCode == RESPONSE_CODE_LIMIT_REACHED) {
                    val limitReachedBuilder: AlertDialog.Builder? = context.let {
                        AlertDialog.Builder(it)
                    }

                    limitReachedBuilder?.setMessage(R.string.dialog_message_limit_reached)
                        ?.setTitle(R.string.dialog_title_limit_reached)
                        ?.setNeutralButton(R.string.ok, null)

                    val dialog: AlertDialog? = limitReachedBuilder?.create()

                    dialog?.show()
                }
            }
        ).setTag(context)

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)
    }

    fun parseArticlesJSON(response: JSONObject) {
        Log.d(TAG, "parse")

        val articles = response.getJSONObject("response").getJSONArray("docs")

        for (i in 0 until articles.length()) {
            val article: JSONObject = articles.get(i) as JSONObject
            val id: String = article.get("_id") as String
            val headline: String = article.getJSONObject("headline").get("main") as String

            val bylinePeople: JSONArray = article.getJSONObject("byline").getJSONArray("person")

            var bylineString: String = ""

            for (j in 0 until bylinePeople.length()) {
                val person: JSONObject = bylinePeople.get(j) as JSONObject
                bylineString += person.get("firstname")
                bylineString += " "
                bylineString += person.get("lastname")

                if (j < bylinePeople.length() - 1) {
                    bylineString += ", "
                }
            }

            if (bylineString == "") {
                bylineString = "No Author"
            }

            var thumbnailUrl = ""

            if (article.getJSONArray("multimedia").length() > 0) {
                val multimedia: JSONObject = article.getJSONArray("multimedia")[0] as JSONObject
                thumbnailUrl = multimedia.get("url") as String
            } else continue

            val articleUrl: String = article.get("web_url") as String

            var leadPara: String? = null

            if (article.get("lead_paragraph") != null) {
                leadPara = article.get("lead_paragraph") as String
            }

            val newArticle = Article(id, headline, bylineString, leadPara, thumbnailUrl, articleUrl)


            if (newArticle.headline != null && newArticle.headline != "") {
                ArticleRepository.singleton.insert(newArticle)
            }

            //Log.d(TAG, "$headline")
        }
    }

    fun shutdown() {
        queue.cancelAll(context)
    }
}