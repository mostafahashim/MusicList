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

    init {
        image.value = ""
        name.value = ""
        description.value = ""

    }

    override fun onCleared() {
        super.onCleared()
    }

    fun setData() {
//        image.value =
//            "${musicModel?.thumbnail?.path}.${musicModel?.thumbnail?.extension}"
//        name.value = musicModel?.name!!
//        description.value = musicModel?.description!!
    }

    interface Observer {
    }

}