package com.recco.internal.core.ui.lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

@Composable
fun LifecycleEffect(
    onResume: (() -> Unit)? = null,
    onPause: (() -> Unit)? = null,
    onStart: (() -> Unit)? = null,
    onStop: (() -> Unit)? = null,
    onCreate: (() -> Unit)? = null,
    onDestroy: (() -> Unit)? = null
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                super.onResume(owner)
                onResume?.invoke()
            }

            override fun onPause(owner: LifecycleOwner) {
                super.onPause(owner)
                onPause?.invoke()
            }

            override fun onStart(owner: LifecycleOwner) {
                super.onStart(owner)
                onStart?.invoke()
            }

            override fun onStop(owner: LifecycleOwner) {
                super.onStop(owner)
                onStop?.invoke()
            }

            override fun onCreate(owner: LifecycleOwner) {
                super.onCreate(owner)
                onCreate?.invoke()
            }

            override fun onDestroy(owner: LifecycleOwner) {
                super.onDestroy(owner)
                onDestroy?.invoke()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}
