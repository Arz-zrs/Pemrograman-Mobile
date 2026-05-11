package com.example.scrollablexml.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.scrollablexml.databinding.FragmentDetailBinding
import com.example.scrollablexml.ui.ScrollableViewModel
import kotlinx.coroutines.launch

class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ScrollableViewModel by activityViewModels()
    private val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        val index = args.itemIndex
        viewModel.updateCurrentItem(index)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    val item = uiState.list.getOrNull(index) ?: uiState.list.first()
                    binding.apply {
                        toolbar.setTitle(item.titleResourceId)
                        ivDetail.setImageResource(item.imageResourceId)
                        tvTitle.setText(item.titleResourceId)
                        tvSubtitle.setText(item.subtitleResourceId)
                        tvDesc.setText(item.descriptionResourceId)
                        tvDetail.setText(item.detailResourceId)
                    }
                }
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
