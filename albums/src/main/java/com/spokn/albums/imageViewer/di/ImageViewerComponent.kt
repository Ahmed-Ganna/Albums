package com.spokn.albums.imageViewer.di

import com.spokn.album.core.di.CoreComponent
import com.spokn.album.core.networking.Scheduler
import com.spokn.albums.imageViewer.ImageViewerActivity
import com.spokn.albums.imageViewer.viewModel.ImageViewerViewModelFactory
import com.squareup.picasso.Picasso
import dagger.Component
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@ImageViewerScope
@Component(dependencies = [CoreComponent::class], modules = [ImageViewerModule::class])
interface ImageViewerComponent {

    fun inject(imageViewerActivity: ImageViewerActivity)
}

@Module
class ImageViewerModule {


    /*ViewModel*/
    @Provides
    @ImageViewerScope
    fun viewerViewModelFactory(compositeDisposable: CompositeDisposable, scheduler: Scheduler, picasso: Picasso): ImageViewerViewModelFactory {
        return ImageViewerViewModelFactory(compositeDisposable,scheduler,picasso)
    }

    @Provides
    @ImageViewerScope
    fun compositeDisposable(): CompositeDisposable = CompositeDisposable()

}