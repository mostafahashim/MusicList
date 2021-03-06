package music.list.remoteConnection.remoteService

import music.list.remoteConnection.remoteService.APIInterface
import music.list.remoteConnection.remoteService.ConnectionHandler
import music.list.remoteConnection.setup.getDefaultHeaders
import com.google.gson.Gson
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject

suspend fun startGetMethodUsingCoroutines(
    urlFunction: String,
    params: MutableMap<String, Any>,
    remoteCallback: RemoteCallback
) {
    val apiInterface = ConnectionHandler.getInstance().getClient()?.create(APIInterface::class.java)
    remoteCallback.onStartConnection()
    try {
        val response = apiInterface!!.doGetConnectionUsingCoroutines(
            getDefaultHeaders(false,true), urlFunction, params
        )
        if ((response.code() == 200 || response.code() == 201) && response.isSuccessful) {
            remoteCallback.onSuccessConnection(response.body().toString())
        } else if (response.code() == 401 && response.errorBody() != null) {
            try {
                remoteCallback.onLoginAgain(
                    JSONObject(
                        response.errorBody()!!.string()
                    ).toString()
                )
            } catch (e: Exception) {
                remoteCallback.onLoginAgain(e.message)
            }
        } else if (response.errorBody() != null) {
            try {
                remoteCallback.onFailureConnection(
                    JSONObject(
                        response.errorBody()!!.string()
                    ).toString()
                )
            } catch (e: Exception) {
                remoteCallback.onFailureConnection(e.message)
            }
        } else {
            remoteCallback.onFailureConnection("")
        }
    } catch (e: Exception) {
        remoteCallback.onFailureConnection(e.message)
    }
}

suspend fun startPostMethodUsingCoroutines(
    urlFunction: String,
    params: MultipartBody?,
    remoteCallback: RemoteCallback
) {
    val apiInterface = ConnectionHandler.getInstance().getClient()?.create(APIInterface::class.java)
    remoteCallback.onStartConnection()
    try {
        val response =
            if (params == null) apiInterface!!.doPostConnectionWithoutParamsUsingCoroutines(
                getDefaultHeaders(true,false),
                urlFunction
            ) else apiInterface!!.doPostConnectionUsingCoroutines(
                getDefaultHeaders(true,true),
                urlFunction,
                params
            )
        if ((response.code() == 200 || response.code() == 201) && response.isSuccessful) {
            remoteCallback.onSuccessConnection(response.body()!!.toString())
        } else if (response.code() == 401 && response.errorBody() != null) {
            try {
                remoteCallback.onLoginAgain(
                    JSONObject(
                        response.errorBody()!!.string()
                    ).toString()
                )
            } catch (e: Exception) {
                remoteCallback.onLoginAgain(e.message)
            }
        } else if (response.errorBody() != null) {
            try {
                remoteCallback.onFailureConnection(
                    JSONObject(
                        response.errorBody()!!.string()
                    ).toString()
                )
            } catch (e: Exception) {
                remoteCallback.onFailureConnection(e.message)
            }
        } else {
            remoteCallback.onFailureConnection("")
        }
    } catch (e: Exception) {
        remoteCallback.onFailureConnection(e.message)
    }
}

suspend fun startPostMethodWithGSONParamsUsingCoroutines(
    urlFunction: String,
    params: MutableMap<String, Any>,
    remoteCallback: RemoteCallback
) {
    val apiInterface = ConnectionHandler.getInstance().getClient()?.create(APIInterface::class.java)
    remoteCallback.onStartConnection()
    try {
        val json = Gson().toJson(params)
        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            json
        )

        val response = apiInterface!!.doPostConnectionUsingCoroutines(
            getDefaultHeaders(false,true),
            urlFunction,
            body
        )
        if (response != null && (response.code() == 200 || response.code() == 201) && response.isSuccessful) {
            remoteCallback.onSuccessConnection(response.body()!!.toString())
        } else if (response.code() == 401 && response.errorBody() != null) {
            try {
                remoteCallback.onLoginAgain(
                    JSONObject(
                        response.errorBody()!!.string()
                    ).toString()
                )
            } catch (e: Exception) {
                remoteCallback.onLoginAgain(e.message)
            }
        } else if (response.errorBody() != null) {
            try {
                remoteCallback.onFailureConnection(
                    JSONObject(
                        response.errorBody()!!.string()
                    ).toString()
                )
            } catch (e: Exception) {
                remoteCallback.onFailureConnection(e.message)
            }
        } else {
            remoteCallback.onFailureConnection("")
        }
    } catch (e: Exception) {
        remoteCallback.onFailureConnection(e.message)
    }
}