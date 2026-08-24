package com.example.obd2diagnostic

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import android.view.LayoutInflater

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun MainDashboardPreview() {
    AndroidView(factory = { context ->
        LayoutInflater.from(context).inflate(R.layout.activity_main, null)
    })
}
