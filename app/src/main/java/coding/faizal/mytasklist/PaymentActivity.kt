package coding.faizal.mytasklist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import coding.faizal.mytasklist.adapter.PaymentAdapter
import coding.faizal.mytasklist.api.model.*
import coding.faizal.mytasklist.databinding.ActivityPaymentBinding
import coding.faizal.mytasklist.viewmodel.PaymentViewModel
import coding.faizal.mytasklist.viewmodel.PaymentViewModelFactory

class PaymentActivity : AppCompatActivity() {

    private var _binding : ActivityPaymentBinding? = null
    private val binding get() = _binding!!

    private var inputSignature = ""
    private var inputMd5 = ""
    private var nameProduct = ""
    private var amount = 0
    private var id = 0

    private lateinit var paymentViewModel : PaymentViewModel
    private lateinit var paymentAdapter: PaymentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelFactory = PaymentViewModelFactory()
        paymentViewModel = ViewModelProvider(this,viewModelFactory)[PaymentViewModel::class.java]
        _binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)


        amount = intent.getIntExtra("amount",0)
        nameProduct = intent.getStringExtra("name").toString()
        id = intent.getIntExtra("id",0)

        Toast.makeText(this, "$amount", Toast.LENGTH_SHORT).show()
        if(amount > 10000 && id != 0){
            binding.tvTotal.text = amount.toString()
            inputSignature = "DS16782"+"$amount"+ currentDateTime() +"a210a442fdab0e7b39e93d4da6d1f4c0"
            getAllPayment(amount.toString(),inputSignature)
        }else{
            Toast.makeText(this, "Pembayaran harus lebih dari 10000", Toast.LENGTH_SHORT).show()
        }

    }


    private fun getAllPayment(amount : String,signature : String){
        paymentViewModel.sendPaymentMethodRequest(
            PaymentMethodRequest(
                amont = amount,
                signature = createSignature(signature)
            )
        )

        paymentViewModel.resultLiveDataPaymentMethod.observe(this){
            Toast.makeText(this, "$it", Toast.LENGTH_SHORT).show()
            setupRecycler(it.paymentFee)
        }
    }

    private fun setupRecycler(data : List<PaymentMethodResponse>){
        paymentAdapter = PaymentAdapter(data,object : PaymentAdapter.OnClick{
            override fun onClick(data: PaymentMethodResponse) {

                inputMd5 = "DS16782"+"$id"+"$amount"+"a210a442fdab0e7b39e93d4da6d1f4c0"
                Log.d("TAGS","${data.paymentMethod} ${amount}")

                try {
                    doPayment(data)
                }catch (e : Exception){
                    Toast.makeText(this@PaymentActivity, "Barang telah dibayar ${e.message}", Toast.LENGTH_SHORT).show()
                }

            }
        })

        binding.tvTransactions.apply {
            adapter = paymentAdapter
            layoutManager = GridLayoutManager(this@PaymentActivity,2)
        }
    }

    private fun doPayment(data: PaymentMethodResponse){

        paymentViewModel.sendPaymentRequest(PaymentRequest(
            productDetails = nameProduct,
            paymentMethod = data.paymentMethod,
            merchantOrderId = id.toString(),
            paymentAmount = amount.toString(),
            signature = md5(inputMd5)
        ))

        paymentViewModel.resultLiveDataTransactions.observe(this@PaymentActivity){ res ->
            startActivity(Intent(this@PaymentActivity,TransactionActivity::class.java).also{
                it.putExtra("url",res.paymentUrl)
            })
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}