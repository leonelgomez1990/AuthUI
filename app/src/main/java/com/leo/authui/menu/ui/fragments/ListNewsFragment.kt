package com.leo.authui.menu.ui.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.leo.authui.R
import com.leo.authui.menu.ui.viewmodels.ListNewsViewModel

class ListNewsFragment : Fragment() {

    companion object {
        fun newInstance() = ListNewsFragment()
    }

    private lateinit var viewModel: ListNewsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_news, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ListNewsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}