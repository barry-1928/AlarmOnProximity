package com.example.dell.asynctask;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    Uri ringtoneUri;
    Ringtone ringtoneSound;
    SensorManager sensorManager;
    int c = 0;
    CountDownTimer countDownTimer;
    Timer timer;
    TextView countDownTimerText;
    MyAsyncTask myAsyncTask;

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(MainActivity.this,sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ringtoneSound = RingtoneManager.getRingtone(this,ringtoneUri);
        countDownTimerText = (TextView) findViewById(R.id.timer);
        timer = new Timer(10000,1);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            if(event.values[0]==0) {
                myAsyncTask = null;
                countDownTimerText.setText("10:000");
                myAsyncTask = new MyAsyncTask(0);
                myAsyncTask.execute();
                timer.start();
            }
            else {
                if(timer!=null) {
                    timer.cancel();
                }
                if(myAsyncTask!=null) {
                    myAsyncTask.cancel(true);
                    myAsyncTask = null;
                }
                countDownTimerText.setText("10:000");
                myAsyncTask = new MyAsyncTask(1);
                myAsyncTask.execute();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    class MyAsyncTask extends AsyncTask<Void, Void, Void> {

        int decider;

        MyAsyncTask(int decider) {
            this.decider = decider;
            Log.d("xyz", "decider " + decider);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected Void doInBackground(Void... params) {
            if (decider == 0) {
                try {
                    Thread thread = Thread.currentThread();
                    thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ringtoneSound.stop();
                ringtoneSound.play();

            }
            else {
                ringtoneSound.stop();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

    }

    class Timer extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public Timer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            countDownTimerText.setText(""+millisUntilFinished/1000 + ":" + millisUntilFinished%1000);
        }

        @Override
        public void onFinish() {
            countDownTimerText.setText("00:000");
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}
