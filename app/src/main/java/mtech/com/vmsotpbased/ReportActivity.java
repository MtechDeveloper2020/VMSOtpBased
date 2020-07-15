package mtech.com.vmsotpbased;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ReportActivity extends Activity{
    ListView list;
    ArrayList<String> name;
    ArrayList<String> purpose;
    ArrayList<String> InTime;
    ArrayList<String> meetTime;
    ArrayList<String> staffMsg;
    ArrayList<String> AccRej;
    ReportAdapter adapter = null;
    Handler refreshHandler;
    Runnable runnable;
    String act = null, CreationDate = null;
    ViewGroup header;
    LayoutInflater inflater;
    Button Refresh;
    TableLayout tableLayout;
    TableRow row;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        if (android.os.Build.VERSION.SDK_INT > 9){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
//        startService(new Intent(this, IncorrectService.class));

        new GetLongLat("E").execute();

//        list = (ListView) findViewById(R.id.list);
        Refresh = (Button) findViewById(R.id.refresh);

        name = new ArrayList<String>();
        purpose = new ArrayList<String>();
        InTime = new ArrayList<String>();
        AccRej = new ArrayList<String>();
        staffMsg = new ArrayList<String>();
        meetTime = new ArrayList<String>();
        //-------------------------------------------------------------------------------------------
        tableLayout = (TableLayout) findViewById(R.id.tablelayout);
        row = new TableRow(getApplicationContext());
        row.setBackgroundColor(Color.parseColor("#c0c0c0"));
        row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));
        String[] headerText = {"Sr No", "Visitor Name", "Purpose", "InTime", "Status", "Message", "Meeting Time"};
        for (String c : headerText) {

            TextView tv = new TextView(this);
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tv.setGravity(Gravity.LEFT);
            tv.setTextSize(18);
            tv.setPadding(5, 5, 5, 5);
            tv.setText(c);
            row.addView(tv);

        }
        tableLayout.addView(row);

        Refresh.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        });

//
//        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
//                // TODO Auto-generated method stub
////                if(position == 0) {
////                    //code specific to first list item
////                    Toast.makeText(getApplicationContext(),"Place Your First Option Code",Toast.LENGTH_SHORT).show();
////                }
////                else if(position == 1) {
////                    //code specific to 2nd list item
////                    Toast.makeText(getApplicationContext(),"Place Your Second Option Code",Toast.LENGTH_SHORT).show();
////                }
////
////                else if(position == 2) {
////
////                    Toast.makeText(getApplicationContext(),"Place Your Third Option Code",Toast.LENGTH_SHORT).show();
////                }
////                else if(position == 3) {
////
////                    Toast.makeText(getApplicationContext(),"Place Your Forth Option Code",Toast.LENGTH_SHORT).show();
////                }
////                else if(position == 4) {
////
////                    Toast.makeText(getApplicationContext(),"Place Your Fifth Option Code",Toast.LENGTH_SHORT).show();
////                }
//
//            }
//        });
    }

    private void showName(ResultSet rs, ResultSet rs1, String flag) {
        try {

            if(flag.equalsIgnoreCase("E")) {
                int i = 1;
                while (rs.next()) {
                    TableRow row = new TableRow(getApplicationContext());
                    row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                    row.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    String datePattern = "dd MMM yyyy";
                    String timePattern = "HH:mm a";
                    Date today;
                    String dateOutput;
                    SimpleDateFormat simpleDateFormat, sp;
                    simpleDateFormat = new SimpleDateFormat(datePattern);
                    Time t;
                    String timeOutput;
                    sp = new SimpleDateFormat(timePattern);
                    t = rs.getTime("AcDate");
                    timeOutput = sp.format(t);
                    today = rs.getDate("AcDate");
                    dateOutput = simpleDateFormat.format(today);
//                        System.out.println(datePattern + " - " + dateOutput);
//                    InTime.add(dateOutput + " at " + timeOutput);
                    String accdate=dateOutput + " at " + timeOutput;

                    String stat = rs.getString("checkAppointment");
                    if(stat.equalsIgnoreCase("0") || stat.equalsIgnoreCase("1") ){
                        stat="Pending";

                    }else if(stat.equalsIgnoreCase("2")){
                        stat="Accepted";
                    }else{
                        stat = "Rejected";
                    }

                    String[] colText = {"" + i, rs.getString("VisitorName"), rs.getString("Purpose"), accdate,stat, rs.getString("StaffMessage"),  rs.getString("MeetingTime")};

                    for (String text : colText) {

                        TextView tv = new TextView(this);
                        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT));
                        tv.setGravity(Gravity.LEFT);
                        tv.setTextSize(16);
                        tv.setPadding(5, 5, 5, 5);
                        tv.setText(text);
                        row.addView(tv);

                    }
                    tableLayout.addView(row);
                    i++;
                }
//                while (rs.next()) {
//                    String a = rs.getString("VisitorName");
//                    name.add(a);
//                    String e = rs.getString("Purpose");
//                    purpose.add(e);
//                    String ca = rs.getString("checkAppointment");
//                    AccRej.add(ca);
//                    String sm = rs.getString("StaffMessage");
//                    staffMsg.add(sm);
//                    String mt = rs.getString("MeetingTime");
//                    meetTime.add(mt);
//                    String datePattern = "dd MMM yyyy";
//                    String timePattern = "HH:mm a";
//                    Date today;
//                    String dateOutput;
//                    SimpleDateFormat simpleDateFormat, sp;
//                    simpleDateFormat = new SimpleDateFormat(datePattern);
//                    Time t;
//                    String timeOutput;
//                    sp = new SimpleDateFormat(timePattern);
//                    t = rs.getTime("AcDate");
//                    timeOutput = sp.format(t);
//                    today = rs.getDate("AcDate");
//                    dateOutput = simpleDateFormat.format(today);
////                        System.out.println(datePattern + " - " + dateOutput);
//                    InTime.add(dateOutput + " at " + timeOutput);
//                }
//                Toast.makeText(this, ""+ rpoints.get(0)+rpoints.get(1), Toast.LENGTH_SHORT).show();
//                adapter = new ReportAdapter(ReportActivity.this, name, purpose, InTime, AccRej,staffMsg,meetTime);
//                inflater = getLayoutInflater();
//                header = (ViewGroup) inflater.inflate(R.layout.sv_listheader, list, false);
//                list.addHeaderView(header);
//                list.setAdapter(adapter);

            }

            }catch(Exception e){
                e.printStackTrace();
    }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();


    }
//-------------------------------------------------------------------------

    @Override
    public void onPause() {
        super.onPause();
    }

    public class GetLongLat extends AsyncTask<String, String, String>{
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
            progressDialog = new ProgressDialog(ReportActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String res){
            progressDialog.dismiss();
            if (res.equalsIgnoreCase("1")){
                showName(rs, rs1, flag);

                //Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT).show();
            } else {
//                Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT).show();
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
                } else{
                    if (flag.equalsIgnoreCase("E")){
                        String query = "SELECT VisitorName,Purpose, AcDate,Id, checkAppointment,StaffMessage,MeetingTime FROM VisitorAppointment where convert(VARCHAR, AcDate,105) = convert(VARCHAR,getDate(),105)  " +
                                "AND ( checkAppointment='3' OR checkAppointment='2' OR checkAppointment='0' OR checkAppointment= '1' ) ORDER BY Id Desc ";
                        Statement stmt = conn.createStatement();
                        rs = stmt.executeQuery(query);
                        ret = "1";
                           }
                       }
                 }
              catch (Exception ex){
                ret = ex.getMessage();
            }
            return ret;
        }
    }
}