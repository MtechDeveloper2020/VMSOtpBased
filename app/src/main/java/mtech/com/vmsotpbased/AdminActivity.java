package mtech.com.vmsotpbased;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by cd02407 on 7/5/2018.
 */

public class AdminActivity extends Activity {
    public static final String MyPREFERENCES = "MyCount" ;
    ImageView img;
    TextView count;
    int notifications;
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity);
        img = (ImageView) findViewById(R.id.notifyimage);
        count = (TextView) findViewById(R.id.text);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedpreferences.edit();
//        editor.putInt("mycount", notifications);
//        editor.commit();
        new GetLongLat("E").execute();
    }

    private void showName(ResultSet rs, String flag) {

        try {
            String countt= null;
            int ncount=0;
            if (flag.equalsIgnoreCase("E")) {
                while(rs.next()) {
                     countt = rs.getString("CountApp");
                    ncount = Integer.parseInt(countt);

                  count.setText("You have "+countt+" Notifications");
                }
                if(ncount > notifications) {
                    notifications=ncount;
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putInt("mycount", notifications);
                    editor.commit();
//                    Toast.makeText(this, "notifications"+notifications, Toast.LENGTH_SHORT).show();
//                    Toast.makeText(this, "ncount"+ncount, Toast.LENGTH_SHORT).show();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    //--------actionbar----------------//
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);

        return super.onCreateOptionsMenu(menu);
    }
//---------------test------------------

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_search:
                // search action
                return true;
            case R.id.action_location_found:
                // location found
                LocationFound();
                return true;
            case R.id.action_refresh:
                // refresh
                return true;
            case R.id.action_help:
                // help action
                return true;
            case R.id.action_check_updates:
                // check for updates action
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void LocationFound() {
//        Intent i = new Intent(AdminActivity.this, LocationFound.class);
//        startActivity(i);
        Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
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
            progressDialog = new ProgressDialog(AdminActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String res) {
            progressDialog.dismiss();
            if (res.equalsIgnoreCase("1")) {

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
        protected String doInBackground(String... params) {
            try {

                conn = dbConnection.connectionclass(); //Connect to database
                if (conn == null) {
                    ret = "Error in connection with SQL server";
                } else {
                    if (flag.equalsIgnoreCase("E")) {

                        String query = "SELECT count(VisitorName) AS CountApp  FROM VisitorAppointment WHERE MeetPerson='Tanuja' AND checkAppointment='0'";
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

    //--------actionbar----------------//
}
