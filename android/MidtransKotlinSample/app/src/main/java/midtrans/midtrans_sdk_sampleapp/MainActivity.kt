package midtrans.midtrans_sdk_sampleapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.PaymentMethod
import com.midtrans.sdk.corekit.core.TransactionRequest
import com.midtrans.sdk.corekit.core.UIKitCustomSetting
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme
import com.midtrans.sdk.corekit.models.CustomerDetails
import com.midtrans.sdk.corekit.models.ItemDetails
import com.midtrans.sdk.corekit.models.snap.Gopay
import com.midtrans.sdk.corekit.models.snap.Shopeepay
import com.midtrans.sdk.corekit.models.snap.TransactionResult
import com.midtrans.sdk.uikit.SdkUIFlowBuilder

class MainActivity : AppCompatActivity(), TransactionFinishedCallback {
    private var buttonUiKit: Button? = null
    private var buttonDirectCreditCard: Button? = null
    private var buttonDirectBcaVa: Button? = null
    private var buttonDirectMandiriVa: Button? = null
    private var buttonDirectBniVa: Button? = null
    private var buttonDirectAtmBersamaVa: Button? = null
    private var buttonDirectPermataVa: Button? = null
    private var buttonPayWithSnaptoken: Button? = null
    private var editSnap: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindViews()
        initActionButtons()
        initMidtransSdk()
    }

    private fun initTransactionRequest(): TransactionRequest {
        // Create new Transaction Request
        val transactionRequestNew = TransactionRequest(System.currentTimeMillis().toString() + "", 36500.0)
        transactionRequestNew.customerDetails = initCustomerDetails()
        transactionRequestNew.gopay = Gopay("mysamplesdk:://midtrans")
        transactionRequestNew.shopeepay = Shopeepay("mysamplesdk:://midtrans")

        val itemDetails1 = ItemDetails("ITEM_ID_1", 36500.0, 1, "ITEM_NAME_1")
        val itemDetailsList = ArrayList<ItemDetails>()
        itemDetailsList.add(itemDetails1)
        transactionRequestNew.itemDetails = itemDetailsList
        return transactionRequestNew
    }

    private fun initCustomerDetails(): CustomerDetails {
        //define customer detail (mandatory for coreflow)
        val mCustomerDetails = CustomerDetails()
        mCustomerDetails.phone = "085310102020"
        mCustomerDetails.firstName = "user fullname"
        mCustomerDetails.email = "mail@mail.com"
        mCustomerDetails.customerIdentifier = "mail@mail.com"
        return mCustomerDetails
    }

    private fun initMidtransSdk() {
        val clientKey: String = SdkConfig.MERCHANT_CLIENT_KEY
        val baseUrl: String = SdkConfig.MERCHANT_BASE_CHECKOUT_URL
        val sdkUIFlowBuilder: SdkUIFlowBuilder = SdkUIFlowBuilder.init()
                .setClientKey(clientKey) // client_key is mandatory
                .setContext(this) // context is mandatory
                .setTransactionFinishedCallback(this) // set transaction finish callback (sdk callback)
                .setMerchantBaseUrl(baseUrl) //set merchant url
                .enableLog(true) // enable sdk log
                .setColorTheme(CustomColorTheme("#FFE51255", "#B61548", "#FFE51255")) // will replace theme on snap theme on MAP
                .setLanguage("en")
        sdkUIFlowBuilder.buildSDK()
        uiKitCustomSetting()
    }

    override fun onTransactionFinished(result: TransactionResult) {
        if (result.response != null) {
            when (result.status) {
                TransactionResult.STATUS_SUCCESS -> Toast.makeText(this, "Transaction Finished. ID: " + result.response.transactionId, Toast.LENGTH_LONG).show()
                TransactionResult.STATUS_PENDING -> Toast.makeText(this, "Transaction Pending. ID: " + result.response.transactionId, Toast.LENGTH_LONG).show()
                TransactionResult.STATUS_FAILED -> Toast.makeText(this, "Transaction Failed. ID: " + result.response.transactionId.toString() + ". Message: " + result.response.statusMessage, Toast.LENGTH_LONG).show()
            }
        } else if (result.isTransactionCanceled) {
            Toast.makeText(this, "Transaction Canceled", Toast.LENGTH_LONG).show()
        } else {
            if (result.status.equals(TransactionResult.STATUS_INVALID, true)) {
                Toast.makeText(this, "Transaction Invalid", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Transaction Finished with failure.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun bindViews() {
        buttonUiKit = findViewById<View>(R.id.button_uikit) as Button
        buttonDirectCreditCard = findViewById<View>(R.id.button_direct_credit_card) as Button
        buttonDirectBcaVa = findViewById<View>(R.id.button_direct_bca_va) as Button
        buttonDirectMandiriVa = findViewById<View>(R.id.button_direct_mandiri_va) as Button
        buttonDirectBniVa = findViewById<View>(R.id.button_direct_bni_va) as Button
        buttonDirectPermataVa = findViewById<View>(R.id.button_direct_permata_va) as Button
        buttonDirectAtmBersamaVa = findViewById<View>(R.id.button_direct_atm_bersama_va) as Button
        buttonPayWithSnaptoken = findViewById<View>(R.id.button_snap_pay) as Button
        editSnap = findViewById<View>(R.id.edit_snaptoken) as EditText
    }

    private fun initActionButtons() {
        buttonUiKit!!.setOnClickListener {
            MidtransSDK.getInstance().transactionRequest = initTransactionRequest()
            MidtransSDK.getInstance().startPaymentUiFlow(this@MainActivity)
        }
        buttonDirectCreditCard!!.setOnClickListener {
            MidtransSDK.getInstance().transactionRequest = initTransactionRequest()
            MidtransSDK.getInstance().startPaymentUiFlow(this@MainActivity, PaymentMethod.CREDIT_CARD)
        }
        buttonDirectBcaVa!!.setOnClickListener {
            MidtransSDK.getInstance().transactionRequest = initTransactionRequest()
            MidtransSDK.getInstance().startPaymentUiFlow(this@MainActivity, PaymentMethod.BANK_TRANSFER_BCA)
        }
        buttonDirectBniVa!!.setOnClickListener {
            MidtransSDK.getInstance().transactionRequest = initTransactionRequest()
            MidtransSDK.getInstance().startPaymentUiFlow(this@MainActivity, PaymentMethod.BANK_TRANSFER_BNI)
        }
        buttonDirectMandiriVa!!.setOnClickListener {
            MidtransSDK.getInstance().transactionRequest = initTransactionRequest()
            MidtransSDK.getInstance().startPaymentUiFlow(this@MainActivity, PaymentMethod.BANK_TRANSFER_MANDIRI)
        }
        buttonDirectPermataVa!!.setOnClickListener {
            MidtransSDK.getInstance().transactionRequest = initTransactionRequest()
            MidtransSDK.getInstance().startPaymentUiFlow(this@MainActivity, PaymentMethod.BANK_TRANSFER_PERMATA)
        }
        buttonDirectAtmBersamaVa!!.setOnClickListener {
            MidtransSDK.getInstance().transactionRequest = initTransactionRequest()
            MidtransSDK.getInstance().startPaymentUiFlow(this@MainActivity, PaymentMethod.BCA_KLIKPAY)
        }
        buttonPayWithSnaptoken?.setOnClickListener {
            val snaptokenValue: String = editSnap?.editableText.toString()
            MidtransSDK.getInstance().startPaymentUiFlow(this@MainActivity, snaptokenValue)
        }
    }

    private fun uiKitCustomSetting() {
        val uIKitCustomSetting = UIKitCustomSetting()
        uIKitCustomSetting.setSaveCardChecked(true)
        MidtransSDK.getInstance().setUiKitCustomSetting(uIKitCustomSetting)
    }
}