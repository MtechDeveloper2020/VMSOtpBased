package mtech.com.vmsotpbased;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class VisitorActivity extends AppCompatActivity {

        public static final String MyPREFERENCES = "MyPrefs" ;
        EditText ftime,ttime,selectDate,name,address,phone,email,purpose,verifyotp;
        Spinner meetperson;
        String m, d, mf, df;
        String vfullname=null,phno=null,verify=null;
        ConnectionDetector cd;
        Boolean isInternetPresent;
        ArrayAdapter<String> person;
        String TransactionLog_GetJSON;
        Model_URIs URLs = new Model_URIs();
        JSONParser_Post jsnp_post = new JSONParser_Post();
        String otp=null;
        String vname=null,vphone=null,vmail=null,curdate=null,vpurpose=null,vmperson=null;
        CheckBox mCbShowPwd;
        Button verifyotpbutton,submitButton;
        SharedPreferences.Editor editor;
        SharedPreferences sharedpreferences;
        private int mYear, mMonth, mDay, mHour, mMinute;
        Button btn_sendotp,btn_verifyotp;
         String  CreationDate;
        @Override
        protected void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_visitor);
            ftime= (EditText)findViewById(R.id.editText15);
            ttime= (EditText)findViewById(R.id.editText16);
            selectDate= (EditText)findViewById(R.id.editText10);
            name= (EditText)findViewById(R.id.editText1);
            phone= (EditText)findViewById(R.id.editText7);
            email= (EditText)findViewById(R.id.editText8);
            purpose= (EditText)findViewById(R.id.editText11);
            meetperson= (Spinner)findViewById(R.id.editText12);
            verifyotp= (EditText)findViewById(R.id.verifyotp);
            mCbShowPwd = (CheckBox) findViewById(R.id.cbShowPwd);
            verifyotpbutton = (Button) findViewById(R.id.verifyotpb);
            submitButton = (Button) findViewById(R.id.submit);
            btn_sendotp = (Button) findViewById(R.id.sendotp);

            cd = new ConnectionDetector(VisitorActivity.this);
            isInternetPresent = cd.isConnectingToInternet();

            sharedpreferences = getSharedPreferences("sp", Context.MODE_PRIVATE);
            editor = sharedpreferences.edit();
            editor.putString("account", "visitor");
            editor.commit();

            if (isInternetPresent){
                new GetLongLat("E").execute();
            } else {
                Toast.makeText(this, "Internet Connection Error", Toast.LENGTH_SHORT).show();
            }
            mCbShowPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                    // checkbox status is changed from uncheck to checked.
                    if (!isChecked){
                        // show password
                        verifyotp.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    } else {
                        // hide password
                        verifyotp.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    }
                }
            });

            ftime.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View arg0){
                    // TODO Auto-generated method stub

                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;

                    mTimePicker = new TimePickerDialog(
                             VisitorActivity.this,
                            new TimePickerDialog.OnTimeSetListener(){

                                @Override
                                public void onTimeSet(TimePicker timePicker,
                                                      int selectedHour, int selectedMinute){
                                    String Hour = "";
                                    String minitus = "";
                                    if (String.valueOf(selectedHour).length() == 1){
                                        Hour = "0" + String.valueOf(selectedHour);
                                    } else {
                                        Hour = String.valueOf(selectedHour);
                                    }

                                    if (String.valueOf(selectedMinute).length() == 1){
                                        minitus = "0"
                                                + String.valueOf(selectedMinute);
                                    } else {
                                        minitus = String.valueOf(selectedMinute);
                                    }
                                    // check time is befoe time or not
                                    Calendar c = Calendar.getInstance();

                                    SimpleDateFormat df = new SimpleDateFormat(
                                            "dd-MM-yyyy");
                                    // SimpleDateFormat ttf = new SimpleDateFormat(
                                    // "KK:mm");
                                    SimpleDateFormat tf = new SimpleDateFormat(
                                            "HH:mm");
                                    String t = tf.format(c.getTime());
                                    Log.e("time", t);
                                    Date one = new Date();
                                    Date two = new Date();
                                    try {
                                        one = tf.parse(t);

                                        two = tf.parse(Hour + ":" + minitus);
                                    } catch (ParseException e){
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }

                                    if (df.format(c.getTime()).equalsIgnoreCase(
                                            selectDate.getText()
                                                    .toString())){
                                        if (two.before(one)){
                                            Toast.makeText(
                                                     VisitorActivity.this,
                                                    "Invalid From time",
                                                    Toast.LENGTH_SHORT).show();
                                            ftime.setText("");
                                        } else {
                                            ftime.setText(Hour + ":"
                                                    + minitus);
                                        }
                                    } else {
                                        ftime.setText(Hour + ":"
                                                + minitus);
                                    }

                                }
                            }, hour, minute, true);// Yes 24 hour time
                    mTimePicker.setTitle("From To");
                    mTimePicker.show();

                }
            });
            ttime.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub

                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;

                    mTimePicker = new TimePickerDialog(
                             VisitorActivity.this,
                            new TimePickerDialog.OnTimeSetListener(){

                                @Override
                                public void onTimeSet(TimePicker timePicker,
                                                      int selectedHour, int selectedMinute){
                                    String Hour = "";
                                    String minitus = "";
                                    if (String.valueOf(selectedHour).length() == 1){
                                        Hour = "0" + String.valueOf(selectedHour);
                                    } else {
                                        Hour = String.valueOf(selectedHour);
                                    }

                                    if (String.valueOf(selectedMinute).length() == 1) {
                                        minitus = "0"
                                                + String.valueOf(selectedMinute);
                                    } else {
                                        minitus = String.valueOf(selectedMinute);
                                    }

                                    // check time is befoe time or not
                                    Calendar c = Calendar.getInstance();

                                    SimpleDateFormat df = new SimpleDateFormat(
                                            "dd-MM-yyyy");
                                    // SimpleDateFormat ttf = new SimpleDateFormat(
                                    // "KK:mm");
                                    SimpleDateFormat tf = new SimpleDateFormat(
                                            "HH:mm");
                                    String t = tf.format(c.getTime());
                                    Log.e("time", t);
                                    Date one = new Date();
                                    Date two = new Date();
                                    try {
                                        one = tf.parse(t);

                                        two = tf.parse(Hour + ":" + minitus);
                                    } catch (ParseException e){
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }

                                    if (df.format(c.getTime()).equalsIgnoreCase(
                                            selectDate.getText()
                                                    .toString())) {
                                        if (two.before(one)) {
                                            Toast.makeText(
                                                     VisitorActivity.this,
                                                    "Invalid To time",
                                                    Toast.LENGTH_SHORT).show();
                                            ttime.setText("");
                                        } else {
                                            ttime.setText(Hour + ":"
                                                    + minitus);
                                        }
                                    } else {
                                        ttime.setText(Hour + ":"
                                                + minitus);
                                    }

                                }
                            }, hour, minute, true);// Yes 24 hour time
                    mTimePicker.setTitle("To Time");
                    mTimePicker.show();

                }
            });
            selectDate.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub

                    // TODO Auto-generated method stub
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);

                    // Launch Date Picker Dialog
                    DatePickerDialog dpd = new DatePickerDialog(
                             VisitorActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    // Display Selected date in textbox
                                    SimpleDateFormat dd = new SimpleDateFormat("dd");
                                    SimpleDateFormat dm = new SimpleDateFormat("MM");
                                    SimpleDateFormat dy = new SimpleDateFormat("yyyy");

                                    int day = Integer.parseInt(dd.format(c
                                            .getTime()));
                                    int month = Integer.parseInt(dm.format(c
                                            .getTime()));
                                    int yearr = Integer.parseInt(dy.format(c
                                            .getTime()));

                                    if (dayOfMonth < 10){
                                        df = "0" + dayOfMonth;
                                    } else {
                                        df = String.valueOf(dayOfMonth);
                                    }
                                    if ((monthOfYear + 1 < 10)){
                                        mf = "0" + (monthOfYear + 1);
                                    } else {
                                        mf = String.valueOf(monthOfYear + 1);
                                    }
                                    String selected_date = df + "-" + mf + "-" + year;
                                    Log.e("selected date", "" + selected_date);

                                    String current_date = String.valueOf(day) + "-" + String.valueOf(month) + "-" + String.valueOf(yearr);
                                    Log.e("selected date", "" + current_date);
                                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                                    Date cur = new Date();
                                    Date sel = new Date();
                                    try {
                                        cur = df.parse(current_date);
                                        sel = df.parse(selected_date);
                                    } catch (ParseException e1) {
                                        // TODO Auto-generated catch block
                                        e1.printStackTrace();
                                    }
                                    if (sel.after(cur) || cur.equals(sel)) {
                                        selectDate.setText(selected_date);
                                    } else {
                                        Toast.makeText(getApplicationContext(),
                                                "Selected Date Is Not Valid",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, mYear, mMonth, mDay);
                    dpd.show();
                }
            });
        }
        public void submit(View v){
            vname= name.getText().toString();
            vphone= phone.getText().toString();
            vmail= email.getText().toString();
            curdate= selectDate.getText().toString();
            vpurpose= purpose.getText().toString();
            vmperson= meetperson.getSelectedItem().toString();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("HH:mm:ss");
            String Date = dateFormat.format(new Date());
            String Time = dateFormat1.format(new Date());
            CreationDate = Date + " " + Time;
            if(!vpurpose.equalsIgnoreCase("")){
                if(!vmperson.equalsIgnoreCase("Select Person to Meet")){

                    new GetLongLat("S").execute();

                }else{
                    Toast.makeText(this, "Select person you want to meet", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "Please insert purpose", Toast.LENGTH_SHORT).show();
            }
        }

        public void reset(View v){
            name.setText(null);
            phone.setText(null);
            phone.setEnabled(true);
            verifyotp.setVisibility(View.GONE);
            verifyotp.setHint("");
            verifyotpbutton.setVisibility(View.GONE);
            verifyotp.setHint("");
            mCbShowPwd.setVisibility(View.GONE);
            mCbShowPwd.setHint("");
            purpose.setVisibility(View.GONE);
            purpose.setHint("");
            meetperson.setVisibility(View.GONE);
            submitButton.setVisibility(View.GONE);

            name.setEnabled(true);
            phone.setEnabled(true);
            verifyotp.setEnabled(true);
            name.setText(null);
            phone.setText(null);
            verifyotp.setText(null);
            verifyotpbutton.setEnabled(true);
            btn_sendotp.setEnabled(true);

        }
        public void verifyotp(View v){
            verifyOTPP();

        }

        private void verifyOTPP() {
            verify = verifyotp.getText().toString();
            phno = phone.getText().toString();

            if (verify.length() == 6) {
                new  GetLongLat("V").execute();
            } else {
                Toast.makeText(this, "Please enter 6 digit otp", Toast.LENGTH_SHORT).show();
            }
        }
        public void sendotp(View v){
            phno= phone.getText().toString();
            vfullname=name.getText().toString();
            if(phno.length() == 10){
                if(!vfullname.equalsIgnoreCase("")){
                    authenticate();
                } else{

                    Toast.makeText(this, "Please Enter Name", Toast.LENGTH_SHORT).show();
                }
            }else{

                Toast.makeText(this, "Invalid Mobile Number", Toast.LENGTH_SHORT).show();
            }
        }

        private void authenticate(){
//        new GetLongLat("C").execute();
//        phone.setEnabled(false);
//        verifyotpbutton.setVisibility(View.VISIBLE);
//        verifyotp.setVisibility(View.VISIBLE);
//        mCbShowPwd.setVisibility(View.VISIBLE);

            int randomPin   =(int)(Math.random()*900000)+100000;
            otp  =String.valueOf(randomPin);
            isInternetPresent = cd.isConnectingToInternet();
            phno = phone.getText().toString();
            //========
//            new GetLongLat("C").execute();                                        //do visible
//            phone.setEnabled(false);
//            verifyotpbutton.setVisibility(View.VISIBLE);
//            verifyotp.setVisibility(View.VISIBLE);
//            mCbShowPwd.setVisibility(View.VISIBLE);

        if (isInternetPresent == true){

            JSONObject jsonObj = new JSONObject();

            try {

                jsonObj.put("msg", otp+" is your Visitor verification code. ");
                jsonObj.put("mobile", phno);
                jsonObj.put("Respose", "TRUE");


                TransactionLog_GetJSON = jsonObj.toString();

              new Perform_log(URLs.getPerform_URI(), TransactionLog_GetJSON).execute();
             //  new Perform_log("", TransactionLog_GetJSON).execute();

            }catch (final Exception e)
            {
                e.printStackTrace();

            }
        }
        }
        //======================================== Perform_log Task ==================================

        private void showName(ResultSet rs, String flag){

            try {
                if (flag.equalsIgnoreCase("E")){
                    ArrayList<String> country = new ArrayList<String>();
                    country.add("Select Person to Meet");
                    while (rs.next()) {
                        country.add(rs.getString("StaffName"));
                    }
                    person = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, country);
                    meetperson.setAdapter(person);
                }
                else if(flag.equalsIgnoreCase("C")){
                    if(rs.next()){
                        phno= phone.getText().toString();
                        new GetLongLat("D").execute();
                        new GetLongLat("O").execute();
                    }else{
                        new GetLongLat("O").execute();
                    }
                }
//            else if(flag.equalsIgnoreCase("D")){
//                new GetLongLat("O").execute();
//
//               }
                else if(flag.equalsIgnoreCase("O")){

                    Toast.makeText(getApplicationContext(), "OTP Saved", Toast.LENGTH_LONG).show();
                    otp=null;
                }
                else if(flag.equalsIgnoreCase("V")){
                    String verify1=null;
                    while(rs.next()) {
                        verify1 = rs.getString("Otp");
                    }
                    if(verify.equalsIgnoreCase(verify1)){
                        // Toast.makeText(this, "OTP matched", Toast.LENGTH_SHORT).show();
                        purpose.setVisibility(View.VISIBLE);
                        meetperson.setVisibility(View.VISIBLE);
                        submitButton.setVisibility(View.VISIBLE);
                        name.setEnabled(false);
                        phone.setEnabled(false);
                        verifyotp.setEnabled(false);
                        verifyotpbutton.setEnabled(false);
                        btn_sendotp.setEnabled(false);


                        new GetLongLat("DO").execute();

                    }else{
                        Toast.makeText(this, "OTP not matched", Toast.LENGTH_SHORT).show();
                    }
                }
                else if(flag.equalsIgnoreCase("S")) {

                    Toast.makeText(getApplicationContext(), "Appointment Saved", Toast.LENGTH_LONG).show();
                    name.setText(null);
                    phone.setText(null);
                    phone.setEnabled(true);
                    verifyotp.setVisibility(View.GONE);
                    verifyotp.setHint("");
                    verifyotpbutton.setVisibility(View.GONE);
                    verifyotp.setHint("");
                    mCbShowPwd.setVisibility(View.GONE);
                    mCbShowPwd.setHint("");
                    purpose.setVisibility(View.GONE);
                    purpose.setHint("");
                    meetperson.setVisibility(View.GONE);
                    submitButton.setVisibility(View.GONE);
                    purpose.setText(null);
                    meetperson.setSelection(0);

                    name.setEnabled(true);
                    phone.setEnabled(true);
                    verifyotp.setEnabled(true);
                    name.setText(null);
                    phone.setText(null);
                    verifyotp.setText(null);
                    verifyotpbutton.setEnabled(true);
                    btn_sendotp.setEnabled(true);

                    Intent i = new Intent(VisitorActivity.this,ReportActivity.class);
                    startActivity(i);
                }

            } catch (Exception e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            }
        }

    //--------actionbar----------------//
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.visitors_logout, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {

            case R.id.action_search:
                // location found
                logout();
                return true;
            case R.id.action_report:
                // location found
                report();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void report() {
        Intent i = new Intent(VisitorActivity.this, ReportActivity.class);
        startActivity(i);
//        finish();
    }

//---------------test------------------

    private void logout(){
//        Intent i = new Intent(AdminActivity.this, LocationFound.class);
//        startActivity(i);

        editor.clear();
        editor.commit();

        Intent i = new Intent(VisitorActivity.this, SelectionActivity.class);
        startActivity(i);
        finish();
    }

        public class Perform_log extends AsyncTask<Void, Integer, Void> {
            ProgressDialog progressDialog;
            String final_out1 = "";
            String URL = "";
            String jsonString = "";

            public Perform_log(String url, String json){
                this.URL = url;
                this.jsonString = json;
            }

            @Override
            protected Void doInBackground(Void... arg0) {
                // TODO Auto-generated method stub
                try {
                    final_out1 = jsnp_post.makeHttpRequest(URL, "POST", jsonString);

                } catch (Exception e) {
                    // TODO: handle exception
                    Toast.makeText(getApplicationContext(), "An unexpected error occurred", Toast.LENGTH_LONG).show();
                }
                return null;
            }
            @Override
            protected void onPostExecute(Void result){

                progressDialog.dismiss();
                try {
                    if (final_out1.equalsIgnoreCase("")) {
                        Toast.makeText( VisitorActivity.this, "OTP not Sent", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        //attribute success then save else error message
                        Toast.makeText( VisitorActivity.this, ""+final_out1, Toast.LENGTH_SHORT).show();
                        new  GetLongLat("C").execute();
                        verifyotpbutton.setVisibility(View.VISIBLE);
                        verifyotp.setVisibility(View.VISIBLE);
                        phone.setEnabled(false);
                        mCbShowPwd.setVisibility(View.VISIBLE);
                    }
//                    Toast.makeText(getApplicationContext(), final_out1, Toast.LENGTH_LONG).show();
                } catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_LONG).show();
                }
                super.onPostExecute(result);
            }
            @Override
            protected void onPreExecute(){
                // TODO Auto-generated method stub
                progressDialog = new ProgressDialog( VisitorActivity.this);
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
                //super.onPreExecute();
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
                progressDialog = new ProgressDialog( VisitorActivity.this);
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

                            String query = "Select * from VisitorStaff where VisitorStaff='S'";
                            Statement stmt = conn.createStatement();
                            rs = stmt.executeQuery(query);
                            ret = "1";
                        }
                        else if (flag.equalsIgnoreCase("DO")) {

                            PreparedStatement stmt = conn.prepareStatement("DELETE FROM VisitorOtp WHERE Phone ='"+ phno +"'");

                            //  String query = "DELETE FROM VisitorOtp WHERE Phone ='"+ phno +"'";
                            //   Statement stmt = conn.createStatement();
                            int i = stmt.executeUpdate();
                            ret = "" + i;
                        }
                        else if(flag.equalsIgnoreCase("O")){

                            PreparedStatement stmt = conn.prepareStatement("insert into VisitorOtp" + "(Phone, Otp)" + " values(?,?)");
                            stmt.setString(1, phno);
                            stmt.setString(2, otp);
                            int i = stmt.executeUpdate();
                            ret = "" + i;
                        }
                        else if(flag.equalsIgnoreCase("D")){
                            PreparedStatement stmt = conn.prepareStatement("DELETE FROM VisitorOtp WHERE Phone ='"+ phno +"'");
                            //  String query = "DELETE FROM VisitorOtp WHERE Phone ='"+ phno +"'";
                            //   Statement stmt = conn.createStatement();
                            int i = stmt.executeUpdate();
                            ret = "" + i;
                        }

                        else if(flag.equalsIgnoreCase("C")){

                            String query = "Select TOP 1 * from VisitorOtp where Phone='"+ phno +"'";
                            Statement stmt = conn.createStatement();
                            rs = stmt.executeQuery(query);
                            ret = "1";

                        }
                        else if(flag.equalsIgnoreCase("V")){

                            String query = "Select TOP 1 * from VisitorOtp where Phone='"+ phno +"'";
                            Statement stmt = conn.createStatement();
                            rs = stmt.executeQuery(query);
                            ret = "1";

                        }
                        else if(flag.equalsIgnoreCase("S")){

                            PreparedStatement stmt = conn.prepareStatement("insert into VisitorAppointment" + "(VisitorName, VisitorPhone, email,  Purpose, MeetPerson, checkAppointment,AcDate)" + " values(?,?,?,?,?,?,?)");
                            stmt.setString(1, vname);
                            stmt.setString(2, vphone);
                            stmt.setString(3, vmail);
                            stmt.setString(4, vpurpose);
                            stmt.setString(5, vmperson);
                            stmt.setString(6, "0");
                            stmt.setTimestamp(7, java.sql.Timestamp.valueOf(CreationDate));
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

    //--------actionbar----------------//


}
