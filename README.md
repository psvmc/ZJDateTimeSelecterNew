# ZJDateTimeSelecterNew
日期时间选择器 新版 

与旧版实现方式不同 调用方式相同


使用方式

## 引用

```
compile 'cn.psvmc:ZJDateTimeSelecterNew:1.1.2'
```

## 代码

```java
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
```