package cn.psvmc.zjdatetimeselecternew;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.psvmc.zjdatetimeselecter.ZJJudgeDate;
import cn.psvmc.zjdatetimeselecter.ZJDateTimeSelector;
import cn.psvmc.zjdatetimeselecter.ZJDateUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //选择日期时间
        final TextView textView = (TextView) findViewById(R.id.textView);
        findViewById(R.id.chooseDateTime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choseDateTime(textView);
            }
        });

        //选择日期
        final TextView datetextView = (TextView) findViewById(R.id.datetextView);
        findViewById(R.id.chooseDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choseDate(datetextView);
            }
        });
    }

    /**
     * 选择日期的方法
     *
     * @param txtView
     */
    public void choseDateTime(final TextView txtView) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        final ZJDateTimeSelector dateTimeSelector = new ZJDateTimeSelector(this, true);
        Date beginDate = ZJDateUtils.getDate(2014, 5, 1, 0, 0);
        Date endDate = ZJDateUtils.getDate(2036, 6, 7, 1, 0);
        dateTimeSelector.setBeginDate(beginDate);
        dateTimeSelector.setEndDate(endDate);
        String time = txtView.getText().toString();
        Calendar calendar = Calendar.getInstance();
        if (ZJJudgeDate.isDate(time, "yyyy-MM-dd HH:mm")) {
            try {
                calendar.setTime(dateFormat.parse(time));
            } catch (ParseException e) {
                Log.i("时间", "choseDateTime " + e.getMessage());

            }
        }

        dateTimeSelector.initDateTimePicker(calendar.getTime());

        new AlertDialog.Builder(MainActivity.this)
                .setTitle("预约时间")
                .setView(dateTimeSelector.getView())
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        txtView.setText(dateTimeSelector.getTime());
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }


    public void choseDate(final TextView txtView) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        final ZJDateTimeSelector dateTimeSelector = new ZJDateTimeSelector(this, ZJDateTimeSelector.showYearAndMonth);
        Date beginDate = ZJDateUtils.getDate(2014, 5, 1, 0, 0);
        Date endDate = ZJDateUtils.getDate(2036, 6, 7, 1, 0);
        dateTimeSelector.setBeginDate(beginDate);
        dateTimeSelector.setEndDate(endDate);
        String time = txtView.getText().toString();
        Calendar calendar = Calendar.getInstance();
        if (ZJJudgeDate.isDate(time, "yyyy-MM-dd")) {
            try {
                calendar.setTime(dateFormat.parse(time));
            } catch (ParseException e) {
                Log.i("时间", "choseDate " + e.getMessage());

            }
        }

        dateTimeSelector.initDateTimePicker(calendar.getTime());

        new AlertDialog.Builder(MainActivity.this)
                .setTitle("预约时间")
                .setView(dateTimeSelector.getView())
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        txtView.setText(dateTimeSelector.getTime());
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }
}
