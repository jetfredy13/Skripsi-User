package com.example.psikologiku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme;
import com.midtrans.sdk.corekit.models.BankType;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.snap.CreditCard;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;

import java.util.ArrayList;

public class TopupWallet extends AppCompatActivity implements TransactionFinishedCallback {
    Button btn_topup;
    TextView tv_saldo;
    EditText et_nominal;
    SharedPreferences sp ;
    DatabaseReference ref;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topup_wallet);
        btn_topup = findViewById(R.id.btn_topup);
        tv_saldo = findViewById(R.id.tv_wallet);
        et_nominal = findViewById(R.id.et_nominal);
        initMidtransSdk();
        btn_topup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int nominal = Integer.parseInt(et_nominal.getText().toString());
                if(nominal<25000)
                {
                    Toast.makeText(TopupWallet.this,"Minimal Nominal Rp 25000 ",Toast.LENGTH_SHORT).show();
                }
                else{
                    MidtransSDK.getInstance().setTransactionRequest(initTransactionRequest());
                    MidtransSDK.getInstance().startPaymentUiFlow(TopupWallet.this);
                }

            }
        });
        getUserData();
    }
    private void getUserData()
    {
        sp  = TopupWallet.this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String id = sp.getString("id","");
        ref = FirebaseDatabase.getInstance().getReference("User").child(id);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               user = dataSnapshot.getValue(User.class);
               tv_saldo.setText(Integer.toString(user.getSaldo()));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private TransactionRequest initTransactionRequest() {
        // Create new Transaction Request
        sp  = TopupWallet.this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String id = sp.getString("id","");
        int nominal = Integer.parseInt(et_nominal.getText().toString());
        TransactionRequest transactionRequestNew = new
                TransactionRequest(System.currentTimeMillis() + "", nominal);
        //set customer details
        transactionRequestNew.setCustomerDetails(initCustomerDetails());
        // set item details
        ItemDetails itemDetails = new ItemDetails("1", nominal, 1, "Topup");
        // Add item details into item detail list.
        ArrayList<ItemDetails> itemDetailsArrayList = new ArrayList<>();
        itemDetailsArrayList.add(itemDetails);
        transactionRequestNew.setItemDetails(itemDetailsArrayList);
        // Create creditcard options for payment
        CreditCard creditCard = new CreditCard();
        creditCard.setSaveCard(false); // when using one/two click set to true and if normal set to  false

//        this methode deprecated use setAuthentication instead
//        creditCard.setSecure(true); // when using one click must be true, for normal and two click (optional)

        creditCard.setAuthentication(CreditCard.AUTHENTICATION_TYPE_3DS);

        // noted !! : channel migs is needed if bank type is BCA, BRI or MyBank
//        creditCard.setChannel(CreditCard.MIGS); //set channel migs
        creditCard.setBank(BankType.BCA); //set spesific acquiring bank
        //transactionRequestNew.setCreditCard(creditCard);
        return transactionRequestNew;
    }

    private CustomerDetails initCustomerDetails() {
        //define customer detail (mandatory for coreflow)
        sp  = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String email = sp.getString("email","");
        String nama = sp.getString("username","");
        CustomerDetails mCustomerDetails = new CustomerDetails();
        mCustomerDetails.setFirstName(nama);
        mCustomerDetails.setEmail(email);
        return mCustomerDetails;
    }
    private void initMidtransSdk() {
        String client_key = SDKConfig.MERCHANT_CLIENT_KEY;
        String base_url = SDKConfig.MERCHANT_BASE_CHECKOUT_URL;
        SdkUIFlowBuilder.init()
                .setClientKey(client_key) // client_key is mandatory
                .setContext(this) // context is mandatory
                .setTransactionFinishedCallback(this) // set transaction finish callback (sdk callback)
                .setMerchantBaseUrl(base_url) //set merchant url
                .enableLog(true) // enable sdk log
                .setColorTheme(new CustomColorTheme("#FFE51255", "#B61548", "#FFE51255")) // will replace theme on snap theme on MAP
                .buildSDK();
    }
    @Override
    public void onTransactionFinished(TransactionResult result) {
        sp = TopupWallet.this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        int saldo = Integer.parseInt(et_nominal.getText().toString());
        int currentWallet = user.getSaldo();
        String id = sp.getString("id","");
        if (result.getResponse() != null) {
            switch (result.getStatus()) {
                case TransactionResult.STATUS_SUCCESS:
                    //Toast.makeText(this, "Transaction Success. ID: " + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
                    FirebaseDatabase.getInstance().getReference("User").child(id).child("saldo").setValue(currentWallet+saldo);
                    break;
                case TransactionResult.STATUS_PENDING:
                    Toast.makeText(this, "Transaction Finished. ID: " + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
                    FirebaseDatabase.getInstance().getReference("User").child(id).child("saldo").setValue(currentWallet+saldo);
                    break;
                case TransactionResult.STATUS_FAILED:
                    Toast.makeText(this, "Transaction Failed. ID: " + result.getResponse().getTransactionId() + ". Message: " + result.getResponse().getStatusMessage(), Toast.LENGTH_LONG).show();
                    break;
            }
            result.getResponse().getValidationMessages();
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
}
