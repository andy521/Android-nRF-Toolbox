package no.nordicsemi.android.gls.main.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import no.nordicsemi.android.gls.R
import no.nordicsemi.android.gls.main.viewmodel.GLSViewModel
import no.nordicsemi.android.service.*
import no.nordicsemi.android.theme.view.BackIconAppBar
import no.nordicsemi.ui.scanner.ui.DeviceConnectingView
import no.nordicsemi.ui.scanner.ui.NoDeviceView
import no.nordicsemi.android.utils.exhaustive
import no.nordicsemi.ui.scanner.ui.DeviceDisconnectedView
import no.nordicsemi.ui.scanner.ui.Reason

@Composable
fun GLSScreen() {
    val viewModel: GLSViewModel = hiltViewModel()
    val state = viewModel.state.collectAsState().value

    Column {
        val navigateUp = { viewModel.onEvent(DisconnectEvent) }

        BackIconAppBar(stringResource(id = R.string.gls_title)) {
            viewModel.onEvent(DisconnectEvent)
        }

        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            when (state) {
                NoDeviceState -> NoDeviceView()
                is WorkingState -> when (state.result) {
                    is ConnectingResult -> DeviceConnectingView { viewModel.onEvent(DisconnectEvent) }
                    is DisconnectedResult -> DeviceDisconnectedView(Reason.USER, navigateUp)
                    is LinkLossResult -> DeviceDisconnectedView(Reason.LINK_LOSS, navigateUp)
                    is MissingServiceResult -> DeviceDisconnectedView(Reason.MISSING_SERVICE, navigateUp)
                    is UnknownErrorResult -> DeviceDisconnectedView(Reason.UNKNOWN, navigateUp)
                    is SuccessResult -> GLSContentView(state.result.data) { viewModel.onEvent(it) }
                }
            }.exhaustive
        }
    }
}
