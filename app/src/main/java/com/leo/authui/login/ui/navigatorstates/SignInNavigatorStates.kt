package com.leo.authui.login.ui.navigatorstates

sealed class SignInNavigatorStates {
    object ToMenuFeature: SignInNavigatorStates()
    object ToSignUp: SignInNavigatorStates()
    object ToPassRecovery: SignInNavigatorStates()
}
