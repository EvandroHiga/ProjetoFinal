package br.com.fiap.projetofinal.ui.product

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import br.com.fiap.projetofinal.models.Product
import br.com.fiap.projetofinal.models.RequestState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects

class ProductListViewModel (application: Application) : AndroidViewModel(application) {
    private val db = FirebaseFirestore.getInstance()
    val mainState = MutableLiveData<RequestState<String>>()
    val productState = MutableLiveData<RequestState<List<Product>>>()

    init {
        getProductsInFireStore()
    }

    private fun getProductsInFireStore(){
        db.collection("products")
            .get()
            .addOnSuccessListener { documentReference ->
                val product = documentReference.toObjects<Product>()
                productState.value = RequestState.Success(product)
            }
            .addOnFailureListener{ it ->
                productState.value = RequestState.Error(Throwable(it.message))
            }
    }

    fun delete(product: Product){

    }
}