package com.escom.tt2021b017

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.escom.tt2021b017.databinding.ActivityHomeBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.*

class HomeActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityHomeBinding
    private val db = FirebaseFirestore.getInstance()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        val currentUser = auth.currentUser

        binding.txtUser.text = "Usuario: " + currentUser?.email
        binding.signOutButton.setOnClickListener{
            signOut()
        }

        binding.btnHistorial.setOnClickListener{
            val intent = Intent(this, HistorialActivity::class.java)
            this.startActivity(intent)
        }

        binding.startButton.setOnClickListener {
            /*val email = currentUser?.email
            if (email != null) {
                db.collection("$email reports").add(
                    hashMapOf("frecuencia" to "70", "fecha" to Timestamp(Date()))
                )
            }*/
            val intent = Intent(this, CheckActivity::class.java)
            this.startActivity(intent)
        }

        binding.userButton.setOnClickListener {
            val intent = Intent(this, UserActivity::class.java)
            this.startActivity(intent)
        }

    }

    private fun signOut(){
        Firebase.auth.signOut()
        val intent = Intent(this, AuthActivity::class.java)
        this.startActivity(intent)
    }
}