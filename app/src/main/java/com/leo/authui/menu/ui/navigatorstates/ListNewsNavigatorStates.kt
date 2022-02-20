package com.leo.authui.menu.ui.navigatorstates

import com.leo.authui.menu.ui.models.NewUI

sealed class ListNewsNavigatorStates {
    data class ToDetailNews(val new: NewUI): ListNewsNavigatorStates()
}


