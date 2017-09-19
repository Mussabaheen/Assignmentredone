package com.example.mussabaheenmalik.assignment;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    int JOB_ID =0;

    private JobScheduler jobScheduler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    private void scheduleJob() {
        jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);

        ComponentName serviceName = new ComponentName(getPackageName(),
                NotificationJobService.class.getName());
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, serviceName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setRequiresDeviceIdle(true)
                .setRequiresCharging(true);

        JobInfo myJobInfo = builder.build();
        jobScheduler.schedule(myJobInfo);
        Toast.makeText(this, "JOB READY ", Toast.LENGTH_SHORT).show();
    }

    private void myNotify(){
        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager mymanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle("DOWNLOADER")
                .setContentText("DOWNLOADING THE FILE")
                .setContentIntent(contentPendingIntent)
                .setSmallIcon(android.R.drawable.button_onoff_indicator_off)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true);

        mymanager.notify(0, builder.build());
    }


    public void download(View view) {
        myNotify();
        scheduleJob();

    }

    public void canceling(View view) {
        jobScheduler.cancelAll();
        Toast.makeText(this, "JOB CANCELLED", Toast.LENGTH_SHORT).show();
    }

    public class NotificationJobService extends JobService {

        @Override
        public boolean onStartJob(JobParameters jobParameters) {
            myNotify();
            return false;
        }

        @Override
        public boolean onStopJob(JobParameters jobParameters) {
            return true;
        }
    }

}

