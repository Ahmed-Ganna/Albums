package com.spokn.albums.details.model

import com.spokn.album.core.extensions.performOnBack
import com.spokn.album.core.networking.Scheduler
import com.spokn.albums.common.data.local.AlbumDb
import com.spokn.albums.common.data.local.Photo
import io.reactivex.Completable
import io.reactivex.Flowable

class DetailsLocalData(private val albumDb: AlbumDb, private val scheduler: Scheduler) : DetailsDataContract.Local {

    override fun getPhotosForAlbum(albumId: Int): Flowable<List<Photo>> {
        return albumDb.photoDao().getForAlbum(albumId)
    }

    override fun savePhotos(photos: List<Photo>) {
        Completable.fromAction {
            albumDb.photoDao().upsertAll(photos)
        }
                .performOnBack(scheduler)
                .subscribe()
    }
}