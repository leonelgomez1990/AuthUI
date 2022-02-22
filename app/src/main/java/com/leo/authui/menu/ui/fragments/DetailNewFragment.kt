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
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.leo.authui.R
import com.leo.authui.core.ui.hideKeyboard
import com.leo.authui.core.ui.views.BaseViewState
import com.leo.authui.core.utils.exhaustive
import com.leo.authui.core.utils.showConfirmDialog
import com.leo.authui.core.utils.snack
import com.leo.authui.databinding.FragmentDetailNewBinding
import com.leo.authui.login.ui.fragments.PassRecoveryFragment
import com.leo.authui.menu.ui.models.NewUI
import com.leo.authui.menu.ui.navigatorstates.DetailNewNavigatorStates
import com.leo.authui.menu.ui.viewmodels.DetailNewViewModel

class DetailNewFragment : Fragment() {

    companion object {
        fun newInstance() = DetailNewFragment()
        const val TAG = "DetailNewFragment"
    }

    private var _binding: FragmentDetailNewBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailNewViewModel  by activityViewModels()
    private val args: DetailNewFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailNewBinding.inflate(layoutInflater)
        setListeners()
        viewModel.new = args.new!!
        updateNewInformation(args.new)
        return binding.root
    }

    private fun setListeners() {
        binding.swipeRefresh.setOnRefreshListener { viewModel.refreshNew() }
        binding.fabEditNew.setOnClickListener { checkEditNew() }
        binding.fabDeleteNew.setOnClickListener { checkDeleteNew() }
    }

    override fun onStart() {
        super.onStart()
        setObservers()
    }

    private fun setObservers() {
        viewModel.navigation.observe(viewLifecycleOwner, Observer { handleNavigation(it) })
        viewModel.viewState.observe(viewLifecycleOwner, Observer { handleViewStates(it)})
        //Otros observadores
    }

    private fun handleNavigation(navigation: DetailNewNavigatorStates) {
        when(navigation) {
            is DetailNewNavigatorStates.ToEditNew -> {
                val action = DetailNewFragmentDirections.actionDetailNewsFragmentToEditNewFragment(navigation.uid)
                findNavController().navigate(action)
            }
            is DetailNewNavigatorStates.GoBack -> {
                showMessage("Se ha eliminado la noticia ${viewModel.new.title}")
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
            //binding.progressLoader.visibility = View.GONE
        } else {
            //binding.progressLoader.visibility = View.VISIBLE
        }
    }

    private fun handleExceptions(e: Exception){
        Log.w("DetailNewFragment", "Exception thrown: ${e.message}")
        showMessage(getString(R.string.msg_error_default))
    }

    private fun updateNewInformation(new: NewUI?) {
        if (new != null) {
            binding.txtName.text = new.title
            binding.txtDescription.text = new.content

            Glide.with(binding.root)
                .load(new.urlToImage)
                .centerCrop()
                .into(binding.imgNews)
        }
    }

    private fun checkEditNew() {
        viewModel.goToEditNew()
    }

    private fun checkDeleteNew() {
        binding.root.hideKeyboard()
        binding.root.showConfirmDialog(
            "Eliminar",
            "¿Desea borrar la noticia: ${viewModel.new.title}?"
        ) {
            viewModel.deleteNew()
        }
    }

}