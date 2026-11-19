package com.animarkode.lida

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.animarkode.lida.ui.navigation.AppNavHost
import com.animarkode.lida.ui.theme.LidaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LidaTheme {
                AppNavHost()
            }
        }
    }
}
