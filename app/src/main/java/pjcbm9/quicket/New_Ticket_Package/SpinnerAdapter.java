package pjcbm9.quicket.New_Ticket_Package;

/**
 * Created by Jason Crow on 12/6/2014.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TableRow;

import java.util.ArrayList;

import pjcbm9.quicket.CustomViews.SpartanTextView;
import pjcbm9.quicket.R;

/***** Adapter class extends with ArrayAdapter ******/
public class SpinnerAdapter extends ArrayAdapter<String> {
    private LayoutInflater layoutInflater;
    private ArrayList<String> objects;
    /*************  CustomAdapter Constructor *****************/
    public SpinnerAdapter(Context context,int spinnerItemResource,ArrayList<String> objects){
        super(context, spinnerItemResource, objects);
        this.objects = objects;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        View row = layoutInflater.inflate(R.layout.spinner_row, parent, false);
        SpartanTextView tv = (SpartanTextView) row.findViewById(R.id.spinner_text_view);
        tv.setText(objects.get(position));
        return row;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = layoutInflater.inflate(R.layout.spinner_row, parent, false);
        SpartanTextView tv = (SpartanTextView) row.findViewById(R.id.spinner_text_view);
        tv.setBackgroundResource(R.drawable.new_ticket_spinner);
        tv.setLayoutParams(
                new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT));
        tv.setText(objects.get(position));
        return row;
    }


}
