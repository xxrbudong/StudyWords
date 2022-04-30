package com.example.big;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import DB.WordDao;

public class WordListActivity extends AppCompatActivity {
    private ImageView backImageView;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);
        initView();
        initListener();
        initListView();
    }
    /**
     * initView
     * */
    private void initView() {
        listView = findViewById(R.id.listView);
        backImageView = findViewById(R.id.back);
    }

    /**
     *
     * 点击事件
     * */
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    setResult(RESULT_OK);
                    finish();
                    break;
            }
        }
    };

    /**
     * initListener
     * */
    private void initListener() {
        backImageView.setOnClickListener(onClickListener);
    }

    /**
     * initListView
     * */
    private void initListView() {
        WordDao wordDao = new WordDao(WordListActivity.this);
        String[] form = new String[]{"_id","word","mean_cn"};
        Cursor cursor = wordDao.getCursorWord(form,WordDao.STUDY);
        int[] to ={R.id._id,R.id.word,R.id.mean_cn};
        SimpleCursorAdapter simpleCursorAdapter = new
                SimpleCursorAdapter(this,R.layout.list_word,cursor,form,to);
        listView.setAdapter(simpleCursorAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(WordListActivity.this, WordActivity.class);
                intent.putExtra("_id",(int)id);
                Log.i("WordListActivity================="," " + (int)id);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        setResult(RESULT_OK);
        return super.onKeyDown(keyCode, event);
    }
}