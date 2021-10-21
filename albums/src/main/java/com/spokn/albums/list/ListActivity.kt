package com.spokn.albums.list

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.spokn.album.core.application.BaseActivity
import com.spokn.album.core.networking.Outcome
import com.spokn.albums.R
import com.spokn.albums.common.AlbumDH
import com.spokn.albums.common.data.local.Album
import com.spokn.albums.databinding.ActivityListBinding
import com.spokn.albums.details.DetailsActivity
import com.spokn.albums.list.viewModel.ListViewModel
import com.spokn.albums.list.viewModel.ListViewModelFactory
import java.io.IOException
import javax.inject.Inject

class ListActivity : BaseActivity(), AlbumListAdapter.Interaction {

    private val component by lazy { AlbumDH.listComponent() }

    @Inject
    lateinit var viewModelFactory: ListViewModelFactory

    @Inject
    lateinit var adapter: AlbumListAdapter

    private lateinit var viewBinding : ActivityListBinding

    private val viewModel: ListViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(ListViewModel::class.java)
    }

    private val context: Context by lazy { this }

    private val TAG = "ListActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this,R.layout.activity_list)

        component.inject(this)

        adapter.interaction = this
        viewBinding.rvAlbums.adapter = adapter
        viewBinding.srlAlbums.setOnRefreshListener { viewModel.refreshAlbums() }

        initiateDataListener()
    }

    private fun initiateDataListener() {

        viewModel.userOutcome.observe(this, { outcome ->
            Log.d(TAG, "userOutcomeInitiateDataListener: $outcome")
            when (outcome) {

                is Outcome.Progress -> viewBinding.srlAlbums.isRefreshing = outcome.loading

                is Outcome.Success -> {
                   Log.d(TAG, "albumsOutcomeInitiateDataListener: Successfully loaded data")
                    viewBinding.tvUserName.text = outcome.data.userName
                    viewBinding.tvUserAddress.text = outcome.data.address.toString()
                }

                is Outcome.Failure -> onFailure(outcome)

            }
        })


        //Observe the outcome and update state of the screen  accordingly
        viewModel.albumsOutcome.observe(this, { outcome ->
            Log.d(TAG, "albumsOutcomeInitiateDataListener: $outcome")
            when (outcome) {

                is Outcome.Progress ->  viewBinding.srlAlbums.isRefreshing = outcome.loading

                is Outcome.Success -> {
                    Log.d(TAG, "albumsOutcomeInitiateDataListener: Successfully loaded data")
                    adapter.swapData(outcome.data)
                }

                is Outcome.Failure ->  onFailure(outcome)

            }
        })


    }

    private fun <T>onFailure(outcome: Outcome.Failure<T>) {
        Log.e(TAG, "onFailure" ,outcome.e)

        if (outcome.e is IOException)
            Toast.makeText(
                context,
                R.string.need_internet,
                Toast.LENGTH_LONG
            ).show()
        else
            Toast.makeText(
                context,
                R.string.failed_try_again,
                Toast.LENGTH_LONG
            ).show()
    }

    override fun albumClicked(
            album: Album,
            tvTitle: TextView,
    ) {
        DetailsActivity.start(context, album, tvTitle)
    }

}
