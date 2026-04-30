package com.example.scrollablexml.ui.settings

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
import com.example.scrollablexml.databinding.FragmentSettingsBinding
import com.example.scrollablexml.ui.ScrollableViewModel
import kotlinx.coroutines.launch


class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ScrollableViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    val checkedLocaleIn = if (uiState.selectedLocale == "en")
                        R.id.rb_english else R.id.rb_indonesian
                    if (binding.rgLocale.checkedRadioButtonId != checkedLocaleIn)
                        binding.rgLocale.check(checkedLocaleIn)
                }
            }
        }

        binding.rgLocale.setOnCheckedChangeListener { _, checkedLocaleIn ->
            val locale = if (checkedLocaleIn == R.id.rb_english) "en" else "id"
            viewModel.updateLocale(locale)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}