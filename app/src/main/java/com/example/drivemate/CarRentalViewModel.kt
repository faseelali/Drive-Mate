package com.example.drivemate

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CarRentalViewModel : ViewModel() {
    // LiveData for the input fields
    val pickupLocation = MutableLiveData<String>()
    val dropOffLocation = MutableLiveData<String>()
    val pickupDate = MutableLiveData<String>()
    val dropOffDate = MutableLiveData<String>()

    // Constants for Kayak deep link
    private val KAYAK_DOMAIN = "www.kayak.com"
    private val AFFILIATE_ID = "awesomecars"

    // Generate deep link URL using the provided format.
    // If dropOff location is empty, we can default it to pickup location.
    fun generateDeepLink(): String? {
        val pickup = pickupLocation.value?.trim()
        val dropOff = dropOffLocation.value?.trim()?.takeIf { it.isNotEmpty() } ?: pickup
        val pickupDt = pickupDate.value?.trim()
        val dropOffDt = dropOffDate.value?.trim()

        // Basic validation: check that required fields are not null or empty
        if (pickup.isNullOrEmpty() || pickupDt.isNullOrEmpty() || dropOffDt.isNullOrEmpty()) {
            return null
        }

        // Construct URL using the format:
        // https://{KAYAK_Domain}/in?a={Affiliate_ID}&url=/cars/{Pickup}/{Dropoff}/{Pickup_Date}/{Dropoff_Date}
        return "https://$KAYAK_DOMAIN/in?a=$AFFILIATE_ID&url=/cars/$pickup/$dropOff/$pickupDt/$dropOffDt"
    }
}
