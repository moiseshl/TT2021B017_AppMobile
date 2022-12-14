package com.escom.tt2021b017

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.escom.tt2021b017.databinding.ActivityCheckBinding

class CheckActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }
}