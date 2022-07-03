package br.com.fiap.projetofinal.ui.product

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.fiap.projetofinal.R
import br.com.fiap.projetofinal.databinding.ActivityMainBinding
import br.com.fiap.projetofinal.databinding.FragmentProductListBinding
import br.com.fiap.projetofinal.models.RequestState
import br.com.fiap.projetofinal.ui.base.auth.BaseAuthFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ProductListFragment : BaseAuthFragment() {

    override val layout = R.layout.fragment_product_list
    private lateinit var binding: FragmentProductListBinding

    private lateinit var productListViewModel: ProductListViewModel
    private lateinit var adapter: MainListAdapter

    private lateinit var fabNewProduct: FloatingActionButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProductListBinding.inflate(layoutInflater)
        setUpRecyclerView(view)
        initViewModel()
        initObservers()


    }

    private fun setUpRecyclerView(view: View) {
        adapter = MainListAdapter { productListViewModel.delete(it) }

        binding.rvProducts.adapter = adapter
        binding.rvProducts.layoutManager = LinearLayoutManager(requireContext())

        fabNewProduct = view.findViewById(R.id.fabNewProduct)
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

//    private lateinit var binding: ActivityMainBinding
//    private lateinit var mainViewModel: MainViewModel
//    private lateinit var adapter: MainListAdapter
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        setUpRecyclerView()
//        initViewModel()
//        initObserver()
//        initListeners()
//    }
//    private fun initListeners() {
//        binding.fabNewProduct.setOnClickListener {
//            val nextScreen = Intent(this, NewProductActivity::class.java)
//            newProductRequest.launch(nextScreen)
//        }
//    }
//    private val newProductRequest =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
//        {
//            if (it.resultCode == RESULT_OK) {
//                it.data?.getStringExtra(NewProductActivity.EXTRA_REPLY)?.let {
//                    val product = Product(it)
//                    mainViewModel.insert(product)
//                }
//            }
//        }
//    private fun initViewModel() {
//        mainViewModel =
//            ViewModelProvider(this).get(MainViewModel::class.java)
//    }
//    private fun initObserver() {
//        mainViewModel.products.observe(this, Observer { produtos ->
//            produtos?.let { adapter.setProducts(it) }
//        })
//    }
//    private fun setUpRecyclerView() {
//        adapter = MainListAdapter()
//        binding.rvProducts.adapter = adapter
//        binding.rvProducts.layoutManager = LinearLayoutManager(this)
//    }
//}

}