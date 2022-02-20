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
import com.leo.authui.core.utils.exhaustive
import com.leo.authui.core.ui.views.BaseViewState
import com.leo.authui.R
import com.leo.authui.core.ui.hideKeyboard
import com.leo.authui.core.utils.showConfirmDialog
import com.leo.authui.core.utils.snack
import com.leo.authui.login.ui.navigatorstates.PassRecoveryNavigatorStates
import com.leo.authui.login.ui.viewmodels.PassRecoveryViewModel
import com.leo.authui.databinding.FragmentPassRecoveryBinding
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class PassRecoveryFragment : Fragment() {

    companion object {
        fun newInstance() = PassRecoveryFragment()
        const val TAG = "PassRecoveryFragment"
    }

    private var _binding: FragmentPassRecoveryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PassRecoveryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPassRecoveryBinding.inflate(layoutInflater)
        setListeners()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setObservers()
    }

    private fun setListeners() {
        binding.btnRecoveryReturn.setOnClickListener { viewModel.goBack() }
        binding.btnRecoveryPassword.setOnClickListener { checkFieldsToRecoveryEmail() }
    }

    private fun setObservers() {
        viewModel.navigation.observe(viewLifecycleOwner, Observer { handleNavigation(it) })
        viewModel.viewState.observe(viewLifecycleOwner, Observer { handleViewStates(it) })
        //Otros observadores
    }

    private fun handleNavigation(navigation: PassRecoveryNavigatorStates) {
        when (navigation) {
            is PassRecoveryNavigatorStates.ToSignIn -> {
                showMessage("Se envió un mail de recuperación")
                findNavController().navigateUp()
            }
            is PassRecoveryNavigatorStates.GoBack -> {
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

    private fun showMessage(msg: String) {
        binding.root.snack(msg, Snackbar.LENGTH_SHORT)
    }

    private fun enableUI(enable: Boolean) {
        if (enable) {
            binding.progressLoader.visibility = View.GONE
        } else {
            binding.progressLoader.visibility = View.VISIBLE
        }
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
                showMessage(getString(R.string.msg_error_default) + e.message!!)
            }
        }
    }

    private fun checkFieldsToRecoveryEmail() {
        with(binding) {
            val email = editRecoveryEmail.editText?.text.toString()
            root.hideKeyboard()

            editRecoveryEmail.error = when (!email.contains('@')) {
                true -> "Ingrese un email válido"
                false -> null
            }
            if (editRecoveryEmail.error == null) {
                root.showConfirmDialog(
                    "Reseteo de Contraseña",
                    "¿Desea enviar un mail a $email para recuperar su contraseña?"
                ) {
                    Log.d(TAG, "$TAG: User: $email")
                    viewModel.resetPassword(email)
                }
            }
        }
    }

}
