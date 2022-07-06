package br.com.fiap.projetofinal.ui.listproduct

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.fiap.projetofinal.R
import br.com.fiap.projetofinal.models.Product

class ProductListAdapter (
    var products : List<Product> = listOf(),
    var listenerDelete: (Product) -> Unit = {},
    var listenerEdit: (Product) -> Unit = {}
) : RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {

    override fun onCreateViewHolder (parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder (holder: ViewHolder, position: Int) {
        holder.bindView(products[position])
    }

    override fun getItemCount (): Int {
        return products.size
    }

    inner class ViewHolder(itemView: View) :

        RecyclerView.ViewHolder(itemView) {

        private lateinit var product: Product
        var tvProduct : TextView = itemView.findViewById(R.id.tvProduct)
        var ivDelete : ImageView = itemView.findViewById(R.id.ivDelete)
        var ivEdit : ImageView = itemView.findViewById(R.id.ivEdit)

        init {
            ivDelete.setOnClickListener{
                if (::product.isInitialized) {
                    listenerDelete(product)
                }
            }

            ivEdit.setOnClickListener{
                if (::product.isInitialized) {
                    listenerEdit(product)
                }
            }

        }

        fun bindView(product: Product) {
            this.product = product
            tvProduct.text = product.name
        }

    }

}