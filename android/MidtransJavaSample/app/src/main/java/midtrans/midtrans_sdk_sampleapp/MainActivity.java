package midtrans.midtrans_sdk_sampleapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.PaymentMethod;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.core.UIKitCustomSetting;
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.snap.CreditCard;
import com.midtrans.sdk.corekit.models.snap.Gopay;
import com.midtrans.sdk.corekit.models.snap.Shopeepay;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TransactionFinishedCallback {
    private Button buttonUiKit, buttonDirectCreditCard, buttonDirectBcaVa, buttonDirectMandiriVa,
            buttonDirectBniVa, buttonDirectAtmBersamaVa, buttonDirectPermataVa, buttonPayWithSnapToken;
    private EditText editTextSnapToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindViews();
        initActionButtons();
        initMidtransSdk();

    }

    private TransactionRequest initTransactionRequest() {
        // Create new Transaction Request
        TransactionRequest transactionRequestNew = new
                TransactionRequest(System.currentTimeMillis() + "", 36500.0);
        transactionRequestNew.setCustomerDetails(initCustomerDetails());
        transactionRequestNew.setGopay(new Gopay("mysamplesdk:://midtrans"));
        transactionRequestNew.setShopeepay(new Shopeepay("mysamplesdk:://midtrans"));

        ItemDetails itemDetails1 = new ItemDetails("ITEM_ID_1", 36500.0, 1, "ITEM_NAME_1");
        ArrayList<ItemDetails> itemDetailsList = new ArrayList<>();
        itemDetailsList.add(itemDetails1);
        transactionRequestNew.setItemDetails(itemDetailsList);
        return transactionRequestNew;
    }

    private CustomerDetails initCustomerDetails() {
        //define customer detail (mandatory for coreflow)
        CustomerDetails mCustomerDetails = new CustomerDetails();
        mCustomerDetails.setPhone("085310102020");
        mCustomerDetails.setFirstName("user fullname");
        mCustomerDetails.setEmail("mail@mail.com");
        mCustomerDetails.setCustomerIdentifier("mail@mail.com");
        return mCustomerDetails;
    }

    private void initMidtransSdk() {
        String client_key = SdkConfig.MERCHANT_CLIENT_KEY;
        String base_url = SdkConfig.MERCHANT_BASE_CHECKOUT_URL;
        SdkUIFlowBuilder sdkUIFlowBuilder = SdkUIFlowBuilder.init()
                .setClientKey(client_key) // client_key is mandatory
                .setContext(this) // context is mandatory
                .setTransactionFinishedCallback(this) // set transaction finish callback (sdk callback)
                .setMerchantBaseUrl(base_url)//set merchant url
                .enableLog(true) // enable sdk log
                .setColorTheme(new CustomColorTheme("#FFE51255", "#B61548", "#FFE51255")) // will replace theme on snap theme on MAP
                .setLanguage("en");
        sdkUIFlowBuilder.buildSDK();
        uiKitCustomSetting();
    }

    @Override
    public void onTransactionFinished(TransactionResult result) {
        if (result.getResponse() != null) {
            switch (result.getStatus()) {
                case TransactionResult.STATUS_SUCCESS:
                    Toast.makeText(this, "Transaction Finished. ID: " + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
                    break;
                case TransactionResult.STATUS_PENDING:
                    Toast.makeText(this, "Transaction Pending. ID: " + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
                    break;
                case TransactionResult.STATUS_FAILED:
                    Toast.makeText(this, "Transaction Failed. ID: " + result.getResponse().getTransactionId() + ". Message: " + result.getResponse().getStatusMessage(), Toast.LENGTH_LONG).show();
                    break;
            }
        } else if (result.isTransactionCanceled()) {
            Toast.makeText(this, "Transaction Canceled", Toast.LENGTH_LONG).show();
        } else {
            if (result.getStatus().equalsIgnoreCase(TransactionResult.STATUS_INVALID)) {
                Toast.makeText(this, "Transaction Invalid", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Transaction Finished with failure.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void bindViews() {
        buttonUiKit = (Button) findViewById(R.id.button_uikit);
        buttonDirectCreditCard = (Button) findViewById(R.id.button_direct_credit_card);
        buttonDirectBcaVa = (Button) findViewById(R.id.button_direct_bca_va);
        buttonDirectMandiriVa = (Button) findViewById(R.id.button_direct_mandiri_va);
        buttonDirectBniVa = (Button) findViewById(R.id.button_direct_bni_va);
        buttonDirectPermataVa = (Button) findViewById(R.id.button_direct_permata_va);
        buttonDirectAtmBersamaVa = (Button) findViewById(R.id.button_direct_atm_bersama_va);
        buttonPayWithSnapToken = (Button) findViewById(R.id.button_snap_pay);
        editTextSnapToken = (EditText) findViewById(R.id.edit_snaptoken);
    }

    private void initActionButtons() {
        buttonUiKit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MidtransSDK.getInstance().setTransactionRequest(initTransactionRequest());
                MidtransSDK.getInstance().startPaymentUiFlow(MainActivity.this);
            }
        });

        buttonDirectCreditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MidtransSDK.getInstance().setTransactionRequest(initTransactionRequest());
                MidtransSDK.getInstance().startPaymentUiFlow(MainActivity.this, PaymentMethod.CREDIT_CARD);
            }
        });


        buttonDirectBcaVa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MidtransSDK.getInstance().setTransactionRequest(initTransactionRequest());
                MidtransSDK.getInstance().startPaymentUiFlow(MainActivity.this, PaymentMethod.BANK_TRANSFER_BCA);
            }
        });


        buttonDirectBniVa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MidtransSDK.getInstance().setTransactionRequest(initTransactionRequest());
                MidtransSDK.getInstance().startPaymentUiFlow(MainActivity.this, PaymentMethod.BANK_TRANSFER_BNI);
            }
        });

        buttonDirectMandiriVa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MidtransSDK.getInstance().setTransactionRequest(initTransactionRequest());
                MidtransSDK.getInstance().startPaymentUiFlow(MainActivity.this, PaymentMethod.BANK_TRANSFER_MANDIRI);
            }
        });


        buttonDirectPermataVa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MidtransSDK.getInstance().setTransactionRequest(initTransactionRequest());
                MidtransSDK.getInstance().startPaymentUiFlow(MainActivity.this, PaymentMethod.BANK_TRANSFER_PERMATA);
            }
        });

        buttonDirectAtmBersamaVa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MidtransSDK.getInstance().setTransactionRequest(initTransactionRequest());
                MidtransSDK.getInstance().startPaymentUiFlow(MainActivity.this, PaymentMethod.BCA_KLIKPAY);
            }
        });

        buttonPayWithSnapToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String snaptokenValue = editTextSnapToken.getEditableText().toString();
                if (snaptokenValue != null) {
                    MidtransSDK.getInstance().startPaymentUiFlow(MainActivity.this, snaptokenValue);
                }
            }
        });
    }

    private void uiKitCustomSetting() {
        UIKitCustomSetting uIKitCustomSetting = new UIKitCustomSetting();
        uIKitCustomSetting.setSaveCardChecked(true);
        MidtransSDK.getInstance().setUiKitCustomSetting(uIKitCustomSetting);
    }
}