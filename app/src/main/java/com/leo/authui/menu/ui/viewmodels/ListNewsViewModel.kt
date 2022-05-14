package com.leo.authui.menu.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leo.authui.core.ui.views.BaseViewState
import com.leo.authui.core.utils.MyResult
import com.leo.authui.core.utils.SingleLiveEvent
import com.leo.authui.menu.domain.News
import com.leo.authui.menu.ui.models.toNewUI
import com.leo.authui.menu.ui.navigatorstates.ListNewsNavigatorStates
import com.leo.authui.menu.usecases.GetNewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListNewsViewModel @Inject constructor(
    val getNewsUseCase: GetNewsUseCase
) : ViewModel() {

    private val _navigation = SingleLiveEvent<ListNewsNavigatorStates>()
    val navigation : LiveData<ListNewsNavigatorStates> get() = _navigation

    private val _viewState : MutableLiveData<BaseViewState> = MutableLiveData()
    val viewState : LiveData<BaseViewState> get() = _viewState

    private val _news : MutableLiveData<List<News>> = MutableLiveData()
    val news : LiveData<List<News>> get() = _news


    fun refreshNews() {
        viewModelScope.launch(Dispatchers.Main) {
            _viewState.value = BaseViewState.Loading
            when(val result = getNewsUseCase("US")) {
                is MyResult.Success -> {
                    _news.postValue(result.data!!)
                    _viewState.value = BaseViewState.Ready
                }
                is MyResult.Failure -> {
                    _viewState.value = BaseViewState.Failure(result.exception)
                }
            }
        }
    }

    fun goToDetailNews(new: News?){
        _navigation.value = ListNewsNavigatorStates.ToDetailNews(new!!.toNewUI())
    }

    fun goToAddNew(){
        _navigation.value = ListNewsNavigatorStates.ToAddNew
    }
}