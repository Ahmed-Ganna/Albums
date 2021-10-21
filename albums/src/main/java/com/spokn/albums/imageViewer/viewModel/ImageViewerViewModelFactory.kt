package com.spokn.albums.imageViewer.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.spokn.album.core.networking.Scheduler
import com.squareup.picasso.Picasso
import io.reactivex.disposables.CompositeDisposable

@Suppress("UNCHECKED_CAST")
class ImageViewerViewModelFactory(private val compositeDisposable: CompositeDisposable,private val scheduler: Scheduler,private val picasso: Picasso) :
        ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ImageViewerViewModel(compositeDisposable,scheduler,picasso) as T
    }
}