package dev.jahidhasanco.diffly.navigation

sealed class Screen(val route: String) {
    object DiffChecker : Screen("diff_checker")
    object DiffViewer : Screen("diff_viewer")
}