package com.escom.tt2021b017

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.escom.tt2021b017.databinding.ActivitySignUpBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.*
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        binding.signUpButton.setOnClickListener {
            val mEmail = binding.emailEditText.text.toString()
            val mPassword = binding.passwordEditText.text.toString()
            val mRepeatPassword = binding.passwordConfirmEditText.text.toString()

            val passwordRegex = Pattern.compile("^" +
                    "(?=.*[-@#$%^&+=])" +
                    ".{6,}" +
                    "$")

            if(mEmail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()){
                Toast.makeText(baseContext, "Ingrese un email valido",
                    Toast.LENGTH_SHORT).show()
            } else if (mPassword.isEmpty() || !passwordRegex.matcher(mPassword).matches()) {
                Toast.makeText(baseContext, "La contraseña no es valida",
                    Toast.LENGTH_SHORT).show()
            } else if (mPassword != mRepeatPassword){
                Toast.makeText(baseContext, "La contraseña es diferente",
                    Toast.LENGTH_SHORT).show()
            } else{
                createAccount(mEmail, mPassword)
                if (mEmail != null) {
                    db.collection("$mEmail user").document("information").set(
                        hashMapOf(
                            "nombre" to "",
                            "apellidos" to "",
                            "fecha" to Timestamp(Date()),
                            "peso" to "",
                            "altura" to "")
                    )
                }
            }
        }

        binding.backButton.setOnClickListener{
            reload()
        }



    }

    private fun createAccount(email: String, password: String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(baseContext, "Cuenta creada",
                        Toast.LENGTH_SHORT).show()
                    reload()
                } else {
                    Log.w("TAG", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Error al crear la cuenta",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun reload(){
        val intent = Intent(this, AuthActivity::class.java)
        this.startActivity(intent)
    }
}