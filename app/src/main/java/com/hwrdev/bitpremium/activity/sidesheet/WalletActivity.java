package com.planet.premium.activity.sidesheet;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.planet.premium.R;
import com.planet.premium.utils.Tools;

public class WalletActivity extends AppCompatActivity {
    private TabLayout tab_layout;
    private NestedScrollView nested_scroll_view;
    private ViewPager viewPager;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_wallet_green);
        context =getApplicationContext();

        initToolbar();
        initComponent();
    }

    private void initToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_menu));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
            }
        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);
        Tools.setSystemBarColor(this, R.color.green_400);

    }



    private void initComponent() {
        nested_scroll_view = (NestedScrollView) findViewById(R.id.nested_scroll_view);
        tab_layout = (TabLayout) findViewById(R.id.tab_layout);

        tab_layout.addTab(tab_layout.newTab().setIcon(R.drawable.binancelogo1), 0);
        tab_layout.addTab(tab_layout.newTab().setIcon(R.drawable.bithumb), 1);
        tab_layout.addTab(tab_layout.newTab().setIcon(R.drawable.upbit), 2);


        // set icon color pre-selected
        tab_layout.getTabAt(0).getIcon().setColorFilter(getResources().getColor(R.color.green_400), PorterDuff.Mode.SRC_IN);
        tab_layout.getTabAt(1).getIcon().setColorFilter(getResources().getColor(R.color.grey_20), PorterDuff.Mode.SRC_IN);
        tab_layout.getTabAt(2).getIcon().setColorFilter(getResources().getColor(R.color.grey_20), PorterDuff.Mode.SRC_IN);
        viewPager=(ViewPager)findViewById(R.id.view_pager);
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab_layout));
        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                tab.getIcon().setColorFilter(getResources().getColor(R.color.green_400), PorterDuff.Mode.SRC_IN);
                switch (tab.getPosition()) {
                    case 0: findViewById(R.id.wallet_background).setBackgroundColor(getResources().getColor(R.color.green_400));
                    break;
                    case 1: findViewById(R.id.wallet_background).setBackgroundColor(getResources().getColor(R.color.orange_400));

                    break;
                    case 2: findViewById(R.id.wallet_background).setBackgroundColor(getResources().getColor(R.color.blue_400));
                    break;
                }
                setSystembarColor(tab.getPosition());


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(getResources().getColor(R.color.grey_20), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
    public void setSystembarColor(int i){
        switch (i) {
            case 0: Tools.setSystemBarColor(this, R.color.green_400);
                break;
            case 1:  Tools.setSystemBarColor(this, R.color.orange_400);

                break;
            case 2:  Tools.setSystemBarColor(this, R.color.blue_400);
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_refresh, menu);
        return true;
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

}

