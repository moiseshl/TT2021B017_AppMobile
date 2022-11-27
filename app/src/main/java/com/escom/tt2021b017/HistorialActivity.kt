package com.escom.tt2021b017

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.escom.tt2021b017.databinding.ActivityHistorialBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class HistorialActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistorialBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistorialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener{
            onBackPressed()
        }

        getReports()

    }

    private fun getReports(){

        val reports = mutableListOf<Report>()
        val currentUser = Firebase.auth.currentUser
        val email = currentUser?.email
        db.collection("$email reports").orderBy("fecha", Query.Direction.DESCENDING).get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val date = transformToDate(document.get("fecha") as Timestamp)
                    val report = (Report(document.id, date, document.get("frecuencia") as String))
                    reports.add(report)
                }
                val recyclerView = binding.recyclerView
                val adapter = CustomAdapter(reports)

                recyclerView.layoutManager = LinearLayoutManager(this)
                recyclerView.adapter = adapter

                //Cantidad de reportes
                binding.txtBadge.text = adapter.itemCount.toString()

                //quitar progressBar
                binding.progressBar.visibility = View.GONE
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error al cargar los datos", Toast.LENGTH_SHORT).show()
            }
    }

    private fun transformToDate(date: Timestamp): String {
        var seconds = date.seconds
        seconds = (seconds * 1000)
        return SimpleDateFormat("dd MMMM yyyy, HH:mm:ss", Locale("ES")).format(seconds)
    }
}