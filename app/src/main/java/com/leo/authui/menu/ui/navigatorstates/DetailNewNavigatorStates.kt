package com.leo.authui.menu.ui.navigatorstates

import com.leo.authui.menu.ui.models.NewUI

sealed class DetailNewNavigatorStates {
    data class ToEditNew(val new: NewUI): DetailNewNavigatorStates()
    object GoBack: DetailNewNavigatorStates()
}
