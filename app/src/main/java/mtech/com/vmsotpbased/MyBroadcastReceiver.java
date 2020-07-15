package mtech.com.vmsotpbased;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MyBroadcastReceiver extends BroadcastReceiver {
    String visitorName = null, purpose = null, date = null, id = null;
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.


        //========================================================================
        visitorName = intent.getStringExtra("visitorName");
        purpose = intent.getStringExtra("purpose");
        date = intent.getStringExtra("AcDate");
        id = intent .getStringExtra("Id");
//        Toast.makeText(context, "hello"+id, Toast.LENGTH_SHORT).show();
        new GetLongLat("E").execute();
        context.startService(new Intent(context,BService.class));
    }
    public class GetLongLat extends AsyncTask<String, String, String> {
        Connection conn;
        ResultSet rs = null, rs1 = null;
//        ProgressDialog progressDialog;
        DBConnection dbConnection = new DBConnection();
        String ret = "", flag = "";


        public GetLongLat(String flag) {
            this.flag = flag;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String res) {
//            progressDialog.dismiss();
            if (res.equalsIgnoreCase("1")) {


                //Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT).show();
            } else {
//                Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT).show();
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
