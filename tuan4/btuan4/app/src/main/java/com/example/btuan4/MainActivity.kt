package com.example.btuan4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.btuan4.model.AppRepository
import com.example.btuan4.screen.HomeScreen
import com.example.btuan4.screen.OnboardingScreen
import com.example.btuan4.screen.Screen
import com.example.btuan4.screen.SplashScreen
import com.example.btuan4.ui.theme.Btuan4Theme

class MainActivity : ComponentActivity() {
    private val repository = AppRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Btuan4Theme {
                val nav = rememberNavController()

                NavHost(navController = nav, startDestination = Screen.Splash.route) {
                    composable(Screen.Splash.route) {
                        SplashScreen(repository.getAppInfo()) {
                            nav.navigate(Screen.Onboarding.route) {
                                popUpTo(Screen.Splash.route) { inclusive = true }
                            }
                        }
                    }
                    composable(Screen.Onboarding.route) {
                        OnboardingScreen(repository.getOnboardingItems()) {
                            nav.navigate(Screen.Home.route) {
                                popUpTo(Screen.Onboarding.route) { inclusive = true }
                            }
                        }
                    }
                    composable(Screen.Home.route) { HomeScreen() }
                }
            }
        }
    }
}