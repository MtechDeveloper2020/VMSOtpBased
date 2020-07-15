package mtech.com.vmsotpbased;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by cd02407 on 7/5/2018.
 */

public class VisitorLogin extends Activity{
    EditText email,password;
    SharedPreferences sharedpreferences;
    String adminEmail=null,adminPass=null;
    SharedPreferences.Editor editor;
    Button newuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visitor_login);
        email= (EditText) findViewById(R.id.email);
        password= (EditText) findViewById(R.id.password);
        newuser= (Button) findViewById(R.id.new_user);
        sharedpreferences = getSharedPreferences("sp", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        newuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(VisitorLogin.this, Visitor_Register.class);
                startActivity(i);
                finish();
            }
        });


    }

    public void signIn(View v){
        adminEmail= email.getText().toString();
        adminPass = password.getText().toString();

        if(adminEmail != null) {
            if(adminPass != null){
                new GetLongLat("E").execute();
            }else{
                Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show();
        }
    }


    //======================================== Perform_log Task =========================

    private void showName(ResultSet rs, String flag){

        try {
            if (flag.equalsIgnoreCase("E")){
                try{
                    if(rs.next()){

                        String stfname = rs.getString("StaffName");
                        editor.putString("Email", adminEmail);
                        editor.putString("Pass", adminPass);
                        editor.putString("staffname", stfname);
                        editor.putString("account", "visitor");
                        editor.putBoolean("sf", true);
                        editor.commit();
                        Intent i = new Intent(VisitorLogin.this, VisitorActivity.class);
                        startActivity(i);
                        finish();
                    }else{
                        Toast.makeText(this, "Invalid Credentials !", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }


        } catch (Exception e){
            e.printStackTrace();

            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
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
            progressDialog = new ProgressDialog(VisitorLogin.this);
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

                        String query = "Select * from VisitorStaff where Email='"+adminEmail+"'  AND  Password = '"+adminPass+"'";
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

//---------------test------------------


}
