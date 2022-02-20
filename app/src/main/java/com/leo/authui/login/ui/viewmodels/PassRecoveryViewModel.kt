package com.leo.authui.login.ui.viewmodels

import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leo.authui.core.ui.views.BaseViewState
import com.leo.authui.core.utils.MyResult
import com.leo.authui.core.utils.SingleLiveEvent
import com.leo.authui.R
import com.leo.authui.login.ui.navigatorstates.PassRecoveryNavigatorStates
import com.leo.authui.login.usecases.SendPasswordResetEmailUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PassRecoveryViewModel @Inject constructor(
    val sendPasswordResetEmailUseCase: SendPasswordResetEmailUseCase
) : ViewModel() {

    private val _navigation = SingleLiveEvent<PassRecoveryNavigatorStates>()
    val navigation : LiveData<PassRecoveryNavigatorStates> get() = _navigation

    private val _viewState : MutableLiveData<BaseViewState> = MutableLiveData()
    val viewState : LiveData<BaseViewState> get() = _viewState

    init {
        _viewState.value = BaseViewState.Ready
    }

    fun resetPassword(email: String) {
        viewModelScope.launch {
            _viewState.value = BaseViewState.Loading
            when(val result = sendPasswordResetEmailUseCase(email)) {
                is MyResult.Failure -> { _viewState.value = BaseViewState.Failure(result.exception) }
                is MyResult.Success -> {
                    goToSignIn()
                }
            }
        }
    }
    fun goToSignIn(){
        _navigation.value = PassRecoveryNavigatorStates.ToSignIn
    }
    fun goBack(){
        _navigation.value = PassRecoveryNavigatorStates.GoBack
    }
}

