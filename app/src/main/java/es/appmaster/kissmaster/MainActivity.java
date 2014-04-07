package es.appmaster.kissmaster;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;


public class MainActivity extends ActionBarActivity {

    private ImageView kissImage;
    private SensorManager sensorManager;
    private SensorEventListener sensorEventListener;
    private Sensor sensor;

    private MediaPlayer mediaPlayer;
    static boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        kissImage = (ImageView) findViewById(R.id.kissImage);
        kissImage.setVisibility(View.INVISIBLE);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        sensorEventListener = new SensorEventListener() {

            @Override
            public void onSensorChanged(SensorEvent event) {
                if ( event.values[0] <= 1 && !isPlaying ) {

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

}
