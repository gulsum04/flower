package com.demir.starter.ui.dashboard.basket

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.demir.starter.R
import com.demir.starter.data.Basket
import com.demir.starter.ui.BaseFragment
import com.demir.starter.ui.dashboard.home.product.ProductViewHolder
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.Base64

class BasketFragment : BaseFragment() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var textTotalPrice: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_basket, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView)
        textTotalPrice = view.findViewById(R.id.textTotalPriceLabel)

        view.findViewById<MaterialToolbar>(R.id.toolbar).setOnMenuItemClickListener {
            if (it.itemId == R.id.emptyBasket) {
                MaterialAlertDialogBuilder(requireContext()).setMessage("Sepetiniz boşaltılacaktır. Emin misiniz?")
                    .setPositiveButton("Evet") { _, _ ->
                        Basket.clear()
                        listBasket()
                        bottomNavigationView.getOrCreateBadge(R.id.basket).clearNumber()
                    }
                    .setNegativeButton("Vazgeç") { _, _ -> }
                    .show()
            }
            true
        }

        listBasket()
    }

    @SuppressLint("SetTextI18n")
    public fun listBasket() {
        val products = Basket.addedProducts.toList()

        requireView().findViewById<View>(R.id.textViewEmpty).isVisible = products.isEmpty()

        val recyclerViewProducts =
            requireView().findViewById<RecyclerView>(R.id.recyclerViewProducts)
        recyclerViewProducts.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): RecyclerView.ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val itemView = layoutInflater.inflate(R.layout.item_product_basket, parent, false)
                return ProductViewHolder(itemView)
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                holder as ProductViewHolder

                val product = products[position]

                holder.itemView.findViewById<ImageView>(R.id.imageView).let {
                    val decodedString: ByteArray = Base64.getDecoder().decode(product.first.image)
                    val decodedByte =
                        BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                    it.setImageDrawable(
                        BitmapDrawable(decodedByte)
                    )
                }
                holder.itemView.findViewById<TextView>(R.id.textView).let {
                    it.text = product.first.label
                }

                holder.itemView.findViewById<TextView>(R.id.textPrice).let {
                    val priceText = "${product.first.price} TL"
                    it.text = priceText
                }

                holder.itemView.findViewById<TextView>(R.id.textViewBasketSize).let {
                    it.text = product.second.toString()
                }
                holder.itemView.findViewById<TextView>(R.id.textPrice).let {
                    val subtotalText = " ${product.first.price * product.second} TL"
                    it.text = subtotalText
                }

            }

            override fun getItemCount(): Int {
                return products.size
            }

        }

        textTotalPrice.text = "Toplam Tutar: ${Basket.totalAmount} TL"


    }
}