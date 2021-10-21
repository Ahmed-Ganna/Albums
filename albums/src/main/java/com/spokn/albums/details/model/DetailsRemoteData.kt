package com.spokn.albums.details.model

import com.spokn.albums.common.data.remote.AlbumService


class DetailsRemoteData(private val albumService: AlbumService) : DetailsDataContract.Remote {

    override fun getPhotosForAlbum(albumId: Int) = albumService.getPhotos(albumId)

}