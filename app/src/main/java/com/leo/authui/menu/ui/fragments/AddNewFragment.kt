package com.leo.authui.menu.ui.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.leo.authui.core.utils.exhaustive
import com.leo.authui.core.utils.snack
import com.leo.authui.core.ui.views.BaseViewState
import com.leo.authui.R
import com.leo.authui.core.ui.hideKeyboard
import com.leo.authui.core.usecases.RequestPermissionsUseCase
import com.leo.authui.core.utils.MyResult
import com.leo.authui.core.utils.showConfirmDialog
import com.leo.authui.menu.ui.navigatorstates.AddNewNavigatorStates
import com.leo.authui.menu.ui.viewmodels.AddNewViewModel
import com.leo.authui.databinding.FragmentAddNewBinding
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception
import javax.inject.Inject

@AndroidEntryPoint
class AddNewFragment : Fragment() {

    companion object {
        fun newInstance() = AddNewFragment()
        const val TAG = "AddNewFragment"
    }

    private var _binding: FragmentAddNewBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddNewViewModel  by activityViewModels()
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    @Inject
    lateinit var requestPermissionsUseCase: RequestPermissionsUseCase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddNewBinding.inflate(layoutInflater)
        setListeners()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setObservers()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                doSomeOperations(data)
            }
        }

    }

    private fun setListeners() {
        binding.fabAddImage.setOnClickListener { doAskForImage() }
        binding.btnCancel.setOnClickListener { viewModel.goBack() }
        binding.btnSave.setOnClickListener { checkDataToSave() }
    }

    private fun setObservers() {
        viewModel.navigation.observe(viewLifecycleOwner, Observer { handleNavigation(it) })
        viewModel.viewState.observe(viewLifecycleOwner, Observer { handleViewStates(it) })
        //Otros observadores
        viewModel.urlImage.observe(viewLifecycleOwner, Observer { binding.editUrlImage.setText(viewModel.urlImage.value.toString()) })
    }

    private fun handleNavigation(navigation: AddNewNavigatorStates) {
        when(navigation) {
            is AddNewNavigatorStates.GoBack -> {
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
            binding.progressLoader.visibility = View.GONE
        } else {
            binding.progressLoader.visibility = View.VISIBLE
        }
    }

    private fun handleExceptions(e: Exception){
        Log.w("AddNewFragment", "Exception thrown: ${e.message}")
        showMessage(getString(R.string.msg_error_default))
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
                    "¿Desea crear la noticia: ${viewModel.new.title}?"
                ) {
                    //viewModel.updateNew(new)
                }
            }
        }
    }

    private fun doAskForImage() {

        when(requestPermissionsUseCase.requestStoragePermission(requireActivity())) {
            is MyResult.Success -> {

                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                Intent.createChooser(intent, "Seleccioná la imagen")
                resultLauncher.launch(intent)
            }
            is MyResult.Failure -> {
                showMessage("Permiso no concedido")
            }
        }

    }

    private fun doSomeOperations(data : Intent?) {
        try {
            if (data != null) {
                viewModel.doUploadImageToDatabase(data.data.toString())
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception caught: $e")
            showMessage("Ocurrió un error al buscar el archivo ${e.message}")
        }
    }


}
