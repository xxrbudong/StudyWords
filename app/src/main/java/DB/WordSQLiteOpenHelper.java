package DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WordSQLiteOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "wordDatabase.db";
    public static final String WORD_TABLE_NAME = "word";
    //public static final String WRONG_WORD_TABLE_NAME = "wrong_word";
    public WordSQLiteOpenHelper(Context context) {
        super(context,DB_NAME,null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "Create table " + WORD_TABLE_NAME + " (_id integer primary key AUTOINCREMENT, "
                + "word text, " + "accent text, " + "mean_cn text, " + "sentence text, " + "sentence_trans text, " + "collection integer default 0, " + "study integer default 0, " + "review integer default 0)";
        db.execSQL(sql);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
