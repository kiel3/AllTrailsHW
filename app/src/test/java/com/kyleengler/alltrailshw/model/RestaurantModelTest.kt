package com.kyleengler.alltrailshw.model

import org.junit.Assert.assertEquals
import org.junit.Test

class RestaurantModelTest {
    @Test
    fun `test formatPriceLevel String`() {
        var model = RestaurantModel(
            lat = 0.0,
            lng = 0.0,
            placeId = "",
            name = "",
            priceLevel = 0,
            vicinity = "",
            rating = 0.0,
            userRatingsTotal = 0,
            pictureId = null
        )
        assertEquals("", model.formatPriceLevel)
        model = model.copy(priceLevel = 1)
        assertEquals("$", model.formatPriceLevel)
        model = model.copy(priceLevel = 2)
        assertEquals("$$", model.formatPriceLevel)
        model = model.copy(priceLevel = 3)
        assertEquals("$$$", model.formatPriceLevel)
        model = model.copy(priceLevel = 4)
        assertEquals("$$$$", model.formatPriceLevel)
    }
}