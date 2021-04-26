package music.list.view.activity.characterDetails

import android.os.SystemClock
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import music.list.MyApplication
import music.list.R
import music.list.adapter.RecyclerCharacterHomeAdapter
import music.list.adapter.RecyclerItemEventAdapter
import music.list.model.CharacterModel
import music.list.model.ItemModel
import music.list.observer.OnRecyclerItemClickListener
import music.list.remoteConnection.JsonParser
import music.list.remoteConnection.URL
import music.list.remoteConnection.remoteService.RemoteCallback
import music.list.remoteConnection.remoteService.startGetMethodUsingCoroutines
import music.list.remoteConnection.setup.getDefaultParams
import music.list.view.activity.baseActivity.BaseActivityViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch

class CharacterDetailsViewModel(
    application: MyApplication
) : BaseActivityViewModel(application) {
    lateinit var observer: Observer
    var isShowLoader = MutableLiveData<Boolean>()
    var isShowError = MutableLiveData<Boolean>()
    var isShowRefresh = MutableLiveData<Boolean>()
    var connectionErrorMessage = ""

    var characterModel: CharacterModel? = CharacterModel()
    var image = MutableLiveData<String>()
    var name = MutableLiveData<String>()
    var description = MutableLiveData<String>()

    var isShowComics = MutableLiveData<Boolean>()
    var comicsItems: ArrayList<ItemModel>? = ArrayList()
    var recyclerComicsItemEventAdapter: RecyclerItemEventAdapter

    var isShowEvents = MutableLiveData<Boolean>()
    var eventsItems: ArrayList<ItemModel>? = ArrayList()
    var recyclerEventsItemEventAdapter: RecyclerItemEventAdapter

    var isShowStories = MutableLiveData<Boolean>()
    var storiesItems: ArrayList<ItemModel>? = ArrayList()
    var recyclerStoriesItemEventAdapter: RecyclerItemEventAdapter

    var isShowSeries = MutableLiveData<Boolean>()
    var seriesItems: ArrayList<ItemModel>? = ArrayList()
    var recyclerSeriesItemEventAdapter: RecyclerItemEventAdapter

    init {
        isShowLoader.value = true
        isShowError.value = false
        isShowRefresh.value = false
        isShowComics.value = false
        isShowEvents.value = false
        isShowStories.value = false
        isShowSeries.value = false
        image.value = ""
        name.value = ""
        description.value = ""

        recyclerComicsItemEventAdapter = RecyclerItemEventAdapter(0.0,
            comicsItems!!, object : OnRecyclerItemClickListener {
                override fun onRecyclerItemClickListener(position: Int) {
                }
            })

        recyclerEventsItemEventAdapter = RecyclerItemEventAdapter(0.0,
            eventsItems!!, object : OnRecyclerItemClickListener {
                override fun onRecyclerItemClickListener(position: Int) {
                }
            })

        recyclerSeriesItemEventAdapter = RecyclerItemEventAdapter(0.0,
            seriesItems!!, object : OnRecyclerItemClickListener {
                override fun onRecyclerItemClickListener(position: Int) {
                }
            })

        recyclerStoriesItemEventAdapter = RecyclerItemEventAdapter(0.0,
            storiesItems!!, object : OnRecyclerItemClickListener {
                override fun onRecyclerItemClickListener(position: Int) {
                }
            })
    }

    fun updateBooksAdapterColumnWidth(screenWidth: Int) {
        var columnWidth = (100.00 * screenWidth) / 360.00
        recyclerComicsItemEventAdapter.setColumnWidthAndRatio(columnWidth)
        recyclerComicsItemEventAdapter.notifyDataSetChanged()

        recyclerEventsItemEventAdapter.setColumnWidthAndRatio(columnWidth)
        recyclerEventsItemEventAdapter.notifyDataSetChanged()

        recyclerStoriesItemEventAdapter.setColumnWidthAndRatio(columnWidth)
        recyclerStoriesItemEventAdapter.notifyDataSetChanged()

        recyclerSeriesItemEventAdapter.setColumnWidthAndRatio(columnWidth)
        recyclerSeriesItemEventAdapter.notifyDataSetChanged()

    }

    override fun onCleared() {
        super.onCleared()
    }

    fun setData() {
        image.value =
            "${characterModel?.thumbnail?.path}.${characterModel?.thumbnail?.extension}"
        name.value = characterModel?.name!!
        description.value = characterModel?.description!!
    }

    fun getHomeDataApi() {
        var params = getDefaultParams(application, HashMap())

        viewModelScope.launch {
            startGetMethodUsingCoroutines(URL.getCharacterDetailsUrl(characterModel?.id!!),
                params,
                object : RemoteCallback {
                    override fun onStartConnection() {
                        isShowLoader.value = true
                        isShowError.value = false
                        isShowRefresh.value = false
                    }

                    override fun onFailureConnection(errorMessage: Any?) {
                        try {
                            Log.i("ApiError", errorMessage.toString())
                            isShowLoader.value = false
                            var responseModel =
                                JsonParser().getParentResponseModel(errorMessage.toString())
                            connectionErrorMessage = responseModel?.message
                                ?: application.context.getString(R.string.something_went_wrong_please_try_again_)
                        } catch (e: Exception) {
                            connectionErrorMessage =
                                application.context.getString(R.string.something_went_wrong_please_try_again_)
                        }
                        isShowError.value = true
                    }

                    override fun onSuccessConnection(response: Any?) {
                        isShowLoader.value = false
                        try {
                            var responseModel =
                                JsonParser().getCharactersListResponseModel(response.toString())
                            if (responseModel != null) {
                                if (!responseModel.data.results.isNullOrEmpty()) {
                                    characterModel = responseModel.data.results[0]
                                    setData()
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
                        onLoginAgain(errorMessage)
                    }
                })
        }
    }

    interface Observer {
    }

}