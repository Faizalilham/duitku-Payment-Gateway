package coding.faizal.mytasklist.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import coding.faizal.mytasklist.room.dao.TransactionDao
import coding.faizal.mytasklist.room.entity.ProductEntity
import coding.faizal.mytasklist.room.entity.TransactionEntity
import coding.faizal.mytasklist.room.entity.UserEntity
import coding.faizal.mytasklist.room.utils.RoomHelper

@Database(
    entities = [TransactionEntity::class, UserEntity::class, ProductEntity::class],
    version = 1
)
abstract class RoomDB  : RoomDatabase() {
    abstract fun dao(): TransactionDao

    companion object {

        @Volatile
        private var instance: RoomDB? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            RoomDB::class.java,
            "Shoppp"
        ).addCallback(RoomHelper(context)).fallbackToDestructiveMigration().build()

    }
}