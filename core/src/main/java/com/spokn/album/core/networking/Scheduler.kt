package com.spokn.album.core.networking

import io.reactivex.Scheduler

/**
 *  Interface to mock different threads during testing.
* */
interface Scheduler {
    fun mainThread():Scheduler
    fun io():Scheduler
}