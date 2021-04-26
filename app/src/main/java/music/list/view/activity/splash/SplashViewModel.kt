package music.list.view.activity.splash

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import music.list.MyApplication
import music.list.R
import music.list.util.DataProvider
import music.list.view.activity.baseActivity.BaseActivityViewModel
import music.list.util.Preferences
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch
import music.list.remoteConnection.JsonParser
import music.list.remoteConnection.URL
import music.list.remoteConnection.remoteService.RemoteCallback
import music.list.remoteConnection.remoteService.startGetMethodUsingCoroutines
import music.list.remoteConnection.remoteService.startPostMethodUsingCoroutines
import music.list.remoteConnection.setup.getDefaultParams
import music.list.util.GatewayKey
import okhttp3.MultipartBody
import java.util.concurrent.TimeUnit

class SplashViewModel(
    application: MyApplication
) : BaseActivityViewModel(application) {

    var compositeDisposable = CompositeDisposable()
    private var delay = 2000L
    var timerFinished = MutableLiveData<Boolean>()
    var connectionFinished = MutableLiveData<Boolean>()
    var isShowLoader = MutableLiveData<Boolean>()
    var isShowError = MutableLiveData<Boolean>()
    var connectionErrorMessage = ""

    init {
        isShowLoader.value = false
        isShowError.value = false

        timerFinished.value = false
        connectionFinished.value = true
        startTimer()

        getAccessDataApi()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    private fun startTimer() {
        compositeDisposable.add(
            Observable.intervalRange(1, delay, 0, 1, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (it == delay) {
                        timerFinished.value = true
                    } else {
                    }
                }
        )
    }

    fun getAccessDataApi() {
        var params = getDefaultParams(application, MultipartBody.Builder())

        viewModelScope.launch {
            startPostMethodUsingCoroutines(URL.getAccessTokenUrl(),
                params.build(),
                object : RemoteCallback {
                    override fun onStartConnection() {
                        isShowLoader.value = true
                        isShowError.value = false
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
                                JsonParser().getParentResponseModel(response.toString())
                            if (responseModel != null) {
                                Preferences.saveAPIToken(responseModel.accessToken)
                                connectionFinished.value = true
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
                        isShowLoader.value = false
                    }
                })
        }
    }

}