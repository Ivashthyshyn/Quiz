package com.example.key.quiz;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.androidannotations.annotations.EFragment;

@EFragment
public class DialogFragment extends Fragment {
String assistantText;

    TextView assistantTalk;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_dialog, container, false);
        assistantTalk = (TextView)fragment.findViewById(R.id.assistantDialogText);
        assistantTalk.setText(assistantText);
        return fragment;
    }
public void setAssistantTalk(String text){
    this.assistantText = text;

}
}
