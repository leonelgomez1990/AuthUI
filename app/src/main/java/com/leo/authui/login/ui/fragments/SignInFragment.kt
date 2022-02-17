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
import com.leo.authui.core.utils.exhaustive
import com.leo.authui.core.utils.snack
import com.leo.authui.databinding.FragmentSignInBinding
import com.leo.authui.login.ui.navigatorstates.SignInNavigatorStates
import com.leo.authui.login.ui.viewmodels.SignInViewModel
import com.leo.authui.login.ui.viewstates.LoginViewState
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

        setListeners()

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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
        }.exhaustive
    }


    private fun handleViewStates(state: LoginViewState) {
        when(state) {
            is LoginViewState.Alert -> {
                binding.txtPswdForgotten.text = "Error"
            }
            is LoginViewState.Done -> { showMessage("Correcto") }
            is LoginViewState.Failure -> { showMessage("Error") }
            is LoginViewState.Loading -> {
                binding.txtPswdForgotten.text = "Cargando"
            }
            is LoginViewState.Ready -> {
                binding.txtPswdForgotten.text = "Entramos"
            }
        }.exhaustive
    }

    private fun setListeners() {
        binding.btnSignIn.setOnClickListener {
            signInCallBack()
        }
    }

    private fun signInCallBack() {
        val username = binding.editUserName.editText?.text.toString()
        val password = binding.editPassword.editText?.text.toString()

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