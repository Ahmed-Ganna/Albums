package com.spokn.albums.list.model


import com.spokn.album.core.extensions.*
import com.spokn.album.core.networking.Scheduler
import com.spokn.album.core.networking.synk.Synk
import com.spokn.album.core.networking.synk.SynkKeys
import com.spokn.album.core.networking.Outcome
import com.spokn.albums.common.data.local.Album
import com.spokn.albums.common.data.local.User
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit


class ListRepository(
    private val local: ListDataContract.Local,
    private val remote: ListDataContract.Remote,
    private val scheduler: Scheduler,
    private val compositeDisposable: CompositeDisposable
) : ListDataContract.Repository {

    override val albumsFetchOutcome: PublishSubject<Outcome<List<Album>>> = PublishSubject.create()

    override val userFetchOutcome: PublishSubject<Outcome<User>> = PublishSubject.create()

    init {
        fetchUser()
    }


    override fun fetchUser() {
        userFetchOutcome.loading(true)
        local.getUser()
            .performOnBackOutOnMain(scheduler)
            .subscribeBy(onSuccess = { user ->
                userFetchOutcome.success(user)
                fetchAlbums() }
                , onError = { refreshUser()})
            .addTo(compositeDisposable)

    }

    private fun refreshUser() {
        userFetchOutcome.loading(true)
        remote.getUsers().map {
            val user = it.random()
            saveUser(user)
            user
        }
            .performOnBackOutOnMain(scheduler)
            .subscribeBy(onSuccess = {fetchUser()} , onError = {handleError(it, userFetchOutcome) })
            .addTo(compositeDisposable)
    }

    private fun fetchAlbums() {
        //Observe changes to the db
        albumsFetchOutcome.loading(true)
        local.getAlbums()
            .performOnBackOutOnMain(scheduler)
            .doAfterNext {
                if (Synk.shouldSync(SynkKeys.ALBUMS_HOME, 2, TimeUnit.HOURS))
                    refreshAlbums()
            }
            .subscribeBy(onNext = {albums -> albumsFetchOutcome.success(albums) } , onError = {handleError(it, albumsFetchOutcome)})
            .addTo(compositeDisposable)
    }

    override fun refreshAlbums() {
        albumsFetchOutcome.loading(true)
        local.getUser().doOnError { refreshUser()}.flatMap {
           remote.getAlbums(it.id)
        }.map {
            saveAlbums(it)
        }
            .performOnBackOutOnMain(scheduler)
            .subscribeBy(onSuccess = {} , onError = {handleError(it, albumsFetchOutcome)})
            .addTo(compositeDisposable)
    }

    override fun saveUser(user: User) {
        local.saveUser(user)
    }

    override fun saveAlbums(albums: List<Album>) {
        local.saveAlbums(albums)
    }

    override fun <T> handleError(error: Throwable, subject: PublishSubject<Outcome<T>>) {
        subject.failed(error)
    }


}