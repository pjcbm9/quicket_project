package pjcbm9.quicket.Quicket_Package;

import android.os.CountDownTimer;

import junit.framework.Assert;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Ticket implements Serializable {

    private String TicketName;
    private String Assignee;
    private String TicketType;
    private String IncidentLocation;
    private String CustomerName;
    private Date TicketDate;
    private String Description;
    private CountDownTimer countDown;
    private Status status;
    private long ID;
    private long countDownDuration;
    private String countDownTimer;

    //private static final long serialVersionUID = 46543445;
    public enum Status {
        DRAFT,
        ACTIVE,
        COMPLETED,
        OVERDUE,
        SEARCH
    }
    public enum Field {
        NAME,ASSIGNEE,TYPE,LOCATION,DATE,DESCRIPTION,COUNTDOWN,CUSTOMER
    }
    public Ticket(long countDownDuration){
        this.TicketName = "";
        this.TicketType = "";
        this.IncidentLocation = "";
        this.TicketDate = new Date();
        this.Assignee = "";
        this.Description = "";
        this.CustomerName = "";
        this.status = Status.ACTIVE;
        this.ID = (long) Math.floor(Math.random() * 9000000000L) + 1000000000L;
        this.countDownDuration = countDownDuration;
        setCountDownTimer(Calendar.getInstance().getTimeInMillis());
    }
    public Ticket(String TN, String TL, String IL, Date TD, String A, String D, String CN, Status _status){
        this.TicketName = TN;
        this.TicketType = TL;
        this.IncidentLocation = IL;
        this.TicketDate = TD;
        this.Assignee = A;
        this.Description = D;
        this.CustomerName = CN;
        this.status = _status;
        this.ID = (long) Math.floor(Math.random() * 9000000000L) + 1000000000L;
        this.countDownDuration = 86400000*7;
        setCountDownTimer(Calendar.getInstance().getTimeInMillis());
    }
    public long getCountDownDuration(){
        return countDownDuration;
    }
    public String getFormatedCountDownDuration(){
        return formatCountDownTimer2(countDownDuration);
    }
    public void setCountDownDuration(long duration){
        countDownDuration = duration;
    }
    public long getID() {
        return ID;
    }
    public void setID(long id){
        this.ID = id;
    }
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status s){
        this.status = s;
    }
    public String getCountDownTimer() {
        return countDownTimer;
    }
    public void setCountDownTimer(long rightNow){
        long millis = getTimeLeft(rightNow);
        this.countDownTimer = formatCountDownTimer1(millis);
    }
    public void setCountDownTimer2(long rightNow){
        long millis = getTimeLeft(rightNow);
        this.countDownTimer = formatCountDownTimer2(millis);
    }
    private String formatCountDownTimer1(long millis){
        String hms = String.format("%01d DAYS\n%02d:%02d:%02d", TimeUnit.MILLISECONDS.toDays(millis),
                TimeUnit.MILLISECONDS.toHours(millis)%24,
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
        return hms;
    }
    private String formatCountDownTimer2(long millis){
        String hms = String.format("%01d DAYS: %02d:%02d:%02d", TimeUnit.MILLISECONDS.toDays(millis),
                TimeUnit.MILLISECONDS.toHours(millis)%24,
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
        return hms;
    }

    public long getTimeLeft(long rightNow) {
        long DueDate = getTicketDate().getTime() + (countDownDuration);
        //long today = Calendar.getInstance().getTimeInMillis();
        if(DueDate <= rightNow){
            return 0;
        } else {
            return DueDate - rightNow;
        }
    }
    private String getDate() {
        SimpleDateFormat dt = new SimpleDateFormat("LLLL dd, yyyy hh:mm a");
        String Date = dt.format(getTicketDate());
       return Date;
    }
    public String getTicketData(Field field) {
        switch (field) {
            case NAME: return getTicketName();
            case CUSTOMER: return getCustomerName();
            case TYPE: return getTicketType();
            case ASSIGNEE: return getAssignee();
            case LOCATION: return getIncidentLocation();
            case DESCRIPTION: return getDescription();
            case DATE: return getDate();
            case COUNTDOWN: return getFormatedCountDownDuration();
        }
        Assert.fail(); // shouldn't make it to this point
        return null;
    }
    // sets ticket's data corresponding to a field
    public void setTicketData(String data,Ticket.Field field){
        switch (field) {
            case NAME: setTicketName(data);
                break;
            case CUSTOMER: setCustomerName(data);
                break;
            case TYPE: setTicketType(data);
                break;
            case ASSIGNEE: setAssignee(data);
                break;
            case LOCATION: setIncidentLocation(data);
                break;
            case DESCRIPTION: setDescription(data);
        }
    }

    /*public void setCountdownTimer(TextView object){
        countDown = new CountDownTimerActivity(object,getTimeLeft(Calendar.getInstance().getTimeInMillis()));
        countDown.start();
    }*/

    public String getTicketName() {
        return TicketName;
    }

    public void setTicketName(String ticketName) {
        TicketName = ticketName;
    }

    public String getAssignee() {
        return Assignee;
    }

    public void setAssignee(String assignee) {
        Assignee = assignee;
    }

    public String getTicketType() {
        return TicketType;
    }

    public void setTicketType(String ticketType) {
        TicketType = ticketType;
    }

    public String getIncidentLocation() {
        return IncidentLocation;
    }

    public void setIncidentLocation(String incidentLocation) {
        IncidentLocation = incidentLocation;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public Date getTicketDate() {
        return TicketDate;
    }

    public void setTicketDate(Date ticketDate) {
        TicketDate = ticketDate;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

}
