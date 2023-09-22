package coding.faizal.mytasklist.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coding.faizal.mytasklist.api.model.PaymentMethodResponse
import coding.faizal.mytasklist.databinding.PaymentItemBinding
import com.bumptech.glide.Glide

class PaymentAdapter(
    private val datas : List<PaymentMethodResponse>,
    private val listener : OnClick
) : RecyclerView.Adapter<PaymentAdapter.PaymentAdapterViewHolder>() {


    inner class PaymentAdapterViewHolder(val binding : PaymentItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentAdapterViewHolder {
        return PaymentAdapterViewHolder(PaymentItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: PaymentAdapterViewHolder, position: Int) {
        holder.binding.apply {
            Glide.with(root).load(datas[position].paymentImage).into(imagePayment)
            paymentMethod.text = datas[position].paymentName

            card.setOnClickListener {
                listener.onClick(datas[position])
            }

        }
    }

    override fun getItemCount(): Int = datas.size

    interface OnClick{

        fun onClick(data : PaymentMethodResponse)
    }
}