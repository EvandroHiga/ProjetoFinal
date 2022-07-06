package br.com.fiap.projetofinal.ui.newproduct

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import br.com.fiap.projetofinal.models.Product
import br.com.fiap.projetofinal.models.RequestState
import com.google.firebase.firestore.FirebaseFirestore

class NewProductViewModel (application: Application) : AndroidViewModel(application) {

    private val db = FirebaseFirestore.getInstance()
    val newProductState = MutableLiveData<RequestState<Product>>()

    fun saveProduct(product: Product){
        newProductState.value = RequestState.Loading
        db.collection("products")
            .add(product)
            .addOnSuccessListener {
                newProductState.value = RequestState.Success(product)
            }
            .addOnFailureListener{ it ->
                newProductState.value = RequestState.Error(Throwable(it.message))
            }
    }
}