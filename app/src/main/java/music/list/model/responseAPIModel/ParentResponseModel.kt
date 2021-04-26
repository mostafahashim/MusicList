package music.list.model.responseAPIModel

import androidx.databinding.BaseObservable

open class ParentResponseModel : BaseObservable() {
    lateinit var accessToken: String
    var description = ""
}