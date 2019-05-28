package com.huanlezhang.frequencyplayer;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Sounds of different frequencies
 *
 * @author  Huanle Zhang, at University of California, Davis
 *          www.huanlezhang.com
 * @version 0.2
 * @since   2019-05-28
 */

public class MainActivity extends AppCompatActivity {

    private Button mBtn1, mBtn2, mBtn3;

    private EditText mEditFreq, mEditFromFreq, mEditToFreq, mEditDuration;

    private SeekBar mSeekBar;

    private TextView mTextFreq;

    private PlaySound mPlaySound;
    private int mFreq = 1;

    private Handler mHandler = new Handler();
    private Timer mTimer;
    private double mStepInc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindUI();
    }

    private void bindUI() {

        mBtn1 = findViewById(R.id.btn1);
        mBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;

                if (button.getText().toString().trim().toLowerCase().equals("play")) {
                    enableUI(false);
                    button.setEnabled(true);

                    if (mEditFreq.getText().toString().trim().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please Type in Frequency", Toast.LENGTH_LONG).show();
                        enableUI(true);
                    } else {
                        mFreq = Integer.parseInt(mEditFreq.getText().toString().trim());
                        if (mFreq < 1 || mFreq > 24000) {
                            Toast.makeText(getApplicationContext(), "Supported Freq Range: 1 - 24k Hz", Toast.LENGTH_LONG).show();

                            enableUI(true);
                        } else {
                            // looks OK, play sound
                            mPlaySound = new PlaySound();
                            mPlaySound.mOutputFreq = mFreq;
                            mPlaySound.start();

                            mTextFreq.setText(mFreq + " Hz");
                            button.setText("Stop");
                        }
                    }
                } else {
                    if (mPlaySound != null) {
                        mPlaySound.stop();
                        mPlaySound = null;
                    }

                    enableUI(true);
                    button.setText("Play");
                }
            }
        });

        mBtn2 = findViewById(R.id.btn2);
        mBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;

                if (button.getText().toString().trim().toLowerCase().equals("play")) {
                    enableUI(false);
                    button.setEnabled(true);
                    mSeekBar.setEnabled(true);

                    mPlaySound = new PlaySound();
                    mPlaySound.mOutputFreq = mFreq;
                    mPlaySound.start();

                    mTextFreq.setText(mFreq + " Hz");
                    button.setText("Stop");

                    button.setText("Stop");
                } else {
                    if (mPlaySound != null) {
                        mPlaySound.stop();
                        mPlaySound = null;
                    }

                    enableUI(true);
                    button.setText("Play");
                }
            }
        });

        mBtn3 = findViewById(R.id.btn3);
        mBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;

                if (button.getText().toString().trim().toLowerCase().equals("play")) {
                    enableUI(false);
                    button.setEnabled(true);

                    if (mEditFromFreq.getText().toString().trim().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "From Frequency is empty", Toast.LENGTH_LONG).show();
                        enableUI(true);
                        return;
                    }
                    if (mEditToFreq.getText().toString().trim().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "To Frequency is empty", Toast.LENGTH_LONG).show();
                        enableUI(true);
                        return;
                    }
                    if (mEditDuration.getText().toString().trim().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Duration is empty", Toast.LENGTH_LONG).show();
                        enableUI(true);
                        return;
                    }

                    int fromFreq = Integer.parseInt(mEditFromFreq.getText().toString().trim());
                    int toFreq = Integer.parseInt(mEditToFreq.getText().toString().trim());
                    if (fromFreq < 1 || fromFreq > 24000 || toFreq < 1 || toFreq > 24000) {
                        Toast.makeText(getApplicationContext(), "Supported Freq Range: 1 - 24k Hz", Toast.LENGTH_LONG).show();
                        enableUI(true);
                        return;
                    }

                    int duration = Integer.parseInt(mEditDuration.getText().toString().trim());
                    if (duration < 1) {
                        Toast.makeText(getApplicationContext(), "Duration must > 1", Toast.LENGTH_LONG).show();
                        enableUI(true);
                        return;
                    }

                    mFreq = fromFreq;
                    mPlaySound = new PlaySound();
                    mPlaySound.mOutputFreq = mFreq;
                    mPlaySound.start();

                    mStepInc = 1.0 * (toFreq - fromFreq) / duration;
                    mTimer = new Timer();
                    mTimer.schedule(new TimerTask() {

                        double tempFreq = mFreq;
                        int countDown = Integer.parseInt(mEditDuration.getText().toString().trim());

                        @Override
                        public void run() {
                            if (mPlaySound == null) return;

                            if (countDown-- < 0) {
                                if (mTimer != null) {
                                    mTimer.cancel();
                                    mTimer = null;
                                }
                                if (mPlaySound != null) {
                                    mPlaySound.stop();
                                    mPlaySound = null;
                                }

                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        enableUI(true);
                                        mBtn3.setText("Play");
                                    }
                                });
                                return;
                            }

                            mFreq = (int) tempFreq;
                            if (mPlaySound != null)
                                mPlaySound.mOutputFreq = mFreq;

                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mTextFreq.setText(mFreq + " Hz");
                                }
                            });

                            tempFreq += mStepInc;
                        }

                    }, 0, 1000);

                    button.setText("Stop");
                } else {

                    if (mTimer != null) {
                        mTimer.cancel();
                        mTimer = null;
                    }
                    if (mPlaySound != null) {
                        mPlaySound.stop();
                        mPlaySound = null;
                    }

                    enableUI(true);
                    button.setText("Play");
                }
            }
        });

        mEditFreq = findViewById(R.id.editTextFreq);
        mEditFromFreq = findViewById(R.id.editFromFreq);
        mEditToFreq = findViewById(R.id.editToFreq);
        mEditDuration = findViewById(R.id.editDuration);

        mSeekBar = findViewById(R.id.seekBar);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mFreq = progress;
                mTextFreq.setText(mFreq + " Hz");
                if (mPlaySound != null) {
                    mPlaySound.mOutputFreq = mFreq;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mTextFreq = findViewById(R.id.textFreq);
    }

    private void enableUI(boolean enable) {
        mBtn1.setEnabled(enable);
        mBtn2.setEnabled(enable);
        mBtn3.setEnabled(enable);

        mEditFreq.setEnabled(enable);
        mEditFromFreq.setEnabled(enable);
        mEditToFreq.setEnabled(enable);
        mEditDuration.setEnabled(enable);

        mSeekBar.setEnabled(enable);
    }
}
