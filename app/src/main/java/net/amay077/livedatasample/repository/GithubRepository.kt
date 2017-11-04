package net.amay077.livedatasample.repository

import android.util.Log
import net.amay077.livedatasample.model.GithubRepo
import net.amay077.livedatasample.api.GitHubService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.util.concurrent.Executors
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory

class GithubRepository {
    private val TAG = "GithubRepository"

    fun loadRepos(user : String, onResult: ((List<GithubRepo>)->Unit)) {

        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .callbackExecutor(Executors.newSingleThreadExecutor())
                .build()

        val service = retrofit.create(GitHubService::class.java)

        val repos = service.listRepos(user)

        repos.enqueue(object : Callback<List<GithubRepo>> {
            override fun onResponse(call: Call<List<GithubRepo>>?, response: Response<List<GithubRepo>>?) {
                response?.body()?.let { repoList ->
                    onResult.invoke(repoList)
                }
            }

            override fun onFailure(call: Call<List<GithubRepo>>?, t: Throwable?) {
                Log.w(TAG, "loadRepos failed.", t)
            }
        })
    }
}