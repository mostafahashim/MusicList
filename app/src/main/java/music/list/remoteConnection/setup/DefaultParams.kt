package music.list.remoteConnection.setup

import android.os.SystemClock
import music.list.BuildConfig
import music.list.MyApplication
import music.list.util.HashUtil
import music.list.util.Preferences
import okhttp3.MultipartBody
import kotlin.collections.HashMap

fun getDefaultParams(
    application: MyApplication,
    params: HashMap<String, Any>
): MutableMap<String, Any> {
    params["lang"] = Preferences.getApplicationLocale()
    return params
}

fun getDefaultParams(
    application: MyApplication,
    builder: MultipartBody.Builder, isPutDefault: Boolean
): MultipartBody.Builder {
    if (isPutDefault) {
        builder.setType(MultipartBody.FORM)
        builder.addFormDataPart("lang", Preferences.getApplicationLocale())
    }
    return builder
}