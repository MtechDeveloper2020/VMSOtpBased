package mtech.com.vmsotpbased;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class ApprovedFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ApprovedListAdapter adapter = null;
    ArrayList<String> day;
    ArrayList<String> month;
    ArrayList<String> totime;
    ArrayList<String> fromtime;
    ArrayList<String> purpose;
    ArrayList<String> visitorName;
    ArrayList<String> Id;
    ListView list;
    String email=null,passwd=null;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public ApprovedFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static PendingFragment newInstance(String param1, String param2) {
        PendingFragment fragment = new PendingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=  inflater.inflate(R.layout.fragment_approved, container, false);
        sharedpreferences = getActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        email  = sharedpreferences.getString("Email",null);
        passwd = sharedpreferences.getString("Pass",null);
        day = new ArrayList<>();
        month = new ArrayList<>();
        totime = new ArrayList<>();
        fromtime = new ArrayList<>();
        purpose = new ArrayList<>();
        visitorName = new ArrayList<>();
        Id = new ArrayList<>();
        list = (ListView) view.findViewById(R.id.list);
        new GetLongLat("E").execute();

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void showName(ResultSet rs, ResultSet rs1, String flag) {
        try {
            if (flag.equalsIgnoreCase("E")) {
                try {

                    while (rs.next()) {


                        String a = rs.getString("VisitorName");
                        visitorName.add(a);
                        String b = rs.getString("Purpose");
                        purpose.add(b);
                        String c = rs.getString("Id");
                        Id.add(c);

                        String dayPattern = "dd";
                        String monthPattern = "MM";
                        String timePattern = "HH:mm a";
                        Date today;
                        String monthh,dayy;
                        SimpleDateFormat simpleDateFormat, sp,dayDateFormat;
                        dayDateFormat = new SimpleDateFormat(dayPattern);
                        simpleDateFormat = new SimpleDateFormat(monthPattern);
                        Time t;
                        String timeOutput;
                        sp = new SimpleDateFormat(timePattern);
                        t = rs.getTime("AcDate");
                        timeOutput = sp.format(t);
                        today = rs.getDate("AcDate");
                        monthh = simpleDateFormat.format(today);
                        dayy = dayDateFormat.format(today);

                        day.add(dayy);
                        month.add(monthh);
                        totime.add(timeOutput);

                    }
                    adapter = new ApprovedListAdapter(getActivity(), day, month, totime, purpose, visitorName,Id);
                    list.setAdapter(adapter);
//                    Log.e("asdas",maintitle.toString());

                } catch (Exception e) {

                    e.toString();
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    //================================================================
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
        protected void onPostExecute(String res) {
            progressDialog.dismiss();
            if (res.equalsIgnoreCase("1")) {

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
                        String query = "SELECT VA.Id,VA.VisitorName,VA.Purpose,VA.AcDate,VS.Email FROM VisitorAppointment AS VA  \n" +
                                "INNER JOIN VisitorStaff VS ON \n" +
                                "VA.MeetPerson = VS.StaffName \n" +
                                " WHERE VS.Email='"+email+"' \n" +
                                "AND VA.checkAppointment='2' ORDER BY VA.Id DESC ";
                        Statement stmt = conn.createStatement();
                        rs = stmt.executeQuery(query);
                        ret = "1";
                    }

                }
            } catch (Exception ex) {
                ret = ex.getMessage();
            }
            return ret;
        }
    }

    //================================================================



}
