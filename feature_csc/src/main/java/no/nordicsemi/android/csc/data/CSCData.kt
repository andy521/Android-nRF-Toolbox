package no.nordicsemi.android.csc.data

import no.nordicsemi.android.csc.view.CSCSettings
import no.nordicsemi.android.csc.view.SpeedUnit
import java.util.*

internal data class CSCData(
    val showDialog: Boolean = false,
    val scanDevices: Boolean = false,
    val selectedSpeedUnit: SpeedUnit = SpeedUnit.M_S,
    val speed: Float = 0f,
    val cadence: Int = 0,
    val distance: Float = 0f,
    val totalDistance: Float = 0f,
    val gearRatio: Float = 0f,
    val batteryLevel: Int = 0,
    val wheelSize: String = CSCSettings.DefaultWheelSize.NAME,
    val isScreenActive: Boolean = true
) {

    private val speedWithUnit = when (selectedSpeedUnit) {
        SpeedUnit.M_S -> speed
        SpeedUnit.KM_H -> speed * 3.6f
        SpeedUnit.MPH -> speed * 2.2369f
    }

    fun displaySpeed(): String {
        return when (selectedSpeedUnit) {
            SpeedUnit.M_S -> String.format("%.1f m/s", speedWithUnit)
            SpeedUnit.KM_H -> String.format("%.1f km/h", speedWithUnit)
            SpeedUnit.MPH -> String.format("%.1f mph", speedWithUnit)
        }
    }

    fun displayCadence(): String {
        return String.format("%d RPM", cadence)
    }

    fun displayDistance(): String {
        return when (selectedSpeedUnit) {
            SpeedUnit.M_S -> String.format("%.0f m", distance)
            SpeedUnit.KM_H -> String.format("%.0f m", distance)
            SpeedUnit.MPH -> String.format("%.0f yd", distance)
        }
    }

    fun displayTotalDistance(): String {
        return when (selectedSpeedUnit) {
            SpeedUnit.M_S -> String.format("%.2f km", distance)
            SpeedUnit.KM_H -> String.format("%.2f km", distance)
            SpeedUnit.MPH -> String.format("%.2f mile", distance)
        }
    }

    fun displayGearRatio(): String {
        return String.format(Locale.US, "%.1f", gearRatio)
    }

    fun items(): List<> {

    }
}
