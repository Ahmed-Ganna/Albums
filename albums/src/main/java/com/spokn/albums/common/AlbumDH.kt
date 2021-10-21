package com.spokn.albums.common

import com.spokn.album.core.application.CoreApp
import com.spokn.albums.details.di.DaggerDetailsComponent
import com.spokn.albums.details.di.DetailsComponent
import com.spokn.albums.imageViewer.di.DaggerImageViewerComponent
import com.spokn.albums.imageViewer.di.ImageViewerComponent
import com.spokn.albums.list.di.DaggerListComponent
import com.spokn.albums.list.di.ListComponent
import javax.inject.Singleton

@Singleton
object AlbumDH {
    private var listComponent: ListComponent? = null
    private var detailsComponent: DetailsComponent? = null
    private var imageViewerComponent: ImageViewerComponent? = null

    fun listComponent(): ListComponent {
        if (listComponent == null)
            listComponent = DaggerListComponent.builder().coreComponent(CoreApp.coreComponent).build()
        return listComponent as ListComponent
    }

    fun destroyListComponent() {
        listComponent = null
    }

    fun detailsComponent(): DetailsComponent {
        if (detailsComponent == null)
            detailsComponent = DaggerDetailsComponent.builder().listComponent(listComponent()).build()
        return detailsComponent as DetailsComponent
    }

    fun destroyDetailsComponent() {
        detailsComponent = null
    }

    fun imageViewerComponent(): ImageViewerComponent {
        if (imageViewerComponent == null)
            imageViewerComponent = DaggerImageViewerComponent.builder().coreComponent(CoreApp.coreComponent).build()
        return imageViewerComponent as ImageViewerComponent
    }



    fun destroyImageViewerComponent() {
        imageViewerComponent = null
    }

}