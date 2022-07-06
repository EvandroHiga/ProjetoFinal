package br.com.fiap.projetofinal.ui.listproduct

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.fiap.projetofinal.R
import br.com.fiap.projetofinal.databinding.FragmentProductListBinding
import br.com.fiap.projetofinal.models.Product
import br.com.fiap.projetofinal.models.RequestState
import br.com.fiap.projetofinal.ui.base.auth.BaseAuthFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ProductListFragment : BaseAuthFragment() {

    override val layout = R.layout.fragment_product_list

    private lateinit var binding: FragmentProductListBinding

    private lateinit var productListViewModel: ProductListViewModel

    private lateinit var fabNewProduct: FloatingActionButton
    private lateinit var rvProductList : RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProductListBinding.inflate(layoutInflater)
        setUpView(view)
        initViewModel()
        initObservers()
    }

    @SuppressLint("ResourceType")
    private fun setUpView(view: View) {
        fabNewProduct = view.findViewById(R.id.fabNewProduct)
        rvProductList = view.findViewById(R.id.rvProducts)
        rvProductList.layoutManager = LinearLayoutManager(context)
        setUpListener()

    }

    private fun setUpListener() {
        fabNewProduct.setOnClickListener{
            findNavController().navigate(R.id.newProductFragment)
        }

    }

    private fun initViewModel(){
        productListViewModel = ViewModelProvider(this).get(ProductListViewModel::class.java)
    }

    private fun initObservers() {
        productListViewModel.productState.observe(viewLifecycleOwner, Observer{
            when(it){
                is RequestState.Success -> {
                    loadRecycleView(it.data)
                    hideLoading()
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

        productListViewModel.productDeleteState.observe(viewLifecycleOwner, Observer{
            when(it){
                is RequestState.Success -> {
                    showMessage(getString(R.string.message_delete_success))
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

    private fun editListener(productId: String){
        val bundle = Bundle()
        bundle.putString("productId", productId)
        findNavController().navigate(R.id.editProductFragment, bundle)
    }

    private fun loadRecycleView(products: List<Product>) {
        var productListAdapter = ProductListAdapter(products)
        rvProductList.adapter = productListAdapter

        productListAdapter.listenerDelete = { product ->
            product.id?.let { productListViewModel.deleteById(it) }
        }

        productListAdapter.listenerEdit = { product ->
            product.id?.let { editListener(it) }
        }
    }

}