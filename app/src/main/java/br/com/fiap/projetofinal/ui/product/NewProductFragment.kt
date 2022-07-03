package br.com.fiap.projetofinal.ui.product

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import br.com.fiap.projetofinal.R
import br.com.fiap.projetofinal.models.Product
import br.com.fiap.projetofinal.ui.base.auth.BaseAuthFragment
import com.google.firebase.firestore.FirebaseFirestore

class NewProductFragment : BaseAuthFragment() {
    override val layout = R.layout.fragment_new_product
    private lateinit var etProduct: EditText
    private lateinit var btSave: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView(view)
    }

    private fun setUpView(view: View) {
        etProduct = view.findViewById(R.id.etProduct)
        btSave = view.findViewById(R.id.btSave)

        setUpListener()
    }

    private fun setUpListener() {
        btSave.setOnClickListener {
            val product = Product(
                etProduct.text.toString()
            )

            insertProduct(product)
        }
    }

    fun insertProduct(product: Product) {
        saveProductInFireStore(product)
    }

    private fun saveProductInFireStore(product: Product){
        val db = FirebaseFirestore.getInstance()
        db.collection("products").add(product)
        Toast.makeText(requireContext(), "Produto adicionado com sucesso", Toast.LENGTH_SHORT).show()
    }

}