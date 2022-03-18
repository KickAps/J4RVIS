package com.example.j4rvis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private ConstraintLayout constraintLayout;
    private ImageView imgAnimation1;
    private ImageView imgAnimation2;
    private Button startButton;

    private SpeechRecognizer speechRecognizer;
    private Intent intentRecognizer;
    private Runnable runnable;
    private Handler handlerAnimation;
    private boolean statusAnimation = false;

    private Vibrator vibrator;

    @Override
    protected void onStart() {
        super.onStart();
        StartButton(null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PackageManager.PERMISSION_GRANTED);

        textView = findViewById(R.id.textView);
        constraintLayout = findViewById(R.id.background);
        imgAnimation1 = findViewById(R.id.imgAnimation1);
        imgAnimation2 = findViewById(R.id.imgAnimation2);
        startButton = findViewById(R.id.start_button);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        intentRecognizer = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intentRecognizer.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {
                StopButton();
            }

            @Override
            public void onResults(Bundle bundle) {
                StopButton();

                ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                String string = "";

                if(matches == null) {
                    return;
                }

                string = matches.get(0);
                if(string.contains("couleur")) {
                    if(string.contains("rouge")) {
                        textView.setTextColor(Color.RED);
                        return;
                    } else if(string.contains("noire")) {
                        textView.setTextColor(Color.BLACK);
                        return;
                    } else if(string.contains("bleu") || string.contains("bleue")) {
                        textView.setTextColor(Color.BLUE);
                        return;
                    } else if(string.contains("verte")) {
                        textView.setTextColor(Color.GREEN);
                        return;
                    }
                } else if(string.contains("fond")) {
                    if(string.contains("rouge")) {
                        constraintLayout.setBackgroundColor(Color.RED);
                        return;
                    } else if(string.contains("noir")) {
                        constraintLayout.setBackgroundColor(Color.BLACK);
                        return;
                    } else if(string.contains("bleu")) {
                        constraintLayout.setBackgroundColor(Color.BLUE);
                        return;
                    } else if(string.contains("vert")) {
                        constraintLayout.setBackgroundColor(Color.GREEN);
                        return;
                    }
                }

                textView.setText(string);
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        handlerAnimation = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                imgAnimation1.animate().scaleX(4.0F).scaleY(4.0F).alpha(0.0F).setDuration(1000L).withEndAction((Runnable)(new Runnable() {
                    public final void run() {
                        imgAnimation1.setScaleX(1.0F);
                        imgAnimation1.setScaleY(1.0F);
                        imgAnimation1.setAlpha(1.0F);
                    }
                }));

                imgAnimation2.animate().scaleX(4.0F).scaleY(4.0F).alpha(0.0F).setDuration(700L).withEndAction((Runnable)(new Runnable() {
                    public final void run() {
                        imgAnimation2.setScaleX(1.0F);
                        imgAnimation2.setScaleY(1.0F);
                        imgAnimation2.setAlpha(1.0F);
                    }
                }));

                handlerAnimation.postDelayed(this, 1500L);
            }
        };

        StartButton(null);
    }

    public void StartButton(View view) {
        if(statusAnimation) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
        }

        speechRecognizer.startListening(intentRecognizer);
        startButton.setText(R.string.waiting);
        startPulse();

        statusAnimation = true;
    }

    public void StopButton() {
        stopPulse();
        startButton.setText(R.string.start);
        statusAnimation = false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK));
        }
    }

    private void startPulse() {
        runnable.run();
    }

    private void stopPulse() {
        handlerAnimation.removeCallbacks(runnable);
    }
}