package com.leo.authui.menu.ui.viewmodels

import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leo.authui.core.ui.views.BaseViewState
import com.leo.authui.core.utils.MyResult
import com.leo.authui.core.utils.SingleLiveEvent
import com.leo.authui.menu.domain.News
import com.leo.authui.menu.ui.models.NewUI
import com.leo.authui.menu.ui.navigatorstates.EditNewNavigatorStates
import com.leo.authui.menu.usecases.GetNewsUseCase
import com.leo.authui.menu.usecases.UpdateNewUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditNewViewModel @Inject constructor(
    val updateNewUseCase: UpdateNewUseCase,
    val getNewUseCase: GetNewsUseCase
) : ViewModel() {

    private val _navigation = SingleLiveEvent<EditNewNavigatorStates>()
    val navigation : LiveData<EditNewNavigatorStates> get() = _navigation

    private val _viewState : MutableLiveData<BaseViewState> = MutableLiveData()
    val viewState : LiveData<BaseViewState> get() = _viewState

    val urlImage: MutableLiveData<String> = MutableLiveData()
    lateinit var new : News

    init {
        _viewState.value = BaseViewState.Ready
    }

    fun updateNew(data: News) {
        viewModelScope.launch {
            _viewState.value = BaseViewState.Loading
            when(val result = updateNewUseCase(data)) {
                is MyResult.Failure -> { _viewState.value = BaseViewState.Failure(result.exception) }
                is MyResult.Success -> {
                    _viewState.value = BaseViewState.Ready
                    goBack()
                }
            }
        }
    }
    fun goBack(){
        _navigation.value = EditNewNavigatorStates.GoBack
    }

    fun refreshNewData(uid: String) {
        viewModelScope.launch {
            _viewState.value = BaseViewState.Loading
            when(val result = (getNewUseCase.getNew(uid))) {
                is MyResult.Failure -> { _viewState.value = BaseViewState.Failure(result.exception) }
                is MyResult.Success -> {
                    new = result.data
                    _viewState.value = BaseViewState.Ready
                }
            }
        }
    }
}
