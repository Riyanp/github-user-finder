package com.tiket.github.userfinder

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.tiket.github.userfinder.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleSearchAction()
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
                if (action == EditorInfo.IME_ACTION_SEARCH) {
                    etSearch.clearFocus()
                    hideKeyboard()
                }
                false
            }
        }
    }

    private fun hideKeyboard() {
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
    }
}