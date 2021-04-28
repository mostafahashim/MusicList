package music.list.view.activity.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import music.list.MyApplication
import music.list.R
import music.list.observer.OnRecyclerItemClickListener
import music.list.remoteConnection.JsonParser
import music.list.remoteConnection.URL
import music.list.remoteConnection.remoteService.RemoteCallback
import music.list.remoteConnection.remoteService.startGetMethodUsingCoroutines
import music.list.remoteConnection.setup.getDefaultParams
import music.list.view.activity.baseActivity.BaseActivityViewModel
import kotlinx.coroutines.launch
import music.list.adapter.RecyclerMusicSearchAdapter
import music.list.model.MusicModel

class MainViewModel(
    application: MyApplication
) : BaseActivityViewModel(application) {
    lateinit var observer: Observer
    var isShowLoader = MutableLiveData<Boolean>()
    var isShowError = MutableLiveData<Boolean>()
    var connectionErrorMessage = ""
    var isShowNoData = MutableLiveData<Boolean>()

    var query = MutableLiveData<String>()
    var musicModels: ArrayList<MusicModel>? = ArrayList()
    var recyclerMusicSearchAdapter: RecyclerMusicSearchAdapter

    var limit = 40

    init {
        isShowLoader.value = false
        isShowError.value = false
        isShowNoData.value = true
        query.value = ""

        recyclerMusicSearchAdapter = RecyclerMusicSearchAdapter(
            musicModels!!, object : OnRecyclerItemClickListener {
                override fun onRecyclerItemClickListener(position: Int) {
                    observer.openMusicDetails(musicModels!![position])
                }

            })

    }

    override fun onCleared() {
        super.onCleared()
    }

    fun onButtonSearchClicked() {
        if (query.value!!.length > 1) {
            observer.hideKeyPad()
            getSearchResultApi()
        }
    }

    fun getSearchResultApi() {
        limit = 40
        var params = getDefaultParams(application, HashMap())
        params["limit"] = limit
        params["query"] = query.value!!

        viewModelScope.launch {
            startGetMethodUsingCoroutines(URL.getMusicListUrl(),
                params,
                object : RemoteCallback {
                    override fun onStartConnection() {
                        isShowLoader.value = true
                        isShowError.value = false
                        isShowNoData.value = false
                    }

                    override fun onFailureConnection(errorMessage: Any?) {
                        isShowLoader.value = false
                        isShowNoData.value = false
                        connectionErrorMessage = try {
                            Log.i("ApiError", errorMessage.toString())
                            var responseModel =
                                JsonParser().getParentResponseModel(errorMessage.toString())
                            responseModel?.description
                                ?: application.context.getString(R.string.something_went_wrong_please_try_again_)
                        } catch (e: Exception) {
                            application.context.getString(R.string.something_went_wrong_please_try_again_)
                        }
                        isShowError.value = true
                    }

                    override fun onSuccessConnection(response: Any?) {
                        isShowLoader.value = false
                        try {
                            var responseModel =
                                JsonParser().getMusicModels(response.toString())
                            if (responseModel != null) {
                                musicModels = responseModel
                                if (musicModels.isNullOrEmpty()) {
                                    isShowNoData.value = true
                                } else {
                                    recyclerMusicSearchAdapter.setList(musicModels!!)
                                }
                            } else {
                                connectionErrorMessage =
                                    application.context.getString(R.string.something_went_wrong_please_try_again_)
                                isShowError.value = true
                            }
                        } catch (e: Exception) {
                            connectionErrorMessage =
                                application.context.getString(R.string.something_went_wrong_please_try_again_)
                            isShowError.value = true
                        }
                    }

                    override fun onLoginAgain(errorMessage: Any?) {
                    }
                })
        }
    }

    interface Observer {
        fun openMusicDetails(musicModel: MusicModel)
        fun hideKeyPad()
    }

}