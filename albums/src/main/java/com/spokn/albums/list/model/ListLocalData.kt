package com.spokn.albums.list.model


import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.google.gson.Gson
import com.spokn.album.core.constants.Constants
import com.spokn.album.core.extensions.performOnBack
import com.spokn.album.core.networking.Scheduler
import com.spokn.albums.common.data.local.Album
import com.spokn.albums.common.data.local.AlbumDb
import com.spokn.albums.common.data.local.User
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import java.lang.Exception
import java.util.*

class ListLocalData(private val albumDb: AlbumDb, private val sharedPreferences: SharedPreferences , private val gson: Gson, private val scheduler: Scheduler) : ListDataContract.Local {

    override fun getAlbums(): Flowable<List<Album>> {
        return albumDb.albumDao().getAll()
    }

    override fun getUser(): Single<User> {
        return Single.create {
            val gsonString = sharedPreferences.getString(Constants.Albums.KEY_USER,null)
            if (gsonString!=null) it.onSuccess(gson.fromJson(gsonString,User::class.java))
            else it.onError(Exception("User not found"))
            return@create
        }
    }

    @SuppressLint("ApplySharedPref")
    override fun saveUser(user: User) {
        Completable.fromAction {
            val json = gson.toJson(user)
            sharedPreferences.edit().putString(Constants.Albums.KEY_USER,json).commit()
        }
                .performOnBack(scheduler)
                .subscribe()
    }

    override fun saveAlbums(albums: List<Album>) {
        Completable.fromAction {
            albumDb.albumDao().upsertAll(albums)
        }
            .performOnBack(scheduler)
            .subscribe()
    }
}