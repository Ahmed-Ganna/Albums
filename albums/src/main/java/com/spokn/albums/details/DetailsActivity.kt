package com.spokn.albums.details

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProviders
import com.spokn.album.core.application.BaseActivity
import com.spokn.album.core.networking.Outcome
import com.spokn.albums.R
import com.spokn.albums.common.AlbumDH
import com.spokn.albums.common.data.local.Album
import com.spokn.albums.common.data.local.Photo
import com.spokn.albums.details.exception.DetailsExceptions
import com.spokn.albums.details.viewModel.DetailsViewModel
import com.spokn.albums.details.viewModel.DetailsViewModelFactory
import java.io.IOException
import javax.inject.Inject
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import com.spokn.albums.databinding.ActivityDetailsBinding
import com.spokn.albums.imageViewer.ImageViewerActivity


class DetailsActivity : BaseActivity(), PhotosAdapter.Interaction {

    companion object {
        private const val SELECTED_ALBUM = "album"
        //Transitions
        private const val TITLE_TRANSITION_NAME = "title_transition"

        fun start(
            context: Context,
            album: Album,
            tvTitle: TextView
        ) {
            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra(SELECTED_ALBUM, album)

            //Transitions
            intent.putExtra(TITLE_TRANSITION_NAME, ViewCompat.getTransitionName(tvTitle))

            val p1 = Pair.create(tvTitle as View, ViewCompat.getTransitionName(tvTitle))
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    context as Activity,
                    p1
            )

            context.startActivity(intent, options.toBundle())
        }

        fun start(context: Context, albumId: Int) {
            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra(SELECTED_ALBUM, albumId)
            context.startActivity(intent)
        }
    }

    private val TAG = "DetailsActivity"
    private var selectedAlbum: Album? = null
    private val context: Context  by lazy { this }

    private val component by lazy { AlbumDH.detailsComponent() }

    @Inject
    lateinit var adapter: PhotosAdapter

    @Inject
    lateinit var viewModelFactory: DetailsViewModelFactory

    private lateinit var viewBinding : ActivityDetailsBinding

    private val viewModel: DetailsViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(DetailsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this,R.layout.activity_details)
        component.inject(this)

        viewBinding.srlPhotos.isEnabled = false


        clickActions()
        getIntentData()
        observeData()
    }

    private fun clickActions() {
        viewBinding.inputSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                performSearch(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun performSearch(query: String) {
        viewModel.onNewQuery(query)
    }

    private fun getIntentData() {
        if (!intent.hasExtra(SELECTED_ALBUM)) {
            Log.d(TAG, "getIntentData: could not find selected album")
            finish()
            return
        }

        selectedAlbum = intent.getParcelableExtra(SELECTED_ALBUM)
        viewBinding.tvTitle.text = selectedAlbum?.title

        handleTransition(intent.extras)

        viewBinding.rvImages.adapter = adapter
        adapter.interaction = this

        viewModel.loadPhotosFor(selectedAlbum?.id)
    }

    private fun handleTransition(extras: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            viewBinding.tvTitle.transitionName = extras?.getString(TITLE_TRANSITION_NAME)
        }
    }

    private fun observeData() {
        viewModel.photosOutcome.observe(this, { outcome ->
            Log.d(TAG, "initiateDataListener: $outcome")
            when (outcome) {

                is Outcome.Progress -> viewBinding.srlPhotos.isRefreshing = outcome.loading

                is Outcome.Success -> {
                    Log.d(TAG, "observeData:  Successfully loaded data")
                    viewBinding.tvPhotoError.visibility = View.GONE
                    adapter.swapData(outcome.data)
                }

                is Outcome.Failure -> {
                    when (outcome.e) {
                        DetailsExceptions.NoPhotos() -> viewBinding.tvPhotoError.visibility =
                                View.VISIBLE
                        IOException() -> Toast.makeText(
                                context,
                                R.string.need_internet,
                                Toast.LENGTH_LONG
                        ).show()
                        else -> Toast.makeText(
                                context,
                                R.string.failed_try_again,
                                Toast.LENGTH_LONG
                        ).show()
                    }
                }

            }
        })
    }

    override fun photoClicked(model: Photo, imageView: ImageView) {
        ImageViewerActivity.start(this,model.thumbnailUrl)
    }
}
