package pjcbm9.quicket;

        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.List;

        import android.app.Activity;
        import android.os.Bundle;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.widget.Button;
        import android.widget.TextView;

public class SendMailActivity extends Activity implements OnClickListener
{
    private Button buttonSend;
    private TextView senderEmail, senderPassword, recipientEmail, emailSubject, emailBody;
    private List<String> recipientEmailList;
    private String strSenderEmail, strSenderPassword, strRecipientEmail, strEmailSubject, strEmailBody;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_mail);
        initializeWidgets();
    }

    private void initializeWidgets()
    {
        buttonSend = (Button) this.findViewById(R.id.button1);
        senderEmail = (TextView) this.findViewById(R.id.editText1);
        senderPassword = (TextView) this.findViewById(R.id.editText2);
        recipientEmail = (TextView) this.findViewById(R.id.editText3);
        emailSubject = (TextView) this.findViewById(R.id.editText4);
        emailBody = (TextView) this.findViewById(R.id.editText5);
        recipientEmailList = new ArrayList<String>();
        buttonSend.setOnClickListener(this);
    }

    private void getValues()
    {
        strSenderEmail = senderEmail.getText().toString();
        strSenderPassword = senderPassword.getText().toString();
        strRecipientEmail = recipientEmail.getText().toString();
        recipientEmailList = Arrays.asList(strRecipientEmail.split("\\s*,\\s*"));
        strEmailSubject = emailSubject.getText().toString();
        strEmailBody = emailBody.getText().toString();
    }

    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        switch(v.getId())
        {
            case R.id.button1:
                getValues();
                new SendMailTask(SendMailActivity.this).execute(strSenderEmail, strSenderPassword, recipientEmailList, strEmailSubject, strEmailBody);
                break;
        }
    }
}
