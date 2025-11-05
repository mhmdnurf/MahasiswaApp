package com.example.mahasiswaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mahasiswaapp.ui.screens.HomeScreen
import com.example.mahasiswaapp.ui.screens.MahasiswaListScreen
import com.example.mahasiswaapp.ui.theme.MahasiswaAppTheme

object Routes {
    const val Home = "home"
    const val MahasiswaList = "mahasiswa_list"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MahasiswaAppTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Routes.Home,
                    modifier = Modifier.padding(vertical = 16.dp)
                ) {
                    composable(Routes.Home) {
                        HomeScreen(
                            onMahasiswaListClick = {
                                navController.navigate(Routes.MahasiswaList)
                            }
                        )
                    }
                    composable(Routes.MahasiswaList) {
                        MahasiswaListScreen(
                            onNavigateBack = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}