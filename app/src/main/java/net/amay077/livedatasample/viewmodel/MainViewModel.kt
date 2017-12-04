package net.amay077.livedatasample.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.text.TextUtils
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import jp.keita.kagurazaka.rxproperty.ReadOnlyRxProperty
import jp.keita.kagurazaka.rxproperty.RxProperty
import jp.keita.kagurazaka.rxproperty.Nothing
import jp.keita.kagurazaka.rxproperty.toRxCommand
import net.amay077.livedatasample.model.GithubRepo
import net.amay077.livedatasample.repository.GithubRepository

/**
 * MainActivity の ViewModel
 */
class MainViewModel : ViewModel() {

    private val _disposables = CompositeDisposable();

    // GitHub のリポジトリ一覧を取得するためのクラス(Dagger とか使って DI すると吉)。
    private val _githubRepository = GithubRepository()

    // GitHub ユーザー名。EditText.text から双方向(TwoWay)バインドされる。
    val user = RxProperty<String>()

    // GitHubリポジトリ一覧。ListView から単方向(OneWay)バインドされる。
    // View側から値をセットされないことを明示するために、外部には ReadOnlyRxProperty として公開。
    private val _repos = RxProperty<List<GithubRepo>>()
    val repos = ReadOnlyRxProperty(_repos)

    // View 側から購読して Toast を表示するための LiveData。
    // 変更通知が必要ない(=EventBus的に使う)なら、LiveData をそのまま使うのがいいんじゃなイカ。
    private val _toast = MutableLiveData<String>()
    val toast : LiveData<String> = _toast

    // GitHubリポジトリ一覧を検索するコマンド。
    // user が空でない場合のみ使用可能。
    // コマンドが実行されると GithubRepository を使って user のリポジトリ一覧を取得し、
    // repos(_repos)にその一覧をセットして Toast にメッセージを投げる。
    val load = user.map { !TextUtils.isEmpty(it) }.toRxCommand<Nothing>().apply {
        this.subscribe {
            val userName = user.get();
            if (userName != null) {
                _githubRepository.loadRepos(userName, { repoList ->
                    _repos.set(repoList)
                    _toast.postValue("${userName} のリポジトリ一覧を表示しました")
                })
            }
        }.addTo(_disposables)
    }

    override fun onCleared() {
        _disposables.clear()
        super.onCleared()
    }
}