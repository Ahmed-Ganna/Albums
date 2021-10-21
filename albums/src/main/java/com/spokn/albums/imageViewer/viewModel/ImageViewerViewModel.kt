package com.spokn.albums.imageViewer.viewModel

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.lifecycle.*
import com.spokn.albums.common.AlbumDH
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import android.content.Intent
import android.provider.MediaStore.Images
import android.content.ContentValues
import android.net.Uri
import android.util.Log
import com.blankj.utilcode.util.ActivityUtils
import com.spokn.album.core.application.CoreApp
import com.spokn.album.core.extensions.addTo
import com.spokn.album.core.extensions.performOnBackOutOnMain
import com.spokn.album.core.networking.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import java.io.OutputStream
import java.lang.Exception


class ImageViewerViewModel(private val compositeDisposable: CompositeDisposable,private val scheduler: Scheduler,private val picasso: Picasso) : ViewModel() {





    fun onShareImageClick(selectedImage: String) {
        picasso.load(selectedImage).into(object : Target {
            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                bitmap?.let { convertToMediaStore(it) }
            }

            override fun onBitmapFailed(errorDrawable: Drawable?) {

            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

            }
        })
    }


    private fun convertToMediaStore(bitmap: Bitmap){

        Single.create<Uri> {
            val values = ContentValues()
            values.put(Images.Media.TITLE, "title")
            values.put(Images.Media.MIME_TYPE, "image/jpeg")

            val uri = CoreApp.coreComponent.context().contentResolver.insert(
                Images.Media.EXTERNAL_CONTENT_URI,
                values
            )
            val outStream: OutputStream?
            try {
                outStream = CoreApp.coreComponent.context().contentResolver.openOutputStream(uri)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
                outStream.close()
                it.onSuccess(uri)
            } catch (e: Exception) {
                e.printStackTrace()
                it.onError(e)
            }
        }
            .performOnBackOutOnMain(scheduler)
            .subscribeBy(onSuccess = {shareImage(it)} , onError = {Log.d("ImageViewerViewModel","convertToMediaStore" , it)})
            .addTo(compositeDisposable)


    }

    private fun shareImage(it: Uri) {
        val share = Intent(Intent.ACTION_SEND)
        share.type = "image/jpeg"
        share.putExtra(Intent.EXTRA_STREAM, it)
        ActivityUtils.startActivity(Intent.createChooser(share, "Share Image"))

    }

    override fun onCleared() {
        //clear the disposables when the viewmodel is cleared
        compositeDisposable.clear()
        AlbumDH.destroyImageViewerComponent()
    }

}