package pjcbm9.quicket.New_Ticket_Package;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;

import pjcbm9.quicket.CustomViews.SpartanButton;
import pjcbm9.quicket.CustomViews.SpartanEditText;
import pjcbm9.quicket.R;
import pjcbm9.quicket.Quicket_Package.Ticket;

import static pjcbm9.quicket.Quicket_Package.Static_Helpers.setUpAnimation;

public class DescriptionDialog extends Dialog implements View.OnClickListener, TicketField {
    private SpartanButton clear, submit,exitb, setDescription;
    private SpartanEditText description;
    private Context context;
    private static Ticket.Field field = Ticket.Field.DESCRIPTION;
    public DescriptionDialog(Context context, SpartanButton setDescription) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.context = context;
        setContentView(R.layout.add_description_dialog);
        this.setDescription = setDescription;
        initializeVariables();
        setListeners();
    }
    private void initializeVariables() {
        clear = (SpartanButton) findViewById(R.id.clearb);
        submit = (SpartanButton) findViewById(R.id.Submitb);
        exitb = (SpartanButton) findViewById(R.id.exitb);
        description = (SpartanEditText) findViewById(R.id.DescriptionText);
    }

    private void setListeners() {
        clear.setOnClickListener(this);
        submit.setOnClickListener(this);
        exitb.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.Submitb):
                updateSetDescriptionButtonLablel();
                setUpAnimation("silver_anim", context, view);
                dismiss();
                break;
            case (R.id.clearb):
                setUpAnimation("silver_anim", context, view);
                description.setText(""); // clears description text view
                break;
            case (R.id.exitb):
                setUpAnimation("silver_anim", context, view);
                dismiss();
                break;
        }

    }


    @Override
    public void setField(Ticket.Field field) {
        // field is static
    }

    @Override
    public Ticket.Field getField() {
        return field;
    }

    @Override
    public void setData(String data) {
        description.setText(data);
        updateSetDescriptionButtonLablel();
    }

    // retrieve ticket field data
    @Override
    public String getData() {
        return description.getText().toString();
    }

    @Override
    public Boolean isSet() {
        return true; // description isn't critical so it is always set
    }
    private void updateSetDescriptionButtonLablel(){
        if(getData().equals("")){
            setDescription.setText("ADD DESCRIPTION");
        } else {
            setDescription.setText("EDIT DESCRIPTION");
        }
    }
}