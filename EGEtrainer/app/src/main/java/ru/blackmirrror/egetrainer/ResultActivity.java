package ru.blackmirrror.egetrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import ru.blackmirrror.egetrainer.Models.DoneTasks;
import ru.blackmirrror.egetrainer.Models.Task;

public class ResultActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    public static final String DIAGRAM_PREFERENCES = "diagramDone";

    TextView out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        out = findViewById(R.id.textout);

        sharedPreferences = getSharedPreferences(DIAGRAM_PREFERENCES, Context.MODE_PRIVATE);

        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, SearchNewActivity.class);
                startActivity(intent);
            }
        });

        createTable();
    }

    private void createTable() {

        int[] done = {0,0};

        ArrayList<String> answers = new ArrayList<String>();
        ArrayList<String> answersYour = new ArrayList<String>();

        Bundle arguments = getIntent().getExtras();
        String subject = arguments.getString("sub");
        String task = arguments.getString("task");
        String num = arguments.getString("num");

        String strForPref = subject+task+num;

        int questionTotalCount = arguments.getInt("questionTotalCount");
        for (int i = 0; i < questionTotalCount; i++){
            answers.add(arguments.getString("a" + i));
            answersYour.add(arguments.getString("ay" + i));
        }

        int ROWS = questionTotalCount;
        int COLUMNS = 3;

        TableLayout tableLayout = (TableLayout) findViewById(R.id.table);

        for (int i = 0; i < ROWS+1; i++) {

            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));
            tableRow.setGravity(Gravity.CENTER);
            tableRow.setPadding(0,16,0,0);

            for (int j = 0; j < COLUMNS; j++) {
                TextView textView = new TextView(this);
                if (j == 0) {
                    if (i == 0) textView.setText("№");
                    else textView.setText("" + i);
                } else if (j == 1) {
                    if (i == 0) textView.setText("Ваш ответ");
                    else {
                        textView.setText(answersYour.get(i-1));
                        if (answers.get(i-1).equals(answersYour.get(i-1))) {
                            textView.setBackgroundResource(R.drawable.correct_answer);
                            done[0]++;
                        } else {
                            textView.setBackgroundResource(R.drawable.correct_no_answer);
                            done[1]++;
                        }
                    }
                }
                else {
                    if (i == 0) textView.setText("Верный ответ");
                    else textView.setText(answers.get(i-1));
                }
                textView.setTextColor(Color.BLACK);
                textView.setTextSize(16);
                textView.setGravity(Gravity.CENTER);
                textView.setPadding(16, 0, 16, 0);

                tableRow.addView(textView, j);
            }

            tableLayout.addView(tableRow, i);
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (int i = 0; i < 2; i++) {
            if(!sharedPreferences.contains(strForPref+i)) {
                editor.putString(strForPref+i, done[i]+"");
                editor.apply();
            }
            else {
                editor.remove(strForPref+i);
                editor.clear();
                editor.putString(strForPref+i, done[i]+"");
                editor.apply();
            }
        }
    }
}