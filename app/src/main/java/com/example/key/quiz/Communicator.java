package com.example.key.quiz;

/**
 * Created by Key on 02.05.2017.
 * Sends a reply to the user from a SelectorFragment to TrialActivity
 */

public interface Communicator {
    public void processingUserAnswer(String data);
}
