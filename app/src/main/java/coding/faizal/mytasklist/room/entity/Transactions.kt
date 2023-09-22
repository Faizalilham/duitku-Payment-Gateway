package coding.faizal.mytasklist.room.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Transactions(
    val id_transaction : Int,
    val name_product : String,
    val quantity : Int,
    val price : Int,
):Parcelable
