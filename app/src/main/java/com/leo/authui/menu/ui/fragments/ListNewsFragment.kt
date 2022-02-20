package com.leo.authui.menu.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.leo.authui.R
import com.leo.authui.core.ui.views.BaseViewState
import com.leo.authui.core.utils.exhaustive
import com.leo.authui.core.utils.snack
import com.leo.authui.databinding.FragmentListNewsBinding
import com.leo.authui.menu.domain.News
import com.leo.authui.menu.ui.adapters.NewsItemAdapter
import com.leo.authui.menu.ui.navigatorstates.ListNewsNavigatorStates
import com.leo.authui.menu.ui.viewmodels.ListNewsViewModel

class ListNewsFragment : Fragment() {

    companion object {
        fun newInstance() = ListNewsFragment()
    }

    private var _binding: FragmentListNewsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ListNewsViewModel  by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListNewsBinding.inflate(layoutInflater)
        viewModel.refreshNews()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setObservers()

    }

    private fun setObservers() {
        viewModel.navigation.observe(viewLifecycleOwner, Observer { handleNavigation(it) })
        viewModel.viewState.observe(viewLifecycleOwner, Observer { handleViewStates(it)})
        //Otros observadores
        viewModel.news.observe(viewLifecycleOwner, Observer { setupNewsRecyclerView(it.toMutableList()) })
    }

    private fun handleNavigation(navigation: ListNewsNavigatorStates) {
        when(navigation) {
            is ListNewsNavigatorStates.ToDetailNews -> {
                val action = ListNewsFragmentDirections.actionListNewsFragmentToDetailNewsFragment(navigation.new)
                findNavController().navigate(action)

            }
        }.exhaustive
    }


    private fun handleViewStates(state: BaseViewState) {
        when(state) {
            is BaseViewState.Ready -> { enableUI(true) }
            is BaseViewState.Loading -> { enableUI(false) }
            is BaseViewState.Failure -> { showMessage(getString(R.string.msg_error_default)) }

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



    private fun setupNewsRecyclerView(list: MutableList<News>) {
        val adapter = NewsItemAdapter()
        adapter.setData(list)
        adapter.onClickListener = {
            viewModel.goToDetailNews(it)
        }

        with(binding.rvNews) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
            isNestedScrollingEnabled = true
        }
    }


}