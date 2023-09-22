package coding.faizal.mytasklist.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import coding.faizal.mytasklist.room.entity.ProductEntity
import coding.faizal.mytasklist.room.entity.TransactionEntity
import coding.faizal.mytasklist.room.entity.Transactions
import coding.faizal.mytasklist.room.entity.UserEntity

@Dao
interface TransactionDao {

    @Query("SELECT * FROM UserEntity")
    fun getAllUser() : LiveData<List<UserEntity>>


    @Query("SELECT * FROM ProductEntity")
    fun getAllProduct(): LiveData<List<ProductEntity>>

    @Query("SELECT TransactionEntity.id_transaction, ProductEntity.name_product, TransactionEntity.quantity, ProductEntity.price FROM TransactionEntity "+
            "LEFT JOIN ProductEntity ON TransactionEntity.id_product = ProductEntity.id_product")
    fun getAllTransaction(): LiveData<List<Transactions>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDataProduct(product : ProductEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDataUser(user : UserEntity)

    @Insert
    suspend fun insertDataTransaction(transaction : TransactionEntity)



}