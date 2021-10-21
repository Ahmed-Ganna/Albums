package com.spokn.albums.list.model

import com.spokn.albums.common.data.remote.AlbumService


class ListRemoteData(private val albumService: AlbumService) : ListDataContract.Remote {

    override fun getUsers() = albumService.getUsers()

    override fun getAlbums(userId:Int) = albumService.getAlbums(userId)
}