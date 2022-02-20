package com.leo.authui.menu.ui.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.leo.authui.databinding.FragmentDetailNewsBinding
import com.leo.authui.menu.ui.models.NewUI
import com.leo.authui.menu.ui.viewmodels.DetailNewsViewModel

class DetailNewsFragment : Fragment() {

    companion object {
        fun newInstance() = DetailNewsFragment()
    }

    private var _binding: FragmentDetailNewsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailNewsViewModel  by activityViewModels()
    private val args: DetailNewsFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailNewsBinding.inflate(layoutInflater)

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshNew()
        }
        updateNewInformation(args.new)
        return binding.root
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

}