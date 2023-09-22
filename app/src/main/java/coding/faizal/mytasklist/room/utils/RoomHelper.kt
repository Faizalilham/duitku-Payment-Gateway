package coding.faizal.mytasklist.room.utils

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import coding.faizal.mytasklist.room.RoomDB
import coding.faizal.mytasklist.room.entity.ProductEntity
import coding.faizal.mytasklist.room.entity.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RoomHelper(private val context: Context) : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            val database = Room.databaseBuilder(context, RoomDB::class.java, "Shoppp").build()
            val userDao = database.dao()

            val user1 = UserEntity(0, "Leonardo")
            val user2 = UserEntity(0, "Da Vinci")


            val status1 = ProductEntity(0, "Indomie Jumbo",5000)
            val status2 = ProductEntity(0, "Supermie",3500)
            val status3 = ProductEntity(0, "Mie gaga",4000)
            val status4 = ProductEntity(0, "Minyak Goreng",4000)
            val status5 = ProductEntity(0, "Telor",4000)


            userDao.insertDataUser(user1)
            userDao.insertDataUser(user2)

            userDao.insertDataProduct(status1)
            userDao.insertDataProduct(status2)
            userDao.insertDataProduct(status3)
            userDao.insertDataProduct(status4)
            userDao.insertDataProduct(status5)

            database.close()
        }
    }

    override fun onOpen(db: SupportSQLiteDatabase) {
        super.onOpen(db)
        // Tindakan yang akan diambil saat database terbuka
        // Ini dapat digunakan untuk tugas lain yang perlu dilakukan saat database terbuka
    }
}