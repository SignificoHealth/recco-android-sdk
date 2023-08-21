package com.recco.showcase.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.recco.showcase.R
import com.recco.showcase.ui.theme.SoftYellow
import com.recco.showcase.ui.theme.Typography
import com.recco.showcase.ui.theme.WarmBrown

@Composable
fun ShowcaseButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    enabled: Boolean = true
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = WarmBrown,
            contentColor = SoftYellow
        ),
        shape = RoundedCornerShape(0.dp),
        onClick = onClick,
        enabled = enabled
    ) {
        Text(
            text = text,
            style = Typography.displayMedium
        )
    }
}

@Preview
@Composable
private fun Preview() {
    ShowcaseButton(onClick = {}, text = stringResource(R.string.login))
}
