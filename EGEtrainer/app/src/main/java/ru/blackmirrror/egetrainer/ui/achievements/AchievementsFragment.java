package ru.blackmirrror.egetrainer.ui.achievements;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.io.IOException;

import ru.blackmirrror.egetrainer.R;
import ru.blackmirrror.egetrainer.Models.DatabaseHelper;
import ru.blackmirrror.egetrainer.Models.QuizContract;
import ru.blackmirrror.egetrainer.databinding.FragmentAccountBinding;
import ru.blackmirrror.egetrainer.databinding.FragmentAchievementsBinding;
import ru.blackmirrror.egetrainer.ui.account.AccountViewModel;

public class AchievementsFragment extends Fragment {

    private AchievementsViewModel achievementsViewModel;
    private FragmentAchievementsBinding binding;

    private TableLayout table;

    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;

    @SuppressLint("Range")
    public void set_statistics(){
        Cursor cursor;
        cursor = mDb.rawQuery("SELECT * FROM " + QuizContract.TaskSuccess.TABLE_NAME, null);

        TableRow columns = new TableRow(getContext());

        TextView nameTextView = new TextView(getActivity());
        nameTextView.setText("Название");
        TextView allTextView = new TextView(getActivity());
        allTextView.setText("Всего");
        TextView successTextView = new TextView(getActivity());
        successTextView.setText("Успешных");
        TextView unsuccessTextView = new TextView(getActivity());
        unsuccessTextView.setText("Неуспешных");

        columns.addView(nameTextView, 0);
        columns.addView(allTextView, 1);
        columns.addView(successTextView, 2);
        columns.addView(unsuccessTextView, 3);

        for (int i = 0; i < 4; i++) {
            TextView temp = (TextView) columns.getVirtualChildAt(i);
            temp.setTextColor(Color.BLACK);
            temp.setTextSize(16);
        }

        table.addView(columns);
        if (cursor.moveToFirst()){
            do {
                int a = cursor.getInt(cursor.getColumnIndex(QuizContract.TaskSuccess.SUCCESS));
                int b = cursor.getInt(cursor.getColumnIndex(QuizContract.TaskSuccess.UNSUCCESS));
                String s = cursor.getString(cursor.getColumnIndex(QuizContract.TaskSuccess.SUBJECT));
                TableRow tableRow = new TableRow(getActivity());
                tableRow.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT));
                TextView t1 = new TextView(getContext());
                t1.setText(s);
                TextView t2 = new TextView(getContext());
                t2.setText(String.valueOf(a+b));
                TextView t3 = new TextView(getContext());
                t3.setText(String.valueOf(a));
                TextView t4 = new TextView(getContext());
                t4.setText(String.valueOf(b));
                tableRow.addView(t1, 0);
                tableRow.addView(t2, 1);
                tableRow.addView(t3, 2);
                tableRow.addView(t4, 3);
                table.addView(tableRow);
            } while(cursor.moveToNext());
        }
        cursor.close();
    }

    public void fetchDb(){
        mDBHelper = new DatabaseHelper(getContext());

        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        achievementsViewModel = new ViewModelProvider(this).get(AchievementsViewModel.class);
        binding = FragmentAchievementsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        table = root.findViewById(R.id.choiseTable);

        fetchDb();
        set_statistics();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}