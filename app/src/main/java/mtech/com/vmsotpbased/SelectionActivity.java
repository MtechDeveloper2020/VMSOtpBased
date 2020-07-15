package mtech.com.vmsotpbased;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by cd02407 on 7/5/2018.
 */

public class SelectionActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_activity);

    }
    public void visitor(View v){
        Intent i = new Intent(SelectionActivity.this,
                VisitorLogin.class);
        startActivity(i);
        finish();
    }
    public void admin(View v){
        Intent i = new Intent(SelectionActivity.this,
                AdminLogin.class);
        startActivity(i);
        finish();
    }
}
