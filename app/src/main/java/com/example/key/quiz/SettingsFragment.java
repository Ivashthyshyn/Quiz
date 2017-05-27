package com.example.key.quiz;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;

import java.util.HashSet;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;
import static com.example.key.quiz.InitialActivity.PREFS_NAME;

/**
 * Created by Key on 26.05.2017.
 */

public class SettingsFragment extends PreferenceFragment    {

    final private String USER_NAME_LIST = "user_name_list";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.menu_ithem);

        PreferenceCategory userName = (PreferenceCategory) findPreference("users_name");
        PreferenceCategory newUser = (PreferenceCategory) findPreference("new_user");

        final ListPreference listPref = setListPreferenceData((ListPreference) findPreference(USER_NAME_LIST), getActivity());



        listPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                setListPreferenceData(listPref, getActivity());
                SharedPreferences dsds = getActivity().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                dsds.edit().putString("userName", (String) newValue).apply();
                return false;
            }
        });
        setHasOptionsMenu(true);
        userName.addPreference(listPref);

    }

    protected ListPreference setListPreferenceData(ListPreference lp, Activity mActivity) {
        SharedPreferences dsds = getActivity().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Set<String> list =  dsds.getStringSet("userName1",new HashSet<String>());
        CharSequence[] charSequences = list.toArray(new CharSequence[list.size()]);
        if (lp == null)
            lp = new ListPreference(mActivity);
        lp.setEntries(charSequences);
        lp.setDefaultValue(charSequences[0]);
        lp.setEntryValues(charSequences);
        lp.setTitle(lp.getDependency());
        lp.setSummary(lp.getEntry());
        lp.setDialogTitle("Виберіть користувача");
        lp.setKey(USER_NAME_LIST);
        return lp;
    }

}