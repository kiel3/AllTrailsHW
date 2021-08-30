package com.kyleengler.alltrailshw.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
import com.kyleengler.alltrailshw.entity.remote.Result
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.io.StringWriter
import javax.inject.Inject

@AndroidEntryPoint
class MapsFragment : Fragment(), GoogleMap.InfoWindowAdapter {
    @Inject
    lateinit var viewModel: MapsViewModel

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
//        val sydney = LatLng(-34.0, 151.0)
//        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.isMyLocationEnabled = true
            googleMap.setInfoWindowAdapter(this)
        }
        viewModel.mapMarkers.observe(viewLifecycleOwner) { markers ->
            val userLocation = viewModel.userLocation
            if (userLocation != null) {
                val latLng = LatLng(userLocation.latitude, userLocation.longitude)
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f))
            }
            markers.forEach { result ->
                val marker = MarkerOptions().position(
                    LatLng(
                        result.geometry.location.lat,
                        result.geometry.location.lng
                    )
                )
                    .title(result.placeId)
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
            createView(place)
        } else null
    }

    private fun createView(place: Result): View {
        val binding = PopupViewBinding.inflate(LayoutInflater.from(requireContext()))
        binding.name.text = place.name
        binding.supportText.text = "${formatPriceLevel(place.priceLevel)} · ${place.vicinity}"
        binding.ratingBar.rating = place.rating?.toFloat() ?: 0f
        binding.ratingCount.text = "(${place.userRatingsTotal})"

        val pictureId = place.photos?.firstOrNull()?.photoReference
        if (pictureId != null) {
            val key = getString(R.string.places_api_key)
            val url = "https://maps.googleapis.com/maps/api/place/photo?photo_reference=$pictureId&maxwidth=100&key=$key"
            Picasso.get().load(url).into(binding.image)
        }
        return binding.root
    }

    private fun formatPriceLevel(priceLevel: Int?): String {
        return if (priceLevel != null) {
            val sb = StringBuilder()
            for (i in 0..priceLevel) {
                sb.append("$")
            }
            sb.toString()
        } else ""
    }

    private fun findPlace(marker: Marker): Result? {
        return viewModel.mapMarkers.value?.find { it.placeId == marker.title }
    }

    override fun getInfoContents(marker: Marker): View? {
//        val place = viewModel.mapMarkers.value?.find { it.placeId == marker.title }
//        return if (place != null) {
//            TextView(requireContext()).apply {
//                text = "Here's the marker: ${place.name}, ${place.placeId}"
//                setTextColor(requireContext().getColor(R.color.purple_700))
//            }
//        } else null
        return null
    }
}