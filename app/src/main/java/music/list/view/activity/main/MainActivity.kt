package music.list.view.activity.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import music.list.R
import music.list.databinding.ActivityMainBinding
import music.list.model.MusicModel
import music.list.remoteConnection.setup.isInternetAvailable
import music.list.view.activity.baseActivity.BaseActivity
import music.list.view.activity.characterDetails.CharacterDetailsActivity

class MainActivity : BaseActivity(
    R.string.app_name, true, false, true,
    false, false, false, false, false,
), MainViewModel.Observer {

    lateinit var binding: ActivityMainBinding
    override fun doOnCreate(arg0: Bundle?) {
        binding = putContentView(R.layout.activity_main) as ActivityMainBinding
        binding.viewModel =
            ViewModelProvider(
                this,
                MainViewModelFactory(application)
            )
                .get(MainViewModel::class.java)
        binding.viewModel!!.baseViewModelObserver = baseViewModelObserver
        binding.viewModel!!.observer = this
        binding.lifecycleOwner = this
        initializeViews()
        setListener()
    }


    override fun initializeViews() {
    }


    override fun setListener() {
        binding.viewModel!!.isShowError.removeObservers(this)
        binding.viewModel!!.isShowError.observe(this, Observer {
            if (it && lifecycle.currentState == Lifecycle.State.RESUMED) {
                binding.layoutError.ivError.setImageResource(
                    if (isInternetAvailable(this))
                        R.drawable.error_ice_creame_icon else R.drawable.error_router_connection_icon
                )
                binding.layoutError.tvErrorTitleConnection.text =
                    if (isInternetAvailable(this))
                        getString(R.string.oh_no) else getString(R.string.you_are_offline)
                binding.layoutError.tvErrorBodyConnection.text = if (isInternetAvailable(this))
                    binding.viewModel!!.connectionErrorMessage else getString(R.string.no_internet_connection)
            }
        })

        binding.layoutError.tvRetry.setOnClickListener {
            binding.viewModel!!.getSearchResultApi()
        }

        binding.edttxtSearchPopupdialogSearch.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                && binding.edttxtSearchPopupdialogSearch.text.toString().length > 1
            ) {
                binding.viewModel?.getSearchResultApi()
            }
            false
        })
    }

    private var doubleBackToExitPressedOnce: Boolean = false

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) {
            if (doubleBackToExitPressedOnce) {
                finish_activity()
                return
            }
            this.doubleBackToExitPressedOnce = true
            Toast.makeText(
                this, getString(R.string.press_again_to_exit),
                Toast.LENGTH_SHORT
            ).show()

            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
        } else {
            super.onBackPressed()
        }
    }

    override fun openMusicDetails(musicModel: MusicModel) {
        Intent(this@MainActivity, CharacterDetailsActivity::class.java).also {
            it.putExtra(
                "CharacterModel",
                musicModel
            )
            startActivity(it)
            overridePendingTransition(R.anim.slide_from_right_to_left, R.anim.slide_in_left)
        }
    }

    override fun hideKeyPad() {
        hideKeyPad(binding.edttxtSearchPopupdialogSearch)
    }

}