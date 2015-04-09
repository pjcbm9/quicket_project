package pjcbm9.quicket.Ticket_View_Package;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import pjcbm9.quicket.MailSender;
import pjcbm9.quicket.Quicket_Package.Ticket;

public class SendMailTask extends AsyncTask<Void, Void, Void> {
    Exception error = null;
    boolean readyToSend = false;
    String username = "mrjasoncrow@gmail.com";
    String pass = "Trusty01";
    Context context;
    String subject;
    String texts;
    Ticket ticket;
    String toAddrss = "mrjasoncrow@gmail.com";
    public enum type{
        DELETE,
        COMPLETE,
        OVERDUE
    }
    public SendMailTask(Context context, Ticket ticket){
        this.ticket = ticket;
        this.context = context;
        this.subject = "New Quicket Ticket";
        SimpleDateFormat dt = new SimpleDateFormat("LLLL dd, yyyy hh:mm a");
        Date TicketDate = ticket.getTicketDate();
        texts = "QUICKET TICKET CREATED" + "\n\n" +
                "Ticket Name: " + ticket.getTicketName() + "\n\n" +
                "Ticket Date: " + dt.format(TicketDate) + "\n\n" +
                "Ticket Type: " + ticket.getTicketType()+ "\n\n" +
                "Ticket Assignee: " + ticket.getAssignee() + "\n\n" +
                "Ticket Customer: " + ticket.getCustomerName() + "\n\n" +
                "Ticket Description: " + ticket.getDescription() + "\n\n" +
                "Ticket Status: " + ticket.getStatus().toString() + "\n\n" +
                "Ticket Location: " + ticket.getIncidentLocation() + "\n\n";

    }
    public SendMailTask(Context context, Ticket ticket, type t){
        this.ticket = ticket;
        this.context = context;
        SimpleDateFormat dt = new SimpleDateFormat("LLLL dd, yyyy hh:mm a");
        Date TicketDate = ticket.getTicketDate();
        if(t.equals(type.DELETE)){
            subject = "Quicket Ticket Deleted";
            texts = "QUICKET TICKET DELETED" + "\n\n" +
                    "Ticket Name: " + ticket.getTicketName() + "\n\n" +
                    "Ticket Date: " + dt.format(TicketDate) + "\n\n" +
                    "Ticket Type: " + ticket.getTicketType()+ "\n\n" +
                    "Ticket Assignee: " + ticket.getAssignee() + "\n\n" +
                    "Ticket Customer: " + ticket.getCustomerName() + "\n\n" +
                    "Ticket Description: " + ticket.getDescription() + "\n\n" +
                    "Ticket Location: " + ticket.getIncidentLocation() + "\n\n";
        }else if(t.equals(type.COMPLETE)){
            subject = "Quicket Ticket Completed";
            texts = "QUICKET TICKET COMPLETED" + "\n\n" +
                    "Ticket Name: " + ticket.getTicketName() + "\n\n" +
                    "Ticket Date: " + dt.format(TicketDate) + "\n\n" +
                    "Ticket Type: " + ticket.getTicketType()+ "\n\n" +
                    "Ticket Assignee: " + ticket.getAssignee() + "\n\n" +
                    "Ticket Customer: " + ticket.getCustomerName() + "\n\n" +
                    "Ticket Description: " + ticket.getDescription() + "\n\n" +
                    "Ticket Location: " + ticket.getIncidentLocation() + "\n\n";
        }else{
            subject = "Quicket Ticket Overdue!";
            texts = "QUICKET TICKET OVERDUE!!" + "\n\n" +
                    "Ticket Name: " + ticket.getTicketName() + "\n\n" +
                    "Ticket Date: " + dt.format(TicketDate) + "\n\n" +
                    "Ticket Type: " + ticket.getTicketType()+ "\n\n" +
                    "Ticket Assignee: " + ticket.getAssignee() + "\n\n" +
                    "Ticket Customer: " + ticket.getCustomerName() + "\n\n" +
                    "Ticket Description: " + ticket.getDescription() + "\n\n" +
                    "Ticket Location: " + ticket.getIncidentLocation() + "\n\n";
        }

    }
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
        Toast.makeText(context, "here", Toast.LENGTH_LONG).show();
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
            Toast.makeText(context, "ERROR: Fill all the fields", Toast.LENGTH_LONG).show();
        } else {
            super.onPreExecute();
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
            Toast.makeText(context, "Message sent!", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(context, "ERROR sending: " + error.getMessage(), Toast.LENGTH_LONG).show();
        }
        readyToSend = false;
    }

}