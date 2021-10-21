package com.spokn.albums.details.di

import com.spokn.album.core.networking.Scheduler
import com.spokn.albums.common.data.local.AlbumDb
import com.spokn.albums.common.data.remote.AlbumService
import com.spokn.albums.details.DetailsActivity
import com.spokn.albums.details.PhotosAdapter
import com.spokn.albums.details.model.DetailsDataContract
import com.spokn.albums.details.model.DetailsLocalData
import com.spokn.albums.details.model.DetailsRemoteData
import com.spokn.albums.details.model.DetailsRepository
import com.spokn.albums.details.viewModel.DetailsViewModelFactory
import com.spokn.albums.list.di.ListComponent
import com.squareup.picasso.Picasso
import dagger.Component
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@DetailsScope
@Component(dependencies = [ListComponent::class], modules = [DetailsModule::class])
interface DetailsComponent {

    fun inject(detailsActivity: DetailsActivity)
}

@Module
class DetailsModule {


    /*Adapter*/
    @Provides
    @DetailsScope
    fun adapter(picasso: Picasso): PhotosAdapter = PhotosAdapter(picasso)

    /*ViewModel*/
    @Provides
    @DetailsScope
    fun detailsViewModelFactory(repo: DetailsDataContract.Repository, compositeDisposable: CompositeDisposable): DetailsViewModelFactory {
        return DetailsViewModelFactory(repo, compositeDisposable)
    }

    /*Repository*/
    @Provides
    @DetailsScope
    fun detailsRepo(local: DetailsDataContract.Local, remote: DetailsDataContract.Remote, scheduler: Scheduler, compositeDisposable: CompositeDisposable)
            : DetailsDataContract.Repository = DetailsRepository(local, remote, scheduler, compositeDisposable)

    @Provides
    @DetailsScope
    fun remoteData(albumService: AlbumService): DetailsDataContract.Remote = DetailsRemoteData(albumService)

    @Provides
    @DetailsScope
    fun localData(albumDb: AlbumDb, scheduler: Scheduler): DetailsDataContract.Local = DetailsLocalData(albumDb, scheduler)

    @Provides
    @DetailsScope
    fun compositeDisposable(): CompositeDisposable = CompositeDisposable()
}