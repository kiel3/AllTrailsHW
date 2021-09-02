package com.kyleengler.alltrailshw.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.kyleengler.alltrailshw.R
import com.kyleengler.alltrailshw.databinding.PopupViewBinding
import com.kyleengler.alltrailshw.model.RestaurantModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception
import javax.inject.Inject

@AndroidEntryPoint
class MapsFragment : Fragment(), GoogleMap.InfoWindowAdapter {
    @Inject
    lateinit var viewModel: MapsViewModel
    private val markerSet = mutableMapOf<String, Boolean>()

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         */
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // On first install, sometimes this isn't called because the map is ready before location permission is granted
            googleMap.isMyLocationEnabled = true
        }
        googleMap.setInfoWindowAdapter(this)
        viewModel.mapMarkers.observe(viewLifecycleOwner) { markers ->
            googleMap.clear()
            val userLocation = viewModel.userLocation
            if (userLocation != null) {
                val latLng = LatLng(userLocation.latitude, userLocation.longitude)
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f))
            }
            markers?.forEach { model ->
                val marker = MarkerOptions().position(
                    model.latLng
                )
                    .title(model.placeId)
                googleMap.addMarker(marker)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun getInfoWindow(marker: Marker): View? {
        val place = findPlace(marker)
        return if (place != null) {
            createView(place, marker)
        } else null
    }

    private fun createView(place: RestaurantModel, marker: Marker): View {
        val binding = PopupViewBinding.inflate(LayoutInflater.from(requireContext()))
        binding.restaurant.name.text = place.name
        val priceLevel = place.formatPriceLevel
        val type = place.type
        val supportText = when {
            priceLevel != null && type != null -> "$priceLevel · $type"
            priceLevel != null -> priceLevel
            type != null -> type
            else -> null
        }
        binding.restaurant.supportText.text = supportText
        binding.restaurant.ratingBar.rating = place.rating.toFloat()
        binding.restaurant.ratingCount.text = "(${place.userRatingsTotal})"

        val key = getString(R.string.places_api_key)
        val url = place.getPhotoUrl(key)
        if (url != null) {
            var isImageLoaded = markerSet[marker.id]
            if (isImageLoaded == true) {
                Picasso.get().load(url).into(binding.restaurant.image)
            } else {
                isImageLoaded = true
                markerSet[marker.id] = isImageLoaded
                Picasso.get().load(url)
                    .into(binding.restaurant.image, InfoWindowRefresher(marker))
            }
        }
        return binding.root
    }

    private fun findPlace(marker: Marker): RestaurantModel? {
        return viewModel.mapMarkers.value?.find { it.placeId == marker.title }
    }

    override fun getInfoContents(marker: Marker): View? {
        return null
    }
}

class InfoWindowRefresher(private val markerToRefresh: Marker) :
    Callback {
    private val TAG = "InfoWindowRefresher"
    override fun onSuccess() {
        Log.e(TAG, "InfoWindowRefresher success!")
        markerToRefresh.showInfoWindow()
    }

    override fun onError(e: Exception?) {
        Log.e(TAG, "InfoWindowRefresher error", e)
    }
}