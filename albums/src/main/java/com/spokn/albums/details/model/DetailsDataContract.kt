package com.spokn.albums.details.model

import com.spokn.album.core.networking.Outcome
import com.spokn.albums.common.data.local.Photo
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

interface DetailsDataContract {
    interface Repository {
        val photosFetchOutcome: PublishSubject<Outcome<List<Photo>>>
        fun fetchPhotosFor(albumId: Int?)
        fun refreshPhotos(albumId: Int)
        fun savePhotosForAlbum(photos: List<Photo>)
        fun handleError(error: Throwable)
    }

    interface Local {
        fun getPhotosForAlbum(albumId: Int): Flowable<List<Photo>>
        fun savePhotos(photos: List<Photo>)
    }

    interface Remote {
        fun getPhotosForAlbum(albumId: Int): Single<List<Photo>>
    }
}