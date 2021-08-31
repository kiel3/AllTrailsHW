package com.kyleengler.alltrailshw.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kyleengler.alltrailshw.R
import com.kyleengler.alltrailshw.databinding.MainFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

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
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun onScreenStateChange(screenValue: ScreenValue) {
        when (screenValue)  {
            ScreenValue.Map -> goToMap()
            ScreenValue.List -> goToList()
        }
    }

    private fun goToList() {
        childFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, listFragment, "list")
            .commit()
    }

    private fun goToMap() {
        childFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, mapFragment, "map")
            .commit()
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}