package mtech.com.vmsotpbased;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by cd02407 on 7/5/2018.
 */

public class Admin_Register extends Activity {

    EditText admin_edt_code,admin_edt_pass,staffname,mobile;
    String staff=null,mob=null,email=null,pass=null;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_register);
        admin_edt_code = (EditText) findViewById(R.id.admin_edt_code);
        admin_edt_pass = (EditText) findViewById(R.id.admin_edt_password);
        staffname = (EditText) findViewById(R.id.staffname);
        mobile = (EditText) findViewById(R.id.phone);

    }
    public void login(View v){
        Intent i = new Intent(this, AdminLogin.class);
        startActivity(i);
        finish();
    }
    public void register(View v){
        staff= staffname.getText().toString();
        mob= mobile.getText().toString();
        email= admin_edt_code.getText().toString();
        pass= admin_edt_pass.getText().toString();

        if(staff!= null){

            if(mob != null){

                if(email != null){

                    if(pass != null ){

                        new GetLongLat("E").execute();

                    }else {

                        Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();

                    }
                }else{

                    Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show();

                }
            }else{

                Toast.makeText(this, "Enter Mobile No.", Toast.LENGTH_SHORT).show();

            }
        }else{

            Toast.makeText(this, "Enter Staff Name", Toast.LENGTH_SHORT).show();
        }

    }

    //======================================== Perform_log Task =========================

    private void showName(ResultSet rs, String flag){

        try {
            if (flag.equalsIgnoreCase("S")){
                Toast.makeText(this, "Staff Saved Successfully !", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, AdminLogin.class);
                startActivity(i);
                finish();
            }
           else if (flag.equalsIgnoreCase("E")){
                try{
                   if(rs.next()){
                       Toast.makeText(this, "Staff Already Exist !", Toast.LENGTH_SHORT).show();
                   }else{
                       new GetLongLat("S").execute();
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
            progressDialog = new ProgressDialog(Admin_Register.this);
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

                        String query = "Select * from VisitorStaff where Phone='"+mob+"'  OR  Email = '"+email+"'   ";
                        Statement stmt = conn.createStatement();
                        rs = stmt.executeQuery(query);
                        ret = "1";
                    }

                    else if(flag.equalsIgnoreCase("S")){
                        PreparedStatement stmt = conn.prepareStatement("insert into VisitorStaff" + "(StaffName, Phone, Email,  Password, VisitorStaff)" + " values(?,?,?,?,?)");
                        stmt.setString(1, staff);
                        stmt.setString(2, mob);
                        stmt.setString(3, email);
                        stmt.setString(4, pass);
                        stmt.setString(5, "S");

                        int i = stmt.executeUpdate();
                        ret = "" + i;

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
