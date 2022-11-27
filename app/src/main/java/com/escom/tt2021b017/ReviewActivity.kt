package com.escom.tt2021b017

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.text.TextPaint
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.escom.tt2021b017.databinding.ActivityReviewBinding
import java.io.File
import java.io.FileOutputStream

class ReviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener{
            onBackPressed()
        }

        val bundle = intent.extras
        val fecha = bundle?.getString("fecha")
        val frecuencia = bundle?.getString("frecuencia")

        binding.txtFecha.text = fecha
        binding.txtFrecuencia.text = frecuencia

        if(!checkPermission()){
            requestPermissions()
        }

        binding.btnSave.setOnClickListener {
            generarPDF(fecha.toString(), frecuencia.toString())
        }
    }

    private fun generarPDF(fecha : String, frecuencia: String){
        val pdfDocument = PdfDocument()
        val paint = Paint()
        val titulo = TextPaint()
        val texto = TextPaint()
        val desc = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard \n"+
                   "dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. \n" +
                   "It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. \n" +
                   "It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with \n" +
                   "desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."

        val paginaInfo = PdfDocument.PageInfo.Builder(816, 1054, 1).create()
        val pagina = pdfDocument.startPage(paginaInfo)

        val canvas = pagina.canvas

        val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher )
        val bitmapEscala = Bitmap.createScaledBitmap(bitmap, 80, 80, false)
        canvas.drawBitmap(bitmapEscala, 368f, 20f, paint)


        titulo.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        titulo.textSize = 24f
        canvas.drawText(fecha, 20f, 150f, titulo)

        texto.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
        texto.textSize = 14f

        val arrDesc = desc.split("\n")

        var y = 200f
        for(item in arrDesc){
            canvas.drawText(item, 15f, y, texto)
            y+=15
        }

        canvas.drawText("La frecuencia cardíaca al momento de la revisión fue de: $frecuencia", 15f, y+30f, texto)

        pdfDocument.finishPage(pagina)

        val file = getFilePath()
        try {
            pdfDocument.writeTo(FileOutputStream(file))
            Toast.makeText(this, "Se creó el PDF correctamente", Toast.LENGTH_SHORT).show()
        } catch (e: Exception){
            Toast.makeText(this, "Error al crear el PDF", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
            Log.d("TAG", e.toString())
        }

        pdfDocument.close()
    }

    private fun getFilePath(): File {
        val contextWrapper = ContextWrapper(applicationContext)
        val documentDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        return File(documentDirectory, "archivo.pdf")
    }

    /*
    * Revisar y solicitar permisos de escritura de archivos
    */

    private fun checkPermission(): Boolean{
        val permission = ContextCompat.checkSelfPermission(applicationContext, WRITE_EXTERNAL_STORAGE)
        val permission2 = ContextCompat.checkSelfPermission(applicationContext, READ_EXTERNAL_STORAGE)
        return permission == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE),
            200
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==200){
            if(grantResults.isNotEmpty()){
                val writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED

                if(writeStorage && readStorage){
                    Toast.makeText(this, "Permisos concedidos", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "Permisos denegados", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}