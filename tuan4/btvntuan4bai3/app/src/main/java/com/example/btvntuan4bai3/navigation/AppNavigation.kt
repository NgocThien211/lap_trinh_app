package com.example.btvntuan4bai3.navigation


import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.btvntuan4bai3.Screen.*

private const val FORGOT = "forgot?email={email}&otp={otp}&pass={pass}"
private const val VERIFY = "verify/{email}"
private const val RESET = "reset/{email}/{otp}"
private const val CONFIRM = "confirm/{email}/{otp}/{pass}"


private fun enc(s: String) = Uri.encode(s)

@Composable
fun AppNavigation() {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = FORGOT) {


        composable(
            route = FORGOT,
            arguments = listOf("email", "otp", "pass").map {
                navArgument(it) { type = NavType.StringType; nullable = true; defaultValue = null }
            }
        ) { entry ->
            ForgotScreen(
                email = entry.arguments?.getString("email"),
                otp = entry.arguments?.getString("otp"),
                pass = entry.arguments?.getString("pass"),
                onNext = { email -> nav.navigate("verify/${enc(email)}") }   // 1 -> 2: gửi email
            )
        }


        composable(VERIFY) { entry ->
            val email = entry.arguments?.getString("email").orEmpty()        // nhận email
            VerifyScreen(
                email = email,
                onNext = { otp -> nav.navigate("reset/${enc(email)}/${enc(otp)}") }  // 2 -> 3: gửi email + otp
                , onBack = { nav.popBackStack() }
            )
        }


        composable(RESET) { entry ->
            val email = entry.arguments?.getString("email").orEmpty()
            val otp = entry.arguments?.getString("otp").orEmpty()
            ResetScreen(
                onNext = { pass ->
                    nav.navigate("confirm/${enc(email)}/${enc(otp)}/${enc(pass)}")  // 3 -> 4: gửi cả 3
                },
                onBack = { nav.popBackStack() }
            )
        }


        composable(CONFIRM) { entry ->
            val email = entry.arguments?.getString("email").orEmpty()
            val otp = entry.arguments?.getString("otp").orEmpty()
            val pass = entry.arguments?.getString("pass").orEmpty()
            ConfirmScreen(
                email = email, otp = otp, pass = pass,
                onSubmit = {
                    nav.navigate("forgot?email=${enc(email)}&otp=${enc(otp)}&pass=${enc(pass)}") {
                        popUpTo(FORGOT) { inclusive = true }
                    }
                },
                onBack = { nav.popBackStack() }
            )
        }
    }
}