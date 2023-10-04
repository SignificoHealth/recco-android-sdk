package com.recco.api.ui

internal sealed class MainUserInteract {
    object Retry : MainUserInteract()
    object ReccoSDKOpen : MainUserInteract()
}
