package net.amay077.livedatasample.view

import android.os.Bundle
import android.arch.lifecycle.Observer
import android.support.v7.app.AppCompatActivity
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.widget.ArrayAdapter
import android.widget.Toast
import net.amay077.livedatasample.R
import net.amay077.livedatasample.databinding.ActivityMainBinding
import net.amay077.livedatasample.extensions.toLiveData
import net.amay077.livedatasample.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // データバインディング使用
        val binding  = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        // MainViewModel を取得して DataBinding に設定する。
        val viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java!!)
        binding.viewModel = viewModel

        // viewModel.repos の OneWay バインドは、敢えて LiveData を使ってやる。
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1)
        binding.listRepos.adapter = adapter;
        // repos:Observable を LiveData に変換して購読。
        viewModel.repos.toLiveData().observe(this, Observer { repoList ->
            // リストを洗い替え
            adapter.clear()
            adapter.addAll(repoList?.map { it.name })
        })

        // Toast を表示するために、 toast:LiveData を購読する。
        viewModel.toast.observe(this, Observer { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        })
    }
}
