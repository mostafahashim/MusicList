package music.list.view.activity.characterDetails

import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import music.list.R
import music.list.databinding.ActivityCharacterDetailsBinding
import music.list.model.MusicModel
import music.list.remoteConnection.setup.isInternetAvailable
import music.list.util.ActivityUtils.collapsingPercentage
import music.list.util.ScreenSizeUtils
import music.list.view.activity.baseActivity.BaseActivity

class CharacterDetailsActivity : BaseActivity(
    R.string.app_name, false, false, false,
    false, false, false, false, false,
), CharacterDetailsViewModel.Observer {

    lateinit var binding: ActivityCharacterDetailsBinding
    override fun doOnCreate(arg0: Bundle?) {
        binding =
            putContentView(R.layout.activity_character_details) as ActivityCharacterDetailsBinding
        binding.viewModel =
            ViewModelProvider(
                this,
                CharacterDetailsViewModelFactory(application)
            )
                .get(CharacterDetailsViewModel::class.java)
        binding.viewModel!!.baseViewModelObserver = baseViewModelObserver
        binding.viewModel!!.observer = this
        binding.lifecycleOwner = this
        binding.viewModel?.musicModel = intent.extras?.get("MusicModel") as MusicModel
        initializeViews()
        setListener()
    }


    override fun initializeViews() {
        setSupportActionBar(binding.toolbarBookDetailsActivity)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        binding.collapsingToolbarBookDetailsActivity.setExpandedTitleColor(Color.TRANSPARENT)
        binding.collapsingToolbarBookDetailsActivity.setCollapsedTitleTextColor(Color.TRANSPARENT)
        binding.appbar.setExpanded(true, false)

        binding.viewModel?.setData()

    }

    override fun setListener() {
    }

}