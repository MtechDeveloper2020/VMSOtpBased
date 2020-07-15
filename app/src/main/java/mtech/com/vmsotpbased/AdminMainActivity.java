package mtech.com.vmsotpbased;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class AdminMainActivity extends AppCompatActivity {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ImageView img;
    TextView count;
    int notifications;
    String email=null,passwd=null, staffname=null;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    PendingFragment pendingFragment;
    ApprovedFragment approvedFragment;

    //Fragments
    RejectedFragment rejectedFragment;
    NotificationFragment notiFragment;
    //This is our tablayout
    private TabLayout tabLayout;
    //This is our viewPager
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
//        startActivity(new Intent(this,BService.class));
        sharedpreferences = getSharedPreferences("sp", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(3);
        setupViewPager(viewPager);

        //Initializing the tablayout
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

        //=====
//        Intent intent1 = new Intent(this, MyBroadcastReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0, intent1, 0);
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
//        builder.setDeleteIntent(pendingIntent);
        //=====




        //------------------ Key Generation Call ----------------------------
        if (!sharedpreferences.getBoolean("sf", false)) {
            Intent intent = new Intent(getApplicationContext(), AdminLogin.class);
            startActivity(intent);
            finish();
        }

        //========================================================================

                email  = sharedpreferences.getString("Email",null);
                passwd = sharedpreferences.getString("Pass",null);
                staffname = sharedpreferences.getString("staffname",null);

        //========================================================================

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                viewPager.setCurrentItem(position, false);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        new GetLongLat("E").execute();
    }

    private void showName(ResultSet rs, String flag){

        try {

        } catch (Exception e) {

            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();

        }
    }
    //--------actionbar----------------//

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.admin_logout, menu);

        return super.onCreateOptionsMenu(menu);

    }

//---------------test------------------

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {

            case R.id.action_search:
                // location found
                stopService(new Intent(this,BService.class));
                logout();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void logout(){
//        Intent i = new Intent(AdminActivity.this, LocationFound.class);
//        startActivity(i);

        SharedPreferences sharedPreferences = getSharedPreferences("sp", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();

        Intent i = new Intent(AdminMainActivity.this, SelectionActivity.class);
        startActivity(i);
        finish();
    }

    //--------actionbar----------------//
    private void setupViewPager(ViewPager viewPager){

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        approvedFragment = new ApprovedFragment();
        pendingFragment = new PendingFragment();
        rejectedFragment = new RejectedFragment();
        notiFragment = new NotificationFragment();
        adapter.addFragment(pendingFragment,"PENDING");
        adapter.addFragment(approvedFragment,"APPROVED");
        adapter.addFragment(rejectedFragment,"REJECTED");

        viewPager.setAdapter(adapter);
        }

        public class GetLongLat extends AsyncTask<String, String, String> {
        Connection conn;
        ResultSet rs=null, rs1=null;
        ProgressDialog progressDialog;
        DBConnection dbConnection = new DBConnection();
        String ret = "", flag = "";

        public GetLongLat(String flag){
            this.flag = flag;
        }

        @Override
        protected void onPreExecute(){

            progressDialog = new ProgressDialog(AdminMainActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();

        }

        @Override
        protected void onPostExecute(String res) {

            progressDialog.dismiss();
            if (res.equalsIgnoreCase("1")){

                showName(rs, flag);
                //Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT).show();
//                msg.setTextColor(Color.RED);
//                msg.setText("Internet Connection Error ");
//                btn_Status.setVisibility(onCreatePanelView(-1).INVISIBLE);
            }

        }

        @Override
        protected String doInBackground(String... params){
            try {

                conn = dbConnection.connectionclass(); //Connect to database
                if (conn == null) {
                    ret = "Error in connection with SQL server";
                } else {
                    if (flag.equalsIgnoreCase("E")) {
                        String query = "SELECT count(VisitorName) AS CountApp  FROM VisitorAppointment WHERE MeetPerson='"+staffname+"' AND checkAppointment='0'";
                        Statement stmt = conn.createStatement();
                        rs = stmt.executeQuery(query);
                        ret = "1";
                    }
                }
            } catch(Exception ex){
                ret = ex.getMessage();
            }
            return ret;
        }
    }
}
