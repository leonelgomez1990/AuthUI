package com.leo.authui.login.ui.fragments

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.leo.authui.R
import com.leo.authui.core.ui.hideKeyboard
import com.leo.authui.core.ui.views.BaseViewState
import com.leo.authui.core.utils.exhaustive
import com.leo.authui.core.utils.snack
import com.leo.authui.databinding.FragmentSignUpBinding
import com.leo.authui.login.ui.navigatorstates.SignUpNavigatorStates
import com.leo.authui.login.ui.viewmodels.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    companion object {
        fun newInstance() = SignUpFragment()
        const val TAG = "SignUpFragment"
    }

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SignUpViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(layoutInflater)
        setListeners()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setObservers()
    }

    private fun setListeners() {
        binding.btnCreateNewUser.setOnClickListener { checkFieldsToCreateNewUser() }
        binding.btnReturn.setOnClickListener { viewModel.goBack() }
    }

    private fun setObservers() {
        viewModel.navigation.observe(viewLifecycleOwner, Observer { handleNavigation(it) })
        viewModel.viewState.observe(viewLifecycleOwner, Observer { handleViewStates(it) })

    }

    private fun handleNavigation(navigation: SignUpNavigatorStates) {
        when (navigation) {
            is SignUpNavigatorStates.ToSignIn -> {
                showMessage("Usuario agregado exitosamente")
                findNavController().navigateUp()
            }
            is SignUpNavigatorStates.ToGoBack -> {
                findNavController().navigateUp()
            }
        }.exhaustive
    }

    private fun handleViewStates(state: BaseViewState) {
        when (state) {
            is BaseViewState.Failure -> {
                handleExceptions(state.exception)
                enableUI(true)
            }
            is BaseViewState.Loading -> {
                enableUI(false)
            }
            is BaseViewState.Ready -> {
                enableUI(true)
            }
        }.exhaustive
    }

    private fun enableUI(enable: Boolean) {
        if (enable) {
            binding.progressBarSignUp.visibility = View.GONE
        } else {
            binding.progressBarSignUp.visibility = View.VISIBLE
        }
    }

    private fun showMessage(msg: String) {
        binding.root.snack(msg, Snackbar.LENGTH_SHORT)
    }

    private fun handleExceptions(e: Exception) {
        Log.w(TAG, "Exception thrown: ${e.message}")
        when (e) {
            is FirebaseAuthInvalidCredentialsException -> {
                showMessage(getString(R.string.msg_error_credentials))
            }
            is FirebaseNetworkException -> {
                showMessage(getString(R.string.msg_error_network))
            }
            is FirebaseAuthInvalidUserException -> {
                showMessage(getString(R.string.msg_error_InvalidUser))
            }
            else -> {
                showMessage(e.message!!)
            }
        }
    }

    private fun checkFieldsToCreateNewUser() {
        with(binding) {
            val username = editNewUserName.editText?.text.toString()
            val password = editNewPassword.editText?.text.toString()
            val passwordRepeat = editNewRepeatPassword.editText?.text.toString()
            root.hideKeyboard()

            editNewUserName.error = when (!username.contains('@')) {
                true -> "Ingrese un email válido"
                false -> null
            }
            editNewPassword.error = when (password.isBlank()) {
                true -> "Ingrese una contraseña"
                false -> null
            }
            editNewRepeatPassword.error = when (password != passwordRepeat) {
                true -> "Las contraseñas deben coincidir"
                false -> null
            }
            if ((editNewUserName.error == null && editNewPassword.error == null && editNewRepeatPassword.error == null)) {
                Log.d(TAG, "$TAG: User: $username, Pass: $password")
                viewModel.doCreateNewUser(username, password)
            }
        }

    }

}