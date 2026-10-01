package br.com.alexsander.leitor.compose

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import br.com.alexsander.leitor.R
import br.com.alexsander.leitor.data.Code

@Composable
fun CodeItem(
    code: Code,
    copy: (String) -> Unit = { },
    delete: (() -> Unit)? = null
) {
    ListItem(
        headlineContent = {
            Text(
                text = code.value,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        trailingContent = {
            Row {
                IconButton({ copy(code.value) }) {
                    Icon(Icons.Rounded.ContentCopy, stringResource(R.string.copy_icon))
                }
                if (delete != null) {
                    IconButton(
                        delete,
                        colors = IconButtonDefaults.iconButtonColors(contentColor = Color.Red),
                    ) {
                        Icon(Icons.Rounded.Clear, stringResource(R.string.delete_icon))
                    }
                }
            }
        }
    )
    HorizontalDivider()
}

@Preview
@Composable
fun CodeItemPreview() {
    CodeItem(Code(value = "123"))
}