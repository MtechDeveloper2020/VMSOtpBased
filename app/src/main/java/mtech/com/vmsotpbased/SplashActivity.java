package mtech.com.vmsotpbased;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

@SuppressLint("NewApi")
public class SplashActivity extends Activity {
	public static final String MyPREFERENCES = "MyPrefs" ;
	SharedPreferences sharedpreferences;
	String account="";
	private Intent intent;
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			Toast.makeText(context, "Helloo", Toast.LENGTH_SHORT).show();
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		sharedpreferences = getSharedPreferences("sp", Context.MODE_PRIVATE);

		account = sharedpreferences.getString("account",null);
	//	Toast.makeText(this, ""+account , Toast.LENGTH_SHORT).show();
		main();
	}

	private void main(){
		// TODO
		final int durection = 2000;
		Thread thrd = new Thread(){
			int wait = 0;

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					super.run();
					while (wait < durection){
						sleep(2000);
						wait += 2000;
					}
				} catch (Exception e){
					// TODO: handle exception
					System.out.print("error" + e);
				} finally {

					 if(account != null && account.equalsIgnoreCase("visitor")){
						Intent i = new Intent(SplashActivity.this, VisitorActivity.class);
						startActivity(i);
						finish();
					}
					else if(account != null && account.equalsIgnoreCase("admin")){
						 stopService(new Intent(SplashActivity.this,BService.class));
						 Intent is = new Intent(SplashActivity.this,BService.class);
						 startService(is);
						 Intent i = new Intent(SplashActivity.this, AdminMainActivity.class);
					     startActivity(i);
						 finish();

					}else {
						Intent i = new Intent(SplashActivity.this, SelectionActivity.class);
						startActivity(i);
						finish();
					 }
				}
			}

		};
		thrd.start();

	}

	@Override
	public void onResume() {
		super.onResume();
		registerReceiver(broadcastReceiver, new IntentFilter(
				BService.BROADCAST_ACTION));

	}
	@Override
	public void onPause() {
		super.onPause();
		unregisterReceiver(broadcastReceiver);
		//stopService(intent);
	}

}
