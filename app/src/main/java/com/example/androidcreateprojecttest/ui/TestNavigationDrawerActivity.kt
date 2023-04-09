package com.example.androidcreateprojecttest.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.androidcreateprojecttest.databinding.ActivityTestNavigationDrawerableBinding


class TestNavigationDrawerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTestNavigationDrawerableBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestNavigationDrawerableBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // It will show Test Prject ===== > testdrawableproject
    }
}