package mtech.com.vmsotpbased;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class NotificationReceiverActivity extends Activity {
    String visitorName = null, purpose = null, date = null, id = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);

        //========================================================================
        Intent i = getIntent();
        visitorName = i.getStringExtra("visitorName");
        purpose = i.getStringExtra("purpose");
        date = i.getStringExtra("AcDate");
        id = i.getStringExtra("Id");


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Visitor: "+visitorName);
        alertDialogBuilder.setMessage(" " + purpose + "\n" +
                "ON :  " + date );
//        alertDialogBuilder.setNegativeButton("CANCEL",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface arg0, int arg1) {
//
//                    }
//                });

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                      new GetLongLat("E").execute();

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    //========================================================================

    //========================================================================

    private void showName(ResultSet rs, ResultSet rs1, String flag) {
        try {

            if (flag.equalsIgnoreCase("E")) {
                Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(NotificationReceiverActivity.this, AdminMainActivity.class);

                startActivity(i);
                finish();

            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();


    }
    //========================================================================
//-------------------------------------------------------------------------------------------

    @Override
    public void onPause() {
        super.onPause();
    }

    public class GetLongLat extends AsyncTask<String, String, String> {
        Connection conn;
        ResultSet rs = null, rs1 = null;
        ProgressDialog progressDialog;
        DBConnection dbConnection = new DBConnection();
        String ret = "", flag = "";


        public GetLongLat(String flag) {
            this.flag = flag;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(NotificationReceiverActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String res) {
            progressDialog.dismiss();
            if (res.equalsIgnoreCase("1")) {

                showName(rs, rs1, flag);
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

                        String sql = "update VisitorAppointment set checkAppointment=?  where VisitorName=?  and Purpose=? and Id=? ";

                        PreparedStatement preparedStatement = conn.prepareStatement(sql);

                        preparedStatement.setString(1, "1");
                        preparedStatement.setString(2, visitorName);
                        preparedStatement.setString(3, purpose);
                        preparedStatement.setString(4, id);

                        int i = preparedStatement.executeUpdate();
                        ret = "" + i;
                    }

                }
            } catch (Exception ex) {
                ret = ex.getMessage();
            }
            return ret;
        }
    }
}
