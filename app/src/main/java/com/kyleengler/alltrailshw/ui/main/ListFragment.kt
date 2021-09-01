package com.kyleengler.alltrailshw.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kyleengler.alltrailshw.R
import com.kyleengler.alltrailshw.databinding.FragmentListBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListFragment : Fragment(R.layout.fragment_list) {
    private val binding
        get() = _binding!!
    private var _binding: FragmentListBinding? = null
    private val restaurantAdapter = RestaurantAdapter()
    @Inject
    lateinit var viewModel: ListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        restaurantAdapter.onFavoriteClick = viewModel.onFavoriteClick
        binding.list.adapter = restaurantAdapter
        viewModel.mapMarkers.observe(viewLifecycleOwner) { restaurants ->
            if (restaurants != null) {
                restaurantAdapter.restaurants = restaurants
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}