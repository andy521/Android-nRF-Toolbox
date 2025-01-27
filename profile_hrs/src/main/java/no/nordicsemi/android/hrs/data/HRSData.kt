package no.nordicsemi.android.hrs.data

internal data class HRSData(
    val heartRates: List<Int> = emptyList(),
    val batteryLevel: Int? = null,
    val sensorLocation: Int = 0,
)
