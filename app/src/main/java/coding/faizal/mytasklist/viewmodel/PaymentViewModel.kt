package coding.faizal.mytasklist.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import coding.faizal.mytasklist.api.ApiConfig
import coding.faizal.mytasklist.api.model.*
import coding.faizal.mytasklist.api.service.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PaymentViewModel : ViewModel() {

    private val retrofit = ApiConfig.retrofit
    private val apiService = retrofit.create(ApiService::class.java)

    private val _resultLiveDataTransactions = MutableLiveData<PaymentResponse>()
    val resultLiveDataTransactions : LiveData<PaymentResponse> = _resultLiveDataTransactions

    private val _resultLiveDataPaymentMethod = MutableLiveData<ListPaymentMethodResponse>()
    val resultLiveDataPaymentMethod :LiveData<ListPaymentMethodResponse> = _resultLiveDataPaymentMethod



    fun sendPaymentRequest(paymentRequest: PaymentRequest) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val response = apiService.sendPaymentTransactionsRequest(paymentRequest)
                _resultLiveDataTransactions.postValue(response)
            }

            Log.d("SUCCESS","${resultLiveDataTransactions.value}")


        }catch (e : Exception){
            Log.d("ERROR","${e.message}")
        }
    }

    fun sendPaymentMethodRequest(paymentMethodRequest: PaymentMethodRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = apiService.sendPaymentMethodRequest(paymentMethodRequest)
            _resultLiveDataPaymentMethod.postValue(response)
        }

    }
}

class PaymentViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PaymentViewModel::class.java)) {
            return PaymentViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
