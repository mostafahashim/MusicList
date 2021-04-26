package music.list.view.activity.splash

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import music.list.MyApplication
import music.list.R
import music.list.view.activity.baseActivity.BaseActivityViewModel
import music.list.util.Preferences
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch
import music.list.remoteConnection.JsonParser
import music.list.remoteConnection.URL
import music.list.remoteConnection.remoteService.RemoteCallback
import music.list.remoteConnection.remoteService.startPostMethodUsingCoroutines
import music.list.remoteConnection.remoteService.startPostMethodUsingRX
import music.list.remoteConnection.setup.getDefaultParams
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
        isShowLoader.value = true
        isShowError.value = false

        timerFinished.value = false
        connectionFinished.value = false
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
        viewModelScope.launch {
            startPostMethodUsingCoroutines(URL.getAccessTokenUrl(),
                null,
                object : RemoteCallback {
                    override fun onStartConnection() {
                        isShowLoader.value = true
                        isShowError.value = false
                    }

                    override fun onFailureConnection(errorMessage: Any?) {
                        isShowLoader.value = false
                        connectionErrorMessage = try {
                            var responseModel =
                                JsonParser().getParentResponseModel(errorMessage.toString())
                            responseModel?.description
                                ?: application.context.getString(R.string.something_went_wrong_please_try_again_)
                        } catch (e: Exception) {
                            application.context.getString(R.string.something_went_wrong_please_try_again_)
                        }
                        isShowError.value = true
                    }

                    override fun onSuccessConnection(response: Any?) {
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
                        connectionErrorMessage = try {
                            var responseModel =
                                JsonParser().getParentResponseModel(errorMessage.toString())
                            responseModel?.description
                                ?: application.context.getString(R.string.something_went_wrong_please_try_again_)
                        } catch (e: Exception) {
                            application.context.getString(R.string.something_went_wrong_please_try_again_)
                        }
                        isShowError.value = true
                    }
                })
        }
    }

}