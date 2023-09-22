package coding.faizal.mytasklist.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class ProductEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_product")
    val id_product : Int,

    @ColumnInfo(name = "name_product")
    val product : String,

    @ColumnInfo(name = "price")
    val price : Int

)
