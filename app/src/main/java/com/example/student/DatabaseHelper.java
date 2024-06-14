package com.example.student;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "student.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "students";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_FIRST_NAME = "first_name";
    private static final String COLUMN_AGE = "age";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_FIRST_NAME + " TEXT, " +
                COLUMN_AGE + " INTEGER)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long addStudent(String name, String firstName, int age) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_FIRST_NAME, firstName);
        values.put(COLUMN_AGE, age);
        long result = -1;

        try {
            result = db.insertOrThrow(TABLE_NAME, null, values);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return result;
    }

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;

        Cursor cursor = db.rawQuery(query, null);
        try {
            while (cursor.moveToNext()) {
                int idIndex = cursor.getColumnIndex(COLUMN_ID);
                int id = (idIndex != -1 && cursor.getInt(idIndex) >= 0) ? cursor.getInt(idIndex) : 0;

                int nameIndex = cursor.getColumnIndex(COLUMN_NAME);
                String name = (nameIndex != -1) ? cursor.getString(nameIndex) : "";

                int firstNameIndex = cursor.getColumnIndex(COLUMN_FIRST_NAME);
                String firstName = (firstNameIndex != -1) ? cursor.getString(firstNameIndex) : "";

                int ageIndex = cursor.getColumnIndex(COLUMN_AGE);
                int age = (ageIndex != -1 && cursor.getInt(ageIndex) >= 0) ? cursor.getInt(ageIndex) : 0;

                students.add(new Student(id, name, firstName, age));
            }
        } finally {
            cursor.close();
        }

        return students;
    }

    public void deleteStudent(int studentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(studentId)});
        db.close();
    }

    // Méthode pour mettre à jour un étudiant dans la base de données
    public int updateStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, student.getLastName());
        values.put(COLUMN_FIRST_NAME, student.getFirstName());
        values.put(COLUMN_AGE, student.getAge());

        // Mettez à jour la ligne dans la table en fonction de l'ID de l'étudiant
        return db.update(TABLE_NAME, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(student.getId())});
    }

}
