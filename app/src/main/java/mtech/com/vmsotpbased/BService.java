package mtech.com.vmsotpbased;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresApi;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.provider.Telephony.Mms.Outbox.CONTENT_URI;

/**
 * Created by cd02407 on 10/5/2018.
 */

public class BService extends Service {
    public static final String BROADCAST_ACTION = "mtech.com.intentt";
    Handler refreshHandler;
    Runnable runnable;
    String visitorName =null,purpose=null,AcDate=null,Srno=null;
    private Intent intent;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    String email=null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate(){
        sharedpreferences = getSharedPreferences("sp", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        email  = sharedpreferences.getString("Email",null);
        intent = new Intent(BROADCAST_ACTION);
        createNotification();

        //  maap();
        super.onCreate();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void showName(ResultSet rs, String flag) {

        try{
            String a = null;
            if(flag.equalsIgnoreCase("E")){
                if(rs.next()) {
                    visitorName = rs.getString("VisitorName");
                    purpose = rs.getString("Purpose");

                    //=====
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
                    AcDate= dateOutput + " at " + timeOutput;
                    //=====

                    Srno= rs.getString("Id");
                     notify1();

                }else{
//                    new runService("E").execute();
                }
            }
              }catch(Exception e){
                 e.printStackTrace();
        }
    }

//======
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
private void notify1(){

        Intent intent = new Intent(this, NotificationReceiverActivity.class);
        intent.putExtra("visitorName", visitorName);
        intent.putExtra("purpose", purpose);
        intent.putExtra("AcDate", AcDate);
        intent.putExtra("Id", Srno);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        // Build notification
        // Actions are just fake
        Notification noti = new Notification.Builder(this)
                .setContentTitle("Visitor: "+visitorName)
                .setContentText("" + purpose).setSmallIcon(R.drawable.purpose)
                .setContentIntent(pIntent)
                .setDeleteIntent(createOnDismissedIntent(this, 000))
                .addAction(R.drawable.purpose, "On : " + AcDate, pIntent).build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

            noti.defaults |= Notification.DEFAULT_SOUND;
            noti.defaults |= Notification.DEFAULT_VIBRATE;
            notificationManager.notify(0, noti);

    stopService(new Intent(this, BService.class));

}

    private PendingIntent createOnDismissedIntent(Context context, int notificationId) {
        Intent intent = new Intent(context, MyBroadcastReceiver.class);
//        intent.putExtra("email", email);
        intent.putExtra("visitorName", visitorName);
        intent.putExtra("purpose", purpose);
        intent.putExtra("AcDate", AcDate);
        intent.putExtra("Id", Srno);

        intent.putExtra("com.my.app.notificationId", notificationId);

        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context.getApplicationContext(),
                        notificationId, intent, 0);
        return pendingIntent;
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void createNotification(){

//        refreshHandler = new Handler();
//                    runnable = new Runnable(){
//                        @Override
//                        public void run() {
                        //    Toast.makeText(BService.this, "Background service", Toast.LENGTH_SHORT).show();
                            new runService("E").execute();
//                            refreshHandler.postDelayed(this, 30 * 1000);
//                        }
//                    };
//                    refreshHandler.postDelayed(runnable, 30 * 1000);
    }
    //========================================================
    //=================================================================================
    public class runService extends AsyncTask<String, String, String>{
        Connection conn;
        ResultSet rs=null, rs1=null;
        ProgressDialog progressDialog;
        DBConnection dbConnection = new DBConnection();
        String ret = "", flag = "";

        public runService(String flag){
            this.flag = flag;
        }

        @Override
        protected void onPreExecute(){
//            progressDialog = new ProgressDialog(BService.this);
//            progressDialog.setMessage("Please wait...");
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String res) {
//            progressDialog.dismiss();
            if (res.equalsIgnoreCase("1")){

                 showName(rs, flag);
            //   Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT).show();
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
                } else {
                    if (flag.equalsIgnoreCase("E")){

                        String query = "SELECT TOP 1 VA.Id,VA.VisitorName,VA.Purpose,VA.AcDate, count(VA.VisitorName)  AS CountApp " +
                                " FROM VisitorAppointment  AS VA\n" +
                                " INNER JOIN VisitorStaff VS\n" +
                                " ON \n" +
                                " VA.MeetPerson = VS.StaffName\n" +
                                " WHERE VS.Email='"+email+"' AND checkAppointment='0'\n" +
                                " GROUP BY VisitorName, Purpose, AcDate, VA.Id\n";

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

    public void servicee(){
        startService(new Intent(this, BService.class));
    }
}

