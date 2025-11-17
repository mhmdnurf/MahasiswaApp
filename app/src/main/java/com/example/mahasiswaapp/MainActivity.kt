package com.example.mahasiswaapp

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.mahasiswaapp.ui.screens.DosenEditScreen
import com.example.mahasiswaapp.ui.screens.DosenFormScreen
import com.example.mahasiswaapp.ui.screens.DosenListScreen
import com.example.mahasiswaapp.ui.screens.HomeScreen
import com.example.mahasiswaapp.ui.screens.MahasiswaDetailScreen
import com.example.mahasiswaapp.ui.screens.MahasiswaEditScreen
import com.example.mahasiswaapp.ui.screens.MahasiswaFormScreen
import com.example.mahasiswaapp.ui.screens.MahasiswaListScreen
import com.example.mahasiswaapp.ui.screens.ProfileScreen
import com.example.mahasiswaapp.ui.theme.MahasiswaAppTheme

object Routes {
    const val Home = "home"
    const val MahasiswaList = "mahasiswa_list"
    const val MahasiswaDetail = "mahasiswa_detail/{nim}"
    const val MahasiswaForm = "mahasiswa_form"
    const val MahasiswaEdit = "mahasiswa_edit/{nim}"
    const val DosenList = "dosen_list"
    const val DosenForm = "dosen_form"
    const val DosenEdit = "dosen_edit/{nidn}"
    const val Profile = "profile"

