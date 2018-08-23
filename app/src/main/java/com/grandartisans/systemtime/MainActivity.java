package com.grandartisans.systemtime;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.grandartisans.systemtime.adpater.AreaListAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private Context mContext;
    private LinearLayout date_ll;
    private RelativeLayout Year_rl, Month_rl, Day_rl, Hour_rl, Minute_rl, Second_rl;

    private ListView YearList;
    private ListView MonthList;
    private ListView DayList;
    private ListView HourList;
    private ListView MinuteList;
    private ListView SecondList;

    private Button Btn_setting;

    private ArrayList<String> YeardataList = new ArrayList<String>();
    private ArrayList<String> MonthdataList = new ArrayList<String>();
    private ArrayList<String> DaydataList = new ArrayList<String>();
    private ArrayList<String> HourdataList = new ArrayList<String>();
    private ArrayList<String> MinutedataList = new ArrayList<String>();
    private ArrayList<String> SeconddataList = new ArrayList<String>();

    private AreaListAdapter mYearAdapter = null;
    private AreaListAdapter mMonthAdapter = null;
    private AreaListAdapter mDayAdapter = null;
    private AreaListAdapter mHourAdapter = null;
    private AreaListAdapter mMinuteAdapter = null;
    private AreaListAdapter mSecondAdapter = null;

    private TextView Year_text, Month_text, Day_text, Hour_text, Minute_text, Second_text;
    private TextView Time_text;

    private static final int UPDATETIME = 1024;
    private final int DATA_SIZE = 30;

    private String lastYear = "";
    private String lastMonth = "";
    private String lastDay = "";
    private String lastHour = "";
    private String lastMinute = "";
    private String lastSecond = "";
    private String currentTime = "";

    private int YearAreaPos = Integer.MAX_VALUE / 2;
    private int MonthAreaPos = Integer.MAX_VALUE / 2;
    private int DayAreaPos = Integer.MAX_VALUE / 2;
    private int HourAreaPos = Integer.MAX_VALUE / 2;
    private int MinuteAreaPos = Integer.MAX_VALUE / 2;
    private int SecondAreaPos = Integer.MAX_VALUE / 2;

    private SimpleDateFormat sdf;

    Handler h = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATETIME:
                    h.sendEmptyMessageDelayed(UPDATETIME, 5*1000);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        initView();
        initDateData();
        initFocus();
        Year_rl.requestFocus();
        Btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "设置时间为: " + currentTime, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        date_ll = (LinearLayout) findViewById(R.id.datelocal_ll);

        YearList = (ListView) findViewById(R.id.AreaList_Year);
        MonthList = (ListView) findViewById(R.id.AreaList_Month);
        DayList = (ListView) findViewById(R.id.AreaList_Day);
        HourList = (ListView) findViewById(R.id.AreaList_Hour);
        MinuteList = (ListView) findViewById(R.id.AreaList_Minute);
        SecondList = (ListView) findViewById(R.id.AreaList_Second);

        Btn_setting = (Button) findViewById(R.id.btn_setting);

        Year_rl = (RelativeLayout) findViewById(R.id.Year_rl);
        Month_rl = (RelativeLayout) findViewById(R.id.Month_rl);
        Day_rl = (RelativeLayout) findViewById(R.id.Day_rl);
        Hour_rl = (RelativeLayout) findViewById(R.id.Hour_rl);
        Minute_rl = (RelativeLayout) findViewById(R.id.Minute_rl);
        Second_rl = (RelativeLayout) findViewById(R.id.Second_rl);

        Year_text = (TextView) findViewById(R.id.Year_text);
        Month_text = (TextView) findViewById(R.id.Month_text);
        Day_text = (TextView) findViewById(R.id.Day_text);
        Hour_text = (TextView) findViewById(R.id.Hour_text);
        Minute_text = (TextView) findViewById(R.id.Minute_text);
        Second_text = (TextView) findViewById(R.id.Second_text);
        Time_text = (TextView) findViewById(R.id.time_text);

        h.sendEmptyMessage(UPDATETIME);
    }

    private void initDateData() {
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        lastYear = Integer.toString(year);
        lastMonth = getIntegerFormat(month + 1);
        lastDay = getIntegerFormat(day);
        lastHour = getIntegerFormat(hour);
        lastMinute = getIntegerFormat(minute);
        lastSecond = getIntegerFormat(second);
        setFormatTime(calendar);
        setYearValue(year);
        setMonthValue();
        setDayValue(year, month);
        setHourValue();
        setMinuteValue();
        setSecondValue();
    }

    private void initFocus() {
        Year_rl.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Year_text.setVisibility(View.GONE);
                    YearList.setVisibility(View.VISIBLE);
                    Month_text.setText(lastMonth);
                    Day_text.setText(lastDay);
                    Hour_text.setText(lastHour);
                    Minute_text.setText(lastMinute);
                    Second_text.setText(lastSecond);
                    YearList.requestFocus();
                }
            }
        });

        YearList.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    Year_text.setText(lastYear);
                    Year_text.setVisibility(View.VISIBLE);
                    YearList.setVisibility(View.GONE);
                }
            }
        });

        Month_rl.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Month_text.setVisibility(View.GONE);
                    MonthList.setVisibility(View.VISIBLE);
                    MonthList.requestFocus();
                }
            }
        });

        MonthList.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    Month_text.setText(lastMonth);
                    Month_text.setVisibility(View.VISIBLE);
                    MonthList.setVisibility(View.GONE);
                }
            }
        });

        Day_rl.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Day_text.setVisibility(View.GONE);
                    DayList.setVisibility(View.VISIBLE);
                    DayList.requestFocus();
                }
            }
        });

        DayList.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    Day_text.setText(lastDay);
                    Day_text.setVisibility(View.VISIBLE);
                    DayList.setVisibility(View.GONE);
                }
            }
        });

        Hour_rl.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Hour_text.setVisibility(View.GONE);
                    HourList.setVisibility(View.VISIBLE);
                    HourList.requestFocus();
                }
            }
        });

        HourList.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    Hour_text.setText(lastHour);
                    Hour_text.setVisibility(View.VISIBLE);
                    HourList.setVisibility(View.GONE);
                }
            }
        });

        Minute_rl.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Minute_text.setVisibility(View.GONE);
                    MinuteList.setVisibility(View.VISIBLE);
                    MinuteList.requestFocus();
                }
            }
        });

        MinuteList.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    Minute_text.setText(lastMinute);
                    Minute_text.setVisibility(View.VISIBLE);
                    MinuteList.setVisibility(View.GONE);
                }
            }
        });

        Second_rl.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Second_text.setVisibility(View.GONE);
                    SecondList.setVisibility(View.VISIBLE);
                    SecondList.requestFocus();
                }
            }
        });

        SecondList.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    Second_text.setText(lastSecond);
                    Second_text.setVisibility(View.VISIBLE);
                    SecondList.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setFormatTime(Calendar calendar) {
        String time = sdf.format(calendar.getTimeInMillis());
        Time_text.setText(time);
    }

    private void setYearValue(int year) {
        YeardataList.clear();
        for (int i = 0; i < DATA_SIZE; i++) {
            YeardataList.add(Integer.toString(year - DATA_SIZE / 2 + i));
        }
        mYearAdapter = new AreaListAdapter(this, YeardataList);
        initYearList(YearList, mYearAdapter, YeardataList);
    }

    private void setMonthValue() {
        MonthdataList.clear();
        for (int i = 0; i < 12; i++) {
            if (i < 9)
                MonthdataList.add(0 + Integer.toString(i + 1));
            else
                MonthdataList.add(Integer.toString(i + 1));
        }
        mMonthAdapter = new AreaListAdapter(this, MonthdataList);
        initMonthList(MonthList, mMonthAdapter, MonthdataList);
    }

    private void setDayValue(int year, int month) {
        DaydataList.clear();
        int day_num = getMaxDayofMonth(year, month);
        for (int i = 0; i < day_num; i++) {
            if (i < 9)
                DaydataList.add(0 + Integer.toString(i + 1));
            else
                DaydataList.add(Integer.toString(i + 1));
        }
        mDayAdapter = new AreaListAdapter(this, DaydataList);
        initDayList(DayList, mDayAdapter, DaydataList);
    }

    private void setHourValue() {
        HourdataList.clear();
        for (int i = 0; i < 24; i++) {
            if (i < 10)
                HourdataList.add(0 + Integer.toString(i));
            else
                HourdataList.add(Integer.toString(i));
        }
        mHourAdapter = new AreaListAdapter(this, HourdataList);
        initHourList(HourList, mHourAdapter, HourdataList);
    }

    private void setMinuteValue() {
        MinutedataList.clear();
        for (int i = 0; i < 60; i++) {
            if (i < 10)
                MinutedataList.add(0 + Integer.toString(i));
            else
                MinutedataList.add(Integer.toString(i));
        }
        mMinuteAdapter = new AreaListAdapter(this, MinutedataList);
        initMinuteList(MinuteList, mMinuteAdapter, MinutedataList);
    }

    private void setSecondValue() {
        SeconddataList.clear();
        for (int i = 0; i < 60; i++) {
            if (i < 10)
                SeconddataList.add(0 + Integer.toString(i));
            else
                SeconddataList.add(Integer.toString(i));
        }
        mSecondAdapter = new AreaListAdapter(this, SeconddataList);
        initSecondList(SecondList, mSecondAdapter, SeconddataList);
    }

    private void initYearList(final ListView AreaList, final AreaListAdapter mAdapter, ArrayList<String> dataList) {
        YearAreaPos = Integer.MAX_VALUE / 2 + getkey(dataList.size(), getCurKeyPos(dataList, lastYear));
        mAdapter.setPosition(YearAreaPos);
        AreaList.setAdapter(mAdapter);
        AreaList.setSelectionFromTop(YearAreaPos, 75);
        AreaList.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_UP && event.getAction() == KeyEvent.ACTION_UP) {
                    AreaList.smoothScrollToPositionFromTop(YearAreaPos, 75, 100);
                    mAdapter.setPosition(YearAreaPos);
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && event.getAction() == KeyEvent.ACTION_UP) {
                    AreaList.smoothScrollToPositionFromTop(YearAreaPos, 75, 100);
                    mAdapter.setPosition(YearAreaPos);
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && event.getAction() == KeyEvent.ACTION_DOWN) {

                }
                return false;
            }
        });

        AreaList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                YearAreaPos = position;
                lastYear = parent.getSelectedItem().toString();
                updateTimeValue();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void initMonthList(final ListView AreaList, final AreaListAdapter mAdapter, ArrayList<String> dataList) {
        MonthAreaPos = Integer.MAX_VALUE / 2 + getkey(dataList.size(), getCurKeyPos(dataList, lastMonth));
        mAdapter.setPosition(MonthAreaPos);
        AreaList.setAdapter(mAdapter);
        AreaList.setSelectionFromTop(MonthAreaPos, 75);
        AreaList.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_UP && event.getAction() == KeyEvent.ACTION_UP) {
                    AreaList.smoothScrollToPositionFromTop(MonthAreaPos, 75, 100);
                    mAdapter.setPosition(MonthAreaPos);
                    updateDayValue();
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && event.getAction() == KeyEvent.ACTION_UP) {
                    AreaList.smoothScrollToPositionFromTop(MonthAreaPos, 75, 10);
                    mAdapter.setPosition(MonthAreaPos);
                    updateDayValue();
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && event.getAction() == KeyEvent.ACTION_DOWN) {

                }
                return false;
            }
        });

        AreaList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MonthAreaPos = position;
                lastMonth = parent.getSelectedItem().toString();
                updateTimeValue();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void initDayList(final ListView AreaList, final AreaListAdapter mAdapter, final ArrayList<String> dataList) {
        DayAreaPos = Integer.MAX_VALUE / 2 + getkey(dataList.size(), getCurKeyPos(dataList, lastDay));
        mAdapter.setPosition(DayAreaPos);
        AreaList.setAdapter(mAdapter);
        AreaList.setSelectionFromTop(DayAreaPos, 75);
        AreaList.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_UP && event.getAction() == KeyEvent.ACTION_UP) {
                    AreaList.smoothScrollToPositionFromTop(DayAreaPos, 75, 100);
                    mAdapter.setPosition(DayAreaPos);
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && event.getAction() == KeyEvent.ACTION_UP) {
                    AreaList.smoothScrollToPositionFromTop(DayAreaPos, 75, 100);
                    mAdapter.setPosition(DayAreaPos);
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && event.getAction() == KeyEvent.ACTION_DOWN) {

                }
                return false;
            }
        });

        AreaList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DayAreaPos = position;
                lastDay = parent.getSelectedItem().toString();
                updateTimeValue();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void initHourList(final ListView AreaList, final AreaListAdapter mAdapter, final ArrayList<String> dataList) {
        HourAreaPos = Integer.MAX_VALUE / 2 + getkey(dataList.size(), getCurKeyPos(dataList, lastHour));
        mAdapter.setPosition(HourAreaPos);
        AreaList.setAdapter(mAdapter);
        AreaList.setSelectionFromTop(HourAreaPos, 75);
        AreaList.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_UP && event.getAction() == KeyEvent.ACTION_UP) {
                    AreaList.smoothScrollToPositionFromTop(HourAreaPos, 75, 100);
                    mAdapter.setPosition(HourAreaPos);
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && event.getAction() == KeyEvent.ACTION_UP) {
                    AreaList.smoothScrollToPositionFromTop(HourAreaPos, 75, 100);
                    mAdapter.setPosition(HourAreaPos);
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && event.getAction() == KeyEvent.ACTION_DOWN) {

                }
                return false;
            }
        });

        AreaList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                HourAreaPos = position;
                lastHour = parent.getSelectedItem().toString();
                updateTimeValue();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void initMinuteList(final ListView AreaList, final AreaListAdapter mAdapter, final ArrayList<String> dataList) {
        MinuteAreaPos = Integer.MAX_VALUE / 2 + getkey(dataList.size(), getCurKeyPos(dataList, lastMinute));
        mAdapter.setPosition(MinuteAreaPos);
        AreaList.setAdapter(mAdapter);
        AreaList.setSelectionFromTop(MinuteAreaPos, 75);
        AreaList.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_UP && event.getAction() == KeyEvent.ACTION_UP) {
                    AreaList.smoothScrollToPositionFromTop(MinuteAreaPos, 75, 100);
                    mAdapter.setPosition(MinuteAreaPos);
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && event.getAction() == KeyEvent.ACTION_UP) {
                    AreaList.smoothScrollToPositionFromTop(MinuteAreaPos, 75, 100);
                    mAdapter.setPosition(MinuteAreaPos);
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && event.getAction() == KeyEvent.ACTION_DOWN) {

                }
                return false;
            }
        });

        AreaList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MinuteAreaPos = position;
                lastMinute = parent.getSelectedItem().toString();
                updateTimeValue();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void initSecondList(final ListView AreaList, final AreaListAdapter mAdapter, final ArrayList<String> dataList) {
        SecondAreaPos = Integer.MAX_VALUE / 2 + getkey(dataList.size(), getCurKeyPos(dataList, lastMinute));
        mAdapter.setPosition(SecondAreaPos);
        AreaList.setAdapter(mAdapter);
        AreaList.setSelectionFromTop(SecondAreaPos, 75);
        AreaList.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_UP && event.getAction() == KeyEvent.ACTION_UP) {
                    AreaList.smoothScrollToPositionFromTop(SecondAreaPos, 75, 100);
                    mAdapter.setPosition(SecondAreaPos);
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && event.getAction() == KeyEvent.ACTION_UP) {
                    AreaList.smoothScrollToPositionFromTop(SecondAreaPos, 75, 100);
                    mAdapter.setPosition(SecondAreaPos);
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && event.getAction() == KeyEvent.ACTION_DOWN) {

                }
                return false;
            }
        });

        AreaList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SecondAreaPos = position;
                lastSecond = parent.getSelectedItem().toString();
                updateTimeValue();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void updateDayValue() {
        DaydataList.clear();
        int year = Integer.parseInt(lastYear);
        int month = Integer.parseInt(lastMonth);
        int data_num = getMaxDayofMonth(year, month);
        for (int i = 0; i < data_num; i++) {
            if (i < 9)
                DaydataList.add(0 + Integer.toString(i + 1));
            else
                DaydataList.add(Integer.toString(i + 1));
        }
        mDayAdapter.notifyDataSetChanged();
    }

    private void updateTimeValue() {
        String time = lastYear + "-" + lastMonth + "-" + lastDay + " " +
                lastHour + ":" + lastMinute + ":" + lastSecond;
        Time_text.setText(time);
        currentTime = time;
    }

    private int getkey(int count, int num) {
        int n = 0;
        if (count > 0) {
            n = (Integer.MAX_VALUE / 2 / count + 1) * count - Integer.MAX_VALUE / 2 + num;
        }
        return n;
    }

    private int getCurKeyPos(ArrayList<String> str, String item) {
        for (int i = 0; i < str.size(); i++) {
            if (str.get(i).equals(item)) {
                return i;
            }
        }
        return 0;
    }

    /**
     * Get days in month
     *
     * @param year
     * @param month
     * @return
     */
    private int getMaxDayofMonth(int year, int month) {
        int result = 0;
        if (month == 1 || month == 3 || month == 5 || month == 7 ||
                month == 8 || month == 10 || month == 12) {
            result = 31;
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            result = 30;
        } else {
            if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                result = 29;
            } else {
                result = 28;
            }
        }
        return result;
    }

    private String getIntegerFormat(int integer) {
        String str;
        if (integer < 10)
            str = "0" + Integer.toString(integer);
        else
            str = Integer.toString(integer);
        return str;
    }
}
