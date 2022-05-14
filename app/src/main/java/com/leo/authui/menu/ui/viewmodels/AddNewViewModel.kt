package com.leo.authui.menu.ui.viewmodels

import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leo.authui.core.ui.views.BaseViewState
import com.leo.authui.core.utils.MyResult
import com.leo.authui.core.utils.SingleLiveEvent
import com.leo.authui.R
import com.leo.authui.menu.domain.News
import com.leo.authui.menu.ui.navigatorstates.AddNewNavigatorStates
import com.leo.authui.menu.usecases.CreateNewUseCase
import com.leo.authui.menu.usecases.UploadImageUseCase
import com.leo.authui.menu.usecases.DeleteImageUseCase
import com.leo.authui.menu.usecases.UpdateNewUseCase
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddNewViewModel @Inject constructor(
    val uploadImageUseCase: UploadImageUseCase,
    val deleteImageUseCase: DeleteImageUseCase,
    val createNewUseCase: CreateNewUseCase
) : ViewModel() {

    private val _navigation = SingleLiveEvent<AddNewNavigatorStates>()
    val navigation: LiveData<AddNewNavigatorStates> get() = _navigation

    private val _viewState: MutableLiveData<BaseViewState> = MutableLiveData()
    val viewState: LiveData<BaseViewState> get() = _viewState

    val urlImage: MutableLiveData<String> = MutableLiveData()
    var new: News
    private var uid: String

    init {
        _viewState.value = BaseViewState.Ready
        new = News("", true, "", "", "", "", "")
        uid = UUID.randomUUID().toString()
    }

    fun doUploadImageToDatabase(localPath: String) {
        viewModelScope.launch {
            _viewState.value = BaseViewState.Loading
            deleteImageUseCase("News/${uid}.bmp")
            when (val result = uploadImageUseCase(localPath, "News", uid)) {
                is MyResult.Failure -> {
                    _viewState.value = BaseViewState.Failure(result.exception)
                }
                is MyResult.Success -> {
                    urlImage.value = result.data ?: ""
                    _viewState.value = BaseViewState.Ready
                }
            }
        }
    }

    fun goBack() {
        _navigation.value = AddNewNavigatorStates.GoBack
    }

    fun createNew(data: News) {
        viewModelScope.launch {
            _viewState.value = BaseViewState.Loading
            when (val result = createNewUseCase(data)) {
                is MyResult.Failure -> {
                    _viewState.value = BaseViewState.Failure(result.exception)
                }
                is MyResult.Success -> {
                    _viewState.value = BaseViewState.Ready
                    goBack()
                }
            }
        }
    }
}
