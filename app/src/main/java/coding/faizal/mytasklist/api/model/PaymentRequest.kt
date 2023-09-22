package coding.faizal.mytasklist.api.model

import com.google.gson.annotations.SerializedName

data class PaymentRequest(

    @SerializedName("merchantCode") val merchantCode: String = "DS16782",
    @SerializedName("paymentAmount") val paymentAmount: String,
    @SerializedName("paymentMethod") val paymentMethod: String,
    @SerializedName("merchantOrderId") val merchantOrderId: String,
    @SerializedName("productDetails") val productDetails: String,
    @SerializedName("customerVaName") val customerVaName: String = "Faizal",
    @SerializedName("email") val email: String = "faizalfalakh19@gmail.com",
    @SerializedName("phoneNumber") val phoneNumber: String = "0895421971694",
    @SerializedName("callbackUrl") val callbackUrl: String = "https://github.com/Faizalilham",
    @SerializedName("returnUrl") val returnUrl: String = "https://github.com/Faizalilham",
    @SerializedName("signature") val signature: String
)
