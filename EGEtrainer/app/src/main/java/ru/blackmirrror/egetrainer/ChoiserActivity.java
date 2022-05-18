package ru.blackmirrror.egetrainer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.api.Distribution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.blackmirrror.egetrainer.Models.DoneTasks;
import ru.blackmirrror.egetrainer.Models.RemDbHelper;
import ru.blackmirrror.egetrainer.Models.Task;

public class ChoiserActivity extends AppCompatActivity {

    TextView variants, tasks, before;
    TableLayout tableLayout;

    String subject;
    int questionTotalCount;
    String[] subjects = {"mathBase", "mathProf", "rus", "eng", "inf", "phis", "soc", "his", "bio", "chem", "geog", "leat"};
    int[] questionTotalCounts = {21, 18, 27, 44, 27, 30, 25, 19, 28, 34, 31, 12};

    RemDbHelper remDbHelper;
    public static final String APP_PREFERENCES = "doneTasks";
    public static final String DIAGRAM_PREFERENCES = "diagramDone";
    SharedPreferences mSettings, sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choiser);

        variants = findViewById(R.id.textVariants);
        tasks = findViewById(R.id.textTasks);
        before = findViewById(R.id.textPrev);
        tableLayout = (TableLayout) findViewById(R.id.choiseTable);

        Bundle arguments = getIntent().getExtras();
        subject = arguments.getString("sub");
        questionTotalCount =questionTotalCounts[Arrays.asList(subjects).indexOf(subject)];

        remDbHelper = new RemDbHelper(this);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences = getSharedPreferences(DIAGRAM_PREFERENCES, Context.MODE_PRIVATE);

        variants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                variants.setBackgroundResource(R.drawable.botton_task_clicked);
                tasks.setBackgroundResource(R.drawable.botton_task_unclicked);
                showLayout("variant");
            }
        });

        tasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                variants.setBackgroundResource(R.drawable.botton_task_unclicked);
                tasks.setBackgroundResource(R.drawable.botton_task_clicked);
                showLayout("task");
            }
        });

        before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChoiserActivity.this, SearchNewActivity.class);
                startActivity(intent);
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showLayout(String choice) {

        tableLayout.removeAllViews();

        int ROWS;
        if (choice == "variant")
            ROWS = 5;
        else
            ROWS = questionTotalCount;
        int COLUMNS = 2;
        int temp = ROWS;

        while (temp > 0) {

            for (int i = 0; i < ROWS/2+ROWS%2; i++) {

                TableRow tableRow = new TableRow(this);
                tableRow.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT));
                tableRow.setGravity(Gravity.CENTER);
                tableRow.setPadding(0,16,0,0);

                for (int j = 0; j < COLUMNS; j++) {
                    if (temp > 0) {

                        LinearLayout linear = new LinearLayout(this);
                        linear.setOrientation(LinearLayout.VERTICAL);
                        linear.setGravity(Gravity.CENTER);

                        PieChart pieChart = new PieChart(this);
                        pieChart.setForegroundGravity(Gravity.CENTER);

                        LinearLayout linearLayout = new LinearLayout(this);
                        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                        linearLayout.setGravity(Gravity.CENTER);
                        //linear.addView(pieChart);
                        //linear.addView(linearLayout);

                        ImageView imageView = new ImageView(this);
                        imageView.setImageResource(R.drawable.ic_baseline_done_all_24);

                        TextView textView = new TextView(this);
                        if (choice == "variant")
                            textView.setText("Вариант "+(ROWS-temp+1));
                        else
                            textView.setText("Задание "+(ROWS-temp+1));

                        //if (done(subject, choice, textView.getText().toString().substring(8)))
                        if (done2(subject, choice, textView.getText().toString().substring(8)))
                            imageView.setBackgroundResource(R.drawable.task_no_done);
                        else
                            imageView.setBackgroundResource(R.drawable.task_done);

                        textView.setTextColor(Color.BLACK);
                        textView.setTextSize(20);
                        textView.setGravity(Gravity.CENTER);
                        textView.setPadding(32, 0, 32, 0);

                        //linearLayout.addView(imageView);
                        //linearLayout.addView(textView);

                        /*ArrayList<PieEntry> pieEntries = new ArrayList();

                        int done, nodone;

                        if (sharedPreferences.contains(subject + choice + textView.getText().toString().substring(8)+0))
                        {
                            done = sharedPreferences.getInt( subject + choice +
                                    textView.getText().toString().substring(8) + "0", 0);
                            nodone = sharedPreferences.getInt(subject + choice +
                                    textView.getText().toString().substring(8) + "1", 0);
                        }
                        else{
                            done = 0;
                            nodone = 10;
                        }

                        pieEntries.add(new PieEntry(done, "Верно"));
                        pieEntries.add(new PieEntry(nodone, "Неверно"));

                        PieDataSet pieDataSet = new PieDataSet(pieEntries, "Решение");
                        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                        pieDataSet.setValueTextColor(Color.BLACK);
                        pieDataSet.setFormSize(128f);
                        pieDataSet.setValueTextSize(16f);

                        PieData pieData = new PieData(pieDataSet);

                        pieChart.setData(pieData);
                        pieChart.getDescription().setEnabled(false);
                        //pieChart.setCenterText("Решение");
                        pieChart.animate();*/

                        //linear.addView(pieChart);
                        linearLayout.addView(imageView);
                        linearLayout.addView(textView);
                        linear.addView(linearLayout);

                        //tableRow.addView(textView, j);
                        tableRow.addView(linear, j);

                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ChoiserActivity.this, Questions2Activity.class);
                                intent.putExtra("sub", subject);
                                intent.putExtra("choice", choice);
                                intent.putExtra("number", textView.getText().toString());

                                String num = textView.getText().toString().substring(8);
                                Task task = new Task(subject, choice, num);
                                String strForPref = subject + choice + num;
                                DoneTasks.done.add(task);

                                SharedPreferences.Editor editor = mSettings.edit();
                                if(!mSettings.contains(strForPref)) {
                                    editor.putString(strForPref, strForPref);
                                    editor.apply();
                                }
                                //remDbHelper.addTask(task);

                                startActivity(intent);
                            }
                        });
                    }
                    temp--;
                }

                tableLayout.addView(tableRow, i);
            }
        }
    }

    private boolean done2(String subject, String choice, String substring) {
        String key = subject + choice + substring;
        if(mSettings.contains(key))
            return false;
        return true;
    }

    private boolean done(String sub, String cho, String num) {
        ArrayList<Task> tasks = remDbHelper.getAllTasks(sub, cho, num);
        if (tasks.isEmpty())
            return true;
        return false;
    }
}