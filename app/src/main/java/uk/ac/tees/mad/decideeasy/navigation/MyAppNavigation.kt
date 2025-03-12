package uk.ac.tees.mad.decideeasy.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.decideeasy.ui.screen.auth.AuthScreen
import uk.ac.tees.mad.decideeasy.ui.screen.home.HomeScreen
import uk.ac.tees.mad.decideeasy.ui.screen.splash.SplashScreen
import uk.ac.tees.mad.decideeasy.utils.Constants

@Composable
fun MyAppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Constants.SPLASH_SCREEN) {

        composable(Constants.SPLASH_SCREEN){
            SplashScreen(navController)
        }
        composable(Constants.AUTH_SCREEN) {
            AuthScreen(navController = navController)
        }
        composable(Constants.HOME_SCREEN) {
            HomeScreen()
        }
    }
}