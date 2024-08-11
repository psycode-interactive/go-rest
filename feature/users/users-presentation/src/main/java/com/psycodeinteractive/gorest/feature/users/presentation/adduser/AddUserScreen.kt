package com.psycodeinteractive.gorest.feature.users.presentation.adduser

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.psycodeinteractive.gorest.core.presentation.PreviewWithThemes
import com.psycodeinteractive.gorest.core.presentation.Screen
import com.psycodeinteractive.gorest.feature.users.presentation.R

@Composable
fun AddUserScreen(
    onNavigateUp: (wasUserAdded: Boolean) -> Unit,
) {
    Screen<AddUserViewModel, _> {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = AlertDialogDefaults.shape
                )
                .padding(16.dp),
        ) {
            val spacingModifier = Modifier.padding(top = 16.dp)
            Text(
                modifier = spacingModifier,
                text = stringResource(R.string.add_user),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = spacingModifier)
            TextField(
                value = viewState.formData.name,
                onValueChange = viewModel::onNameChanged,
                supportingText = {
                    if (viewState.formValidation.isNameEntered.not()) {
                        Text(
                            text = stringResource(R.string.error_name_empty),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                label = {
                    Text(
                        text = stringResource(R.string.name),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            )
            Spacer(modifier = spacingModifier)
            TextField(
                value = viewState.formData.email,
                onValueChange = viewModel::onEmailChanged,
                supportingText = {
                    if (viewState.formValidation.isEmailValid.not()) {
                        Text(
                            text = stringResource(R.string.error_invalid_email),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                label = {
                    Text(
                        text = stringResource(R.string.email),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            )
            Spacer(modifier = spacingModifier)
            RadioGroup(
                title = stringResource(R.string.gender),
                options = viewState.genderOptions,
                labelResources = viewState.genderOptions.map { it.textResource },
                selectedOption = viewState.formData.gender,
                onOptionSelected = viewModel::onGenderSelected
            )
            Spacer(modifier = spacingModifier)
            RadioGroup(
                title = stringResource(R.string.status),
                options = viewState.statusOptions,
                labelResources = viewState.statusOptions.map { it.textResource },
                selectedOption = viewState.formData.status,
                onOptionSelected = viewModel::onStatusSelected
            )

            Spacer(modifier = spacingModifier)
            Buttons(
                isFormValid = viewState.formValidation.isFormValid,
                onCancelClick = viewModel::onCancelAction,
                onAddUserClick = viewModel::onAddUserAction,
                isLoading = viewState.isLoading
            )

            viewState.errorMessage?.run {
                val context = LocalContext.current
                Toast.makeText(context, getMessage(), Toast.LENGTH_LONG).show()
            }

            if (viewState.isFlowFinished) {
                LaunchedEffect(Unit) {
                    onNavigateUp(viewState.wasUserAdded)
                }
            }
        }
    }
}

@Composable
private fun Buttons(
    isLoading: Boolean,
    isFormValid: Boolean,
    onCancelClick: () -> Unit,
    onAddUserClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextButton(
            onClick = onCancelClick
        ) {
            Text(
                text = stringResource(R.string.cancel),
                style = MaterialTheme.typography.titleMedium,
            )
        }

        TextButton(
            enabled = isFormValid && !isLoading,
            onClick = onAddUserClick
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Text(
                    text = stringResource(R.string.add_user),
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
    }
}

@Composable
private fun <Model> RadioGroup(
    title: String,
    options: List<Model>,
    labelResources: List<Int>,
    selectedOption: Model,
    onOptionSelected: (Model) -> Unit
) {
    Text(
        modifier = Modifier.padding(top = 16.dp),
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurface
    )
    FlowRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        options.forEachIndexed { index, option ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedOption == option,
                    onClick = { onOptionSelected(option) }
                )
                Text(
                    text = stringResource(labelResources[index]),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@PreviewWithThemes
@Composable
private fun AddUserScreenPreview() {
    AddUserScreen {}
}
