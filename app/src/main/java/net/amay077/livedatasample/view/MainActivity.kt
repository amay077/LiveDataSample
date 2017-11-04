package net.amay077.livedatasample.view

import android.arch.lifecycle.Observer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import net.amay077.livedatasample.R
import android.arch.lifecycle.ViewModelProviders
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import net.amay077.livedatasample.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // DataBinding は使ってないので
        val editUserName = findViewById<EditText>(R.id.editUserName)
        val buttonLoad = findViewById<Button>(R.id.buttonLoad)
        val listRepos = findViewById<ListView>(R.id.listRepos)

        // MainViewModel を取得
        val viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java!!)

        // viewModel.user の TwoWay バインド
        // EditText -> LiveData
        editUserName.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                val userName = editUserName.text;
                if (!TextUtils.equals(viewModel.user.value, userName)) {
                    viewModel.user.postValue(userName.toString())
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun afterTextChanged(p0: Editable?) { }
        })

        // LiveData -> EditText
        viewModel.user.observe(this, Observer { userName ->
            editUserName.setTextKeepState(userName ?: "")
        })
        editUserName.setTextKeepState(viewModel.user?.value ?: "")

        // Button -> Load
        buttonLoad.setOnClickListener({
            viewModel.load()
        })

        // viewModel.repos の OneWay バインド
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1)
        listRepos.adapter = adapter;
        viewModel.repos.observe(this, Observer { repoList ->
            adapter.clear()
            adapter.addAll(repoList?.map { it.name })
        })
    }
}
