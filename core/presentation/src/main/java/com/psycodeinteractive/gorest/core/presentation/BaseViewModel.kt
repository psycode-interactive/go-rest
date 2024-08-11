package com.psycodeinteractive.gorest.core.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

abstract class BaseViewModel<State : ViewState>(): ViewModel() {

    private val _viewState by lazy { MutableStateFlow(initialViewState) }
    val viewState by lazy { _viewState.asStateFlow() }

    abstract val initialViewState: State

    protected fun updateViewState(update: (lastState: State) -> State) {
        _viewState.update(update)
    }
}

interface ViewState
