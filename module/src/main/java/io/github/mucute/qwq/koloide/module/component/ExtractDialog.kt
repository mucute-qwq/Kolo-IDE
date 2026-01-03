package io.github.mucute.qwq.koloide.module.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.mucute.qwq.koloide.module.R
import io.github.mucute.qwq.koloide.module.util.ExtractState

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ExtractDialog(
    extractState: ExtractState,
    onDismissRequest: () -> Unit = {}
) {
    if (extractState is ExtractState.Processing) {
        BasicAlertDialog(
            onDismissRequest = onDismissRequest
        ) {
            Surface(
                shape = MaterialTheme.shapes.large,
                tonalElevation = AlertDialogDefaults.TonalElevation,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        progress = {
                            extractState.progress
                        },
                    )
                    Spacer(Modifier.width(16.dp))
                    Text(
                        stringResource(R.string.extracting, extractState.name),
                        modifier = Modifier
                            .animateContentSize()
                    )
                }
            }
        }
    }
}