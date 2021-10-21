package com.spokn.albums.common.data.local

import androidx.room.*
import io.reactivex.Flowable


@Dao
interface AlbumDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertAll(albums: List<Album>)


    @Query("SELECT * FROM album")
    fun getAll(): Flowable<List<Album>>
}