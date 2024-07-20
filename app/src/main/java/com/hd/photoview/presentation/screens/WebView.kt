package com.hd.photoview.presentation.screens

import android.app.Activity
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun WebView(url : String){
    val context = LocalContext.current as Activity
    AndroidView({
        WebView( context).apply {
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
            loadUrl(url)
        }
    })
    
}




@Preview
@Composable
fun HD(){
    val url = "https://medium.com/@sahar.asadian90/webview-in-jetpack-compose-71f237873c2e"
    
    WebView(url = url)
}