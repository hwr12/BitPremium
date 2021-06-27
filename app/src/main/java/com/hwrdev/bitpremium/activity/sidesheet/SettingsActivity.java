package com.planet.premium.activity.sidesheet;



import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.planet.premium.R;
import com.planet.premium.utils.ThemeUtil;


import java.util.List;

import static com.planet.premium.activity.sidesheet.MainActivity.killApp;

public class SettingsActivity extends AppCompatActivity {

    private static final String LATEST_VERSION_KEY = "latest_version";
    public String latest_version;
    TextView version_stat;
    SharedPreferences pref;
    TextView username;
    Context ctx;
    Switch switch_;
    Intent onMenuClickIntent;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_fragment);
        version_stat = findViewById(R.id.version_status);
        pref = getSharedPreferences("data", Activity.MODE_PRIVATE);
        String version = pref.getString("latestVersionInfo", "");
        ctx = getApplicationContext();
        switch_ = (Switch) findViewById(R.id.darkModeSwitch);
        if(ThemeUtil.modLoad(ctx) ==  ThemeUtil.DARK_MODE) {
            String themeColor = ThemeUtil.DARK_MODE;
            ThemeUtil.applyTheme(themeColor);
            switch_.setChecked(true);
        } else {
            String themeColor = ThemeUtil.LIGHT_MODE;
            ThemeUtil.applyTheme(themeColor);
            switch_.setChecked(false);
        }
        Log.d("settings", "실행됨");

        switch_.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    String themeColor = ThemeUtil.DARK_MODE;
                    ThemeUtil.applyTheme(themeColor);
                    ThemeUtil.modSave(getApplicationContext(),themeColor);
                    //isChecked ==true
                }

                else{
                    String themeColor = ThemeUtil.LIGHT_MODE;
                    ThemeUtil.applyTheme(themeColor);
                    ThemeUtil.modSave(getApplicationContext(),themeColor);
                    //off 일때 함수
                }
            }
        });
    }

    @Override
    public void onPause() {
        if (isApplicationSentToBackground(this)){

            //finishAffinity();
            killApp(true);
            //thread.interrupt();
            // Do what you want to do on detecting Home Key being Pressed
            Log.d("thread", "쓰레드 interrupt");
        }
        super.onPause();
    }

    public boolean isApplicationSentToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }




    @Nullable


    private String MyAppVersion(){

        PackageInfo pi = null;
        try {
            pi = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        String version = pi.versionName;
        return version;
    }

    public void onSettingClick(View view) {
        switch (view.getId()){
            case R.id.manage_api: onMenuClickIntent = new Intent(getApplicationContext(), TradingViewActivity.class);


            break;


        }

    }




}
