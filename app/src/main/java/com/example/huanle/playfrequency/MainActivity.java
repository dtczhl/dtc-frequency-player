package com.example.huanle.playfrequency;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.os.Handler;
import java.util.logging.LogRecord;

/**
 * You can reuse my codes freely. I would appreciate if you could keep my name and website address.
 * Name: Huanle Zhang
 * Personal website: www.huanlezhang.com
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mMetBtn1, mMetBtn2, mMetBtn3;

    private EditText mMet_1_edit1;
    private EditText mMet_3_edit1, mMet_3_edit2, mMet_3_edit3;

    private SeekBar mMet_2_seekbar;
    private int mSeekBarStep = 200;
    private TextView mSeekBarReading;

    private int mRangeLow = 0;
    private int mRangeHigh = 0;
    private int mDuration = 0;
    private boolean mIsInc = false;
    private double mStepInc = 0;
    private TimerTask mTimerTask;
    private Timer mTimer;
    private double mTempFreq;
    private Runnable mRun = null;
    private Handler mHandler = null;

    private int mFreq = 0;
    private boolean mIsPlay = false;
    private PlaySound mPlaySound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMetBtn1 = (Button) findViewById(R.id.met_1_play);
        mMetBtn1.setOnClickListener(this);
        mMetBtn2 = (Button) findViewById(R.id.met_2_play);
        mMetBtn2.setOnClickListener(this);
        mMetBtn3 = (Button) findViewById(R.id.met_3_play);
        mMetBtn3.setOnClickListener(this);

        mMet_1_edit1 = (EditText) findViewById(R.id.met_1_edit);
        mMet_3_edit1 = (EditText) findViewById(R.id.met_3_edit1);
        mMet_3_edit2 = (EditText) findViewById(R.id.met_3_edit2);
        mMet_3_edit3 = (EditText) findViewById(R.id.met_3_edit3);

        mSeekBarReading = (TextView) findViewById(R.id.met_2_text2);
        mMet_2_seekbar = (SeekBar) findViewById(R.id.met_2_seekbar);
        mMet_2_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mFreq = (progress + 1) * mSeekBarStep;
                if (mIsPlay && (mPlaySound!=null)){
                    mPlaySound.mOutputFreq = mFreq;
                }
                mSeekBarReading.setText(Integer.toString(mFreq) + " Hz");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        mHandler = new Handler();
        mRun = new Runnable() {
            @Override
            public void run() {
                enableAllInputs();
                mMetBtn3.setText("PLAY");
            }
        };
    }

    public void aboutMe(View view){
        AboutMe.showDialog(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.met_1_play:
                if (!mIsPlay){
                    if (mMet_1_edit1.getText().toString().trim().isEmpty()){
                        Toast.makeText(this, "Input Freq", Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        mFreq = Integer.parseInt(mMet_1_edit1.getText().toString().trim());
                        if (mFreq <= 0){
                            Toast.makeText(this, ">0", Toast.LENGTH_LONG).show();
                            return;
                        } else{
                            mIsPlay = true;
                            disableAllInputs();
                            mMetBtn1.setEnabled(true);
                            mMetBtn1.setText("STOP");
                            mPlaySound = new PlaySound();
                            mPlaySound.mOutputFreq = mFreq;
                            mPlaySound.start();
                        }
                    }
                } else {
                    mIsPlay = false;
                    if (mPlaySound != null){
                        mPlaySound.stop();
                        mPlaySound = null;
                    }
                    enableAllInputs();
                    mMetBtn1.setText("PLAY");
                }
                break;
            case R.id.met_2_play:
                if (!mIsPlay){
                    mIsPlay = true;
                    disableAllInputs();
                    mMet_2_seekbar.setEnabled(true);
                    mMetBtn2.setEnabled(true);
                    mMetBtn2.setText("STOP");
                    mPlaySound = new PlaySound();
                    mPlaySound.mOutputFreq = mFreq;
                    mPlaySound.start();
                } else {
                    mIsPlay = false;
                    if (mPlaySound != null){
                        mPlaySound.stop();
                        mPlaySound = null;
                    }
                    enableAllInputs();
                    mMetBtn2.setText("PLAY");
                }
                break;
            case R.id.met_3_play:
                if (!mIsPlay){
                    if (mMet_3_edit1.getText().toString().trim().isEmpty()){
                        Toast.makeText(this, "From > 0", Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        mRangeLow = Integer.parseInt(mMet_3_edit1.getText().toString().trim());
                        if (mRangeLow <= 0) {
                            Toast.makeText(this, "From > 0", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    if (mMet_3_edit2.getText().toString().trim().isEmpty()){
                        Toast.makeText(this, "To > 0", Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        mRangeHigh = Integer.parseInt(mMet_3_edit2.getText().toString().trim());
                        if (mRangeHigh <= 0){
                            Toast.makeText(this, "To > 0", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    if (mMet_3_edit3.getText().toString().trim().isEmpty()){
                        Toast.makeText(this, "Duration > 0", Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        mDuration = Integer.parseInt(mMet_3_edit3.getText().toString().trim());
                        if (mDuration <= 0){
                            Toast.makeText(this, "Duration > 0", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    if (mRangeLow == mRangeHigh) return;
                    mFreq = mRangeLow;
                    mTempFreq = mFreq;
                    if (mRangeLow < mRangeHigh){
                        mIsInc = true;
                        mStepInc = (mRangeHigh - mRangeLow) / mDuration;
                    } else {
                        mIsInc = false;
                        mStepInc = (mRangeLow - mRangeHigh) / mDuration;
                    }
                    disableAllInputs();
                    mMetBtn3.setEnabled(true);
                    mMetBtn3.setText("STOP");
                    mIsPlay = true;
                    mPlaySound = new PlaySound();
                    mPlaySound.mOutputFreq = mFreq;
                    mPlaySound.start();
                    mTimer = new Timer();
                    mTimerTask = new TimerTask() {
                        @Override
                        public void run() {
                            if (mIsPlay && (mPlaySound!=null)){
                                if (mIsInc)
                                    mTempFreq += mStepInc;
                                else
                                    mTempFreq -= mStepInc;

                                mFreq = (int)mTempFreq;

                                if ( (mIsInc && (mFreq > mRangeHigh)) ||
                                        (!mIsInc && (mFreq < mRangeHigh))){
                                    if (mTimer != null){
                                        mTimer.cancel();
                                        mTimer = null;
                                    }
                                    if (mPlaySound != null) {
                                        mPlaySound.stop();
                                        mPlaySound = null;
                                    }
                                    mIsPlay = false;
                                    mHandler.post(mRun);
                                    return;
                                }

                                mPlaySound.mOutputFreq = mFreq;
                            }
                        }
                    };
                    mTimer.schedule(mTimerTask, 0, 1000);
                } else {
                    mIsPlay = false;
                    if (mTimer != null){
                        mTimer.cancel();
                        mTimer = null;
                    }
                    if (mPlaySound != null){
                        mPlaySound.stop();
                        mPlaySound = null;
                    }
                    enableAllInputs();
                    mMetBtn3.setText("PLAY");
                }
                break;
            default:
                Toast.makeText(this, "onClick", Toast.LENGTH_LONG).show();
        }
    }

    private void disableAllInputs(){

        // buttons
        mMetBtn1.setEnabled(false);
        mMetBtn2.setEnabled(false);
        mMetBtn3.setEnabled(false);

        // edits
        mMet_1_edit1.setEnabled(false);
        mMet_3_edit1.setEnabled(false);
        mMet_3_edit2.setEnabled(false);
        mMet_3_edit3.setEnabled(false);

        // seek bar
        mMet_2_seekbar.setEnabled(false);
    }

    private void enableAllInputs(){

        // buttons
        mMetBtn1.setEnabled(true);
        mMetBtn2.setEnabled(true);
        mMetBtn3.setEnabled(true);

        // edits
        mMet_1_edit1.setEnabled(true);
        mMet_3_edit1.setEnabled(true);
        mMet_3_edit2.setEnabled(true);
        mMet_3_edit3.setEnabled(true);

        // seek bar
        mMet_2_seekbar.setEnabled(true);
    }
}
