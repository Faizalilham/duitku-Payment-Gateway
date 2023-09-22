package coding.faizal.mytasklist.viewmodel

import android.content.Context
import androidx.lifecycle.*
import coding.faizal.mytasklist.room.RoomDB
import coding.faizal.mytasklist.room.entity.ProductEntity
import coding.faizal.mytasklist.room.entity.TransactionEntity
import coding.faizal.mytasklist.room.entity.Transactions
import coding.faizal.mytasklist.room.entity.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TransactionsViewModel(context : Context):ViewModel() {


    lateinit var getAllTransactions : LiveData<List<Transactions>>


    lateinit var getAllUser : LiveData<List<UserEntity>>
    lateinit var getAllProduct : LiveData<List<ProductEntity>>


    private val database = RoomDB.buildDatabase(context)
    private val dao = database.dao()


    init{

        getAllTransactions = dao.getAllTransaction()
        getAllProduct = dao.getAllProduct()
        getAllUser = dao.getAllUser()

    }


    fun addTransaction(price : Int,product : Int,user : Int,date : String){
        viewModelScope.launch(Dispatchers.IO) {
            dao.insertDataTransaction(
                TransactionEntity(
                    0,product,user,price,date
                )
            )
        }
    }


}

class TransactionViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionsViewModel::class.java)) {
            return TransactionsViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}