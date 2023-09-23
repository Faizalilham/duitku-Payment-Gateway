# ProductApp

Aplikasi ini adalah aplikasi android sederhana yang sudah terintegrasi dengan Payment Gateway yang memungkinkan Anda untuk melakukan transaksi pembayaran secara mudah dan aman di platform Android. Dengan menggunakan aplikasi ini, Anda dapat mengintegrasikan berbagai metode pembayaran dan mengelola transaksi dengan cepat.

## Tech Stack

- [x] Android
- [x] Kotlin
- [x] Coroutine
- [x] MVVM Design Pattern
- [x] Retrofit For Consume API
- [x] Room Database 

## Daftar Isi
1. [Pendahuluan](#pendahuluan)
2. [Langkah 1: Persiapan](#langkah-1-persiapan)
3. [Langkah 2: Integrasi Payment Gateway](#langkah-2-integrasi-payment-gateway)
4. [Langkah 3: Menjalankan Transaksi](#langkah-3-menjalankan-transaksi)
5. [Contoh Kode](#contoh-kode)
6. [FAQ](#faq)
7. [Lisensi](#lisensi)

## Pendahuluan
Payment Gateway adalah solusi yang memungkinkan aplikasi Anda untuk menerima pembayaran dari pengguna dengan berbagai metode pembayaran seperti kartu kredit, e-wallet,perbankan dan lainnya. Dokumentasi ini akan membantu Anda mengintegrasikan Payment Gateway dengan mudah ke dalam aplikasi android Anda.

## Langkah 1: Persiapan
Sebelum Anda mulai mengintegrasikan Payment Gateway, Anda perlu mempersiapkan beberapa hal sebagai berikut:
- Mendaftar akun di layanan Payment Gateway, disini saya pakai layanan dari duitku.
- Membuat project untuk mendapatkan API Key  dan Merchant Kode dari layanan Payment Gateway duitku.
- Memahami dokumentasi API yang disediakan oleh Payment Gateway duitku (Request / Response).
- Memastikan aplikasi Anda memenuhi semua prasyarat yang diperlukan oleh Payment Gateway duitku seperti sdk  	   android atau yang lain.

## Langkah 2: Integrasi Payment Gateway
Integrasi Payment Gateway ke dalam aplikasi Anda melibatkan langkah-langkah berikut:
- Mengimpor library yang dibutuhkan untuk access ake API duitku ke dalam proyek android Anda.
- Inisialisasi Payment Gateway dari response API dengan menggunakan API Key  dan Merchant Kode yang Anda dapatkan.
- Menyesuaikan tampilan pembayaran sesuai dengan desain aplikasi Anda.

## Langkah 3: Menjalankan Transaksi
Langkah terakhir adalah menjalankan transaksi pembayaran dengan Payment Gateway:
- Menambah data untuk melakukan transaksi data akan tersimpan ke database, dan ditampilkan dihalaman awal.
- Kkik salah satu transaksi kemudian akan menampilkan metode pembayaran.
- Pilih metode pembayaran yang diinginkan kemdian sistem akan Mengirim permintaan pembayaran ke Payment Gateway.
- Memproses respons transaksi dan memberikan kode transaksi kepada pengguna.
- Kode transaksi bisa digunakan untuk membayar nilai transaksi, untuk uji coba pembayaran bisa klik 
[disini](https://docs.duitku.com/api/id/#uji-coba)


## Contoh Kode
Berikut adalah contoh kode dalam bahasa pemrograman kotlin untuk mengintegrasikan Payment Gateway ke dalam aplikasi android Anda:

### Api Konfigurasi<br>

```kotlin
object ApiConfig {

    private const val baseUrl = "https://sandbox.duitku.com/webapi/api/merchant/"

    private val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

interface ApiService {


    @POST("paymentmethod/getpaymentmethod")
    suspend fun sendPaymentMethodRequest(@Body paymentMethodRequest: PaymentMethodRequest): ListPaymentMethodResponse

    @POST("v2/inquiry")
    suspend fun sendPaymentTransactionsRequest(@Body paymentRequest: PaymentRequest): PaymentResponse



}

}

```

### Request & Response Model 

```kotlin

// Request untuk mendapatkan semua metode pembayaran yang tersedia di duitku
data class PaymentMethodRequest(

    @SerializedName("merchantcode")
    val merchantcode : String = "Sesuaikan dengan merchane code masing masing",

    @SerializedName("amount")
    val amont : String,

    @SerializedName("datetime")
    val datetime : String = currentDateTime(),

    @SerializedName("signature")
    val signature : String,

)

// Mendapatkan tanggal saat ini dengan format yang sudah ditentukan untuk kebutuhan request metode pembayaran
fun currentDateTime(): String{
    val currentDate = Date()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return dateFormat.format(currentDate)
}

// Fungsi untuk kebutuhan request metode pembayaran menggunakan signature(enkripsi menggunakan SHA256)
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

// Fungsi untuk kebutuhan request transaksi menggunakan signature(enkripsi menggunakan MD5)
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

// Response Untuk mendapatkan semua metode pembayaran di duitku
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

// Request untuk lanjut ke transaksi
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

// Response dari hasil transaksi
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

```

### Viewmodel

```kotlin

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



```

### Activity payment dan transaksi

```kotlin

// Activity untuk menampilkan semua metode pembayaran
class PaymentActivity : AppCompatActivity() {

    private var _binding : ActivityPaymentBinding? = null
    private val binding get() = _binding!!

    private var inputSignature = ""
    private var inputMd5 = ""
    private var nameProduct = ""
    private var amount = 0
    private var id = 0
    private val apiKey = "Isi dengan Api Key masing masing"

    private lateinit var paymentViewModel : PaymentViewModel
    private lateinit var paymentAdapter: PaymentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelFactory = PaymentViewModelFactory()
        paymentViewModel = ViewModelProvider(this,viewModelFactory)[PaymentViewModel::class.java]
        _binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)


        amount = intent.getIntExtra("amount",0)
        nameProduct = intent.getStringExtra("name").toString()
        id = intent.getIntExtra("id",0)

        Toast.makeText(this, "$amount", Toast.LENGTH_SHORT).show()
        if(amount > 10000 && id != 0){
            binding.tvTotal.text = amount.toString()
            inputSignature = "DS16782"+"$amount"+ currentDateTime() +apiKey
            getAllPayment(amount.toString(),inputSignature)
        }else{
            Toast.makeText(this, "Pembayaran harus lebih dari 10000", Toast.LENGTH_SHORT).show()
        }

    }


    private fun getAllPayment(amount : String,signature : String){
        paymentViewModel.sendPaymentMethodRequest(
            PaymentMethodRequest(
                amont = amount,
                signature = createSignature(signature)
            )
        )

        paymentViewModel.resultLiveDataPaymentMethod.observe(this){
            Toast.makeText(this, "$it", Toast.LENGTH_SHORT).show()
            setupRecycler(it.paymentFee)
        }
    }

    private fun setupRecycler(data : List<PaymentMethodResponse>){
        paymentAdapter = PaymentAdapter(data,object : PaymentAdapter.OnClick{
            override fun onClick(data: PaymentMethodResponse) {

                inputMd5 = "DS16782"+"$id"+"$amount"+apiKey
                Log.d("TAGS","${data.paymentMethod} ${amount}")

                try {
                    doPayment(data)
                }catch (e : Exception){
                    Toast.makeText(this@PaymentActivity, "Barang telah dibayar ${e.message}", Toast.LENGTH_SHORT).show()
                }

            }
        })

        binding.tvTransactions.apply {
            adapter = paymentAdapter
            layoutManager = GridLayoutManager(this@PaymentActivity,2)
        }
    }

    private fun doPayment(data: PaymentMethodResponse){

        paymentViewModel.sendPaymentRequest(PaymentRequest(
            productDetails = nameProduct,
            paymentMethod = data.paymentMethod,
            merchantOrderId = id.toString(),
            paymentAmount = amount.toString(),
            signature = md5(inputMd5)
        ))

        paymentViewModel.resultLiveDataTransactions.observe(this@PaymentActivity){ res ->
            startActivity(Intent(this@PaymentActivity,TransactionActivity::class.java).also{
                it.putExtra("url",res.paymentUrl)
            })
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}


// Activity untuk transaksi 
class TransactionActivity : AppCompatActivity() {

    private var _binding : ActivityTransactionBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val url = intent.getStringExtra("url")
        setupWebView(url ?: "https://github.com/Faizalilham")
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView(url : String){
       binding.apply {
           webView.webViewClient = WebViewClient()
           webView.loadUrl(url)
           webView.settings.javaScriptEnabled = true
           webView.webViewClient = object : WebViewClient() {
               @Deprecated("Deprecated in Java")
               override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                   view?.loadUrl(url.toString())
                   return true
               }
           }
       }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}


```




## FAQ
Pertanyaan yang sering diajukan (FAQ) tentang integrasi Payment Gateway:<br>

### Apa itu API Key dan bagaimana saya bisa mendapatkannya?<br>
Api key bisa didapatkan ketika sudah mendaftar kemudian membuat project di duitku

### Bagaimana cara mengatasi kesalahan transaksi yang sering terjadi?<br>
Error atau kesalahan traksaksi bisa diketahui lewat response API Berupa HTTP status kode, untuk lebih lebih detailnya anda baca [disini](https://docs.duitku.com/api/id/#http-code)

## Lisensi
Anda dapat menemukan informasi tentang lisensi penggunaan Payment Gateway di [sini](https://github.com/Faizalilham).

Dengan mengikuti panduan ini, Anda akan dapat dengan mudah mengintegrasikan Payment Gateway ke dalam aplikasi android  Anda dan mulai menerima pembayaran dengan lancar. Jika Anda memiliki pertanyaan tambahan, jangan ragu untuk menghubungi tim dukungan kami.



