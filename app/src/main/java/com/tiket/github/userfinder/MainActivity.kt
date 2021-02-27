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
import androidx.recyclerview.widget.RecyclerView
import com.tiket.github.userfinder.adapter.UserRecyclerAdapter
import com.tiket.github.userfinder.databinding.ActivityMainBinding
import com.tiket.github.userfinder.models.GithubBaseResponse
import com.tiket.github.userfinder.models.UserResponse
import com.tiket.github.userfinder.network.Config
import com.tiket.github.userfinder.network.DataManager
import com.tiket.github.userfinder.network.NetworkCallback


class MainActivity : AppCompatActivity() {

    private lateinit var scrollListener: RecyclerView.OnScrollListener
    private lateinit var binding: ActivityMainBinding
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var userAdapter: UserRecyclerAdapter

    private var userList = emptyList<UserResponse>().toMutableList()
    private var currentPage = 1
    private var totalItemCount = 0
    private var queryValue = ""

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
                queryValue = etSearch.text.toString()
                if (action == EditorInfo.IME_ACTION_SEARCH) {
                    if (queryValue.isNotEmpty()) {
                        currentPage = 1
                        fetchApiUserSearch(currentPage, true)
                    } else {
                        toast(getString(R.string.field_must_not_empty))
                    }
                    etSearch.clearFocus()
                    hideKeyboard()
                }
                false
            }

            ivRemove.setOnClickListener {
                etSearch.setText("")
            }
        }
    }

    private fun fetchApiUserSearch(page: Int, isFromZero: Boolean) {
        if (isFromZero) {
            binding.progressCircular.visibility = View.VISIBLE
            binding.rvUser.visibility = View.GONE
        } else {
            binding.llLoadMore.visibility = View.VISIBLE
        }

        val queryFilter = emptyMap<String, String>().toMutableMap()
        queryFilter["q"] = queryValue
        queryFilter["page"] = page.toString()
        queryFilter["per_page"] = "10"

        DataManager.getInstance(this).network(
            Config.getAPI().fetchApiSearchUser(queryFilter),
            object : NetworkCallback {
                override fun onSuccess(model: GithubBaseResponse) {
                    if (!model.items.isNullOrEmpty()) {
                        if (isFromZero) {
                            totalItemCount = model.totalCount ?: 0
                            userList = model.items!!.subList(0, model.items!!.size)
                            initUserAdapter()
                        } else {
                            binding.llLoadMore.visibility = View.GONE
                            userList.addAll(model.items!!.subList(0, model.items!!.size))
                            userAdapter.notifyDataSetChanged()
                        }
                    } else {
                        userList.clear()
                        if (binding.rvUser.adapter != null) {
                            userAdapter.notifyItemRangeRemoved(0, userList.size)
                            userAdapter.notifyDataSetChanged()
                        }
                        toast(getString(R.string.username_not_found))
                    }

                    binding.rvUser.visibility = View.VISIBLE
                    binding.progressCircular.visibility = View.GONE
                }

                override fun onError(message: String?) {
                    binding.progressCircular.visibility = View.GONE
                    binding.llLoadMore.visibility = View.GONE
                    toast(message.toString())
                }

            })
    }

    private fun initUserAdapter() {
        userAdapter = UserRecyclerAdapter(this@MainActivity, userList)
        binding.rvUser.apply {
            layoutManager = linearLayoutManager
            adapter = userAdapter
        }
        binding.rvUser.addOnScrollListener(recyclerScroll)
        userAdapter.onItemClick = { _, response ->
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(response.htmlUrl)))
        }
    }

    private fun isHavingMoreData(): Boolean {
        if (totalItemCount > userList.size) {
            return true
        }
        return false
    }

    private fun hideKeyboard() {
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
    }

    private val recyclerScroll: RecyclerView.OnScrollListener =
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy < 0) {
                    return
                }
                if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == -1
                    + userAdapter.itemCount
                ) {
                    val totalItemCount: Int = linearLayoutManager.itemCount
                    val lastVisibleItem: Int = linearLayoutManager.findLastVisibleItemPosition()
                    if (lastVisibleItem > totalItemCount - 2 && dy > 0) {
                        if (isHavingMoreData()) {
                            currentPage++
                            fetchApiUserSearch(currentPage, false)
                        }
                    }
                }
            }
        }
}