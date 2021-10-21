package com.spokn.albums.common.data.local

import androidx.room.*
import io.reactivex.Flowable

@Dao
interface PhotoDao {

    @Query("SELECT * from photo where albumId = :albumId")
    fun getForAlbum(albumId: Int): Flowable<List<Photo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertAll(comments: List<Photo>)

    @Delete
    fun delete(photo: Photo)
}