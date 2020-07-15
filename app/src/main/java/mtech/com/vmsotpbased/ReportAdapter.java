package mtech.com.vmsotpbased;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class ReportAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> name;
    private final ArrayList<String> purpose;
    private final ArrayList<String> InTime;
    private final ArrayList<String> AccRej;
    private final ArrayList<String> stafmsg;
    private final ArrayList<String> meetTime;

    Button accept,reject;
    String SrNo = null;
    EditText et_name;
    String idd = null;
    EditText msgg;
//    private final Integer[] imgid;

    public ReportAdapter(Activity context, ArrayList<String> name, ArrayList<String> purpose, ArrayList<String> InTime, ArrayList<String> AccRej,
                         ArrayList<String> staffmsg, ArrayList<String> meetTime){
        super(context, R.layout.reportlist, name);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.name = name;
        this.purpose = purpose;
        this.InTime = InTime;
        this.AccRej = AccRej;
        this.stafmsg = staffmsg;
        this.meetTime = meetTime;

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public View getView(final int position, View view, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.reportlist, null, true);

        TextView namee = (TextView) rowView.findViewById(R.id.name);
        TextView purposee = (TextView) rowView.findViewById(R.id.purpose);
        TextView timee = (TextView) rowView.findViewById(R.id.time);
        TextView ar = (TextView) rowView.findViewById(R.id.accrej);
        TextView msgg = (TextView) rowView.findViewById(R.id.msg);
        TextView meetTimee = (TextView) rowView.findViewById(R.id.meetTime);

        namee.setText("" + name.get(position));
        purposee.setText("" + purpose.get(position));
        timee.setText("" + InTime.get(position));
        msgg.setText("" + stafmsg.get(position));
        meetTimee.setText("" + meetTime.get(position));

        String status= AccRej.get(position);
        if(status.equalsIgnoreCase("2")){
            ar.setText("Accepted");
        }else if(status.equalsIgnoreCase("0")){
            ar.setText("Pending");
        }
        else{
            ar.setText("Rejected");
        }


        return rowView;

    }
}