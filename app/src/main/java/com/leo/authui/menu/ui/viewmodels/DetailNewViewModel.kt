package com.leo.authui.menu.ui.viewmodels

import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leo.authui.core.ui.views.BaseViewState
import com.leo.authui.core.utils.MyResult
import com.leo.authui.core.utils.SingleLiveEvent
import com.leo.authui.menu.ui.models.NewUI
import com.leo.authui.menu.ui.navigatorstates.DetailNewNavigatorStates
import com.leo.authui.menu.usecases.DeleteNewUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailNewViewModel @Inject constructor(
    val deleteNewUseCase: DeleteNewUseCase
) : ViewModel() {

    private val _navigation = SingleLiveEvent<DetailNewNavigatorStates>()
    val navigation : LiveData<DetailNewNavigatorStates> get() = _navigation

    private val _viewState : MutableLiveData<BaseViewState> = MutableLiveData()
    val viewState : LiveData<BaseViewState> get() = _viewState

    lateinit var new : NewUI

    init {
        _viewState.value = BaseViewState.Ready
    }

    fun deleteNew() {
        viewModelScope.launch {
            _viewState.value = BaseViewState.Loading
            when(val result = deleteNewUseCase(new.uid)) {
                is MyResult.Failure -> { _viewState.value = BaseViewState.Failure(result.exception) }
                is MyResult.Success -> {
                    goBack()
                }
            }
        }
    }

    fun goToEditNew(){
        _navigation.value = DetailNewNavigatorStates.ToEditNew(new.uid)
    }
    fun goBack(){
        _navigation.value = DetailNewNavigatorStates.GoBack
    }
    fun refreshNew() {
    }
}