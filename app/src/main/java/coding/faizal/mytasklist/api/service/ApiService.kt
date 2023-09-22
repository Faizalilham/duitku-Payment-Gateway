package coding.faizal.mytasklist.api.service

import coding.faizal.mytasklist.api.model.*
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {


    @POST("paymentmethod/getpaymentmethod")
    suspend fun sendPaymentMethodRequest(@Body paymentMethodRequest: PaymentMethodRequest): ListPaymentMethodResponse

    @POST("v2/inquiry")
    suspend fun sendPaymentTransactionsRequest(@Body paymentRequest: PaymentRequest): PaymentResponse



}