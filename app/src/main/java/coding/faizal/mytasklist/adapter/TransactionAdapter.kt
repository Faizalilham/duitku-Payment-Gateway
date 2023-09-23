package coding.faizal.mytasklist.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import coding.faizal.mytasklist.databinding.TransactionItemBinding
import coding.faizal.mytasklist.room.entity.Transactions
import com.paypal.checkout.approve.OnApprove
import com.paypal.checkout.cancel.OnCancel
import com.paypal.checkout.createorder.CreateOrder
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.OrderIntent
import com.paypal.checkout.createorder.UserAction
import com.paypal.checkout.error.OnError
import com.paypal.checkout.order.Amount
import com.paypal.checkout.order.AppContext
import com.paypal.checkout.order.OrderRequest
import com.paypal.checkout.order.PurchaseUnit
import com.paypal.checkout.paymentbutton.PaymentButtonContainer

class TransactionAdapter(
    private val data : MutableList<Transactions>,
    private val listener : OnClick,
    private val context : Context
) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {


    inner class TransactionViewHolder(val binding : TransactionItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
       return TransactionViewHolder(TransactionItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.binding.apply {
            val subT = "Harga : ${data[position].price}"
            val q = "Jumlah item : ${data[position].quantity}"

            nameProduct.text = data[position].name_product
            price.text = subT
            quantity.text = q

            card.setOnClickListener {
                listener.onClick(data[position])
            }

            val value = ((data[position].quantity * data[position].price) / 15000).toDouble()

            paymentButtonContainer.setup(
                createOrder =
                CreateOrder { createOrderActions ->
                    if(value >= 1.0){
                        val order =
                            OrderRequest(
                                intent = OrderIntent.CAPTURE,
                                appContext = AppContext(userAction = UserAction.PAY_NOW),
                                purchaseUnitList =
                                listOf(
                                    PurchaseUnit(
                                        amount =
                                        Amount(currencyCode = CurrencyCode.USD, value = value.toString())
                                    )
                                )
                            )
                        createOrderActions.create(order)
                    }else{
                        Toast.makeText(context, "Minimal transaksi dengan paypal sebesar  $1", Toast.LENGTH_SHORT).show()

                    }
                },
                onApprove =
                OnApprove { approval ->
                    approval.orderActions.capture { captureOrderResult ->
                        Log.d("TAG", "CaptureOrderResult: $captureOrderResult")
                        Toast.makeText(context, "Pembayaran Berhasil", Toast.LENGTH_SHORT).show()
                    }
                },
                onCancel = OnCancel {
                    Log.d("TAG", "Buyer Cancelled This Purchase")
                    Toast.makeText(context, "Pembayaran Dibatalkan", Toast.LENGTH_SHORT).show()
                },
                onError = OnError { errorInfo ->
                    Log.d("TAG", "Error: $errorInfo")
                    Toast.makeText(context, "Pembayaran Bermasalah ${errorInfo.error.message}", Toast.LENGTH_SHORT).show()
                }
            )

        }
    }

    override fun getItemCount(): Int  = data.size

    fun onRefresh(datas : MutableList<Transactions>){
        data.clear()
        data.addAll(datas)
        notifyDataSetChanged()
    }

    interface OnClick{
        fun onClick(data : Transactions)

    }

}