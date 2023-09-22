package coding.faizal.mytasklist.room.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class UserEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_user")
    val id_user : Int,

    @ColumnInfo(name = "name_user")
    val name_user : String

):Parcelable
