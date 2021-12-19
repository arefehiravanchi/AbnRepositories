package com.abn.test.ui.gitrepo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.abn.test.R
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class GitRepoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repository)
    }

}