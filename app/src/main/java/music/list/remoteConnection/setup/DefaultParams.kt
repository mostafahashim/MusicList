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
    params["apikey"] = "Cd3a532b0-97be-49d5-b20d-5572ef8e265d"
    return params
}

fun getDefaultParams(
    application: MyApplication,
    builder: MultipartBody.Builder
): MultipartBody.Builder {
    builder.setType(MultipartBody.FORM)
    var token = Preferences.getUserToken()
    builder.addFormDataPart("notification_Token", token)
    builder.addFormDataPart("lang", Preferences.getApplicationLocale())
    builder.addFormDataPart("userId", Preferences.getUserID())
    builder.addFormDataPart("user_id", Preferences.getUserID())
    builder.addFormDataPart("user_type", Preferences.getUserType())
    builder.addFormDataPart("userType", Preferences.getUserType())
    builder.addFormDataPart("version_code", BuildConfig.VERSION_CODE.toString())
    builder.addFormDataPart("os_version", application.getOSVersion())
    builder.addFormDataPart("mobile_model", application.getDeviceModel())
    builder.addFormDataPart("applicationId", "0")
    builder.addFormDataPart("android", "true")
    return builder
}