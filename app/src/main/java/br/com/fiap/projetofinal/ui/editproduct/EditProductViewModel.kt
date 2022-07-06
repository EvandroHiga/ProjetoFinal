package br.com.fiap.projetofinal.ui.editproduct


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import br.com.fiap.projetofinal.models.Product
import br.com.fiap.projetofinal.models.RequestState
import com.google.firebase.firestore.FirebaseFirestore

class EditProductViewModel (application: Application) : AndroidViewModel(application) {

    private val db = FirebaseFirestore.getInstance()
    val newProductState = MutableLiveData<RequestState<Product>>()
    val productState = MutableLiveData<RequestState<Product>>()

    fun editProduct(product: Product){
        db.collection("products")
            .document(product.id ?: "")
            .set(product)
            .addOnSuccessListener {
                newProductState.value = RequestState.Success(product)
            }
            .addOnFailureListener { e ->
                newProductState.value = RequestState.Error(Throwable(e.message))
            }
    }

    fun findById(productId: String) {
        db.collection("products")
            .document(productId)
            .get()
            .addOnSuccessListener { documentReference ->
                var product = Product(
                    id = documentReference.id,
                    name = documentReference["name"] as String,
                )
                productState.value = RequestState.Success(product)
            }
            .addOnFailureListener { it ->
                productState.value = RequestState.Error(Throwable(it.message))
            }
    }
}