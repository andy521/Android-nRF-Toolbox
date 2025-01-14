package no.nordicsemi.android.hrs.view

import no.nordicsemi.android.hrs.data.HRSData
import no.nordicsemi.android.service.BleManagerResult

internal sealed class HRSViewState

internal data class WorkingState(
    val result: BleManagerResult<HRSData>,
    val zoomIn: Boolean = false,
) : HRSViewState()

internal object NoDeviceState : HRSViewState()
