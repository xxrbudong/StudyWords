package DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import bean.DBWordBean;
import bean.WordBean;

public class WordDao {
    private WordSQLiteOpenHelper wordSQLiteOpenHelper;
    private SQLiteDatabase database;
    public static final int STUDY = 1;
    public static final int REVIEW = 2;
    public static final int COLLECTION = 3;
    public static final int SEARCH = 4;
    public WordDao(Context context) {
        wordSQLiteOpenHelper = new WordSQLiteOpenHelper(context);
    }
    /**
     * 插入数据
    * @param word WordBean
    * */
    public void insertWord(WordBean word) {
        database = wordSQLiteOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("word",word.getWord());
        values.put("accent",word.getAccent());
        values.put("mean_cn",word.getMean_cn());
        values.put("sentence",word.getSentence());
        values.put("sentence_trans",word.getSentence_trans());
        database.insert(WordSQLiteOpenHelper.WORD_TABLE_NAME,null,values);
        database.close();
    }
    /**
     * 获得数据
     * @param columns 获得哪些列
     * @param number 结尾id
     * @param startId 开始id
     * */
    public Cursor getCursorWord(int startId, int number, String[] columns) {
        database = wordSQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = database.query(WordSQLiteOpenHelper.WORD_TABLE_NAME,columns,"_id>=? and _id<?",new String[]{String.valueOf(startId),String.valueOf(startId+number)},null,null,null);
        return cursor;
    }

    /**
     *更新单词状态
     * @param word id
     * @param value 是否被收藏
     * @param flag STUDY or REVIEW or COLLECTION
     * */
    public void updateStatus(int value, String word, int flag) {
        database = wordSQLiteOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        switch (flag) {
            case STUDY:
                values.put("study", value);
                break;
            case REVIEW:
                values.put("review",value);
                break;
            case COLLECTION:
                values.put("collection",value);
                break;
        }
        int i =  database.update(WordSQLiteOpenHelper.WORD_TABLE_NAME,values,"word=?",new String[]{word});
        Log.i("EEEEEEE",String.valueOf(i));
        database.close();
    }

    /**
     * 获得收藏单词
     * @param columns 选择列
     * */
    public Cursor getCursorWord(String[] columns,int flag) {
        database = wordSQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = null;
        switch (flag) {
            case COLLECTION:
                cursor = database.query(WordSQLiteOpenHelper.WORD_TABLE_NAME,columns,"collection=?",new String[]{"1"},null,null,null);
                break;
            case STUDY:
                cursor = database.query(WordSQLiteOpenHelper.WORD_TABLE_NAME,columns,null,null,null,null,null);

        }
        return cursor;
    }

    /**
     * @param flag
     * */
    public List<DBWordBean> getListWord(int number,int flag) {
        database = wordSQLiteOpenHelper.getReadableDatabase();
        List<DBWordBean> list = new ArrayList<>();
        String[] form = new String[]{"_id","word","accent","mean_cn","sentence","sentence_trans","collection"};
        Cursor cursor;
        switch (flag)  {
            case 0://collection
                cursor = database.query(WordSQLiteOpenHelper.WORD_TABLE_NAME,form,"collection=?",new String[]{"1"},null,null,null);
                break;
            case 1://study
                cursor = database.query(WordSQLiteOpenHelper.WORD_TABLE_NAME,form,"study=?",new String[]{"0"},null,null,null,String.valueOf(number));
                break;
            case 2://review
                cursor = database.query(WordSQLiteOpenHelper.WORD_TABLE_NAME,form,"study=? and review=?",new String[]{"1","0"},null,null,null,String.valueOf(number));
                break;
            default:
                cursor = null;
                break;
        }
        while(cursor.moveToNext()) {
            DBWordBean dbWordBean = new DBWordBean();
            dbWordBean.set_id(cursor.getInt(0));
            dbWordBean.setWord(cursor.getString(1));
            dbWordBean.setAccent(cursor.getString(2));
            dbWordBean.setMean_cn(cursor.getString(3));
            dbWordBean.setSentence(cursor.getString(4));
            dbWordBean.setSentence_trans(cursor.getString(5));
            dbWordBean.setIsCollection(cursor.getInt(6));
            list.add(dbWordBean);
        }
        database.close();
        return list;
    }


    /**
     * 查询数据库中的总条数.
     * @return
     */
    public int allCaseNum(){
        database = wordSQLiteOpenHelper.getReadableDatabase();
        String sql = "select count(*) from " + WordSQLiteOpenHelper.WORD_TABLE_NAME;
        Cursor cursor = database.rawQuery(sql, null);
        cursor.moveToFirst();
        int count = (int)cursor.getLong(0);
        cursor.close();
        return count;
    }
    public void close() {
        database.close();
    }

    /**
     *重置
     */
    public void reSetStudyAndReview() {
        database = wordSQLiteOpenHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("study",0);
        values.put("review",0);
        database.update(WordSQLiteOpenHelper.WORD_TABLE_NAME,values,"study=?",new String[]{"1"});
    }

    /**
     * 获得某个
     * */
    public DBWordBean getOneWord(int _id) {
        database = wordSQLiteOpenHelper.getReadableDatabase();
        String[] form = new String[]{"_id","word","accent","mean_cn","sentence","sentence_trans","collection"};
        Cursor cursor = database.query(WordSQLiteOpenHelper.WORD_TABLE_NAME,form,"_id=?",new String[]{String.valueOf(_id)},null,null,null);
        DBWordBean dbWordBean = new DBWordBean();
        if(cursor.moveToNext()){
            dbWordBean.set_id(cursor.getInt(0));
            dbWordBean.setWord(cursor.getString(1));
            dbWordBean.setAccent(cursor.getString(2));
            dbWordBean.setMean_cn(cursor.getString(3));
            dbWordBean.setSentence(cursor.getString(4));
            dbWordBean.setSentence_trans(cursor.getString(5));
            dbWordBean.setIsCollection(cursor.getInt(6));
        }
        database.close();
        return dbWordBean;
    }

    /**
     * 获得查询列表
     * */
    public Cursor getCursorSearchWords( String searchString) {
        database = wordSQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = null;
        cursor = database.query(WordSQLiteOpenHelper.WORD_TABLE_NAME,new String[]{"_id","word","mean_cn"},"word like ? ",new String[]{ "%" +searchString + "%"},null,null,null,"10");
        return cursor;
    }
    /**
     * 获得id
     * */
    public int getId(String word) {
        database = wordSQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = null;
        cursor = database.query(WordSQLiteOpenHelper.WORD_TABLE_NAME,new String[]{"_id","word","mean_cn"},"word=?",new String[]{word},null,null,null);
        if(cursor.moveToNext()) {
            return cursor.getInt(0);
        }else {
            return -1;
        }
    }
}
