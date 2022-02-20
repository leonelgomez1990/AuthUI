package com.leo.authui.login.ui.fragments

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
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
import com.leo.authui.databinding.FragmentSignInBinding
import com.leo.authui.login.ui.navigatorstates.SignInNavigatorStates
import com.leo.authui.login.ui.viewmodels.SignInViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment() {

    companion object {
        fun newInstance() = SignInFragment()
        const val TAG = "SignInFragment"
    }

    private val viewModel: SignInViewModel by viewModels()
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignInBinding.inflate(layoutInflater)
        //No implementado por ahora
        binding.checkRemember.visibility = View.GONE
        setListeners()

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setObservers()
    }

    private fun setListeners() {
        binding.btnSignIn.setOnClickListener {
            signInCallBack()
        }
        binding.txtSignUp.setOnClickListener { viewModel.goToSignUp() }
        binding.txtPswdForgotten.setOnClickListener { viewModel.goToPassRecovery() }
    }

    private fun setObservers() {
        viewModel.navigation.observe(viewLifecycleOwner, Observer { handleNavigation(it) })
        viewModel.viewState.observe(viewLifecycleOwner, Observer { handleViewStates(it) })
    }

    private fun handleNavigation(navigation: SignInNavigatorStates) {
        when (navigation) {
            is SignInNavigatorStates.ToMenuFeature -> {
                val action = SignInFragmentDirections.actionSignInFragmentToMenuActivity()
                findNavController().navigate(action)
                activity?.finish()
            }
            SignInNavigatorStates.ToSignUp -> {
                val action = SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
                findNavController().navigate(action)
            }
            SignInNavigatorStates.ToPassRecovery -> {
                val action = SignInFragmentDirections.actionSignInFragmentToPassRecoveryFragment()
                findNavController().navigate(action)
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
            binding.progressBarSignIn.visibility = View.GONE
        } else {
            binding.progressBarSignIn.visibility = View.VISIBLE
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

    private fun signInCallBack() {
        with(binding) {
            val username = editUserName.editText?.text.toString()
            val password = editPassword.editText?.text.toString()
            root.hideKeyboard()

            editUserName.error = when (!username.contains('@')) {
                true -> "Ingrese un email válido"
                false -> null
            }
            editPassword.error = when (password.isBlank()) {
                true -> "Ingrese una contraseña"
                false -> null
            }
            if ((editUserName.error == null && editPassword.error == null)) {
                Log.d(TAG, "$TAG: User: $username, Pass: $password")
                viewModel.doUserLogin(username, password)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}