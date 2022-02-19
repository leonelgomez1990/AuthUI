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
    }

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SignUpViewModel  by activityViewModels()


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

    private fun setObservers() {
        viewModel.navigation.observe(viewLifecycleOwner, Observer { handleNavigation(it) })
        viewModel.viewState.observe(viewLifecycleOwner, Observer { handleViewStates(it)})

    }


    private fun setListeners() {
        binding.btnCreateNewUser.setOnClickListener { checkFieldsToCreateNewUser() }
        binding.btnReturn.setOnClickListener { viewModel.goBack() }
    }

    private fun handleNavigation(navigation: SignUpNavigatorStates) {
        when(navigation) {
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
        when(state) {
            is BaseViewState.Failure -> { handleExceptions(state.exception)
                enableUI(true)}
            is BaseViewState.Loading -> { enableUI(false) }
            is BaseViewState.Ready -> { enableUI(true) }
        }.exhaustive
    }

    private fun showMessage(msg: String) {
        binding.root.snack(msg, Snackbar.LENGTH_SHORT)
    }

    private fun enableUI(enable: Boolean) {
        if(enable) {
            binding.progressBarSignUp.visibility = View.GONE
        } else {
            binding.progressBarSignUp.visibility = View.VISIBLE
        }
    }

    private fun handleExceptions(e: Exception){
        Log.w("LoginFragment", "Exception thrown: ${e.message}")
        when(e) {
            is FirebaseAuthInvalidCredentialsException -> {
                showMessage(getString(R.string.msg_error_credentials))
            }
            is FirebaseNetworkException ->{
                showMessage(getString(R.string.msg_error_network))
            }
            is FirebaseAuthInvalidUserException ->{
                showMessage(getString(R.string.msg_error_InvalidUser))
            }
            else -> { showMessage(e.message!!) }
        }
    }

    private fun checkFieldsToCreateNewUser() {
        val username = binding.editNewUserName.editText?.text.toString()
        val password = binding.editNewPassword.editText?.text.toString()
        val passwordRepeat = binding.editNewRepeatPassword.editText?.text.toString()
        binding.root.hideKeyboard()

        binding.editNewUserName.error = when(username.isBlank()) {
            true -> "Ingrese un email"
            false -> null
        }
        binding.editNewUserName.error = when(!username.contains('@')) {
            true -> "Ingrese un email válido"
            false -> null
        }
        binding.editNewPassword.error = when(password.isBlank()) {
            true -> "Ingrese una contraseña"
            false -> null
        }
        binding.editNewRepeatPassword.error = when(password != passwordRepeat) {
            true -> "Las contraseñas deben coincidir"
            false -> null
        }
        if((username.isNotBlank() and password.isNotBlank()) and (password == passwordRepeat)) {
            Log.d(ContentValues.TAG, "SignUpFragment: User: $username, Pass: $password")
            viewModel.doCreateNewUser(username, password)
        }

    }

}