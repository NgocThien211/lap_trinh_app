package com.example.apputhtodo.data


import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

class AuthRepository(private val context: Context) {

    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    suspend fun signInWithGoogle() {
        val googleOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(
                context.getString(
                    com.example.apputhtodo.R.string.default_web_client_id
                )
            )
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleOption)
            .build()

        val result = CredentialManager.create(context)
            .getCredential(context, request)

        val credential = result.credential

        require(
            credential is CustomCredential &&
                    credential.type ==
                    GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            "Không nhận được Google ID token"
        }

        val idToken =
            GoogleIdTokenCredential.createFrom(credential.data).idToken

        auth.signInWithCredential(
            GoogleAuthProvider.getCredential(idToken, null)
        ).await()
    }

    suspend fun signInWithEmail(
        email: String,
        password: String
    ) {
        auth.signInWithEmailAndPassword(
            email.trim(),
            password
        ).await()
    }


    suspend fun registerWithEmail(
        email: String,
        password: String
    ) {
        auth.createUserWithEmailAndPassword(
            email.trim(),
            password
        ).await()
    }

    suspend fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(
            email.trim()
        ).await()
    }

    fun signOut() {
        auth.signOut()
    }
}
