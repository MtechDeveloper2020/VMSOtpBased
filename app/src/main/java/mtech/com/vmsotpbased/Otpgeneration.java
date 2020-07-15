package mtech.com.vmsotpbased;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by cd02407 on 5/5/2018.
 */

public class Otpgeneration extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otpgenerate);


        int randomPin   =(int)(Math.random()*900000)+100000;
        String otp  = String.valueOf(randomPin);
        Toast.makeText(this, "hello  "+otp, Toast.LENGTH_SHORT).show();
    }
}
