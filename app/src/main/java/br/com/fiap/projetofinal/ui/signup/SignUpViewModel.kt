package br.com.fiap.projetofinal.ui.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.fiap.projetofinal.models.User
import br.com.fiap.projetofinal.models.RequestState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class SignUpViewModel : ViewModel() {
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    val signUpState = MutableLiveData<RequestState<FirebaseUser>>()

    fun signUp(user: User) {
        signUpState.value = RequestState.Loading
        if (validateFields(user)) {
            mAuth.createUserWithEmailAndPassword(
                user.email ?: "",
                user.password ?: ""
            )
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        saveInFirestore(user)
                    } else {
                        signUpState.value = RequestState.Error(
                            Throwable(
                                task.exception?.message ?: "Não foi possível realizar a requisição"
                            )
                        )
                    }
                }
        }
    }

    private fun saveInFirestore(user: User) {
        db.collection("users")
            .add(user)
            .addOnSuccessListener {
                sendEmailVerification()
            } .
            addOnFailureListener { e ->
                signUpState.value = RequestState.Error(Throwable(e.message))
            }
    }

    private fun sendEmailVerification() {
        mAuth.currentUser?.sendEmailVerification()
            ?.addOnCompleteListener {
                signUpState.value = RequestState.Success(mAuth.currentUser!!)
            }
    }

    private fun validateFields(user: User): Boolean {
        if (user.email?.isNotEmpty() == false) {
            signUpState.value = RequestState.Error(Throwable("E-mail inválido"))
            return false
        }
        if (user.password?.isEmpty() == true) {
            signUpState.value = RequestState.Error(Throwable("Informe uma senha"))
            return false
        }
        if (user.password?.length ?: 0 < 6) {
            signUpState.value = RequestState.Error(Throwable("Senha com no mínimo 6 caracteres"))
            return false
        }
        return true
    }

}