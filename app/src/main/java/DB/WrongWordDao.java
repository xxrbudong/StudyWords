package DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import bean.WordBean;

/*public class WrongWordDao {
    private WordSQLiteOpenHelper wordSQLiteOpenHelper;
    private SQLiteDatabase database;
    public WrongWordDao(Context context) {
        wordSQLiteOpenHelper = new WordSQLiteOpenHelper(context);
    }
    public void insertWrongWord(WordBean word, int id) {
        database = wordSQLiteOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("number",id);
        values.put("word",word.getWord());
        values.put("accent",word.getAccent());
        values.put("mean_cn",word.getMean_cn());
        values.put("sentence",word.getSentence());
        values.put("sentence_trans",word.getSentence_trans());
        database.insert(WordSQLiteOpenHelper.WRONG_WORD_TABLE_NAME,null,values);
        database.close();
    }
    public Cursor getCursorWrongWord(int startId, int number,String[] columns) {
        database = wordSQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = database.query(WordSQLiteOpenHelper.WRONG_WORD_TABLE_NAME,columns,"_id>=? and _id<?",new String[]{String.valueOf(startId),String.valueOf(startId+number)},null,null,null);
        //database.close();
        return cursor;
    }
    public void close() {
        database.close();
    }
}*/
