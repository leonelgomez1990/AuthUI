package com.leo.authui.login.ui.navigatorstates

sealed class PassRecoveryNavigatorStates {
    object ToSignIn: PassRecoveryNavigatorStates()
    object GoBack: PassRecoveryNavigatorStates()
}
