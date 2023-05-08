package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity4 extends AppCompatActivity {

    private EditText hoursEditText, minutesEditText, secondsEditText;

    TextView countDownText;

    private Button start, pause, end, reset;
    private CountDownTimer timer;

    int startTime;

    int hoursLeft, minutesLeft, secondsLeft;
    TextView hoursLeftText, minutesLeftText, secondsLeftText;

    int totalSecondsLeft;

    boolean isPaused = false;

    private void finishTimer(String message) {
        countDownText.setText(message);
        start.setEnabled(true);
        pause.setEnabled(false);
        end.setEnabled(false);
    }

    private void setupEditTexts() {
        hoursEditText = findViewById(R.id.hours);
        minutesEditText = findViewById(R.id.minutes);
        secondsEditText = findViewById(R.id.seconds);

        hoursEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 2) {
                    minutesEditText.requestFocus();
                }
            }
        });

        minutesEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 2) {
                    secondsEditText.requestFocus();
                }
            }
        });
    }

    private void updateTimeRemaining(long millisUntilFinished) {
        totalSecondsLeft = (int) millisUntilFinished / 1000;
        hoursLeft = totalSecondsLeft / 3600;
        minutesLeft = (totalSecondsLeft % 3600) / 60;
        secondsLeft = totalSecondsLeft % 60;
        hoursLeftText.setText(String.format("%02d", hoursLeft));
        minutesLeftText.setText(String.format("%02d", minutesLeft));
        secondsLeftText.setText(String.format("%02d", secondsLeft));
        countDownText.setText("Count down in progress");
    }

    private void setupButtons() {
        start = findViewById(R.id.start_button);
        reset = findViewById(R.id.reset_button);
        pause = findViewById(R.id.pause_button);
        end = findViewById(R.id.end_button);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTime = 0;
                startTime += Integer.parseInt(secondsEditText.getText().toString()) * 1000;
                startTime += Integer.parseInt(minutesEditText.getText().toString()) * 60 * 1000;
                startTime += Integer.parseInt(hoursEditText.getText().toString()) * 60 * 60 * 1000;

                start.setEnabled(false);
                reset.setEnabled(true);
                pause.setEnabled(true);
                end.setEnabled(true);
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
                    NotificationChannel notificationChannel = new
                            NotificationChannel("Notification", "Notification 1", NotificationManager.IMPORTANCE_DEFAULT);
                            NotificationManager manager = (NotificationManager)
                            getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.createNotificationChannel(notificationChannel);
                }

                timer = new CountDownTimer(startTime, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        updateTimeRemaining(millisUntilFinished);
                    }

                    @Override
                    public void onFinish() {
                        finishTimer("Count down complete");
                        Toast.makeText(getApplicationContext(), "Count down complete", Toast.LENGTH_SHORT).show();
                        NotificationCompat.Builder builder = new
                                NotificationCompat.Builder(MainActivity4.this, "Notification 1")
                                .setContentTitle("Count Stopped")
                                .setContentText("Task Completed")
                                .setSmallIcon(R.drawable.ic_launcher_foreground);
                        Notification notification = builder.build();
                        NotificationManagerCompat manager =
                                NotificationManagerCompat.from(MainActivity4.this);


                        if (ActivityCompat.checkSelfPermission(MainActivity4.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        manager.notify(1, notification);

                    }
                }.start();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start.setEnabled(false);
                reset.setEnabled(true);
                pause.setEnabled(true);
                pause.setText("Pause");
                isPaused = false;
                end.setEnabled(true);

                timer.cancel();
                timer = new CountDownTimer(startTime, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        updateTimeRemaining(millisUntilFinished);
                    }

                    @Override
                    public void onFinish() {
                        finishTimer("Count down complete");
                    }
                }.start();
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPaused = !isPaused;
                if (isPaused) {
                    pause.setText("Resume");
                    timer.cancel();
                    countDownText.setText("Count down paused");
                } else {
                    pause.setText("Pause");
                    timer = new CountDownTimer(totalSecondsLeft * 1000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            updateTimeRemaining(millisUntilFinished);
                        }

                        @Override
                        public void onFinish() {
                            finishTimer("Count down complete");
                        }
                    }.start();
                }

            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                finishTimer("Count down cancelled");

            }
        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        setupEditTexts();

        countDownText = findViewById(R.id.countDownText);
        hoursLeftText = findViewById(R.id.hoursLeftText);
        minutesLeftText = findViewById(R.id.minutesLeftText);
        secondsLeftText = findViewById(R.id.secondsLeftText);

        setupButtons();
    }
}