package com.kyleengler.alltrailshw.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kyleengler.alltrailshw.R
import com.kyleengler.alltrailshw.databinding.MainFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.main_fragment) {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var mapFragment: MapsFragment
    private lateinit var listFragment: ListFragment
    private val binding
        get() = _binding!!
    private var _binding: MainFragmentBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            mapFragment = MapsFragment()
            listFragment = ListFragment()
        } else {
            val map = childFragmentManager.findFragmentByTag("map")
            mapFragment = if (map == null) {
                MapsFragment()
            } else {
                map as MapsFragment
            }
            val list = childFragmentManager.findFragmentByTag("list")
            listFragment = if (list == null) {
                ListFragment()
            } else {
                list as ListFragment
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.screenState.observe(viewLifecycleOwner) { onScreenStateChange(it) }
        binding.toggleButton.setOnClickListener { viewModel.onScreenToggleClick() }
        binding.searchField.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val imm: InputMethodManager =
                    v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
                val searchTerm = v.text
                if (!searchTerm.isNullOrEmpty()) {
                    performSearch(searchTerm)
                }
                false
            } else false
        }
        binding.searchLayout.setEndIconOnClickListener {
            binding.searchField.setText("")
            viewModel.clearSearchText()
        }
    }

    private fun performSearch(text: CharSequence?) {
        if (text != null) {
            viewModel.performSearch(text.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun onScreenStateChange(screenValue: ScreenValue) {
        when (screenValue) {
            ScreenValue.Map -> goToMap()
            ScreenValue.List -> goToList()
        }
    }

    private fun goToList() {
        childFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, listFragment, "list")
            .commit()
        val leftDrawable =
            AppCompatResources.getDrawable(requireContext(), R.drawable.ic_outline_place_24)
        binding.toggleButton.apply {
            setCompoundDrawablesWithIntrinsicBounds(leftDrawable, null, null, null)
            text = getString(R.string.map)
        }
    }

    private fun goToMap() {
        childFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, mapFragment, "map")
            .commit()
        val leftDrawable = AppCompatResources.getDrawable(
            requireContext(),
            R.drawable.ic_baseline_format_list_bulleted_24
        )
        binding.toggleButton.apply {
            setCompoundDrawablesWithIntrinsicBounds(leftDrawable, null, null, null)
            text = getString(R.string.list)
        }
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}