package coding.faizal.mytasklist

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import coding.faizal.mytasklist.adapter.TransactionAdapter
import coding.faizal.mytasklist.databinding.ActivityMainBinding
import coding.faizal.mytasklist.room.entity.Transactions
import coding.faizal.mytasklist.viewmodel.PaymentViewModel
import coding.faizal.mytasklist.viewmodel.PaymentViewModelFactory
import coding.faizal.mytasklist.viewmodel.TransactionViewModelFactory
import coding.faizal.mytasklist.viewmodel.TransactionsViewModel
import com.duitku.sdk.DuitkuCallback.DuitkuCallbackTransaction
import com.duitku.sdk.DuitkuClient
import com.duitku.sdk.DuitkuUtility.BaseKitDuitku
import com.duitku.sdk.DuitkuUtility.DuitkuKit
import com.duitku.sdk.Model.ItemDetails

class MainActivity : DuitkuClient() {

    private var _binding : ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val TAG = "TAG"

    private lateinit var transactionsViewModel : TransactionsViewModel
    private lateinit var paymentViewModel: PaymentViewModel
    private lateinit var transactionAdapter : TransactionAdapter

    private lateinit var duitku: DuitkuKit
    private lateinit var callbackKit: DuitkuCallbackTransaction


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)

        val viewModelFactory = TransactionViewModelFactory(this)
        val viewModelFactory2 = PaymentViewModelFactory()

        transactionsViewModel = ViewModelProvider(this,viewModelFactory)[TransactionsViewModel::class.java]
        paymentViewModel = ViewModelProvider(this,viewModelFactory2)[PaymentViewModel::class.java]
        setContentView(binding.root)

        duitku = DuitkuKit()
        callbackKit = DuitkuCallbackTransaction()

        transactionsViewModel.getAllTransactions.observe(this){
            if(it != null){
                setupRecycler(it.toMutableList())
                Toast.makeText(this, "$it", Toast.LENGTH_SHORT).show()

               var total = 0

               it.forEach { tr ->
                   total += tr.price * tr.quantity
               }

                val totalT = "Total transaksi :  $total"
                binding.tvTotal.text = totalT
            }
        }

        binding.btnSubmit.setOnClickListener {
            startActivity(Intent(this,AddActivity::class.java))
        }

    }

    private fun setupRecycler(data : MutableList<Transactions>){
        transactionAdapter = TransactionAdapter(data,object : TransactionAdapter.OnClick{
            override fun onClick(data: Transactions) {

                startActivity(Intent(this@MainActivity,PaymentActivity::class.java).also{
                    it.putExtra("amount",data.quantity * data.price)
                    it.putExtra("name",data.name_product)
                    it.putExtra("id",data.id_transaction)
                })

//                Jika ingin menggunakan android SDK
//                settingMerchant(data.name_product,data.quantity,data.price)
//                startPayment(this@MainActivity)

            }



        },this)

        binding.tvTransactions.apply {
            adapter = transactionAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    // Dipakai Jika menggunakan library duitku
    override fun onSuccessTransaction(status: String, reference: String, amount: String, Code: String) {
        Toast.makeText(this, "Transaction$status", Toast.LENGTH_LONG).show()

        clearSdkTask()
        super.onSuccessTransaction(status, reference, amount, Code)
    }

    override fun onPendingTransaction(status: String, reference: String, amount: String, Code: String) {
        Toast.makeText(this, "Transaction$status", Toast.LENGTH_LONG).show()

        clearSdkTask()
        super.onPendingTransaction(status, reference, amount, Code)
    }

    override fun onCancelTransaction(status: String, reference: String, amount: String, Code: String) {
        Toast.makeText(this, "Transaction :$status", Toast.LENGTH_LONG).show()

        clearSdkTask()
        super.onCancelTransaction(status, reference, amount, Code)
    }


    // Dipakai Jika menggunakan library duitku
    private fun settingMerchant(name :String,quantity : Int,price : Int) {

        callbackKit.isCallbackFromMerchant = false

        run(this)
        BaseKitDuitku.setBaseUrlApiDuitku("https://merchantsite.com/")
        BaseKitDuitku.setUrlRequestTransaction("requestTransaction.php")
        BaseKitDuitku.setUrlCheckTransaction("checkTransaction.php")
        BaseKitDuitku.setUrlListPayment("listPayment.php")

        Toast.makeText(this@MainActivity, "$name", Toast.LENGTH_SHORT).show()

        duitku.paymentAmount = binding.tvTotal.text.toString().toInt()
        duitku.productDetails = name
        duitku.email = "faizalfalakh19@gmail.com"
        duitku.phoneNumber = "0895421971694"
        duitku.additionalParam = "" //optional
        duitku.merchantUserInfo = "" //optional
        duitku.customerVaName = "Faizal"
        duitku.expiryPeriod = "10"
        duitku.callbackUrl = "http://merchantsite.com/callbackUrl"
        duitku.returnUrl = "http://merchantsite.com/returnUrl"

        //customer detail
        duitku.firstName = "John"
        duitku.lastName = "Doe"

        //address
        duitku.address = "Jl. Kembangan Raya"
        duitku.city = "Jakarta"
        duitku.postalCode = "11530"
        duitku.countryCode = "ID"

        //set item details
        val itemDetails = ItemDetails(price, quantity, name)
        val arrayList = ArrayList<ItemDetails>()
        arrayList.add(itemDetails)
        duitku.itemDetails = arrayList
    }

    override fun onResume() {
        super.onResume()
        run(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.root
    }
}