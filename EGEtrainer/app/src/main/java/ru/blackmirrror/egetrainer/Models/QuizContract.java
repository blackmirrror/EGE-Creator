package ru.blackmirrror.egetrainer.Models;

import android.provider.BaseColumns;

public final class QuizContract {

    public QuizContract(){}

    public static class QuestionTable implements BaseColumns {

        /*public static final String TABLE_NAME = "quiz_questions";
        public static final String COLUMN_QUESTION = "question";
        public static final String ANSWER = "answer";
        public static final String SUBJECT = "subject";
        public static final String NUMBER_QUESTION = "number_question";
        public static final String NUMBER_NUMBER_QUESTION = "number_number_question";*/

        public static final String TABLE_NAME = "quections";
        public static final String COLUMN_QUESTION = "question";
        public static final String ANSWER = "answer";
        public static final String SUBJECT = "subject";
        public static final String NUMBER_QUESTION = "num_question";
        public static final String NUMBER_NUMBER_QUESTION = "num_num_question";
        public static final String _ID = "_id";
    }
    public static class FavouriteTable implements BaseColumns {
        public static final String TABLE_NAME = "favourite_tasks";
        public static final String COLUMN_ID = "ID";
        public static final String USER_UID = "USERUID";
        public static final String QUESTION_ID = "QUESTION_ID";
        public static final String QUESTION_SUBJ = "QUESTION_SUBJ";
    }
    public static class RemTable implements BaseColumns {
        public static final String TABLE_NAME = "tasks";
        public static final String _ID = "_id";
        public static final String SUBJECT = "subject";
        public static final String TASK = "task";
        public static final String NUMBER = "number";
        //public static final String UID = "uid";
    }
    public static class TaskSuccess implements BaseColumns {
        public static final String TABLE_NAME = "task_success";
        public static final String _ID = "_id";
        public static final String SUBJECT = "subject";
        public static final String SUCCESS = "success";
        public static final String UNSUCCESS = "unsuccess";
        public static final String DATE = "date";
    }

}
