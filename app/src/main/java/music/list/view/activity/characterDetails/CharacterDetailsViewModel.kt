package music.list.view.activity.characterDetails

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import music.list.MyApplication
import music.list.R
import music.list.observer.OnRecyclerItemClickListener
import music.list.remoteConnection.JsonParser
import music.list.remoteConnection.remoteService.RemoteCallback
import music.list.remoteConnection.remoteService.startGetMethodUsingCoroutines
import music.list.remoteConnection.setup.getDefaultParams
import music.list.view.activity.baseActivity.BaseActivityViewModel
import kotlinx.coroutines.launch
import music.list.model.MusicModel

class CharacterDetailsViewModel(
    application: MyApplication
) : BaseActivityViewModel(application) {
    lateinit var observer: Observer
    var musicModel: MusicModel? = MusicModel()
    var image = MutableLiveData<String>()
    var name = MutableLiveData<String>()
    var description = MutableLiveData<String>()

    var type = MutableLiveData<String>()
    var duration = MutableLiveData<String>()
    var artist = MutableLiveData<String>()
    var genres = MutableLiveData<String>()
    var numnerOfTracks = MutableLiveData<String>()
    var publishingDate = MutableLiveData<String>()

    init {
        image.value = ""
        name.value = ""
        description.value = ""
        type.value = ""
        genres.value = ""
        numnerOfTracks.value = ""
        publishingDate.value = ""

    }

    fun setData() {
        image.value = musicModel?.cover?.large ?: ""
        name.value = musicModel?.title ?: ""
        description.value = musicModel?.label ?: ""
        type.value = musicModel?.type ?: ""
        artist.value = musicModel?.mainArtist?.name ?: ""
        duration.value = musicModel?.duration ?: "0"
        numnerOfTracks.value = musicModel?.numberOfTracks ?: "0"
        publishingDate.value = musicModel?.publishingDate ?: ""
        for (genre in musicModel?.genres ?: ArrayList()) {
            if (genres.value.isNullOrEmpty())
                genres.value = genre
            else
                genres.value = "${genres.value ?: ""}, $genre"
        }
    }

    interface Observer {
    }

}