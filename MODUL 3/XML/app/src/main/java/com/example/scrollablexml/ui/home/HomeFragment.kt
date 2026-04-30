package com.example.scrollablexml.ui.home

import android.content.Intent
import androidx.recyclerview.widget.ConcatAdapter
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
import com.example.scrollablexml.R
import com.example.scrollablexml.databinding.FragmentHomeBinding
import com.example.scrollablexml.ui.ScrollableViewModel
import com.example.scrollablexml.ui.home.adapter.CarouselHeaderAdapter
import com.example.scrollablexml.ui.home.adapter.ItemCardAdapter
import kotlinx.coroutines.launch
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager



class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ScrollableViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.inflateMenu(R.menu.menu_home)
        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_setting -> {
                    findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
                    true
                }
                else -> false
            }
        }
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().finish()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    val carouselAdapter = CarouselHeaderAdapter(
                        items = uiState.list,
                        onDetailClick = { index ->
                            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(itemIndex = index)
                            findNavController().navigate(action)
                        }
                    )
                    val itemCardAdapter = ItemCardAdapter(
                        uiState.list,
                        onDetailClicked = { index ->
                            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(itemIndex = index)
                            findNavController().navigate(action)
                        },
                        onSteamClicked = { url ->
                            startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
                        }
                    )
                    binding.rvMain.apply {
                        layoutManager = LinearLayoutManager(requireContext())
                        adapter = ConcatAdapter(carouselAdapter, itemCardAdapter)
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