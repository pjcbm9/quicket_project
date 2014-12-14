package pjcbm9.quicket;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import pjcbm9.quicket.CustomViews.SpartanTextView;
import pjcbm9.quicket.MAIN_ACTIVITY_PACKAGE.MainActivity;

import static pjcbm9.quicket.Quicket_Package.Static_Helpers.DelayIntent;
import static pjcbm9.quicket.Quicket_Package.Static_Helpers.updateActionBar;


public class InfoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        updateActionBar(this,"ABOUT QUICKET");
        getActionBar().setDisplayHomeAsUpEnabled(true);
        SpartanTextView infoView = (SpartanTextView)findViewById(R.id.info_text_view);
        String info = "\nQUICKET\n\n\t\tQuicket is an application for employees who use ticket reporting software for logging " +
                "the incidents they handle while working. Quicket eliminates some of the overhead in that process, by providing " +
                "a way to quickly record the more essential details of a given incident so they don't have to immediately " +
                "report the incident on their ticketing software, which can often be a long and tedious process.\n\t\tQuicket provides " +
                "a countdown timer to remind the employee to enter the incident they saved, which sets alarm when the countdown runs out" +
                "\n\t\tQuicket also features the ability to edit,delete,or complete tickets if they would like a quick way to reference tickets they've " +
                "already finished\n\nCreated by Jason Crow\n\nDecember 13, 2014\n\n";
        infoView.setText(info);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                break;
            }
            case R.id.home_icon:
                delayIntent(0, MainActivity.class);
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
    private void delayIntent(int milliseconds, Class<?> c){
        DelayIntent(this,milliseconds,c);
    }
}
