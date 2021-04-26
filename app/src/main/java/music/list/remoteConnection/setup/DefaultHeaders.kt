package music.list.remoteConnection.setup

import music.list.BuildConfig
import music.list.util.GatewayKey
import music.list.util.Preferences
import kotlin.collections.HashMap

inline fun getDefaultHeaders(
    isFormData: Boolean,
    isUseAuthorization: Boolean
): MutableMap<String, String> {
    var params = HashMap<String, String>()
    if (isFormData) {
        params["Content-Type"] = "application/x-www-form-urlencoded"
    } else {
        params["Content-Type"] = "application/json"
    }

    params["Accept"] = "application/json"
    params["Accept-Language"] = Preferences.getApplicationLocale()
    if (isUseAuthorization)
        params["Authorization"] = if (!Preferences.getAPIToken()
                .isNullOrEmpty()
        ) "Bearer ${Preferences.getAPIToken()}" else ""
    else
        params["X-MM-GATEWAY-KEY"] = GatewayKey


    return params
}
