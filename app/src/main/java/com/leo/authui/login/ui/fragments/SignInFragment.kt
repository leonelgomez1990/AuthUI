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

    private fun setObservers() {
        viewModel.navigation.observe(viewLifecycleOwner, Observer { handleNavigation(it) })
        viewModel.viewState.observe(viewLifecycleOwner, Observer { handleViewStates(it)})
    }

    private fun handleNavigation(navigation: SignInNavigatorStates) {
        when(navigation) {
            is SignInNavigatorStates.ToMenuFeature -> {
                val action = SignInFragmentDirections.actionSignInFragmentToMenuActivity()
                findNavController().navigate(action)
                activity?.finish()
            }
            SignInNavigatorStates.ToSignUp -> {
                val action = SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
                findNavController().navigate(action)
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

    private fun setListeners() {
        binding.btnSignIn.setOnClickListener {
            signInCallBack()
        }
        binding.txtSignUp.setOnClickListener { viewModel.goToSignUp() }
    }

    private fun enableUI(enable: Boolean) {
        if(enable) {
            binding.progressBarSignIn.visibility = View.GONE
        } else {
            binding.progressBarSignIn.visibility = View.VISIBLE
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

    private fun signInCallBack() {
        val username = binding.editUserName.editText?.text.toString()
        val password = binding.editPassword.editText?.text.toString()
        binding.root.hideKeyboard()

        if(username.isNotBlank() and password.isNotBlank()) {
            Log.d(ContentValues.TAG, "SignInFragment: User: $username, Pass: $password")
            viewModel.doUserLogin(username, password)
        } else {
            binding.editUserName.error = when(username.isBlank()) {
                true -> "Ingrese su usuario"
                false -> null
            }
            binding.editPassword.error = when(password.isBlank()) {
                true -> "Ingrese su contraseña"
                false -> null
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showMessage(msg: String) {
        binding.root.snack(msg, Snackbar.LENGTH_SHORT)
    }

}