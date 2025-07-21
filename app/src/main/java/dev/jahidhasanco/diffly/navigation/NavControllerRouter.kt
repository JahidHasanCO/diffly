package dev.jahidhasanco.diffly.navigation

import androidx.navigation.NavController

class NavControllerRouter(private val navController: NavController) : NavigationRouter {

    override fun navigateToDiffChecker() {
        navController.navigate(Screen.DiffChecker.route)
    }

    override fun navigateToDiffViewer() {
        navController.navigate(Screen.DiffViewer.route)
    }

    override fun goBack() {
        navController.popBackStack()
    }
}