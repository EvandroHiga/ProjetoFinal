package br.com.fiap.projetofinal.ui.listproduct

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import br.com.fiap.projetofinal.models.Product
import br.com.fiap.projetofinal.models.RequestState
import com.google.firebase.firestore.FirebaseFirestore

class ProductListViewModel (application: Application) : AndroidViewModel(application) {
    private val db = FirebaseFirestore.getInstance()
    val productState = MutableLiveData<RequestState<List<Product>>>()
    var productDeleteState = MutableLiveData<RequestState<Boolean>>()

    init {
        getProducts()
    }

    private fun getProducts(){
        productState.value = RequestState.Loading
        db.collection("products")
            .get()
            .addOnSuccessListener {
                it?.let { snapshot ->
                    val products = mutableListOf<Product>()
                    for(document in snapshot.documents){
                        var product = document.toObject(Product::class.java)
                        if (product != null) {
                            product.id = document.id
                            products.add(product)
                        }
                    }
                    productState.value = RequestState.Success(products)
                }
            }
            .addOnFailureListener{ it ->
                productState.value = RequestState.Error(Throwable(it.message))
            }
    }

    fun deleteById(id: String) {
        db.collection("products").document(id).delete()
            .addOnCompleteListener {
                getProducts()
                productDeleteState.value = RequestState.Success(true)
            }
            .addOnFailureListener { exception ->
                productDeleteState.value = RequestState.Error(exception)
            }
    }
}