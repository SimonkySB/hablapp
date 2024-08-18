package com.example.hablapp.core

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hablapp.services.UsuariosService
import com.example.hablapp.views.LoginView
import com.example.hablapp.views.RecuperarClaveView
import com.example.hablapp.views.RegistrarUsuarioView

@Composable
fun MyNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    snackController: SnackbarController,
    usuariosService: UsuariosService = viewModel()
){
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = NavScreen.Login.route
    ) {
        composable(NavScreen.Login.route) {
            LoginView(
                onNavigateToRegistro = { navController.navigate(NavScreen.RegistrarUsuario.route) },
                onNavigateToRecuperarClave = {navController.navigate(NavScreen.RecuperarClave.route)},
                snackController = snackController,
                usuariosService = usuariosService
            )
        }
        composable(NavScreen.RegistrarUsuario.route){
            RegistrarUsuarioView(
                onNavigateToLogin = { navController.navigate(NavScreen.Login.route) },
                snackController = snackController,
                usuariosService = usuariosService
            )
        }
        composable(NavScreen.RecuperarClave.route) {
            RecuperarClaveView(
                onNavigateToLogin = { navController.navigate(NavScreen.Login.route) },
                snackController = snackController,
                usuariosService = usuariosService
            )
        }
    }
}

sealed class NavScreen(val route: String) {
    object Login: NavScreen("login")
    object RegistrarUsuario: NavScreen("registro")
    object RecuperarClave: NavScreen("recuperar-clave")
}