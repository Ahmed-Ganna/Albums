package com.spokn.albums.list.model


import com.spokn.album.core.networking.Outcome
import com.spokn.albums.common.data.local.Album
import com.spokn.albums.common.data.local.User
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

interface ListDataContract {
    interface Repository {
        val albumsFetchOutcome: PublishSubject<Outcome<List<Album>>>
        val userFetchOutcome: PublishSubject<Outcome<User>>
        fun fetchUser()
        fun refreshAlbums()
        fun saveUser(user: User)
        fun saveAlbums(albums: List<Album>)
        fun <T>handleError(error: Throwable,subject:PublishSubject<Outcome<T>>)
    }

    interface Local {
        fun getAlbums(): Flowable<List<Album>>
        fun getUser(): Single<User>
        fun saveUser(user: User)
        fun saveAlbums(albums: List<Album>)
    }

    interface Remote {
        fun getUsers(): Single<List<User>>
        fun getAlbums(userId:Int): Single<List<Album>>
    }
}