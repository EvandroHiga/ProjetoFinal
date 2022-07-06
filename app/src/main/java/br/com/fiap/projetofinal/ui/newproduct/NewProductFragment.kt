package br.com.fiap.projetofinal.ui.newproduct

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import br.com.fiap.projetofinal.R
import br.com.fiap.projetofinal.models.Product
import br.com.fiap.projetofinal.ui.base.auth.BaseAuthFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import br.com.fiap.projetofinal.models.RequestState

class NewProductFragment : BaseAuthFragment() {
    override val layout = R.layout.fragment_new_product
    private lateinit var etProduct: EditText
    private lateinit var btSave: Button
    private val newProductViewModel: NewProductViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView(view)
        registerObserver()
    }

    private fun setUpView(view: View) {
        etProduct = view.findViewById(R.id.etProduct)
        btSave = view.findViewById(R.id.btSave)
        setUpListener()
    }

    private fun setUpListener() {
        btSave.setOnClickListener {
            val product = Product(null, etProduct.text.toString())
            newProductViewModel.saveProduct(product)
        }
    }


    private fun registerObserver() {
        newProductViewModel.newProductState.observe(viewLifecycleOwner, Observer {
            when (it) {
                is RequestState.Success -> {
                    hideLoading()
                    showMessage(getString(R.string.message_product_success_save))
                    findNavController().navigate(R.id.productListFragment)
                }
                is RequestState.Error -> {
                    hideLoading()
                    showMessage(it.throwable.message)
                }
                is RequestState.Loading -> {
                    showLoading(getString(R.string.loading_message_processing))
                }
            }
        })
    }

}