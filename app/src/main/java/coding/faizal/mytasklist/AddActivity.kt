package coding.faizal.mytasklist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.lifecycle.ViewModelProvider
import coding.faizal.mytasklist.databinding.ActivityAddBinding
import coding.faizal.mytasklist.viewmodel.TransactionViewModelFactory
import coding.faizal.mytasklist.viewmodel.TransactionsViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AddActivity : AppCompatActivity(),AdapterView.OnItemClickListener {

    private var _binding : ActivityAddBinding? = null
    private val binding get() = _binding!!

    private lateinit var transactionsViewModel : TransactionsViewModel

    private var resultDropDownProduct = 1
    private var resultDropDownUser = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddBinding.inflate(layoutInflater)
        val viewModelProvider = TransactionViewModelFactory(this)
        transactionsViewModel = ViewModelProvider(this,viewModelProvider)[TransactionsViewModel::class.java]
        setContentView(binding.root)

        showData()
        insertDataTransactions()
    }

    private fun insertDataTransactions(){
        binding.btnSubmit.setOnClickListener {

            try {
                transactionsViewModel.addTransaction(
                    binding.etQuantity.text.toString().toInt(),
                    resultDropDownProduct,
                    resultDropDownUser,
                    getDateNow()
                )

                startActivity(Intent(this,MainActivity::class.java).also{
                    finish()
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                })

            }catch (e : Exception){
                Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun showData(){

        transactionsViewModel.getAllUser.observe(this){ user ->
            Toast.makeText(this, "$user", Toast.LENGTH_SHORT).show()
            val listUser = mutableListOf<String>()
            user.forEach {
               listUser.add(it.name_user)
            }

            dropDownMenu(listUser.toTypedArray(),R.layout.dropdown_item,binding.tvUser)
        }

        transactionsViewModel.getAllProduct.observe(this){ product ->

            Toast.makeText(this, "$product", Toast.LENGTH_SHORT).show()
            val listProduct = mutableListOf<String>()
            product.forEach {
                listProduct.add(it.product)
            }
            dropDownMenu(listProduct.toTypedArray(),R.layout.dropdown_item,binding.tvProduct)
        }
    }

    private fun dropDownMenu(arr : Array<String>, @LayoutRes layout : Int, tv : AutoCompleteTextView){
        val adapter = ArrayAdapter(this, layout,arr)
        with(tv){
            setAdapter(adapter)
            onItemClickListener = this@AddActivity
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, p1: View?, position: Int, p3: Long) {

        if (binding.tvUser.text.toString() == "Leonardo" || binding.tvUser.text.toString() == "Da Vinci") {

            transactionsViewModel.getAllUser.observe(this){
                resultDropDownUser = it?.get(position)?.id_user!!.toInt()
                Toast.makeText(this, "$resultDropDownUser", Toast.LENGTH_SHORT).show()
            }

        } else  {

            transactionsViewModel.getAllProduct.observe(this) { productList ->
                productList?.get(position)?.id_product?.toInt()?.let { productId ->
                    resultDropDownProduct = productId
                    Toast.makeText(this, "$productId", Toast.LENGTH_SHORT).show()
                }
            }

        }

    }

    private fun getDateNow(): String{
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
        return currentDate.format(formatter)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}