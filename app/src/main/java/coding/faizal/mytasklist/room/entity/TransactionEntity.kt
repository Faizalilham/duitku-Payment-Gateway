package coding.faizal.mytasklist.room.entity



import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.*
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    foreignKeys = [

        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id_product"],
            childColumns = ["id_product"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id_user"],
            childColumns = ["id_user"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("id_product"), Index("id_user")]
)
data class TransactionEntity(

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name="id_transaction")
    val idTransaction : Int,

    @ColumnInfo(name = "id_product")
    val id_product : Int,

    @ColumnInfo(name = "id_user")
    val id_user : Int,

    @ColumnInfo(name = "quantity")
    var quantity : Int?,

    @ColumnInfo(name = "date")
    var date : String?

):Parcelable
