package com.spokn.albums.details.model

import com.spokn.album.core.extensions.*
import com.spokn.album.core.networking.Scheduler
import com.spokn.album.core.networking.synk.Synk
import com.spokn.album.core.networking.synk.SynkKeys
import com.spokn.album.core.networking.Outcome
import com.spokn.albums.common.data.local.Photo
import com.spokn.albums.details.exception.DetailsExceptions
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class DetailsRepository(
    private val local: DetailsDataContract.Local,
    private val remote: DetailsDataContract.Remote,
    private val scheduler: Scheduler,
    private val compositeDisposable: CompositeDisposable
) : DetailsDataContract.Repository {

    override val photosFetchOutcome: PublishSubject<Outcome<List<Photo>>> =
        PublishSubject.create()


    override fun fetchPhotosFor(albumId: Int?) {
        if (albumId == null)
            return

        photosFetchOutcome.loading(true)
        local.getPhotosForAlbum(albumId)
            .performOnBackOutOnMain(scheduler)
            .doAfterNext {
                if (Synk.shouldSync(SynkKeys.ALBUM_DETAILS + "_" + albumId, 2, TimeUnit.HOURS))
                    refreshPhotos(albumId)
            }
            .subscribeBy(onNext = {photos -> photosFetchOutcome.success(photos) } , onError = {handleError(it) })
            .addTo(compositeDisposable)
    }

    override fun refreshPhotos(albumId: Int) {
        photosFetchOutcome.loading(true)
        remote.getPhotosForAlbum(albumId)
            .performOnBackOutOnMain(scheduler)
            .subscribeBy(onSuccess = {photos -> savePhotosForAlbum(photos)} , onError = {error -> handleError(error) })
            .addTo(compositeDisposable)
    }

    override fun savePhotosForAlbum(photos: List<Photo>) {
        if (photos.isNotEmpty()) {
            local.savePhotos(photos)
        } else
            photosFetchOutcome.failed(DetailsExceptions.NoPhotos())
    }

    override fun handleError(error: Throwable) {
        photosFetchOutcome.failed(error)
    }
}