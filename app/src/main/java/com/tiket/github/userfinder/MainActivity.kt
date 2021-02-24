package com.tiket.github.userfinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tiket.github.userfinder.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
    }
}