package com.example.hablapp.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.hablapp.models.Nota
import com.example.hablapp.utils.AuthManager
import com.example.hablapp.utils.AuthManagerInterface
import com.example.hablapp.utils.NotasDBManager
import com.example.hablapp.views.HomeView
import com.example.hablapp.views.LoadingView
import com.example.hablapp.views.LoginView
import com.example.hablapp.views.NotaDetalleView
import com.example.hablapp.views.NotasView
import com.example.hablapp.views.RecuperarClaveView
import com.example.hablapp.views.RegistrarUsuarioView
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase


@Composable
fun MyNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    snackController: SnackbarController,
    authManager: AuthManagerInterface = AuthManager(Firebase.auth)
){
    val routerManager = RouterManager(navController);
    val user: FirebaseUser? = authManager.getCurrentUser()

    val notasDbManager = NotasDBManager(authManager, FirebaseDatabase.getInstance().reference)

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = if(user == null) NavScreen.Login.route else NavScreen.Home.route
    ) {
        composable(NavScreen.Login.route) {
            LoginView(
                routerManager = routerManager,
                snackController = snackController,
                authManager = authManager
            )
        }
        composable(NavScreen.RegistrarUsuario.route){
            RegistrarUsuarioView(
                routerManager = routerManager,
                snackController = snackController,
                authManager = authManager
            )
        }
        composable(NavScreen.RecuperarClave.route) {
            RecuperarClaveView(
                routerManager = routerManager,
                snackController = snackController,
                authManager = authManager
            )
        }
        composable(NavScreen.Home.route) {
            HomeView(
                routerManager = routerManager,
                authManager = authManager,
                snackbarController = snackController
            )
        }
        composable(NavScreen.Notas.route) {
            NotasView(
                routerManager = routerManager,
                notasDbManager = notasDbManager
            )
        }
        composable(
            route = NavScreen.NotasDetalle("{notaId}").route,
            arguments = listOf(navArgument("notaId") {type = NavType.StringType})
        ) { backStackEntry ->
            val notaId = backStackEntry.arguments?.getString("notaId")


            if(notaId == "0"){
                NotaDetalleView(
                    routerManager = routerManager,
                    notasDbManager = notasDbManager,
                    nota = null
                )
            }
            else {
                val (nota, setNota) = remember {
                    mutableStateOf<Nota?>(null)
                }

                val coroutineScope = rememberCoroutineScope()

                LaunchedEffect(notaId) {
                    notaId?.let {
                        setNota(notasDbManager.obtenerNotaPorId(notaId))
                    }
                }
                if(nota != null ){
                    NotaDetalleView(
                        routerManager = routerManager,
                        notasDbManager = notasDbManager,
                        nota = nota
                    )
                }
                else {
                    LoadingView()
                }


            }


        }
    }
}

sealed class NavScreen(val route: String) {
    object Login: NavScreen("login")
    object RegistrarUsuario: NavScreen("registro")
    object RecuperarClave: NavScreen("recuperar-clave")
    object Notas: NavScreen("notas")
    object Home: NavScreen("home")
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
    fun onNavigateToNotaDetalle(notaId: String) {
        _navController.navigate(NavScreen.NotasDetalle(notaId).route)
    }
    fun onNavigateToHome() {
        _navController.navigate(NavScreen.Home.route)
    }
}