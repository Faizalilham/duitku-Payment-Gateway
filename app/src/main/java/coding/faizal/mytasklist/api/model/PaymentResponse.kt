package coding.faizal.mytasklist.api.model

import com.google.gson.annotations.SerializedName

data class PaymentResponse(


    @SerializedName("merchantCode")
    val merchantCode : String,

    @SerializedName("reference")
    val reference : String,

    @SerializedName("paymentUrl")
    val paymentUrl : String,

    @SerializedName("vaNumber")
    val vaNumber : String,

    @SerializedName("amount")
    val amount : String,

    @SerializedName("statusCode")
    val statusCode : String,

    @SerializedName("statusMessage")
    val statusMessage : String,

)
