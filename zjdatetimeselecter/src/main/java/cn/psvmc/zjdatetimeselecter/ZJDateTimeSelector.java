package cn.psvmc.zjdatetimeselecter;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class ZJDateTimeSelector {
    String TAG = "ZJDateTimeSelector";

    private View outerView;
    ZJWheelView yearView, monthView, dayView;
    ZJWheelView hourView, minuteView;
    TextView hour_minute_delimiter;

    private boolean hasSelectTime;
    private static int START_YEAR = 1990, END_YEAR = 2100;
    private static int START_MONTH = 1, END_MONTH = 12;
    private static int START_DAY = 1, END_DAY = 31;
    private static int START_HOUR = 0, END_HOUR = 23;
    private static int START_MINUTE = 0, END_MINUTE = 59;


    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    // 添加大小月月份并将其转换为list,方便之后的判断
    String[] months_big = {"1", "3", "5", "7", "8", "10", "12"};
    String[] months_little = {"4", "6", "9", "11"};

    final List<String> list_big = Arrays.asList(months_big);
    final List<String> list_little = Arrays.asList(months_little);

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    private Date beginDate;
    private Date endDate;

    public View getView() {
        return outerView;
    }

    public void setView(View view) {
        if (beginDate == null) {
            beginDate = ZJDateUtils.getDate(2000, 1, 1, 0, 0);
        }

        if (endDate == null) {
            endDate = ZJDateUtils.getDate(2100, 1, 1, 0, 0);
        }
        this.outerView = view;
    }


    public ZJDateTimeSelector(View view) {
        super();
        this.outerView = view;
        hasSelectTime = true;
        setView(view);
    }

    public ZJDateTimeSelector(View view, boolean hasSelectTime) {
        super();
        this.outerView = view;
        this.hasSelectTime = hasSelectTime;
        setView(view);
    }

    /**
     * @Description: 弹出日期时间选择器
     */
    public void initDateTimePicker(Date selectDate) {
        if (selectDate.before(beginDate)) {
            selectDate = beginDate;
        }
        if (selectDate.after(endDate)) {
            selectDate = endDate;
        }

        int year = ZJDateUtils.getYearByDate(selectDate);
        int month = ZJDateUtils.getMonthByDate(selectDate);
        int day = ZJDateUtils.getDayByDate(selectDate);
        int hour = ZJDateUtils.getHourByDate(selectDate);
        int minute = ZJDateUtils.getMinuteByDate(selectDate);


        //设置开始和结束

        // 判断大小月及是否闰年,用来确定"日"的数据
        if (list_big.contains(String.valueOf(month))) {
            END_DAY = 31;
        } else if (list_little.contains(String.valueOf(month))) {
            END_DAY = 30;
        } else {
            // 闰年
            if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
                END_DAY = 29;
            else
                END_DAY = 28;
        }

        START_YEAR = ZJDateUtils.getYearByDate(beginDate);
        END_YEAR = ZJDateUtils.getYearByDate(endDate);
        if (START_YEAR == END_YEAR) {
            START_MONTH = ZJDateUtils.getMonthByDate(beginDate);
            END_MONTH = ZJDateUtils.getMonthByDate(endDate);
            if (START_MONTH == END_MONTH) {
                START_DAY = ZJDateUtils.getDayByDate(beginDate);
                END_DAY = ZJDateUtils.getDayByDate(endDate);
                if (START_DAY == END_DAY) {
                    START_HOUR = ZJDateUtils.getHourByDate(beginDate);
                    END_HOUR = ZJDateUtils.getHourByDate(endDate);
                    if (START_HOUR == END_HOUR) {
                        START_MINUTE = ZJDateUtils.getMinuteByDate(beginDate);
                        END_MINUTE = ZJDateUtils.getMinuteByDate(endDate);
                    }
                }
            }
        }
        yearView = (ZJWheelView) outerView.findViewById(R.id.wheel_view_year);
        monthView = (ZJWheelView) outerView.findViewById(R.id.wheel_view_month);
        dayView = (ZJWheelView) outerView.findViewById(R.id.wheel_view_day);
        hourView = (ZJWheelView) outerView.findViewById(R.id.wheel_view_hour);
        minuteView = (ZJWheelView) outerView.findViewById(R.id.wheel_view_minute);
        hour_minute_delimiter = (TextView) outerView.findViewById(R.id.hour_minute_delimiter);
        Log.i(TAG, "initDateTimePicker: year:" + year + "  month:" + month + "  day:" + day);
        yearView.setItemsNew(ZJWheelItem.listFromNum(START_YEAR, END_YEAR));
        yearView.setSelectValue("" + year, false);
        yearView.setOnWheelViewListener(new ZJWheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, ZJWheelItem item) {
                jiaozhengDate();
            }
        });

        monthView.setItemsNew(ZJWheelItem.listFromNum(START_MONTH, END_MONTH));
        monthView.setSelectValue("" + month, false);
        monthView.setOnWheelViewListener(new ZJWheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, ZJWheelItem item) {
                jiaozhengDate();
            }
        });

        dayView.setItemsNew(ZJWheelItem.listFromNum(START_DAY, END_DAY));
        dayView.setSelectValue("" + day, false);
        dayView.setOnWheelViewListener(new ZJWheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, ZJWheelItem item) {
                jiaozhengDate();
            }
        });

        hourView.setItemsNew(ZJWheelItem.listFromNum(START_HOUR, END_HOUR));
        hourView.setSelectValue("" + hour, false);
        hourView.setOnWheelViewListener(new ZJWheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, ZJWheelItem item) {
                jiaozhengDate();
            }
        });

        minuteView.setItemsNew(ZJWheelItem.listFromNum(START_MINUTE, END_MINUTE));
        minuteView.setSelectValue("" + minute, false);
        minuteView.setOnWheelViewListener(new ZJWheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, ZJWheelItem item) {
                jiaozhengDate();
            }
        });

        if(!hasSelectTime){
            hourView.setVisibility(View.GONE);
            minuteView.setVisibility(View.GONE);
            hour_minute_delimiter.setVisibility(View.GONE);
        }
    }

    /**
     * 获取日期字符串
     *
     * @return
     */
    public String getTime() {
        Date date = getDateTime();
        String dateStr = "";
        if (!hasSelectTime) {
            dateStr = dateFormat.format(date);
        } else {
            dateStr = dateTimeFormat.format(date);
        }
        return dateStr;
    }

    /**
     * 获取日期
     *
     * @return
     */
    public Date getDateTime() {
        int year = Integer.valueOf(yearView.getSeletedItem().value);
        int month = Integer.valueOf(monthView.getSeletedItem().value);
        int day = Integer.valueOf(dayView.getSeletedItem().value);
        int hour = Integer.valueOf(hourView.getSeletedItem().value);
        int minute = Integer.valueOf(minuteView.getSeletedItem().value);
        Date date = ZJDateUtils.getDate(year, month, day, hour, minute);
        return date;
    }

    private void jiaozhengDate() {
        int year = Integer.valueOf(yearView.getSeletedItem().value);
        int month = Integer.valueOf(monthView.getSeletedItem().value);
        int day = Integer.valueOf(dayView.getSeletedItem().value);
        int maxDay = Integer.valueOf(dayView.getItemsOriginal().get(dayView.getItemsOriginal().size() - 1).value);
        if (month == 2) {
            if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                if (maxDay != 29) {
                    dayView.setItemsNew(ZJWheelItem.listFromNum(1, 29));
                    if (day > 29) {
                        dayView.setSelectValue("29", false);
                    }
                }

            } else {
                if (maxDay != 28) {
                    dayView.setItemsNew(ZJWheelItem.listFromNum(1, 28));
                    if (day > 28) {
                        dayView.setSelectValue("28", false);
                    }
                }
            }

        } else if (Arrays.asList(months_big).contains("" + month)) {
            if (maxDay != 31) {
                dayView.setItemsNew(ZJWheelItem.listFromNum(1, 31));
                if (day > 31) {
                    dayView.setSelectValue("31", false);
                }
            }
        } else {
            if (maxDay != 30) {
                dayView.setItemsNew(ZJWheelItem.listFromNum(1, 30));
                if (day > 30) {
                    dayView.setSelectValue("30", false);
                }
            }
        }
        Date date = getDateTime();
        if (date.after(endDate)) {
            date = endDate;
        }else if(date.before(beginDate)){
            date = beginDate;
        }

        final int yearNew = ZJDateUtils.getYearByDate(date);
        final int monthNew = ZJDateUtils.getMonthByDate(date);
        final int dayNew = ZJDateUtils.getDayByDate(date);
        final int hourNew = ZJDateUtils.getHourByDate(date);
        final int minuteNew = ZJDateUtils.getMinuteByDate(date);
        new Handler().postDelayed(new Runnable(){
            public void run() {
                yearView.setSelectValue(""+yearNew, true);
                monthView.setSelectValue(""+monthNew, true);
                dayView.setSelectValue(""+dayNew, true);
                hourView.setSelectValue(""+hourNew, true);
                minuteView.setSelectValue(""+minuteNew, true);
            }
        }, 300);


    }

}
