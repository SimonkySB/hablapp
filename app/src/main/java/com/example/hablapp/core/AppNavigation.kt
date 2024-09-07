package com.example.hablapp.core

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.hablapp.services.NotasService
import com.example.hablapp.services.UsuariosService
import com.example.hablapp.views.LoginView
import com.example.hablapp.views.NotaDetalleView
import com.example.hablapp.views.NotasView
import com.example.hablapp.views.RecuperarClaveView
import com.example.hablapp.views.RegistrarUsuarioView

@Composable
fun MyNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    snackController: SnackbarController,
    usuariosService: UsuariosService = viewModel(),
    notasService: NotasService = viewModel()
){
    val routerManager = RouterManager(navController);
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = NavScreen.Login.route
    ) {
        composable(NavScreen.Login.route) {
            LoginView(
                routerManager = routerManager,
                snackController = snackController,
                usuariosService = usuariosService
            )
        }
        composable(NavScreen.RegistrarUsuario.route){
            RegistrarUsuarioView(
                routerManager = routerManager,
                snackController = snackController,
                usuariosService = usuariosService
            )
        }
        composable(NavScreen.RecuperarClave.route) {
            RecuperarClaveView(
                routerManager = routerManager,
                snackController = snackController,
                usuariosService = usuariosService
            )
        }
        composable(NavScreen.Notas.route) {
            NotasView(
                routerManager = routerManager,
                snackController = snackController,
                usuariosService = usuariosService,
                notasService = notasService
            )
        }
        composable(
            route = NavScreen.NotasDetalle("{notaId}").route,
            arguments = listOf(navArgument("notaId") {type = NavType.StringType})
        ) { backStackEntry ->
            val notaId = backStackEntry.arguments?.getString("notaId")?.toIntOrNull()
            val nota = notaId?.let { id -> notasService.obtenerNotaPorId(id) }

            NotaDetalleView(
                routerManager = routerManager,
                snackController = snackController,
                usuariosService = usuariosService,
                notasService = notasService,
                nota = nota
            )
        }
    }
}

sealed class NavScreen(val route: String) {
    object Login: NavScreen("login")
    object RegistrarUsuario: NavScreen("registro")
    object RecuperarClave: NavScreen("recuperar-clave")
    object Notas: NavScreen("notas")
    class NotasDetalle(val notaId: String): NavScreen("notas-detalle/{notaId}".replace(oldValue = "{notaId}", newValue = notaId))
}

class RouterManager(navController: NavHostController) {
    private var _navController: NavHostController = navController

    fun onNavigateToRegistro() {
        _navController.navigate(NavScreen.RegistrarUsuario.route)
    }
    fun onNavigateToRecuperarClave() {
        _navController.navigate(NavScreen.RecuperarClave.route)
    }
    fun onNavigateToLogin() {
        _navController.navigate(NavScreen.Login.route)
    }
    fun onNavigateToNotas() {
        _navController.navigate(NavScreen.Notas.route)
    }
    fun onNavigateToNotaDetalle(notaId: Int) {
        _navController.navigate(NavScreen.NotasDetalle(notaId.toString()).route)
    }
}