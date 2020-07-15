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

public class ApprovedListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> day;
    private final ArrayList<String> month;
    private final ArrayList<String> totime;
    private final ArrayList<String> purpose;
    private final ArrayList<String> visitorname;
    private final ArrayList<String> Id;
    Button accept,reject;
    String SrNo = null;
    EditText et_name;
//    private final Integer[] imgid;

    public ApprovedListAdapter(Activity context, ArrayList<String> day, ArrayList<String> month,
                               ArrayList<String> totime, ArrayList<String> purpose, ArrayList<String> visitorname, ArrayList<String> Id) {
        super(context, R.layout.approvedlist, day);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.day = day;
        this.month = month;
        this.totime = totime;
        this.purpose = purpose;
        this.visitorname = visitorname;
        this.Id = Id;


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public View getView(final int position, View view, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.approvedlist, null, true);

        TextView dayy = (TextView) rowView.findViewById(R.id.txt_day);
        TextView monthh = (TextView) rowView.findViewById(R.id.txt_month);
        TextView totimee = (TextView) rowView.findViewById(R.id.txt_to_time);

        TextView purposee = (TextView) rowView.findViewById(R.id.purpose);
        TextView visitornamee = (TextView) rowView.findViewById(R.id.visitorName);


        dayy.setText("" + day.get(position));
        monthh.setText("" + month.get(position));
        totimee.setText("" + totime.get(position));
        purposee.setText("" + purpose.get(position));
        visitornamee.setText("" + visitorname.get(position));

//        btn = (Button) rowView.findViewById(R.id.button);




        return rowView;

    }

}