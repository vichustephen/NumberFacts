package com.seven.zion.numberfacts;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private AppBarLayout appbar;
    ViewPagerAdapter viewPagerAdapter;
    private static final String TAG_THEME = "THEME_PREF";
    private static final String THEME_PREF = "THEME_COLOR";
    private static final int DRAW_OVER_OTHER_APPS = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(TAG_THEME,0);
        String preference = pref.getString(THEME_PREF,null);
        try {
            if (preference.equals("Red"))
                setTheme(R.style.MyMaterialThemeRed);
            else if (preference.equals("Blue"))
                setTheme(R.style.MyMaterialThemeBlue);
            else if (preference.equals("Green"))
                setTheme(R.style.MyMaterialThemeGreen);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      //  (CollapsingToolbarLayout)findViewById(R.id.collapselayout)).setTitle("NumberFacts#");
        appbar = (AppBarLayout)findViewById(R.id.appBar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        AppBarLayout.LayoutParams p = (AppBarLayout.LayoutParams)toolbar.getLayoutParams();
        p.setScrollFlags(0);
        toolbar.setLayoutParams(p);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this))
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Permission Required").setMessage("Please grant the overlay permission for the pop head to work")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:"+getPackageName()));
                            startActivityForResult(intent,DRAW_OVER_OTHER_APPS);
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(MainActivity.this,"Permission Needed for Pop head to function",Toast.LENGTH_LONG).show();
                }
            }).create().show();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.settings) {
            startActivity(new Intent(MainActivity.this, settingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.imenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DRAW_OVER_OTHER_APPS)
        {
            if (resultCode == RESULT_OK)
                Toast.makeText(this,"Permission Granted",Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this,"Permission Needed for Pop head to function",Toast.LENGTH_LONG).show();
        }
        else
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.getBoolean("pop_head_key",false))
            startService(new Intent(MainActivity.this,PopHeadService.class));
    }

    private void setupViewPager(ViewPager viewPager)
    {
        viewPagerAdapter.addFragment(new RandomFacts(),"random");
        viewPagerAdapter.addFragment(new MathFragment(),"math");
        viewPagerAdapter.addFragment(new DateFragment(),"date");
        viewPagerAdapter.addFragment(new YearFragment(),"year");
        viewPagerAdapter.addFragment(new TriviaFragment(),"trivia");
        viewPager.setAdapter(viewPagerAdapter);

    }
    class ViewPagerAdapter extends FragmentPagerAdapter
    {
        List<Fragment> mFragmentList = new ArrayList<>();
        List<String> titleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fragment)
        {
            super(fragment);
        }
        public void addFragment(Fragment fragment,String title)
        {
            mFragmentList.add(fragment);
            titleList.add(title);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
    }
        /*    <android.support.v7.widget.Toolbar
    android:layout_width="match_parent"
    android:id="@+id/toolbar"
    android:layout_height="?attr/actionBarSize"
    android:background="?attr/colorPrimary"
    app:layout_scrollFlags="scroll|enterAlways"
    app:popupTheme="@style/ThemeOverlay.AppCompat.Light"/>

         <android.support.design.widget.FloatingActionButton
    android:id="@+id/fab"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_gravity="end|top"
    android:layout_marginTop="50dp"
    android:layout_marginLeft="25dp"
    android:layout_marginStart="25dp"
    android:clickable="true"
    app:backgroundTint="?android:attr/colorFocusedHighlight"
    app:fabSize="normal"
    app:pressedTranslationZ="12dp"
    app:srcCompat="@android:drawable/ic_menu_preferences" />

     <android.support.design.widget.CollapsingToolbarLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:id="@+id/collapselayout"
            app:layout_scrollFlags="scroll|exitUntilCollapsed">
*/
}
