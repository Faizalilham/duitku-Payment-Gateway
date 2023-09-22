package coding.faizal.mytasklist.api.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PaymentMethodResponse(
    @SerializedName("paymentMethod") val paymentMethod: String,
    @SerializedName("paymentName") val paymentName: String,
    @SerializedName("paymentImage") val paymentImage: String,
    @SerializedName("totalFee") val totalFee: String
):Parcelable

data class ListPaymentMethodResponse(
    @SerializedName("paymentFee") val paymentFee: List<PaymentMethodResponse>,
    @SerializedName("responseCode") val responseCode: String,
    @SerializedName("responseMessage") val responseMessage: String
)