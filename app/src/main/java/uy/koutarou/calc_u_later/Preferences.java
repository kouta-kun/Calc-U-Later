package uy.koutarou.calc_u_later;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

public class Preferences extends AppCompatActivity {
    SharedPreferences preferences;
    String preferenceID;
    boolean updating = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        setContentView(R.layout.activity_preferences);
        Button okButton = findViewById(R.id.okButton);
        okButton.setOnClickListener((_l) -> {
            updating = true;
            preferenceID = "ok_key";
            showPreferenceKey();
        });
        Button deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener((_l) -> {
            updating = true;
            preferenceID = "delete_key";
            showPreferenceKey();
        });
    }

    private void setPreferenceKey(KeyEvent event) {
        do
            if (preferences.edit().putString(preferenceID, KeyEvent.keyCodeToString(event.getKeyCode())).commit())
                break;
        while (true);
    }

    private void showPreferenceKey() {
        TextView debugKey = findViewById(R.id.debugKey);
        String confKey = preferences.getString(preferenceID, getDefault(preferenceID));
        debugKey.setText(
                String.format(
                        "%s is configured as: %s.%s",
                        preferenceID, confKey, updating ? " Press a button to update it!" : ""
                )
        );
    }

    private String getDefault(String preferenceID) {
        switch (preferenceID) {
            case "ok_key":
                return "KEYCODE_DPAD_CENTER";
            case "delete_key":
                return "KEYCODE_BACK";
        }
        return null;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        TextView debugKey = findViewById(R.id.debugKey);
        if (preferenceID == null) {
            debugKey.setText(KeyEvent.keyCodeToString(event.getKeyCode()) + " = " + event.getAction());
        } else if (updating) {
            setPreferenceKey(event);
            updating = false;
            showPreferenceKey();
        }
        return super.dispatchKeyEvent(event);
    }
}