    fun mahasiswaDetailRoute(nim: String): String = "mahasiswa_detail/${Uri.encode(nim)}"
    fun mahasiswaEditRoute(nim: String): String = "mahasiswa_edit/${Uri.encode(nim)}"
    fun dosenEditRoute(nidn: String): String = "dosen_edit/${Uri.encode(nidn)}"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = false
        }
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        enableEdgeToEdge()
        setContent {
            MahasiswaAppTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Routes.Home,
                    modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
                ) {
                    composable(Routes.Home) {
                        HomeScreen(
                            onMahasiswaListClick = {
                                navController.navigate(Routes.MahasiswaList)
                            },
                            onDosenListClick = {
                                navController.navigate(Routes.DosenList)
                            },
                            onProfileClick = {
                                navController.navigate(Routes.Profile)
                            }
                        )
                    }
                    composable(Routes.MahasiswaList) { backStackEntry ->
                        val savedStateHandle = backStackEntry.savedStateHandle
                        val shouldRefreshFlow = remember {
                            savedStateHandle.getStateFlow("shouldRefreshMahasiswa", false)
                        }
                        val snackbarFlow = remember {
                            savedStateHandle.getStateFlow("mahasiswaSnackbar", "")
                        }
                        val shouldRefresh by shouldRefreshFlow.collectAsState()
                        val snackbarMessage by snackbarFlow.collectAsState()

                        MahasiswaListScreen(
                            onMahasiswaClick = { mahasiswa ->
                                mahasiswa.nim?.let { nim ->
                                    navController.navigate(Routes.mahasiswaDetailRoute(nim))
                                }
                            },
                            onNavigateBack = {
                                navController.popBackStack()
                            },
                            onAddMahasiswaClick = {
                                navController.navigate(Routes.MahasiswaForm)
                            },
                            shouldRefresh = shouldRefresh,
                            onRefreshConsumed = {
                                savedStateHandle["shouldRefreshMahasiswa"] = false
                            },
                            snackbarMessage = snackbarMessage.takeIf { it.isNotBlank() },
                            onSnackbarShown = {
                                savedStateHandle["mahasiswaSnackbar"] = ""
                            }
                        )
                    }
                    composable(
                        route = Routes.MahasiswaDetail,
                        arguments = listOf(navArgument("nim") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val savedStateHandle = backStackEntry.savedStateHandle
                        val shouldRefreshFlow = remember {
                            savedStateHandle.getStateFlow("shouldRefreshDetail", false)
                        }
                        val snackbarFlow = remember {
                            savedStateHandle.getStateFlow("mahasiswaDetailSnackbar", "")
                        }
                        val shouldRefresh by shouldRefreshFlow.collectAsState()
                        val snackbarMessage by snackbarFlow.collectAsState()

                        MahasiswaDetailScreen(
                            onNavigateBack = {
                                navController.popBackStack()
                            },
                            onEditMahasiswa = { nim ->
                                navController.navigate(Routes.mahasiswaEditRoute(nim))
                            },
                            onDeleteSuccess = {
                                navController.previousBackStackEntry?.savedStateHandle?.apply {
                                    set("shouldRefreshMahasiswa", true)
                                    set("mahasiswaSnackbar", "Mahasiswa berhasil dihapus")
                                }
                                navController.popBackStack()
                            },
                            shouldRefresh = shouldRefresh,
                            onRefreshConsumed = {
                                savedStateHandle["shouldRefreshDetail"] = false
                            },
                            snackbarMessage = snackbarMessage.takeIf { it.isNotBlank() },
                            onSnackbarShown = {
                                savedStateHandle["mahasiswaDetailSnackbar"] = ""
                            }
                        )
                    }
                    composable(Routes.MahasiswaForm) {
                        MahasiswaFormScreen(
                            onNavigateBack = { navController.popBackStack() },
                            onSuccess = {
                                navController.previousBackStackEntry?.savedStateHandle?.apply {
                                    set("shouldRefreshMahasiswa", true)
                                    set("mahasiswaSnackbar", "Mahasiswa berhasil ditambahkan")
                                }
                                navController.popBackStack()
                            }
                        )
                    }
                    composable(
                        route = Routes.MahasiswaEdit,
                        arguments = listOf(navArgument("nim") { type = NavType.StringType })
                    ) {
                        MahasiswaEditScreen(
                            onNavigateBack = { navController.popBackStack() },
                            onSuccess = {
                                navController.previousBackStackEntry?.savedStateHandle?.apply {
                                    set("shouldRefreshDetail", true)
                                    set("mahasiswaDetailSnackbar", "Data mahasiswa diperbarui")
                                }
                                runCatching {
                                    navController.getBackStackEntry(Routes.MahasiswaList)
                                }.getOrNull()?.savedStateHandle?.apply {
                                    set("shouldRefreshMahasiswa", true)
                                    set("mahasiswaSnackbar", "Mahasiswa berhasil diperbarui")
                                }
                                navController.popBackStack()
                            }
                        )
                    }
                    composable(Routes.DosenList) { backStackEntry ->
                        val savedStateHandle = backStackEntry.savedStateHandle
                        val shouldRefreshFlow = remember {
                            savedStateHandle.getStateFlow("shouldRefreshDosen", false)
                        }
                        val snackbarFlow = remember {
                            savedStateHandle.getStateFlow("dosenSnackbar", "")
                        }
                        val shouldRefresh by shouldRefreshFlow.collectAsState()
                        val snackbarMessage by snackbarFlow.collectAsState()

                        DosenListScreen(
                            onNavigateBack = { navController.popBackStack() },
                            onAddDosenClick = { navController.navigate(Routes.DosenForm) },
                            onEditDosen = { nidn ->
                                navController.navigate(Routes.dosenEditRoute(nidn))
                            },
                            shouldRefresh = shouldRefresh,
                            onRefreshConsumed = {
                                savedStateHandle["shouldRefreshDosen"] = false
                            },
                            snackbarMessage = snackbarMessage.takeIf { it.isNotBlank() },
                            onSnackbarShown = {
                                savedStateHandle["dosenSnackbar"] = ""
                            }
                        )
                    }
                    composable(Routes.DosenForm) {
                        DosenFormScreen(
                            onNavigateBack = { navController.popBackStack() },
                            onSuccess = {
                                navController.previousBackStackEntry?.savedStateHandle?.apply {
                                    set("shouldRefreshDosen", true)
                                    set("dosenSnackbar", "Dosen berhasil ditambahkan")
                                }
                                navController.popBackStack()
                            }
                        )
                    }
                    composable(
                        route = Routes.DosenEdit,
                        arguments = listOf(navArgument("nidn") { type = NavType.StringType })
                    ) {
                        DosenEditScreen(
                            onNavigateBack = { navController.popBackStack() },
                            onSuccess = {
                                navController.previousBackStackEntry?.savedStateHandle?.apply {
                                    set("shouldRefreshDosen", true)
                                    set("dosenSnackbar", "Dosen berhasil diperbarui")
                                }
                                navController.popBackStack()
                            }
                        )
                    }
                    composable(Routes.Profile) {
                        ProfileScreen(
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
