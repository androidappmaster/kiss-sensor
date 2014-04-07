package es.appmaster.kissmaster;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

/**
 * Main activity
 *
 * @author manolovn
 */
public class MainActivity extends ActionBarActivity {

    private static final String DEFAULT_DISTANCE = "1";

    private ImageView kissImage;
    private SensorManager sensorManager;
    private SensorEventListener sensorEventListener;
    private Sensor sensor;

    private SharedPreferences preferences;

    private MediaPlayer mediaPlayer;
    static boolean isPlaying = false;

    private int distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        kissImage = (ImageView) findViewById(R.id.kissImage);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        sensorEventListener = new SensorEventListener() {

            @Override
            public void onSensorChanged(SensorEvent event) {
                if ( event.values[0] <= distance && !isPlaying ) {

                    // play kiss sound
                    isPlaying = true;
                    kissImage.setVisibility(View.VISIBLE);
                    mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.kiss);
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            mediaPlayer.release();
                            kissImage.setVisibility(View.INVISIBLE);
                            isPlaying = false;
                        }

                    });
                    mediaPlayer.start();
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                // not used
            }

        };
    }

    @Override
    protected void onResume() {
        super.onResume();

        kissImage.setVisibility(View.INVISIBLE);
        String prefKey = getString(R.string.pref_distance_key);
        distance = Integer.parseInt(preferences.getString(prefKey, DEFAULT_DISTANCE));
        sensorManager.registerListener(
                sensorEventListener,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();

        sensorManager.unregisterListener(sensorEventListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
