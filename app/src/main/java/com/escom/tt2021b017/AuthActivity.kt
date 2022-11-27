package com.escom.tt2021b017

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.escom.tt2021b017.databinding.ActivityAuthBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AuthActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        binding.logInButton.setOnClickListener{
            val mEmail = binding.emailEditText.text.toString()
            val mPassword = binding.passwordEditText.text.toString()

            when{
                mEmail.isEmpty() || mPassword.isEmpty() -> {
                    Toast.makeText(baseContext, "Correo o contraseña invalidos",
                        Toast.LENGTH_SHORT).show()
                }else -> {
                    signIn(mEmail, mPassword)
                }
            }
        }

        binding.signUpButton.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            this.startActivity(intent)
        }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            reload()
        }
    }

    private fun signIn(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("TAG", "signInWithEmail:success")
                    reload()
                } else {
                    Log.w("TAG", "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Error al iniciar sesion",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun reload(){
        val intent = Intent(this, HomeActivity::class.java)
        this.startActivity(intent)
    }
}