package es.appmaster.kissmaster;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;


public class MainActivity extends ActionBarActivity {

    private SensorManager sensorManager;
    private SensorEventListener sensorEventListener;
    private Sensor sensor;

    private MediaPlayer mp;

    static boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        sensorEventListener = new SensorEventListener() {

            @Override
            public void onSensorChanged(SensorEvent event) {
                if ( event.values[0] <= 1 ) {
                    if ( isPlaying )
                        return;

                    // play kiss sound
                    isPlaying = true;
                    mp = MediaPlayer.create(MainActivity.this, R.raw.kiss);
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.release();
                            isPlaying = false;
                        }

                    });
                    mp.start();
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }

        };
    }

    @Override
    protected void onPause() {
        super.onPause();

        sensorManager.unregisterListener(sensorEventListener);
    }

    @Override
    protected void onResume() {
        super.onResume();

        sensorManager.registerListener(
                sensorEventListener,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }
}
