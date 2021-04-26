package music.list.model.responseAPIModel

import androidx.databinding.BaseObservable

open class ParentResponseModel : BaseObservable() {
    lateinit var accessToken: String
    lateinit var status: String
    lateinit var copyright: String
    lateinit var attributionText: String
    lateinit var attributionHTML: String
    lateinit var etag: String
    var message = ""
    var code = ""
}