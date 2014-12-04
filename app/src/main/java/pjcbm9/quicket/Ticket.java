package pjcbm9.quicket;

import android.os.CountDownTimer;

import java.io.Serializable;
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
        try{
            return countDownDuration;
        }catch(NullPointerException e){
            return 86400000*7;
        }
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
        String hms = String.format("%01d DAYS\n%02d:%02d:%02d", TimeUnit.MILLISECONDS.toDays(millis),
                TimeUnit.MILLISECONDS.toHours(millis)%24,
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
        this.countDownTimer = hms;
    }
    public void setCountDownTimer2(long rightNow){
        long millis = getTimeLeft(rightNow);
        String hms = String.format("%01d DAYS: %02d:%02d:%02d", TimeUnit.MILLISECONDS.toDays(millis),
                TimeUnit.MILLISECONDS.toHours(millis)%24,
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
        this.countDownTimer = hms;
    }
    public long getAlarmTime(){
        return getTicketDate().getTime() + countDownDuration;
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
