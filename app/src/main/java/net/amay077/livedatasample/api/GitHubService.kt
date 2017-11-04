package net.amay077.livedatasample.api

import net.amay077.livedatasample.model.GithubRepo
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Call


/**
 * Created by h_okuyama on 2017/11/03.
 */
interface GitHubService {
    @GET("users/{user}/repos")
    fun listRepos(@Path("user") user: String): Call<List<GithubRepo>>
}