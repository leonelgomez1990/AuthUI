package com.leo.authui.menu.ui.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.leo.authui.R
import com.leo.authui.menu.ui.viewmodels.EditNewViewModel

class EditNewFragment : Fragment() {

    companion object {
        fun newInstance() = EditNewFragment()
    }

    private lateinit var viewModel: EditNewViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_new, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(EditNewViewModel::class.java)
        // TODO: Use the ViewModel
    }

}