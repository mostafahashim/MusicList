package music.list.view.activity.main

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import music.list.R
import music.list.databinding.ActivityMainBinding
import music.list.model.CharacterModel
import music.list.remoteConnection.setup.isInternetAvailable
import music.list.util.ScreenSizeUtils
import music.list.view.activity.baseActivity.BaseActivity
import music.list.view.activity.characterDetails.CharacterDetailsActivity

class MainActivity : BaseActivity(
    R.string.app_name, true, false, true,
    false, false, false, true, false,
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
        var screenWidth: Int = ScreenSizeUtils().getScreenWidth(this)
        binding.viewModel!!.updateBooksAdapterColumnWidth(screenWidth)
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
            binding.viewModel!!.getHomeDataApi()
        }

        binding.recyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val pastVisibleItems =
                    (recyclerView.layoutManager as GridLayoutManager).findFirstCompletelyVisibleItemPosition()
                //first item
                binding.swipeRefreshHomeFragment.isEnabled = pastVisibleItems == 0

                val visibleItemCount = recyclerView.layoutManager!!.childCount
                val totalItemCount = recyclerView.layoutManager!!.itemCount

                if (pastVisibleItems + visibleItemCount >= totalItemCount &&
                    !binding.viewModel?.isShowRefresh?.value!! &&
                    !binding.viewModel?.isShowLoader?.value!!
                    && binding.viewModel?.characterModels?.size!! < binding.viewModel?.total!!
                ) {
                    //End of list
                    binding.viewModel!!.getNextItemsDataApi()
                }
            }
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

    override fun openCharacterDetails(characterModel: CharacterModel) {
        Intent(this@MainActivity, CharacterDetailsActivity::class.java).also {
            it.putExtra(
                "CharacterModel",
                characterModel
            )
            startActivity(it)
            overridePendingTransition(R.anim.slide_from_right_to_left, R.anim.slide_in_left)
        }
    }

}