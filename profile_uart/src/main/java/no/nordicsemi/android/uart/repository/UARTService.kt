package no.nordicsemi.android.uart.repository

import android.content.Intent
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import no.nordicsemi.android.service.DEVICE_DATA
import no.nordicsemi.android.service.NotificationService
import no.nordicsemi.ui.scanner.DiscoveredBluetoothDevice
import javax.inject.Inject

@AndroidEntryPoint
internal class UARTService : NotificationService() {

    @Inject
    lateinit var repository: UARTRepository

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        val device = intent!!.getParcelableExtra<DiscoveredBluetoothDevice>(DEVICE_DATA)!!

        repository.start(device, lifecycleScope)

        repository.hasBeenDisconnected.onEach {
            if (it) stopSelf()
        }.launchIn(lifecycleScope)

        return START_REDELIVER_INTENT
    }
}
