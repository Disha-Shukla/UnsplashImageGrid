package com.das.unsplashimagegrid

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.das.unsplashimagegrid.databinding.ListItemBinding
import com.squareup.picasso.Picasso
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class ImageAdapter(private val context: Context, private val imageUrls: MutableList<String>) : BaseAdapter() {

    override fun getCount(): Int {
        return imageUrls.size
    }

    override fun getItem(position: Int): Any {
        return imageUrls[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }



    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val imageView: ImageView
        if (convertView == null) {
            imageView = ImageView(context)
            imageView.layoutParams = AbsListView.LayoutParams(
                AbsListView.LayoutParams.WRAP_CONTENT, // Adjust width as needed
                350) // Adjust width and height as needed
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            imageView.setPadding(4, 8, 4, 8)
        } else {
            imageView = convertView as ImageView
        }

      /*  // Load image using Picasso library
        Picasso.get()
            .load(imageUrls[position])
            .into(imageView)*/

        // Load image using AsyncTask
        ImageLoadTask(imageView).execute(imageUrls[position])

        return imageView
    }

    // Method to add new items to the existing list
    fun addAll(newImageUrls: List<String>) {
        imageUrls.addAll(newImageUrls)
        notifyDataSetChanged()
    }

    private inner class ImageLoadTask(private val imageView: ImageView) : AsyncTask<String, Void, Bitmap>() {
        override fun doInBackground(vararg params: String): Bitmap? {
            val imageUrl = params[0]
            var bitmap: Bitmap? = null
            try {
                val url = URL(imageUrl)
                val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val inputStream: InputStream = connection.inputStream
                bitmap = BitmapFactory.decodeStream(inputStream)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return bitmap
        }

        override fun onPostExecute(result: Bitmap?) {
            super.onPostExecute(result)
            if (result != null) {
                imageView.setImageBitmap(result)
            }
        }
    }
}