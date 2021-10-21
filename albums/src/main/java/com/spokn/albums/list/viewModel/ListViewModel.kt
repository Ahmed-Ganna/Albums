package com.spokn.albums.list.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.spokn.album.core.extensions.toLiveData
import com.spokn.album.core.networking.Outcome
import com.spokn.albums.common.AlbumDH
import com.spokn.albums.common.data.local.Album
import com.spokn.albums.common.data.local.User
import com.spokn.albums.list.model.ListDataContract
import io.reactivex.disposables.CompositeDisposable

class ListViewModel(private val repo: ListDataContract.Repository,
                    private val compositeDisposable: CompositeDisposable) : ViewModel() {

    val userOutcome: LiveData<Outcome<User>> by lazy {
        repo.userFetchOutcome.toLiveData(compositeDisposable)
    }
    val albumsOutcome: LiveData<Outcome<List<Album>>> by lazy {
        repo.albumsFetchOutcome.toLiveData(compositeDisposable)
    }

    fun refreshAlbums() {
        repo.refreshAlbums()
    }

    override fun onCleared() {
        super.onCleared()
        //clear the disposables when the viewmodel is cleared
        compositeDisposable.clear()
        AlbumDH.destroyListComponent()
    }
}