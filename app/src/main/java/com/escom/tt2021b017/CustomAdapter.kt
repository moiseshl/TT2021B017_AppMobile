package com.escom.tt2021b017

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.escom.tt2021b017.databinding.CardLayoutBinding

class CustomAdapter(private val reports: List<Report>): RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_layout, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val report = reports[position]

        with(viewHolder){
            binding.txtFecha.text = report.fecha
            binding.txtFrecuencia.text = report.getFrecuenciaToString()
        }
    }

    override fun getItemCount(): Int {
        return reports.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val binding = CardLayoutBinding.bind(itemView)

        init {
            binding.btnRevisar.setOnClickListener {
                val intent = Intent(it.context, ReviewActivity::class.java).apply {
                    putExtra("fecha", binding.txtFecha.text.toString())
                    putExtra("frecuencia", binding.txtFrecuencia.text.toString())
                }
                it.context.startActivity(intent)
            }
        }
    }
}