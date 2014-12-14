package pjcbm9.quicket;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.Button;
import android.widget.EditText;

import pjcbm9.quicket.New_Ticket_Package.NewTicket;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class NewTicketTest extends ActivityUnitTestCase<NewTicket> {
    // activity of the target application
    NewTicket newticket;
    // EditTexts to be tested
    EditText ticketName,ticketCustomer;
    // Button to test if enabled once characters have been inserted into edittexts
    Button submitButton;

    public NewTicketTest() {
        super(NewTicket.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        // Starts the NewTicket activity
        startActivity(new Intent(getInstrumentation().getTargetContext(), NewTicket.class), null, null);
        // Getting a reference to the MainActivity of the target application
        newticket = (NewTicket)getActivity();
        // Getting a reference to the TextView of the MainActivity of the target application
        ticketName = (EditText) newticket.findViewById(pjcbm9.quicket.R.id.NameText);
        ticketCustomer = (EditText) newticket.findViewById(pjcbm9.quicket.R.id.CustomerText);
        submitButton = (Button) newticket.findViewById(pjcbm9.quicket.R.id.SubmitB);

    }
    // test if after entering characters into the edit texts the submit button becomes enabled
    @SmallTest
    public void testSubmitButtonIsEnabled(){
        System.out.println("" + submitButton.isEnabled());
        // Insert text into edit texts
        ticketName.setText("Test Ticket");
        ticketCustomer.setText("John Doe");
        // expected result
        Boolean expected = true;
        // actual result
        Boolean actual = submitButton.isEnabled();
        // Check whether both are equal, otherwise test fails
        assertEquals(expected,actual );
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

}