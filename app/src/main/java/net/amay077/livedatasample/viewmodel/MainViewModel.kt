package net.amay077.livedatasample.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.view.View
import net.amay077.livedatasample.model.GithubRepo
import net.amay077.livedatasample.repository.GithubRepository

/**
 * Created by h_okuyama on 2017/11/03.
 */
class MainViewModel : ViewModel() {

    val user : MutableLiveData<String> = MutableLiveData()
    val repos : MutableLiveData<List<GithubRepo>> = MutableLiveData()

    private val _githubRepogitory = GithubRepository()

    fun load() {

        val userName = user.value;
        if (userName != null) {
            _githubRepogitory.loadRepos(userName, { repoList ->
                repos.postValue(repoList)
            })
        }
    }
}