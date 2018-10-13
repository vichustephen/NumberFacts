package com.seven.zion.numberfacts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

/**
  Created by Stephen on 17-Oct-17.
 */

public class settingsActivity extends AppCompatActivity{
    private static final String TAG_THEME = "THEME_PREF";
    private static final String THEME_PREF = "THEME_COLOR";
    String defaultTheme = "Red";
   static Handler themeChooser;

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(TAG_THEME,0);
        String preference = pref.getString(THEME_PREF,null);
        try {
            if (preference.equals("Red")) {
                setTheme(R.style.SettingsTheme);
                defaultTheme = "Red";
            }
            else if (preference.equals("Blue")) {
                setTheme(R.style.SettingsThemeBlue);
                defaultTheme ="Blue";
            }
            else if (preference.equals("Green")) {
                setTheme(R.style.SettingsThemeGreen);
                defaultTheme = "Green";
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getFragmentManager().beginTransaction().replace(android.R.id.content,new MainPrefFragment()).commit();
        themeChooser = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                String message = msg.getData().getString("msg");

                    SharedPreferences pref = getApplicationContext().getSharedPreferences(TAG_THEME,0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString(THEME_PREF,message);
                    editor.apply();
                try {
                    if (!defaultTheme.equals(message)) {
                        Intent i = new Intent(settingsActivity.this, MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        settingsActivity.this.finish();
                    }
                    if (message.equals("POP_HEAD"))
                        startService(new Intent(settingsActivity.this, PopHeadService.class));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                return false;
            }
        });

    }
    public static class MainPrefFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
           bindPrefrenceSummaryValue(findPreference(getString(R.string.key_theme_color)));
            SwitchPreference switchPreference = (SwitchPreference)findPreference(getString(R.string.pop_head_key));
            switchPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {

                        Log.i("Pref","Changed");
                        if ((boolean)newValue)
                        {
                            Bundle bundle = new Bundle();
                            bundle.putString("msg","POP_HEAD");
                            Message message = new Message();
                            message.setData(bundle);
                            themeChooser.sendMessage(message);
                        }
                    return true;
                }
            });
        }
    }
    public static void bindPrefrenceSummaryValue(Preference preference)
    {
        preference.setOnPreferenceChangeListener(preferenceChangeListener);
        preferenceChangeListener.onPreferenceChange(preference, PreferenceManager.getDefaultSharedPreferences(preference.getContext()
        ).getString(preference.getKey(),""));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.home) {
            Toast.makeText(this,"Hi",Toast.LENGTH_LONG).show();
            onBackPressed();
            settingsActivity.this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static Preference.OnPreferenceChangeListener preferenceChangeListener = new
            Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    String value = newValue.toString();
                    if (preference instanceof ListPreference) {
                        ListPreference listPreference = (ListPreference) preference;
                        int index = listPreference.findIndexOfValue(value);
                        preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);
                            change(listPreference.getEntries()[index].toString());
                    }
                    return true;
                }
            };

            public static void change(String theme)
            {
                Bundle bundle = new Bundle();
                bundle.putString("msg",theme);
                Message message = new Message();
                message.setData(bundle);
                themeChooser.sendMessage(message);
            }
}
