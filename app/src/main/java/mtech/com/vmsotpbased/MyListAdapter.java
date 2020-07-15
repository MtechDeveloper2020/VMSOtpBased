package mtech.com.vmsotpbased;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> day;
    private final ArrayList<String> month;
    private final ArrayList<String> totime;
    private final ArrayList<String> purpose;
    private final ArrayList<String> visitorname;
    private final ArrayList<String> Id;
    private final ArrayList<String> phone;
    Button accept,reject;
    String SrNo = null,message = null ,CreationDate = null;
    EditText et_name,meetTime;
    String idd = null,mobno = null,meetingTime = null;
    EditText msg;
    ConnectionDetector cd;
    Boolean isInternetPresent;
    String TransactionLog_GetJSON;
    Model_URIs URLs = new Model_URIs();
    JSONParser_Post jsnp_post = new JSONParser_Post();
    Boolean ar_flag = false;
//    private final Integer[] imgid;

    public MyListAdapter(Activity context, ArrayList<String> day, ArrayList<String> month,
                         ArrayList<String> totime, ArrayList<String> purpose, ArrayList<String> visitorname, ArrayList<String> Id, ArrayList<String> phone) {
        super(context, R.layout.mylist, day);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.day = day;
        this.month = month;
        this.totime = totime;
        this.purpose = purpose;
        this.visitorname = visitorname;
        this.Id = Id;
        this.phone = phone;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public View getView(final int position, View view, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.mylist, null, true);

        TextView dayy = (TextView) rowView.findViewById(R.id.txt_day);
        TextView monthh = (TextView) rowView.findViewById(R.id.txt_month);
        TextView totimee = (TextView) rowView.findViewById(R.id.txt_to_time);

        TextView purposee = (TextView) rowView.findViewById(R.id.purpose);
        TextView visitornamee = (TextView) rowView.findViewById(R.id.visitorName);
        Button acceptt = (Button) rowView.findViewById(R.id.btn_accept);
        Button rejectt = (Button) rowView.findViewById(R.id.btn_reject);

        dayy.setText("" + day.get(position));
        monthh.setText("" + month.get(position));
        totimee.setText("" + totime.get(position));
        purposee.setText("" + purpose.get(position));
        visitornamee.setText("" + visitorname.get(position));

//        btn = (Button) rowView.findViewById(R.id.button);


        acceptt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
//                        Toast.makeText(getContext(), "STill TO DO   "+position, Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                LayoutInflater inflater = LayoutInflater.from(getContext());
                View dialogView = inflater.inflate(R.layout.accept_alert_dialog,null);

                // Specify alert dialog is not cancelable/not ignorable
                builder.setCancelable(false);

                // Set the custom layout as alert dialog view
                builder.setView(dialogView);

                Button btn_positive = (Button) dialogView.findViewById(R.id.dialog_positive_btn);
                Button btn_negative = (Button) dialogView.findViewById(R.id.dialog_negative_btn);
                et_name = (EditText) dialogView.findViewById(R.id.message);
                meetTime = (EditText) dialogView.findViewById(R.id.meetingTime);

                final AlertDialog dialog = builder.create();

                btn_positive.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        dialog.cancel();
                        message= et_name.getText().toString();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat dateFormat1 = new SimpleDateFormat("HH:mm:ss");
                        String Date = dateFormat.format(new Date());
                        String Time = dateFormat1.format(new Date());
                        CreationDate = Date + " " + Time;
                        idd = Id.get(position);
                        mobno= phone.get(position);
                        meetingTime= meetTime.getText().toString();
//                        Toast.makeText(getContext(), "mob: "+mobno, Toast.LENGTH_SHORT).show();
                        if(!message.equalsIgnoreCase("")){
                            sendMsg();
                        }else{
                            Toast.makeText(getContext(), "Please Enter Message !", Toast.LENGTH_SHORT).show();
                        }


                    }
                });

                btn_negative.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){

                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        //=====reject button========================================================================

        rejectt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
//                        Toast.makeText(getContext(), "STill TO DO   "+position, Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                LayoutInflater inflater =LayoutInflater.from(getContext());
                View dialogView = inflater.inflate(R.layout.custom_alert_dialog,null);

                // Specify alert dialog is not cancelable/not ignorable
                builder.setCancelable(false);

                // Set the custom layout as alert dialog view
                builder.setView(dialogView);

                Button btn_positive = (Button) dialogView.findViewById(R.id.dialog_positive_btn);
                Button btn_negative = (Button) dialogView.findViewById(R.id.dialog_negative_btn);
                et_name = (EditText) dialogView.findViewById(R.id.message);

                final AlertDialog dialog = builder.create();

                btn_positive.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        dialog.cancel();
                        message= et_name.getText().toString();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat dateFormat1 = new SimpleDateFormat("HH:mm:ss");
                        String Date = dateFormat.format(new Date());
                        String Time = dateFormat1.format(new Date());
                        CreationDate = Date + " " + Time;
                        idd = Id.get(position);
                        mobno= phone.get(position);
//                        Toast.makeText(getContext(), "mob: "+mobno, Toast.LENGTH_SHORT).show();
                        ar_flag=true;
                        if(!message.equalsIgnoreCase("")){
                            sendMsg();
                        }else{
                            Toast.makeText(getContext(), "Please Enter Message !", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                btn_negative.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){

                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });


        //=====reject button========================================================================

        return rowView;
    }

    private void sendMsg() {
        cd = new ConnectionDetector(getContext());
        isInternetPresent = cd.isConnectingToInternet();

        if (isInternetPresent == true){

            JSONObject jsonObj = new JSONObject();

            try {

                jsonObj.put("msg", ""+message);
                jsonObj.put("mobile", mobno);
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

    private void showName(ResultSet rs, ResultSet rs1, String flag){
        try {

            if (flag.equalsIgnoreCase("D")){
                try {
                    Toast.makeText(getContext(), "Rejected !", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getContext(),AdminMainActivity.class);
                    ((AdminMainActivity) getContext()).finish();

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
           else if (flag.equalsIgnoreCase("E")){
                try {
                    Intent i = new Intent(getContext(),AdminMainActivity.class);
                    getContext().startActivity(i);
                    ((AdminMainActivity) getContext()).finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (flag.equalsIgnoreCase("R")){
                try {
                    ar_flag=false;
                    Intent i = new Intent(getContext(),AdminMainActivity.class);
                    getContext().startActivity(i);
                    ((AdminMainActivity) getContext()).finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
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
            progressDialog = new ProgressDialog(getContext());
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
                Toast.makeText(getContext(), res, Toast.LENGTH_SHORT).show();
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
                    ret = "Internet connection Error";
                } else {
                    if (flag.equalsIgnoreCase("E")) {

                        String sql = "UPDATE dbo.VisitorAppointment SET checkAppointment = ?,StaffMessage=?,MeetingTime=? WHERE Id = ? ";
                        PreparedStatement preparedStatement = conn.prepareStatement(sql);
                        preparedStatement.setString(1, "2");
                        preparedStatement.setString(2, message);
                        preparedStatement.setString(3, meetingTime);
                        preparedStatement.setString(4, idd);
                        int i = preparedStatement.executeUpdate();
                        ret = "" + i;
                    }
                    else  if (flag.equalsIgnoreCase("R")) {

                        String sql = "UPDATE dbo.VisitorAppointment SET checkAppointment = ?,StaffMessage=?  WHERE Id = ? ";
                        PreparedStatement preparedStatement = conn.prepareStatement(sql);
                        preparedStatement.setString(1, "3");
                        preparedStatement.setString(2, message);
                        preparedStatement.setString(3, idd);
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

    //==============================================================================================
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
            Toast.makeText(getContext(), "An unexpected error occurred", Toast.LENGTH_LONG).show();
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void result){

        progressDialog.dismiss();
        try {
            if (final_out1.equalsIgnoreCase("")) {
                Toast.makeText( getContext(), "Message not Sent", Toast.LENGTH_SHORT).show();
            }
            else {
                //attribute success then save else error message
//                Toast.makeText(context, ""+final_out1, Toast.LENGTH_SHORT).show();
                Toast.makeText( getContext(), "Message Sent", Toast.LENGTH_SHORT).show();
               if(ar_flag){

                   new GetLongLat("R").execute();

               }else{
                   new GetLongLat("E").execute();
               }

            }
//                    Toast.makeText(getApplicationContext(), final_out1, Toast.LENGTH_LONG).show();
        } catch (Exception e){
            Toast.makeText(getContext(), "Error occurred", Toast.LENGTH_LONG).show();
        }
        super.onPostExecute(result);
    }
    @Override
    protected void onPreExecute(){
        // TODO Auto-generated method stub
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        //super.onPreExecute();
    }
}





}