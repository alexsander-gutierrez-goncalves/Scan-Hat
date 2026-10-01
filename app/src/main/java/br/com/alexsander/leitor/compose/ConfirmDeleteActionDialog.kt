package br.com.alexsander.leitor.compose

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import br.com.alexsander.leitor.R
import br.com.alexsander.leitor.data.Code

@Composable
fun ConfirmDeleteActionDialog(
    code: Code,
    delete: (Code) -> Unit,
    close: () -> Unit
) {
    val title = stringResource(R.string.delete_confirm_title)
    val message = "${stringResource(R.string.delete_confirm_message)} ${code.value}?"
    val yes = stringResource(R.string.yes)
    val not = stringResource(R.string.no)
    AlertDialog(
        icon = { Icon(Icons.Rounded.Warning, stringResource(R.string.alert_icon)) },
        title = {
            Text(title, Modifier.semantics {
                liveRegion = LiveRegionMode.Polite
                contentDescription = title
            })
        },
        text = {
            Text(
                message,
                overflow = TextOverflow.Ellipsis,
                maxLines = 3,
                modifier = Modifier.semantics {
                    liveRegion = LiveRegionMode.Polite
                    contentDescription = message
                }
            )
        },
        onDismissRequest = { close() },
        confirmButton = {
            TextButton({
                delete(code)
                close()
            }) {
                Text(yes, Modifier.semantics {
                    liveRegion = LiveRegionMode.Polite
                    contentDescription = yes
                })
            }
        }, dismissButton = {
            TextButton(onClick = { close() }) {
                Text(not, Modifier.semantics {
                    liveRegion = LiveRegionMode.Polite
                    contentDescription = not
                })
            }
        })
}