package com.example.lesson84handlerrunnable;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import static android.widget.CompoundButton.OnCheckedChangeListener;

public class MainActivity extends AppCompatActivity {

    ProgressBar pbCount;
    TextView tvInfo;
    CheckBox chbInfo;
    int cnt;

    final String LOG_TAG = "myLogs";
    final int max = 100;

    Handler h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        h = new Handler();

        pbCount = findViewById(R.id.pbCount);
        pbCount.setMax(max);
        pbCount.setProgress(0);

        tvInfo = findViewById(R.id.tvInfo);

        chbInfo = findViewById(R.id.chbInfo);
        chbInfo.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    tvInfo.setVisibility(View.VISIBLE);
                    // показываем информацию
                    h.post(showInfo);
                } else {
                    tvInfo.setVisibility(View.GONE);
                    // отменяем показ информации
                    h.removeCallbacks(showInfo);
                }
            }
        });
        Thread t = new Thread(load);
//               new Runnable() {
//            public void run() {
//                try {
//                    for (cnt = 1; cnt < max; cnt++) {
//                        TimeUnit.MILLISECONDS.sleep(100);
//                        // обновляем ProgressBar
//                        h.post(updateProgress);
//                    }
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
        t.start();
    }

    Runnable load = new Runnable() {
        @Override
        public void run() {
            try {
                for (cnt = 1; cnt < max; cnt++) {
                    TimeUnit.MILLISECONDS.sleep(100);
                    // обновляем ProgressBar
                    h.post(updateProgress);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    // обновление ProgressBar
    Runnable updateProgress = new Runnable() {
        @Override
        public void run() {
            pbCount.setProgress(cnt);
            if (cnt >= max) {
                Thread t = new Thread(load);
                t.start();
            }
        }
    };
    // показ информации
    Runnable showInfo = new Runnable() {
        @Override
        public void run() {
            Log.d(LOG_TAG, "showInfo");
            tvInfo.setText("Count = " + cnt);
            // планирует сам себя через 1000 мсек
            h.postDelayed(showInfo, 100);
        }
    };
}
