package uy.koutarou.calc_u_later;

import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.*;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

public class MainActivity extends AppCompatActivity {

    List<Character> specialChars = Arrays.asList('+', '-', '*', '/', '.');

    ExpressionProcessor expressionProcessor = new ExpressionProcessor();
    private EditText textInput;
    private ListView historyList;
    private final List<String> history = new ArrayList<>();
    private HistoryAdapter adapter;
    private int historyIndex = 0;
    private final KeyListener keyListener = new KeyListener() {
        @Override
        public int getInputType() {
            return InputType.TYPE_NULL;
        }

        @Override
        public boolean onKeyDown(View view, Editable text, int keyCode, KeyEvent event) {
            return true;
        }

        @Override
        public boolean onKeyUp(View view, Editable text, int keyCode, KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_UP) {
                Editable textEditable = textInput.getText();
                if (event.getKeyCode() == KeyEvent.KEYCODE_STAR) {
                    if (textEditable.length() > 0 && specialChars.contains(textEditable.charAt(textEditable.length() - 1))) {
                        char newChar = specialChars.get((specialChars.indexOf(textEditable.charAt(textEditable.length() - 1)) + 1) % specialChars.size());
                        textEditable.replace(textEditable.length() - 1, textEditable.length(), "" + newChar);
                    } else {
                        textEditable.append(specialChars.get(0));
                    }
                } else if (event.getKeyCode() >= KeyEvent.KEYCODE_0 && event.getKeyCode() <= KeyEvent.KEYCODE_9) {
                    if (textEditable.toString().equals("3RR0R")) {
                        textEditable.clear();
                    }
                    textEditable.append((char) ('0' + (event.getKeyCode() - KeyEvent.KEYCODE_0)));
                } else if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && textEditable.length() > 0) {
                    if (textEditable.toString().equals("3RR0R")) {
                        textEditable.clear();
                    } else
                        textEditable.delete(textEditable.length() - 1, textEditable.length());
                } else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP && !history.isEmpty()) {
                    if (historyIndex > 0) {
                        historyIndex--;
                    } else {
                        historyIndex = 0;
                    }
                    textInput.setText(history.get(historyIndex).split("=")[0].trim());
                } else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN && !history.isEmpty()) {
                    if (historyIndex < history.size() - 1) {
                        historyIndex++;
                    } else {
                        historyIndex = history.size() - 1;
                    }
                    textInput.setText(history.get(historyIndex).split("=")[0].trim());
                }
            }
            return true;
        }

        @Override
        public boolean onKeyOther(View view, Editable text, KeyEvent event) {
            return true;
        }

        @Override
        public void clearMetaKeyState(View view, Editable content, int states) {

        }
    };

    private class HistoryAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return history.size();
        }

        @Override
        public String getItem(int position) {
            return history.get(position);
        }

        @Override
        public long getItemId(int position) {
            return history.get(position).hashCode();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item, parent, false);
            }

            TextView textView = convertView.findViewById(R.id.listtext);
            textView.setText(getItem(position));
            textView.findViewById(R.id.listtext).setOnClickListener(view -> {
                textInput.setText(HistoryAdapter.this.getItem(position).split("=")[0].trim());
                view.setPressed(true);
                view.setPressed(false);
            });
            return convertView;
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER && event.getAction() == KeyEvent.ACTION_UP) {
            Editable textEditable = textInput.getText();
            String expression = textEditable.toString();
            try {
                float number = expressionProcessor.execute(expression);
                DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
                df.setMaximumFractionDigits(10);
                String numberFormatted = df.format(number);
                textInput.setText(numberFormatted);
                history.add(expression + " = " + numberFormatted);
                historyIndex = history.size()-1;
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                textInput.setText("3RR0R");
            }
        } else if (event.getKeyCode() == KeyEvent.KEYCODE_MENU && event.getAction() == KeyEvent.ACTION_UP) {
            showAbout();
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        historyList = findViewById(R.id.historyList);
        adapter = new HistoryAdapter();
        historyList.setAdapter(adapter);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        textInput = findViewById(R.id.calculationInput);
        textInput.setKeyListener(keyListener);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_about) {
            showAbout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAbout() {
        Intent aboutIntent = new Intent(this, About.class);
        this.startActivity(aboutIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbarmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}