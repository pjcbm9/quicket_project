package pjcbm9.quicket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import android.app.Activity;

import pjcbm9.quicket.Quicket_Package.Ticket;
import pjcbm9.quicket.Ticket_View_Package.ticketView;

import static pjcbm9.quicket.Quicket_Package.Static_Helpers.DelayIntent;
import static pjcbm9.quicket.Quicket_Package.Static_Helpers.updateActionBar;

public class SendMailActivity extends Activity {
    private Ticket ticket;
    private EditText etUserName;
    private EditText etUserPass;
    private EditText etToAddresses;
    private EditText etSubject;
    private EditText etTexts;
    private View fieldsContainer;
    private View loadingContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_mail);
        ticket = (Ticket)getIntent().getSerializableExtra("ticket");
        etUserName = (EditText) findViewById(R.id.et_usr_name);
        etUserPass = (EditText) findViewById(R.id.et_usr_pass);
        etSubject = (EditText) findViewById(R.id.et_subject);
        etToAddresses = (EditText) findViewById(R.id.etml_to_addresses);
        etTexts = (EditText) findViewById(R.id.etml_text_content);
        fieldsContainer = findViewById(R.id.sv_fields_container);
        loadingContainer = findViewById(R.id.ly_loading_container);
        showFields();
        updateActionBar(this,"EMAIL TICKET");
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void hideFields() {
        fieldsContainer.setVisibility(View.GONE);
        loadingContainer.setVisibility(View.VISIBLE);
    }
    public void showFields() {
        fieldsContainer.setVisibility(View.VISIBLE);
        loadingContainer.setVisibility(View.GONE);
    }

}