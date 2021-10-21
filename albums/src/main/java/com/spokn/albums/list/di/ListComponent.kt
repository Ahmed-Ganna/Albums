package com.spokn.albums.list.di

import androidx.room.Room
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.spokn.album.core.constants.Constants
import com.spokn.album.core.di.CoreComponent
import com.spokn.album.core.networking.Scheduler
import com.spokn.albums.common.data.local.AlbumDb
import com.spokn.albums.common.data.remote.AlbumService
import com.spokn.albums.list.AlbumListAdapter
import com.spokn.albums.list.ListActivity
import com.spokn.albums.list.model.ListDataContract
import com.spokn.albums.list.model.ListLocalData
import com.spokn.albums.list.model.ListRemoteData
import com.spokn.albums.list.model.ListRepository
import com.spokn.albums.list.viewModel.ListViewModelFactory
import com.squareup.picasso.Picasso
import dagger.Component
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Retrofit

@ListScope
@Component(dependencies = [CoreComponent::class], modules = [ListModule::class])
interface ListComponent {

    //Expose to dependent components
    fun albumDb(): AlbumDb

    fun albumService(): AlbumService
    fun picasso(): Picasso
    fun scheduler(): Scheduler

    fun inject(listActivity: ListActivity)
}

@Module
@ListScope
class ListModule {

    /*Adapter*/
    @Provides
    @ListScope
    fun adapter(): AlbumListAdapter = AlbumListAdapter()

    /*ViewModel*/
    @Provides
    @ListScope
    fun listViewModelFactory(repository: ListDataContract.Repository, compositeDisposable: CompositeDisposable): ListViewModelFactory = ListViewModelFactory(repository,compositeDisposable)

    /*Repository*/
    @Provides
    @ListScope
    fun listRepo(local: ListDataContract.Local, remote: ListDataContract.Remote, scheduler: Scheduler, compositeDisposable: CompositeDisposable): ListDataContract.Repository = ListRepository(local, remote, scheduler, compositeDisposable)

    @Provides
    @ListScope
    fun remoteData(albumService: AlbumService): ListDataContract.Remote = ListRemoteData(albumService)

    @Provides
    @ListScope
    fun localData(albumDb: AlbumDb,pref: SharedPreferences ,gson:Gson,scheduler: Scheduler): ListDataContract.Local = ListLocalData(albumDb,pref ,gson , scheduler)

    @Provides
    @ListScope
    fun compositeDisposable(): CompositeDisposable = CompositeDisposable()

    /*Parent providers to dependents*/
    @Provides
    @ListScope
    fun albumsDb(context: Context): AlbumDb = Room.databaseBuilder(context, AlbumDb::class.java, Constants.Albums.DB_NAME).build()

    @Provides
    @ListScope
    fun albumService(retrofit: Retrofit): AlbumService = retrofit.create(AlbumService::class.java)
}