package com.example.mahasiswaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mahasiswaapp.model.Dosen
import com.example.mahasiswaapp.model.Mahasiswa
import com.example.mahasiswaapp.ui.DosenListScreen
import com.example.mahasiswaapp.ui.MahasiswaListScreen
import com.example.mahasiswaapp.ui.theme.MahasiswaAppTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.currentBackStackEntryAsState


object Routes {
    const val Mahasiswa = "mahasiswa"
    const val Dosen = "dosen"
}

private data class BottomNavItem(val route: String, val label: String, val icon: ImageVector)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MahasiswaAppTheme {
                MaterialTheme {
                    val navController = rememberNavController()

                    val sampleMahasiswa = listOf(
                        Mahasiswa("Alya Putri", "23123456", "Informatika", 3.82),
                        Mahasiswa("Budi Santoso", "23123457", "Sistem Informasi", 3.45),
                        Mahasiswa("Citra Lestari", "23123458", "Teknik Industri", 3.92),
                        Mahasiswa("Dian Pratama", "23123459", "Manajemen", 3.10),
                        Mahasiswa("Eka Rahma", "23123460", "Informatika", 3.67),
                        Mahasiswa("Fajar Nugroho", "23123461", "Sistem Informasi", 3.21),
                        Mahasiswa("Gina Marlina", "23123462", "Teknik Elektro", 3.78),
                        Mahasiswa("Hendra Wijaya", "23123463", "Teknik Mesin", 3.05),
                        Mahasiswa("Intan Prasetya", "23123464", "Teknik Industri", 3.88),
                        Mahasiswa("Joko Susilo", "23123465", "Manajemen", 2.95)
                    )


                    val sampleDosen = listOf(
                        Dosen("Dr. Andi Wijaya", "1234567890", "Informatika", "Data Science"),
                        Dosen("Ir. Siti Rahma, M.Kom.", "0987654321", "Sistem Informasi", "Internet of Things"),
                        Dosen("Dharma Putra, M.T.", "1112223334", "Teknik Elektro", "Embedded Systems"),
                        Dosen("Prof. Lina Kartika", "2223334445", "Manajemen", "Manajemen Operasi"),
                        Dosen("Dr. Rudi Hartono", "3334445556", "Teknik Industri", "Ergonomi"),
                        Dosen("Dr. Maya Sari", "4445556667", "Informatika", "Machine Learning"),
                        Dosen("Ir. Anton Pratama", "5556667778", "Teknik Mesin", "Material Rekayasa"),
                        Dosen("Dr. Yuni Astuti", "6667778889", "Teknik Elektro", "Telekomunikasi"),
                        Dosen("Drs. Budi Hartono", "7778889990", "Sistem Informasi", "Sistem Informasi Kesehatan"),
                        Dosen("Dra. Rina Setiawan", "8889990001", "Teknik Industri", "Manajemen Rantai Pasok")
                    )

                    val items = remember {
                        listOf(
                            BottomNavItem(Routes.Mahasiswa, "Mahasiswa", Icons.Filled.Person),
                            // use Person icon as fallback for second tab to avoid unresolved icon
                            BottomNavItem(Routes.Dosen, "Dosen", Icons.Filled.Person)
                        )
                    }

                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route ?: Routes.Mahasiswa

                    Scaffold(
                        bottomBar = {
                            NavigationBar {
                                items.forEach { item ->
                                    NavigationBarItem(
                                        selected = currentRoute == item.route,
                                        onClick = {
                                            if (currentRoute != item.route) {
                                                navController.navigate(item.route) {
                                                    // Pop up to the start destination of the graph to
                                                    // avoid building up a large stack of destinations
                                                    popUpTo(Routes.Mahasiswa) {
                                                        inclusive = false
                                                    }
                                                    // Avoid multiple copies of the same destination when
                                                    // reselecting the same item
                                                    launchSingleTop = true
                                                    // Restore state when reselecting a previously selected item
                                                    restoreState = true
                                                }
                                            }
                                        },
                                        icon = { androidx.compose.material3.Icon(item.icon, contentDescription = item.label) },
                                        label = { Text(item.label) }
                                    )
                                }
                            }
                        }
                    ) { innerPadding ->
                        NavHost(
                            modifier = Modifier.padding(innerPadding),
                            navController = navController,
                            startDestination = Routes.Mahasiswa,
                            builder = {
                                composable(Routes.Mahasiswa) {
                                    MahasiswaListScreen(
                                        mahasiswas = sampleMahasiswa,
                                        onMahasiswaClick = { /* TODO: detail */ }
                                    )
                                }
                                composable(Routes.Dosen) {
                                    DosenListScreen(
                                        dosens = sampleDosen
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}