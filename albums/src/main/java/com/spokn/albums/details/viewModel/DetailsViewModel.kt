package com.spokn.albums.details.viewModel

import androidx.lifecycle.*
import com.spokn.album.core.extensions.toLiveData
import com.spokn.album.core.networking.Outcome
import com.spokn.albums.common.AlbumDH
import com.spokn.albums.common.data.local.Photo
import com.spokn.albums.details.model.DetailsDataContract
import io.reactivex.disposables.CompositeDisposable
import java.lang.Exception

class DetailsViewModel(private val repo: DetailsDataContract.Repository, private val compositeDisposable: CompositeDisposable) : ViewModel() {

    private val repoPhotos: LiveData<Outcome<List<Photo>>> by lazy {
        repo.photosFetchOutcome.toLiveData(compositeDisposable)
    }

    val photosOutcome = MediatorLiveData<Outcome<List<Photo>>>()

    init {
        photosOutcome.addSource(repoPhotos){
            photosOutcome.value = it
        }
    }

    fun loadPhotosFor(albumId: Int?) {
        repo.fetchPhotosFor(albumId)
    }

    fun refreshPhotosFor(albumId: Int?) {
        if (albumId != null)
            repo.refreshPhotos(albumId)
    }

    override fun onCleared() {
        //clear the disposables when the viewmodel is cleared
        compositeDisposable.clear()
        AlbumDH.destroyDetailsComponent()
    }

    fun onNewQuery(query: String) {
        val filteredPhotos = try { (repoPhotos.value as Outcome.Success? )?.data?.filter { query == ""  || it.title.contains(query) } !!}catch (e:Exception){ emptyList()}
        photosOutcome.value = Outcome.success(filteredPhotos)
    }
}