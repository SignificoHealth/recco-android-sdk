package com.shadowflight.uicommons.components

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.shadowflight.uicommons.theme.AppTheme

@Composable
fun AppTopBar(
    title: String? = null,
    navigateUp: (() -> Unit)? = null,
    closeApp: (context: Context) -> Unit = { (it as AppCompatActivity).finish() }
) {
    val context = LocalContext.current
    Row {
        title?.let {
            Text(modifier = Modifier.weight(1f), text = title)
        }
        navigateUp?.let {
            Button(onClick = navigateUp) {
                Text(
                    text = "Back",
                    style = AppTheme.typography.cta.copy(color = AppTheme.colors.onPrimary)
                )
            }
        }
        Button(onClick = { closeApp(context) }) {
            Text(
                text = "Close",
                style = AppTheme.typography.cta.copy(color = AppTheme.colors.onPrimary)
            )
        }
    }
}
