package ru.blackmirrror.egetrainer.ui.favorite;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;

import ru.blackmirrror.egetrainer.ChoiserActivity;
import ru.blackmirrror.egetrainer.Favourite2Activity;
import ru.blackmirrror.egetrainer.Models.DatabaseHelper;
import ru.blackmirrror.egetrainer.Models.Question;
import ru.blackmirrror.egetrainer.Models.QuizContract;
import ru.blackmirrror.egetrainer.Questions2Activity;
import ru.blackmirrror.egetrainer.R;
import ru.blackmirrror.egetrainer.databinding.FragmentFavoriteBinding;

public class FavoriteFragment extends Fragment {

    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    private FavoriteViewModel favoriteViewModel;
    private FragmentFavoriteBinding binding;

    TableLayout tableLayout;

    @SuppressLint("Range")
    public ArrayList<Question> getAllFavouriteQuestions() {
        ArrayList<Question> questionList = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        String[] selectionArgs = new String[]{uid};
        Cursor cursor = mDb.rawQuery("SELECT * FROM " + QuizContract.FavouriteTable.TABLE_NAME +
                " WHERE " + QuizContract.FavouriteTable.USER_UID + " = ?", selectionArgs);

        ArrayList<Integer> favourite_ids = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                int a = cursor.getColumnIndex(QuizContract.FavouriteTable.QUESTION_ID);
                favourite_ids.add(cursor.getInt(a));
            } while (cursor.moveToNext());
        }

        cursor.close();
        String where = "";
        String[] selectionArgsIds = new String[favourite_ids.size()];
        for(int i = 0; i < favourite_ids.size(); i++){
            if (i == 0) {
                where += QuizContract.QuestionTable._ID + " = ?";
            } else {
                where += " OR " + QuizContract.QuestionTable._ID + " = ?";
            }

            selectionArgsIds[i] = favourite_ids.get(i).toString();
        }
        if (!where.equals("")){
            cursor = mDb.rawQuery("SELECT * FROM " + QuizContract.QuestionTable.TABLE_NAME +
                    " WHERE " + where, selectionArgsIds);
        }
        if (cursor.moveToFirst()) {
            do {

                Question question = new Question();
                question.setTextQuestion(cursor.getString(cursor.getColumnIndex(QuizContract.QuestionTable.COLUMN_QUESTION)));
                question.setAnswer(cursor.getString(cursor.getColumnIndex(QuizContract.QuestionTable.ANSWER)));
                question.setSubject(cursor.getString(cursor.getColumnIndex(QuizContract.QuestionTable.SUBJECT)));
                question.setNumberQuestion(cursor.getInt(cursor.getColumnIndex(QuizContract.QuestionTable.NUMBER_QUESTION)));
                question.setNumberNumberQuestion(cursor.getInt(cursor.getColumnIndex(QuizContract.QuestionTable.NUMBER_NUMBER_QUESTION)));
                question.setQuestionId(cursor.getInt(cursor.getColumnIndex(QuizContract.QuestionTable._ID)));

                questionList.add(question);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return questionList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDBHelper = new DatabaseHelper(getActivity());

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
        favoriteViewModel =
                new ViewModelProvider(this).get(FavoriteViewModel.class);
        binding = FragmentFavoriteBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        tableLayout = (TableLayout) root.findViewById(R.id.choiseTable);
        showLayout();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void showLayout() {
        ArrayList<Question> questions = getAllFavouriteQuestions();
        tableLayout.removeAllViews();
        int COLUMNS = 1;

        for (int i = 0; i < questions.size(); i++) {
            TableRow tableRow = new TableRow(getActivity());
            tableRow.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));
            TextView textView = new TextView(getActivity());
            textView.setText("Задание " + (i + 1));
            tableRow.addView(textView, 0);
            int finalI = i;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), Favourite2Activity.class);
                    intent.putExtra("question_id", questions.get(finalI).getQuestionId());
                    startActivity(intent);
                }
            });
            tableLayout.addView(tableRow, i);
        }
    }
}