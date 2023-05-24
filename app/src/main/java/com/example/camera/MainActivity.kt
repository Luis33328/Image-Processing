package com.example.camera

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.core.view.drawToBitmap

class MainActivity : AppCompatActivity() {


    private lateinit var imageView: ImageView
    private lateinit var takePicButton: Button
    private lateinit var appFilterButton: Button

    val GET_IMAGE = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        imageView = findViewById(R.id.imageView)

        takePicButton = findViewById(R.id.takePicture)
        takePicButton.setOnClickListener{
            takePicture()
        }

        appFilterButton = findViewById(R.id.applyFilter)
        appFilterButton.setOnClickListener{
            val bitmap = this.imageView.drawToBitmap()
            this.imageView.setImageBitmap(convertToGray(bitmap))
        }
    }

    private fun convertToGray(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        val grayBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        for (x in 0 until width) {
            for (y in 0 until height) {

                val pixel = bitmap.getPixel(x, y)
                val red = Color.red(pixel)
                val green = Color.green(pixel)
                val blue = Color.blue(pixel)

                val gray = (red + green + blue) / 3
                val grayPixel = Color.rgb(gray, gray, gray)

                grayBitmap.setPixel(x, y, grayPixel)
            }
        }

        return grayBitmap
    }

    private fun takePicture() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, GET_IMAGE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GET_IMAGE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            this.imageView.setImageBitmap(imageBitmap)
        }
    }



}