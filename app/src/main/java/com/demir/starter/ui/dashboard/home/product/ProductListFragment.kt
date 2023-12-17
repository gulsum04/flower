package com.demir.starter.ui.dashboard.home.product

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.demir.starter.R
import com.demir.starter.data.Basket
import com.demir.starter.data.ProductCategory
import com.demir.starter.manager.FirestoreManager
import com.demir.starter.ui.BaseFragment
import com.demir.starter.ui.dashboard.DashboardActivity
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal
import java.util.Base64


@Parcelize
data class ProductListArgument(val productCategory: ProductCategory) : Parcelable

class ProductListFragment : BaseFragment() {
    private lateinit var productListArgument: ProductListArgument

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        productListArgument = requireArguments().getParcelable(ARG_PRODUCT_LIST)!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_product_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val products = FirestoreManager.products.filter {
            it.productCategory == productListArgument.productCategory
        }
        val recyclerViewProducts = view.findViewById<RecyclerView>(R.id.recyclerViewProducts)
        recyclerViewProducts.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): RecyclerView.ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val itemView = layoutInflater.inflate(R.layout.item_product, parent, false)
                return ProductViewHolder(itemView)
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                holder as ProductViewHolder

                val product = products[position]
                holder.itemView.findViewById<ImageView>(R.id.imageView).let {
                    holder.itemView.post {
                        val decodedString: ByteArray = Base64.getDecoder().decode(product.image)
                        val decodedByte =
                            BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                        it.setImageDrawable(
                            BitmapDrawable(decodedByte)
                        )
                    }
                }
                val textViewBasketSize =
                    holder.itemView.findViewById<TextView>(R.id.textViewBasketSize)
                holder.itemView.findViewById<TextView>(R.id.textView).let {
                    it.text = product.label
                }
                holder.itemView.findViewById<TextView>(R.id.Price).let {
                    val price = "${
                        BigDecimal.valueOf(product.price).apply {
                            setScale(2)
                        }
                    } TL"
                    it.text = price
                }
                holder.itemView.findViewById<View>(R.id.buttonViewAdd).setOnClickListener {
                    val activity = activity as DashboardActivity
                    activity.addToBasket(product)
                    textViewBasketSize.text = Basket.getProductSize(product).toString()
                }
                holder.itemView.findViewById<View>(R.id.buttonViewRemove).setOnClickListener {
                    val activity = activity as DashboardActivity
                    activity.removeFromBasket(product)
                    textViewBasketSize.text = Basket.getProductSize(product).toString()
                }
                textViewBasketSize.text = Basket.getProductSize(product).toString()
            }

            override fun getItemCount(): Int {
                return products.size
            }
        }
    }

    companion object {
        private const val ARG_PRODUCT_LIST = "product_list_argument"

        fun newInstance(productCategory: ProductCategory): ProductListFragment {
            return ProductListFragment().apply {
                arguments = bundleOf(ARG_PRODUCT_LIST to ProductListArgument(productCategory))
            }
        }
    }
}

class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)