package com.escom.tt2021b017

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.escom.tt2021b017.databinding.ActivityUserBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.*


class UserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding
    private val db = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        val currentUser = auth.currentUser

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        getInformation()

        binding.saveButton.setOnClickListener{
            val email = currentUser?.email
            if (email != null) {
                db.collection("$email user").document("information").set(
                    hashMapOf(
                        "nombre" to binding.txtNombre.text.toString(),
                        "apellidos" to binding.txtApellidos.text.toString(),
                        "fecha" to transformToDate(),
                        "peso" to binding.peso.text.toString(),
                        "altura" to binding.altura.text.toString())
                ).addOnSuccessListener {
                    Toast.makeText(this, "Información guardada correctamente", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loading(flag : Boolean) {
        if(flag){
            binding.txtNombre.visibility = View.GONE
            binding.txtApellidos.visibility = View.GONE
            binding.txtfechaaux.visibility = View.GONE
            binding.txtdia.visibility = View.GONE
            binding.txtmes.visibility = View.GONE
            binding.txtanio.visibility = View.GONE
            binding.peso.visibility = View.GONE
            binding.altura.visibility = View.GONE
            binding.saveButton.visibility = View.GONE
            binding.txtpesoaux.visibility = View.GONE
            binding.txtalturaaux.visibility = View.GONE
        }else{
            binding.txtNombre.visibility = View.VISIBLE
            binding.txtApellidos.visibility = View.VISIBLE
            binding.txtfechaaux.visibility = View.VISIBLE
            binding.txtdia.visibility = View.VISIBLE
            binding.txtmes.visibility = View.VISIBLE
            binding.txtanio.visibility = View.VISIBLE
            binding.peso.visibility = View.VISIBLE
            binding.altura.visibility = View.VISIBLE
            binding.saveButton.visibility = View.VISIBLE
            binding.txtpesoaux.visibility = View.VISIBLE
            binding.txtalturaaux.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
        }

    }

    private fun getInformation() {
        loading(true)
        val currentUser = auth.currentUser
        val email = currentUser?.email
        db.collection("$email user").document("information").get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    binding.txtNombre.setText(document.get("nombre").toString())
                    binding.txtApellidos.setText(document.get("apellidos").toString())
                    trasnformDateToStrings(document.get("fecha") as Timestamp)
                    binding.peso.setText(document.get("peso").toString())
                    binding.altura.setText(document.get("altura").toString())
                }

                loading(false)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al traer la información", Toast.LENGTH_SHORT).show()
                loading(false)
            }
    }

    private fun transformToDate() : Date {
        val day: Int = Integer.parseInt(binding.txtdia.text.toString())
        val month: Int = Integer.parseInt(binding.txtmes.text.toString()) -1
        val year: Int = Integer.parseInt(binding.txtanio.text.toString())

        val calendar: Calendar = Calendar.getInstance()
        calendar.set(year, month, day)

        return calendar.time
    }

    private fun transformTimestampToDate(date: Timestamp): Date {
        var seconds = date.seconds
        seconds = (seconds * 1000)
        return Date(seconds)
    }

    @SuppressLint("SetTextI18n")
    private fun trasnformDateToStrings(timestamp: Timestamp) {
        val date = transformTimestampToDate(timestamp)
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = date

        binding.txtdia.setText(calendar.get(Calendar.DAY_OF_MONTH).toString())
        binding.txtmes.setText((calendar.get(Calendar.MONTH) +1).toString())
        binding.txtanio.setText(calendar.get(Calendar.YEAR).toString())
    }
}