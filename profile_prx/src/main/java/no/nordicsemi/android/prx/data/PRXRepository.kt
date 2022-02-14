package no.nordicsemi.android.prx.data

import android.bluetooth.BluetoothDevice
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import no.nordicsemi.android.ble.ktx.suspend
import no.nordicsemi.android.prx.repository.AlarmHandler
import no.nordicsemi.android.prx.repository.PRXManager
import no.nordicsemi.android.prx.repository.PRXService
import no.nordicsemi.android.prx.repository.ProximityServerManager
import no.nordicsemi.android.service.BleManagerResult
import no.nordicsemi.android.service.ConnectingResult
import no.nordicsemi.android.service.ServiceManager
import no.nordicsemi.android.service.SuccessResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class PRXRepository @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val serviceManager: ServiceManager,
    private val proximityServerManager: ProximityServerManager,
    private val alarmHandler: AlarmHandler
) {

    private var manager: PRXManager? = null

    private val _data = MutableStateFlow<BleManagerResult<PRXData>>(ConnectingResult())
    internal val data = _data.asStateFlow()

    private val _isRunning = MutableStateFlow(false)
    val isRunning = _isRunning.asStateFlow()

    fun launch(device: BluetoothDevice) {
        serviceManager.startService(PRXService::class.java, device)
        proximityServerManager.open()
    }

    fun start(device: BluetoothDevice, scope: CoroutineScope) {
        val manager = PRXManager(context, scope)
        this.manager = manager
        manager.useServer(proximityServerManager)

        manager.dataHolder.status.onEach {
            _data.value = it
            handleLocalAlarm(it)
        }.launchIn(scope)

        scope.launch {
            manager.start(device)
        }
    }

    private suspend fun PRXManager.start(device: BluetoothDevice) {
        try {
            connect(device)
                .useAutoConnect(false)
                .retry(3, 100)
                .suspend()
            _isRunning.value = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun handleLocalAlarm(result: BleManagerResult<PRXData>) {
        (result as? SuccessResult<PRXData>)?.let {
            if (it.data.localAlarmLevel != AlarmLevel.NONE) {
                alarmHandler.playAlarm(it.data.localAlarmLevel)
            } else {
                alarmHandler.pauseAlarm()
            }
        }
    }

    fun enableAlarm() {
        manager?.writeImmediateAlert(true)
    }

    fun disableAlarm() {
        manager?.writeImmediateAlert(false)
    }

    fun release() {
        serviceManager.stopService(PRXService::class.java)
        manager?.disconnect()?.enqueue()
        manager = null
        _isRunning.value = false
    }
}
