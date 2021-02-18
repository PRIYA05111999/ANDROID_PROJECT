package com.google.firebase.myapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.google.firebase.myapp.quizContract.*;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.dom.DOMLocator;

public class QuizDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MyQuiz.db";
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase db;
    public QuizDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                QuestionsTable.TABLE_NAME + " ( " +
                QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionsTable.COLUMN_QUESTION + " TEXT, " +
                QuestionsTable.COLUMN_OPTION1 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION2 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION3 + " TEXT, " +
                QuestionsTable.COLUMN_ANSWER_NR + " INTEGER" +
                ")";
        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        fillQuestionsTable();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + QuestionsTable.TABLE_NAME);
        onCreate(db);
    }
    private void fillQuestionsTable() {
        question q1 = new question("A is correct", "A", "B", "C", 1);
        addQuestion(q1);
        question q2 = new question("B is correct", "A", "B", "C", 2);
        addQuestion(q2);
        question q3 = new question("C is correct", "A", "B", "C", 3);
        addQuestion(q3);
        question q4 = new question("A is correct again", "A", "B", "C", 1);
        addQuestion(q4);
        question q5 = new question("B is correct again", "A", "B", "C", 2);
        addQuestion(q5);
    }
    private void addQuestion(question ques) {
        ContentValues cv = new ContentValues();
        cv.put(QuestionsTable.COLUMN_QUESTION, ques.getQuestion());
        cv.put(QuestionsTable.COLUMN_OPTION1, ques.getOption1());
        cv.put(QuestionsTable.COLUMN_OPTION2, ques.getOption2());
        cv.put(QuestionsTable.COLUMN_OPTION3, ques.getOption3());
        cv.put(QuestionsTable.COLUMN_ANSWER_NR, ques.getAnswerNr());
        db.insert(QuestionsTable.TABLE_NAME, null, cv);
    }
    public List<question> getAllQuestions() {
        List<question> questionList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + QuestionsTable.TABLE_NAME, null);
        if (c.moveToFirst()) {
            do {
                question ques = new question();
                ques.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                ques.setOption1(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
                ques.setOption2(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
                ques.setOption3(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
                ques.setAnswerNr(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NR)));
                questionList.add(ques);
            } while (c.moveToNext());
        }
        c.close();
        return questionList;
    }
}