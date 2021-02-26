package com.tiket.github.userfinder

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.tiket.github.userfinder.adapter.UserRecyclerAdapter
import com.tiket.github.userfinder.databinding.ActivityMainBinding
import com.tiket.github.userfinder.models.GithubBaseResponse
import com.tiket.github.userfinder.network.Config
import com.tiket.github.userfinder.network.DataManager
import com.tiket.github.userfinder.network.NetworkCallback


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleSearchAction()
        linearLayoutManager = LinearLayoutManager(this)
    }

    private fun handleSearchAction() {
        with(binding) {
            etSearch.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    ivLogo.visibility = View.VISIBLE
                    ivRemove.visibility = View.GONE
                } else {
                    ivLogo.visibility = View.GONE
                    ivRemove.visibility = View.VISIBLE
                }
            }

            etSearch.setOnEditorActionListener { _, action, _ ->
                val seachValue = etSearch.text.toString()
                if (action == EditorInfo.IME_ACTION_SEARCH) {
                    if (seachValue.isNotEmpty()) {
                        fetchApiUserSearch(seachValue)
                        etSearch.clearFocus()
                        hideKeyboard()
                    } else {
                        toast(getString(R.string.field_must_not_empty))
                    }
                }
                false
            }

            ivRemove.setOnClickListener {
                etSearch.setText("")
            }
        }
    }

    private fun fetchApiUserSearch(queryValue: String) {
        val queryFilter = emptyMap<String, String>().toMutableMap()
        queryFilter["q"] = queryValue
        DataManager.getInstance(this).network(
            Config.getAPI().fetchApiSearchUser(queryFilter),
            object : NetworkCallback {
                override fun onSuccess(model: GithubBaseResponse) {
                    toast(model.totalCount.toString())
                    if (!model.items.isNullOrEmpty()) {
                        val data = model.items!!.subList(
                            0,
                            if (model.items!!.size > 5) 9 else model.items!!.size
                        )
                        val userAdapter =
                            UserRecyclerAdapter(this@MainActivity, data)
                        binding.rvUser.apply {
                            layoutManager = linearLayoutManager
                            userAdapter.onItemClick = { _, response ->
                                val browserIntent =
                                    Intent(Intent.ACTION_VIEW, Uri.parse(response.htmlUrl))
                                startActivity(browserIntent)
                            }
                            adapter = userAdapter
                        }
                    }
                }

                override fun onError(message: String?) {
                    toast(message.toString())
                }

            })
    }

    private fun hideKeyboard() {
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
    }
}