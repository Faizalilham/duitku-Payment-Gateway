package coding.faizal.mytasklist.api.model

import com.google.gson.annotations.SerializedName
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*

data class PaymentMethodRequest(

    @SerializedName("merchantcode")
    val merchantcode : String = "DS16782",

    @SerializedName("amount")
    val amont : String,

    @SerializedName("datetime")
    val datetime : String = currentDateTime(),

    @SerializedName("signature")
    val signature : String,

)

fun currentDateTime(): String{
    val currentDate = Date()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return dateFormat.format(currentDate)
}

fun createSignature(input : String) : String{
    val messageDigest = MessageDigest.getInstance("SHA-256")
    val bytes = messageDigest.digest(input.toByteArray())
    val hexString = StringBuilder()

    for (byte in bytes) {
        val hex = Integer.toHexString(0xff and byte.toInt())
        if (hex.length == 1) {
            hexString.append('0')
        }
        hexString.append(hex)
    }

    return hexString.toString()
}

fun md5(input: String): String {
    val messageDigest = MessageDigest.getInstance("MD5")
    val bytes = messageDigest.digest(input.toByteArray())
    val hexString = StringBuilder()

    for (byte in bytes) {
        val hex = Integer.toHexString(0xff and byte.toInt())
        if (hex.length == 1) {
            hexString.append('0')
        }
        hexString.append(hex)
    }

    return hexString.toString()
}

