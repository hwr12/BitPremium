package com.planet.premium.activity.sidesheet;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.Toolbar;


import com.planet.premium.R;
import com.planet.premium.model.Event;
import com.planet.premium.utils.Tools;

import java.util.ArrayList;
import java.util.List;


public class ApiManageActivity extends AppCompatActivity {

    ImageButton binance_edit;
    ImageButton bithumb_edit;
    ImageButton upbit_edit;
    ImageButton binance_delete;
    ImageButton bithumb_delete;
    ImageButton upbit_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_card_list);
        List<String> company_array = new ArrayList<>();
        company_array.add("binance");
        company_array.add("bithumb");
        company_array.add("upbit");
        binance_edit = (ImageButton) findViewById(R.id.binance_edit);
        bithumb_edit = (ImageButton) findViewById(R.id.bithumb_edit);
        upbit_edit = (ImageButton) findViewById(R.id.upbit_edit);
        binance_delete = (ImageButton) findViewById(R.id.binance_delete);
        bithumb_delete = (ImageButton) findViewById(R.id.bithumb_delete);
        upbit_delete = (ImageButton) findViewById(R.id.upbit_delete);



        for (int i = 0; i < company_array.size(); i++) {
            SharedPreferences pref = getSharedPreferences(company_array.get(i), MODE_PRIVATE);
            switch (company_array.get(i)){
                case "binance" : ((TextView) findViewById(R.id.binance_key)).setText(pref.getString("api_key", "비활성화"));
                        if(pref.getString("api_key", "비활성화").equals("비활성화")){
                            binance_delete.setEnabled(false);
                            findViewById(R.id.binance_link).setVisibility(View.INVISIBLE);
                        }else {
                            findViewById(R.id.binance_link).setVisibility(View.VISIBLE);
                        }
                break;
                case "bithumb" : ((TextView) findViewById(R.id.bithumb_key)).setText(pref.getString("api_key", "비활성화"));
                    if(pref.getString("api_key", "비활성화").equals("비활성화")){
                        bithumb_delete.setEnabled(false);
                        findViewById(R.id.bithumb_link).setVisibility(View.INVISIBLE);
                    }else {
                        findViewById(R.id.bithumb_link).setVisibility(View.VISIBLE);
                    }
                break;
                case "upbit" : ((TextView) findViewById(R.id.upbit_key)).setText(pref.getString("api_key", "비활성화"));
                    if(pref.getString("api_key", "비활성화").equals("비활성화")){
                        upbit_delete.setEnabled(false);
                        findViewById(R.id.upbit_link).setVisibility(View.INVISIBLE);
                    }else {
                        findViewById(R.id.upbit_link).setVisibility(View.VISIBLE);
                    }
                break;

            }
        }

        initComponent();
        initToolbar();
    }

    public void initComponent(){



        binance_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog("binance");
            }
        });
        bithumb_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog("bithumb");
            }
        });
        upbit_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog("upbit");
            }
        });
        binance_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                areYouSureToDialog("binance");
            }
        });
        bithumb_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                areYouSureToDialog("bithumb");
            }
        });
        upbit_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                areYouSureToDialog("upbit");
            }
        });


    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setNavigationIcon(R.drawable.ic_menu);

        //toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("수정하기를 눌러 API 연결");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.grey_10);
        Tools.setSystemBarLight(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_close, menu);
        Tools.changeMenuIconColor(menu, getResources().getColor(R.color.colorPrimary));
        return true;
    }

    private void displayDataResult(Event event) {
        switch (event.company) {
            case "binance" : ((TextView) findViewById(R.id.binance_key)).setText(event.api_key);

            break;
            case "bithumb" : ((TextView) findViewById(R.id.bithumb_key)).setText(event.api_key);

            break;
            case "upbit" : ((TextView) findViewById(R.id.upbit_key)).setText(event.api_key);

            break;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showCustomDialog(String company) {
        final Dialog apiSetDialog = new Dialog(this);
        apiSetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        apiSetDialog.setContentView(R.layout.dialog_event);
        apiSetDialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(apiSetDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;



// If the string contains data, then the paste operation is done

        final ImageButton paste_api_key = (ImageButton) apiSetDialog.findViewById(R.id.paste1);
        final ImageButton paste_secret_key = (ImageButton) apiSetDialog.findViewById(R.id.paste2);

        final TextView company_title = (TextView) apiSetDialog.findViewById(R.id.company_title);
        final EditText api_key = (EditText) apiSetDialog.findViewById(R.id.api_key);
        final EditText api_secret = (EditText) apiSetDialog.findViewById(R.id.api_secret);
        final AppCompatCheckBox use_buy_sell = (AppCompatCheckBox) apiSetDialog.findViewById(R.id.use_buy_sell);
        company_title.setText(company + "에 연결");
        SharedPreferences pref = getSharedPreferences(company, MODE_PRIVATE);
        api_key.setText(pref.getString("api_key", ""));
        api_secret.setText(pref.getString("api_secret", ""));

        paste_api_key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                String pasteData = "";
                ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
                pasteData = item.getText().toString();
                api_key.setText(pasteData);
            }
        });

        paste_secret_key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                String pasteData = "";
                ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
                pasteData = item.getText().toString();
                api_secret.setText(pasteData);
            }
        });


        ((ImageButton) apiSetDialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiSetDialog.dismiss();
            }
        });

        ((Button) apiSetDialog.findViewById(R.id.bt_save)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Event event = new Event();
                event.api_key = api_key.getText().toString();
                event.company = company;

                SharedPreferences pref = getSharedPreferences(company, MODE_PRIVATE);
                Editor editor = pref.edit();
                editor.putString("api_key", api_key.getText().toString());
                editor.putString("api_secret", api_secret.getText().toString());
                Log.d("api__key", api_key.getText().toString());
                editor.apply();
                displayDataResult(event);

                switch (company) {
                    case "binance" :
                        binance_delete.setEnabled(true);
                        findViewById(R.id.binance_link).setVisibility(View.VISIBLE);
                        break;
                    case "bithumb" :
                        bithumb_delete.setEnabled(true);
                        findViewById(R.id.bithumb_link).setVisibility(View.VISIBLE);
                        break;
                    case "upbit" :
                        upbit_delete.setEnabled(true);
                        findViewById(R.id.upbit_link).setVisibility(View.VISIBLE);
                        break;
                }

                apiSetDialog.dismiss();
            }
        });

        apiSetDialog.show();
        apiSetDialog.getWindow().setAttributes(lp);
    }

    public void deleteApi(String company){
        SharedPreferences pref = getSharedPreferences(company, MODE_PRIVATE);
        Editor editor = pref.edit();
        editor.clear();
        editor.apply();
    }
    private void areYouSureToDialog(String company) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_warning);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });



        ((AppCompatButton) dialog.findViewById(R.id.bt_delete)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Event event= new Event();
                event.api_key= "비활성화";
                switch (company){
                    case "binance": deleteApi("binance");
                        findViewById(R.id.binance_link).setVisibility(View.INVISIBLE);
                    event.company ="binance";
                        binance_delete.setEnabled(true);
                    displayDataResult(event);
                        break;
                    case "bithumb": deleteApi("bithumb");
                        findViewById(R.id.bithumb_link).setVisibility(View.INVISIBLE);
                        bithumb_delete.setEnabled(true);
                        event.company ="bithumb";
                        displayDataResult(event);
                        break;
                    case "upbit": deleteApi("upbit");
                        findViewById(R.id.upbit_link).setVisibility(View.INVISIBLE);
                        upbit_delete.setEnabled(true);
                        event.company ="upbit";
                        displayDataResult(event);
                        break;

                }
                Toast.makeText(getApplicationContext(), "Api가 삭제 되었습니다.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
}