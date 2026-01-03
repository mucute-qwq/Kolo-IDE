package io.github.mucute.qwq.koloide.component

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.github.mucute.qwq.koloide.model.SelectableCardDropDownMenuItem

@Composable
fun SelectableCardDropDownMenu(
    expanded: Boolean,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    selectableCardDropDownMenuItems: List<SelectableCardDropDownMenuItem> = emptyList(),
    onClick: (index: Int, selectableCardDropDownMenuItem: SelectableCardDropDownMenuItem) -> Unit = { _, _ -> }
) {
    DropdownMenu(
        modifier = modifier,
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        selectableCardDropDownMenuItems.forEachIndexed { index, selectableCardDropDownMenuItem ->
            DropdownMenuItem(
                leadingIcon = {
                    Icon(selectableCardDropDownMenuItem.leadingIcon, contentDescription = null)
                },
                text = {
                    Text(stringResource(selectableCardDropDownMenuItem.textResId))
                },
                onClick = {
                    onClick(index, selectableCardDropDownMenuItem)
                }
            )
        }
    }
}