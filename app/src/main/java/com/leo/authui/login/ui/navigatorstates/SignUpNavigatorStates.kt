package com.leo.authui.login.ui.navigatorstates

sealed class SignUpNavigatorStates {
    object ToSignIn: SignUpNavigatorStates()
    object ToGoBack: SignUpNavigatorStates()
}
