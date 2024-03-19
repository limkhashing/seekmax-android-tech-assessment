package io.limkhashing.seekmax.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import io.limkhashing.seekmax.navigation.graph.RootNavigationGraph
import io.limkhashing.seekmax.presentation.ui.theme.SeekMAXAndroidTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SeekMAXAndroidTheme {
                RootNavigationGraph(rootNavHostController = rememberNavController())
            }
        }
    }
}