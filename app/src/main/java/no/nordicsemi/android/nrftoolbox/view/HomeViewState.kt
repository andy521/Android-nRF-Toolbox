package no.nordicsemi.android.nrftoolbox.view

data class HomeViewState(
    val isCSCModuleRunning: Boolean = false,
    val isHRSModuleRunning: Boolean = false,
    val isHTSModuleRunning: Boolean = false,
    val isRSCSModuleRunning: Boolean = false,
    val isPRXModuleRunning: Boolean = false,
    val isCGMModuleRunning: Boolean = false,
    val isUARTModuleRunning: Boolean = false,
    val refreshToggle: Boolean = false
) {

    fun copyWithRefresh(): HomeViewState {
        return copy(refreshToggle = !refreshToggle)
    }
}
