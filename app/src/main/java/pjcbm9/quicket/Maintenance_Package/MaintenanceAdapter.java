package pjcbm9.quicket.Maintenance_Package;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import pjcbm9.quicket.CustomViews.SpartanButton;
import pjcbm9.quicket.CustomViews.SpartanEditText;
import pjcbm9.quicket.CustomViews.SpartanTextView;
import pjcbm9.quicket.R;

/**
 * Created by Jason Crow on 12/13/2014.
 */
public class MaintenanceAdapter extends BaseAdapter {
    // private class variables
    private ArrayList<MItem> items;
    private LayoutInflater layoutInflater;
    private SpartanTextView itemIndex;
    private SpartanEditText itemName;
    private SpartanButton delete;
    // constructor
    public MaintenanceAdapter(Context context, ArrayList<MItem> Items){
        this.items = Items;
        System.out.println(items.size());
        layoutInflater = LayoutInflater.from(context);
    }
    public ArrayList<MItem> getMItems() {
        return items;
    }

    @Override
    public int getCount() {
        if (items != null){
            return items.size();
        }else
            return 0;
    }
    @Override
    public Object getItem(int i) {
        return items.get(i);
    }
    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = layoutInflater.inflate(R.layout.maintenance_list_item,viewGroup,false);
        itemIndex = (SpartanTextView)view.findViewById(R.id.maintenance_itemnumber);
        itemName = (SpartanEditText)view.findViewById(R.id.maintenance_edittext);
        delete = (SpartanButton)view.findViewById(R.id.maintenance_delete);
        setupDeleteListener(i);
        setupTextWatcher(i);
        loadRowValues(i);
        return view;
    }
    private void updateIndexes(int position){
        for(MItem item: items){
            if(item.getIndex() > position){
                item.setIndex(item.getIndex()-1);
            }
        }
    }
    private void setupDeleteListener(final int position){
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                items.remove(items.get(position));
                updateIndexes(position);
                notifyDataSetChanged();
            }
        });
    }
    private void loadRowValues(int position){
        System.out.println(items.get(position).getItemName());
        itemIndex.setText(items.get(position).getIndex() + "");
        itemName.setText(items.get(position).getItemName(), TextView.BufferType.EDITABLE);
    }
    private void setupTextWatcher(final int position){
        itemName.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {
                System.out.println(position);
                items.get(position).setItemName(s.toString());
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });
    }
}
