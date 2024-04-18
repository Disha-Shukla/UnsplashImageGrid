package com.das.unsplashimagegrid

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.AbsListView
import android.widget.GridView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.das.unsplashimagegrid.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var gridView: GridView
    private lateinit var adapter: ImageAdapter
    private val networkService = APICalls()
    private var currentPage = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        gridView = binding.gridview

        // Initialize adapter
        adapter = ImageAdapter(this, ArrayList())
        gridView.adapter = adapter

        // Fetch images from Unsplash API
        fetchImages()

        // Set up endless scrolling
        gridView.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
                if (firstVisibleItem + visibleItemCount >= totalItemCount) {
                    // Load more items when user scrolls to the end
                    fetchImages()
                }
            }

            override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
                // Not needed for pagination
            }
        })
    }

    private fun fetchImages() {
        val accessKey = "74NzR_1VF7iGB-rJmv8HhkollEIv6GvyJP62CDht0sQ"

        // Check for internet connectivity
        if (!isNetworkAvailable()) {
            // Show appropriate message when there is no internet
            showMessage("No internet connection")
            return
        }


        networkService.fetchImagesFromUnsplash(accessKey, currentPage) { imageUrls, error ->
            if (error != null) {
                // Handle error
            } else {
                /*runOnUiThread {
                    imageUrls?.let {
                            (gridView.adapter as? ImageAdapter)?.let { adapter ->
                                adapter.addAll(imageUrls)
                                adapter.notifyDataSetChanged()
                                currentPage++ // Increment page for the next fetch
                            }
                    }
                }*/

                runOnUiThread {
                    imageUrls?.let {
                        adapter.addAll(imageUrls)
                        currentPage++ // Increment page for the next fetch
                    }
                }
            }
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        val networkInfo = connectivityManager?.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


}