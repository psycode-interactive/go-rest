package com.psycodeinteractive.gorest.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Lifecycle.State.STARTED
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch

@Composable
inline fun <reified VM : BaseViewModel<State>, reified State : ViewState> Screen(
    key: String? = null,
    content: @Composable ScreenScope<VM, State>.() -> Unit,
) {
    val viewModel: VM = hiltViewModel(key = key)
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()
    val screenScope = remember(key, viewModel, viewState) {
        ScreenScope(
            viewModel = viewModel,
            viewState = viewState,
        )
    }
    screenScope.content()
}

data class ScreenScope<VM : BaseViewModel<State>, State : ViewState>(
    val viewModel: VM,
    val viewState: State,
)

@Composable
fun OnLifecycle(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    minActiveState: Lifecycle.State = STARTED,
    action: suspend () -> Unit,
) {
    LaunchedEffect(Unit) {
        with(lifecycleOwner) {
            lifecycleScope.launch {
                repeatOnLifecycle(minActiveState) {
                    action()
                }
            }
        }
    }
}
