package com.spokn.albums.common.data.remote


import com.spokn.albums.common.data.local.Album
import com.spokn.albums.common.data.local.Photo
import com.spokn.albums.common.data.local.User
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface AlbumService {

    @GET("/albums/")
    fun getAlbums(@Query("userId") userId:Int): Single<List<Album>>

    @GET("/users/")
    fun getUsers(): Single<List<User>>

    @GET("/photos/")
    fun getPhotos(@Query("albumId") albumId: Int): Single<List<Photo>>
}