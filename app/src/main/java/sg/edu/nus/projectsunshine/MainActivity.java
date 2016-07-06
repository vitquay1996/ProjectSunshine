package sg.edu.nus.projectsunshine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private final String DETAILFRAGMENT_TAG = "DFTAG";
    private String mLocation;
    private Boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocation = Utility.getPreferredLocation(this);
        setContentView(R.layout.activity_main);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);


        if (findViewById(R.id.weather_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                Log.v(LOG_TAG, "begining transaction");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.weather_detail_container, new DetailFragment(), DETAILFRAGMENT_TAG)
                        .commit();
                Log.v(LOG_TAG, "transaction completed");
            }
        } else {
            mTwoPane = false;
        }
        Log.v(LOG_TAG, "mTwoPane is " + mTwoPane);
    }



    @Override
    protected void onStart() {
        super.onStart();
        Log.v(LOG_TAG, "start");
    }

    @Override
    protected void onResume() {
        super.onResume();
        String location = Utility.getPreferredLocation(this);
        Log.v(LOG_TAG, "Old Location is " + mLocation + " New location is " + location);
        if (!mLocation.equals(location)) {
            FragmentManager fm = getSupportFragmentManager();
            ForecastFragment ff = (ForecastFragment)fm.findFragmentById(R.id.fragment_forecast);
            Log.v(LOG_TAG, "ff is null ?" + ff);

            if (ff != null) {
                Log.v(LOG_TAG, "changing location");
                ff.onLocationChanged();
            }
            mLocation = location;
        }
        Log.v(LOG_TAG, "resume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(LOG_TAG, "pause");
    }
    protected void onStop() {
        super.onStop();
        Log.v(LOG_TAG, "stop");
    }
    protected void onDestroy() {
        super.onDestroy();
        Log.v(LOG_TAG, "destroy");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        if (id == R.id.action_map) {
            openPreferredLocationInMap();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void openPreferredLocationInMap() {
        String location = Utility.getPreferredLocation(this);
        Uri geoLocation = Uri.parse("geo:0,0?").buildUpon()
                .appendQueryParameter("q", location)
                .build();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
        else{
            Log.d(LOG_TAG, "Couldn't call " + location + ", no postal code");
        }
    }
}
