package com.spokn.albums.imageViewer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.spokn.album.core.application.BaseActivity
import com.spokn.albums.R
import com.spokn.albums.common.AlbumDH
import com.spokn.albums.databinding.ActivityImageViewerBinding
import javax.inject.Inject
import com.spokn.albums.imageViewer.viewModel.ImageViewerViewModel
import com.spokn.albums.imageViewer.viewModel.ImageViewerViewModelFactory
import com.squareup.picasso.Picasso


class ImageViewerActivity : BaseActivity(){

    companion object {
        private const val SELECTED_IMAGE = "image"


        fun start(
            context: Context,
            image: String
        ) {
            val intent = Intent(context, ImageViewerActivity::class.java)
            intent.putExtra(SELECTED_IMAGE, image)

            context.startActivity(intent)
        }

    }

    private lateinit var viewBinding : ActivityImageViewerBinding

    private val TAG = "ImageViewerActivity"
    private var selectedImage: String? = null

    private val component by lazy { AlbumDH.imageViewerComponent() }


    @Inject
    lateinit var viewModelFactory: ImageViewerViewModelFactory

    @Inject
    lateinit var picasso: Picasso

    private val viewModel: ImageViewerViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(ImageViewerViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this,R.layout.activity_image_viewer)
        component.inject(this)

        getIntentData()
    }


    private fun getIntentData() {
        if (!intent.hasExtra(SELECTED_IMAGE)) {
            Log.d(TAG, "getIntentData: could not find selected photo")
            finish()
            return
        }

        selectedImage = intent.getStringExtra(SELECTED_IMAGE)

        picasso.load(selectedImage).into(viewBinding.ivImage)


    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_viewer, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_share ->{
                viewModel.onShareImageClick(selectedImage!!)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
