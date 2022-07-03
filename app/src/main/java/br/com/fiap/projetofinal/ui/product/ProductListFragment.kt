package br.com.fiap.projetofinal.ui.product

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.fiap.projetofinal.databinding.FragmentProductListBinding
import br.com.fiap.projetofinal.models.RequestState
import br.com.fiap.projetofinal.ui.base.auth.BaseAuthFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ProductListFragment : BaseAuthFragment() {

    override val layout = br.com.fiap.projetofinal.R.layout.fragment_product_list
    private lateinit var binding: FragmentProductListBinding

    private lateinit var productListViewModel: ProductListViewModel
    private lateinit var adapter: ProductListAdapter

    private lateinit var fabNewProduct: FloatingActionButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProductListBinding.inflate(layoutInflater)
        setUpRecyclerView(view)
        initViewModel()
        initObservers()
    }

    private fun setUpRecyclerView(view: View) {
        adapter = ProductListAdapter { productListViewModel.delete(it) }

        binding.rvProducts.adapter = adapter
        binding.rvProducts.layoutManager = LinearLayoutManager(requireContext())

        fabNewProduct = view.findViewById(br.com.fiap.projetofinal.R.id.fabNewProduct)
        fabNewProduct.setOnClickListener{
            findNavController().navigate(br.com.fiap.projetofinal.R.id.newProductFragment)
        }

    }

    private fun initViewModel(){
        productListViewModel = ViewModelProvider(this).get(ProductListViewModel::class.java)
    }

    private fun initObservers() {
        productListViewModel.productState.observe(viewLifecycleOwner, Observer{
            when(it){
                is RequestState.Success -> {
                    it?.let {
                        adapter.setProducts(it.data)
                    }
                }
                is RequestState.Error -> {
                    Toast.makeText(requireContext(), it.throwable.message, Toast.LENGTH_SHORT).show()
                }
                is RequestState.Loading -> {

                }
            }
        })

        productListViewModel.mainState.observe(viewLifecycleOwner, Observer {
            when (it) {
                is RequestState.Success -> {
                    Toast.makeText(requireContext(), it.data, Toast.LENGTH_SHORT).show()
                }
                is RequestState.Error -> {
                    Toast.makeText(requireContext(), it.throwable.message, Toast.LENGTH_SHORT).show()
                }
                is RequestState.Loading -> {
                }
            }
        })
    }

}