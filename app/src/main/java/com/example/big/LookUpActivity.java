package com.example.big;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;

import java.util.List;

import DB.WordDao;
import bean.DBWordBean;

public class LookUpActivity extends AppCompatActivity {
    private ImageView backImageView;
    private Button lookUpButton;
    private AutoCompleteTextView autoWords;
    private Cursor cursor;
    private List<DBWordBean> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_up);
        initView();
        initAutoWords();
        initListener();
    }


    /**
     * initView
     * */
    private void initView() {
        backImageView = findViewById(R.id.back);
        lookUpButton = findViewById(R.id.find);
        autoWords = findViewById(R.id.autoWord);
    }

    /**
     * initAutoWords
     * */
    private void initAutoWords() {
        WordDao wordDao = new WordDao(LookUpActivity.this);
        String[] from = {"_id","word","mean_cn"};
        int[] to ={R.id._id,R.id.word,R.id.mean_cn};
        AutoCompleteAdapter autoCompleteAdapter = new
                AutoCompleteAdapter(this,R.layout.list_word,null,from,to);
        autoWords.setThreshold(1);
        autoWords.setAdapter(autoCompleteAdapter);
    }

    class AutoCompleteAdapter extends SimpleCursorAdapter {
        private Context context;
        private String queryField;
        private WordDao wordDao;
        public AutoCompleteAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
            super(context, layout, c, from, to);
            this.context = context;
            //this.wordDao = new WordDao(context);
        }

        @Override
        public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
            if(constraint != null) {
                Log.i("Cursor=====================",(String) constraint);
                return getWordDao().getCursorSearchWords((String)constraint);
            }
            return null;
        }

        @Override
        public CharSequence convertToString(Cursor cursor) {
            CharSequence charSequence = cursor.getString(cursor.getColumnIndex("word"));
            Log.i("Cursor=====================",(String) charSequence);
            return charSequence;
        }
        public WordDao getWordDao() {
            if(wordDao == null) {
                wordDao = new WordDao(this.context);
            }
            return wordDao;
        }
    }
    /**
     * 设置点击事件
     * */
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    setResult(RESULT_OK);
                    finish();
                    break;
                case R.id.find:
                    lookUp();
                    break;
            }
        }
    };

    /**
     * */
    private void initListener() {
        backImageView.setOnClickListener(onClickListener);
        lookUpButton.setOnClickListener(onClickListener);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        setResult(RESULT_OK);
        return super.onKeyDown(keyCode, event);
    }

    /**
     *
     * */
    private void lookUp() {
        WordDao wordDao = new WordDao(LookUpActivity.this);
        int id = wordDao.getId(autoWords.getText().toString());
        if(id != -1) {
            Intent intent = new Intent(LookUpActivity.this,WordActivity.class);
            intent.putExtra("_id",id);
            startActivity(intent);
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(LookUpActivity.this);
            builder.setTitle("未找到，你要使用浏览器搜索吗？");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");

                    Uri content_url = Uri.parse("https://www.iciba.com/word?w=" + autoWords.getText());

                    intent.setData(content_url);

                    startActivity(intent);
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.create().show();
        }
    }
}