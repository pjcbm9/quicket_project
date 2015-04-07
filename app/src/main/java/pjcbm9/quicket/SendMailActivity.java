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
    private void delayIntent(int milliseconds, Class<?> c, Ticket ticket){
        DelayIntent(this,milliseconds,c,ticket);
    }
    public void sendMail(View v) {
        new SendMailTask().execute((Void[]) null);
        delayIntent(500, ticketView.class, ticket);
    }
    public void hideFields() {
        fieldsContainer.setVisibility(View.GONE);
        loadingContainer.setVisibility(View.VISIBLE);
    }
    public void showFields() {
        fieldsContainer.setVisibility(View.VISIBLE);
        loadingContainer.setVisibility(View.GONE);
    }
    private class SendMailTask extends AsyncTask<Void, Void, Void> {
        Exception error = null;
        boolean readyToSend = false;
        String username = "mrjasoncrow@gmail.com";
        String pass = "Trusty01";
        String subject = ticket.getTicketName();
        SimpleDateFormat dt = new SimpleDateFormat("LLLL dd, yyyy hh:mm a");
        Date TicketDate = ticket.getTicketDate();
        String texts = "QUICKET TICKET EMAIL UPDATE" + "\n\n" +
                "Ticket Name: " + ticket.getTicketName() + "\n\n" +
                "Ticket Date: " + dt.format(TicketDate) + "\n\n" +
                "Ticket Type: " + ticket.getTicketType()+ "\n\n" +
                "Ticket Assignee: " + ticket.getAssignee() + "\n\n" +
                "Ticket Customer: " + ticket.getCustomerName() + "\n\n" +
                "Ticket Description: " + ticket.getDescription() + "\n\n" +
                "Ticket Status: " + ticket.getStatus().toString() + "\n\n" +
                "Ticket Location: " + ticket.getIncidentLocation() + "\n\n";

        String toAddrss = "dechterc@umkc.edu";
        private static final String EMAIL_REXP = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)" +
                "*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        private String[] extractEmails(final String all){
            Pattern pattern = Pattern.compile(EMAIL_REXP);
            StringTokenizer tokenizer = new StringTokenizer(all, ";");
            List<String> mailList = new ArrayList<String>();
            String currentMail;
            while (tokenizer.hasMoreElements()) {
                currentMail = (String) tokenizer.nextElement();
                if (pattern.matcher(currentMail.trim()).matches()){
                    mailList.add(currentMail);
                }
            }
            return mailList.toArray(new String[0]);
        }
        private void sendMail() throws Exception {
            String[] tos = extractEmails(toAddrss);
            // 1 - Create one instance
            MailSender m = new MailSender();
            // 2 - Set addressees
            m.setCredentials(username, pass)
                    .setToAddresses(tos);
            // 3 - Set the content of the mail
            m.setSubject(subject).setMailText(texts);
            // 5 - Set properties to use and send
            m.useMailPropertiesGMail().send();
        }
        @Override
        protected void onPreExecute() {
            if ((username.length() < 1)
                    || (pass.length() < 1)
                    || (subject.length() < 1)
                    || (texts.length() < 1)
                    || (toAddrss.length() < 1)) {
                Toast.makeText(getApplicationContext(), "ERROR: Fill all the filelds", Toast.LENGTH_LONG).show();
            } else {
                super.onPreExecute();
                hideFields();
                readyToSend = true;
            }
        }
        @Override
        protected Void doInBackground(Void... params) {
            if (readyToSend) {
                try {
                    sendMail();
                } catch (Exception e) {
                    error = e;
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (error == null) {
                Toast.makeText(getApplicationContext(), "Message sent!", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(getApplicationContext(), "ERROR sending: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
            showFields();
            readyToSend = false;
        }

    }
}