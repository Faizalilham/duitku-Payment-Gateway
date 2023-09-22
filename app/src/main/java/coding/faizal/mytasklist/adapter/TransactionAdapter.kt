package coding.faizal.mytasklist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coding.faizal.mytasklist.databinding.TransactionItemBinding
import coding.faizal.mytasklist.room.entity.Transactions

class TransactionAdapter(
    private val data : MutableList<Transactions>,
    private val listener : OnClick
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