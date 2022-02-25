package com.leo.authui.menu.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.leo.authui.core.utils.exhaustive
import com.leo.authui.core.utils.snack
import com.leo.authui.core.ui.views.BaseViewState
import com.leo.authui.R
import com.leo.authui.core.ui.hideKeyboard
import com.leo.authui.core.utils.showConfirmDialog
import com.leo.authui.menu.ui.navigatorstates.EditNewNavigatorStates
import com.leo.authui.menu.ui.viewmodels.EditNewViewModel
import com.leo.authui.databinding.FragmentEditNewBinding
import com.leo.authui.login.ui.fragments.SignUpFragment
import com.leo.authui.menu.domain.News
import com.leo.authui.menu.ui.models.toNew
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class EditNewFragment : Fragment() {

    companion object {
        fun newInstance() = EditNewFragment()
        const val TAG = "EditNewFragment"
    }

    private var _binding: FragmentEditNewBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EditNewViewModel by activityViewModels()
    private val args: EditNewFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditNewBinding.inflate(layoutInflater)
        viewModel.refreshNewData(args.uid)
        setListeners()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setObservers()
    }

    private fun setListeners() {
        binding.btnCancel.setOnClickListener { viewModel.goBack() }
        binding.btnSave.setOnClickListener { checkDataToSave() }
    }

    private fun setObservers() {
        viewModel.navigation.observe(viewLifecycleOwner, Observer { handleNavigation(it) })
        viewModel.viewState.observe(viewLifecycleOwner, Observer { handleViewStates(it) })
        //Otros observadores
    }

    private fun handleNavigation(navigation: EditNewNavigatorStates) {
        when (navigation) {
            is EditNewNavigatorStates.GoBack -> {
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
                setUIData()
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
        Log.w("EditNewFragment", "Exception thrown: ${e.message}")
        showMessage(getString(R.string.msg_error_default))
    }

    private fun setUIData() {
        val new = viewModel.new
        binding.editTitle.setText(new.title)
        binding.editAuthor.setText(new.author)
        binding.editUrl.setText(new.url)
        binding.editUrlImage.setText(new.urlToImage)
        binding.editContent.setText(new.content)
    }

    private fun checkDataToSave() {
        var new = viewModel.new
        with(binding) {
            new.title = editTitle.text.toString()
            new.author = editAuthor.text.toString()
            new.content = editContent.text.toString()
            new.url = editUrl.text.toString()
            new.urlToImage = editUrlImage.text.toString()

            root.hideKeyboard()

            editTitle.error = when (new.title.isBlank()) {
                true -> "Ingrese un título para la noticia"
                false -> null
            }
            editUrlImage.error = when (new.urlToImage.isBlank()) {
                true -> "Ingrese una imagen para la noticia"
                false -> null
            }
            if ((editTitle.error == null) && (editUrlImage.error == null)) {
                root.showConfirmDialog(
                    "Edición",
                    "¿Desea editar la noticia: ${viewModel.new.title}?"
                ) {
                    viewModel.updateNew(new)
                }
            }
        }
    }
}
