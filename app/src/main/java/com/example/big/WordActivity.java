package com.example.big;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.big.Utils.DownloadUtil;
import com.example.big.Utils.PlayWordsAccentUtils;

import DB.WordDao;
import bean.DBWordBean;

public class WordActivity extends AppCompatActivity {
    private  String FILE_DIR_ACCENT;
    private  String FILE_ACCENT;
    private ImageView backImageView, playAccentImageView;
    private TextView wordTextView, accentTextView, mean_cnTextView,sentenceTextView, sentence_transTextView, _idTextView, word_title;
    private int _id;
    private PlayWordsAccentUtils playSound;
    private DBWordBean dbWordBean;
    private CheckBox checkBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);
        initView();
        initListener();
    }

    /**
     * initView
     * */
    private void initView(){
        playAccentImageView = findViewById(R.id.play_the_pronunciation);
        backImageView = findViewById(R.id.back);
        wordTextView = findViewById(R.id.word);
        accentTextView = findViewById(R.id.accent);
        mean_cnTextView = findViewById(R.id.mean_cn);
        sentenceTextView = findViewById(R.id.sentence);
        sentence_transTextView = findViewById(R.id.sentence_trans);
        //_idTextView = findViewById(R.id._id);
        word_title = findViewById(R.id.word_title);
        checkBox = findViewById(R.id.isCheck);
        Intent intent = getIntent();
        _id = intent.getIntExtra("_id", 1);
        Log.i("_id==========================="," " + _id);
        WordDao wordDao = new WordDao(WordActivity.this);
        dbWordBean = wordDao.getOneWord(_id);
        word_title.setText(dbWordBean.getWord());
        wordTextView.setText(dbWordBean.getWord());
        accentTextView.setText(dbWordBean.getAccent());
        mean_cnTextView.setText(dbWordBean.getMean_cn());
        sentence_transTextView.setText(dbWordBean.getSentence_trans());
        sentenceTextView.setText(dbWordBean.getSentence());
        Log.i("Collection=======================", " " + dbWordBean.getIsCollection());
        checkBox.setChecked(dbWordBean.getIsCollection() == 1? true:false);
//        _idTextView.setText(dbWordBean.get_id());
        /**
         * init 变量
         * */
        FILE_DIR_ACCENT = getExternalFilesDir(Environment.DIRECTORY_MUSIC).getPath();
        FILE_ACCENT = getExternalFilesDir(Environment.DIRECTORY_MUSIC).getPath() + "/";
        Log.i("OPEN====================",FILE_DIR_ACCENT + "=========================" + FILE_ACCENT);
        playSound = new PlayWordsAccentUtils(WordActivity.this);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int collectionSelect = isChecked ? 1:0;
                wordDao.updateStatus(collectionSelect,dbWordBean.getWord(),WordDao.COLLECTION);
            }
        });
    }

    /**
     * 点击事件
     * */

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.back:
                    setResult(RESULT_OK);
                    finish();
                    break;
                case R.id.play_the_pronunciation:
                    playThePronunciation();
                    break;
            }
        }
    };

    /**
     * initListener
     * */
    private void initListener() {
        backImageView.setOnClickListener(onClickListener);
        playAccentImageView.setOnClickListener(onClickListener);
    }
    /**
     * 播放音频
     * */
    private void playThePronunciation() {
        String name = dbWordBean.getWord();
        name = name.replace('/','_');
        name = name.replace(' ','_');
        Log.i("ADDRESS=====================",FILE_ACCENT);
        Log.i("NAMENAME:::::::::::::::::",name);
        if (DownloadUtil.fileIsExists(name + ".mp3", FILE_DIR_ACCENT, ".mp3")) {
            Log.i("ONCLICK:::::::::::::::::::::",FILE_ACCENT + name + ".mp3");
            playSound.playSound(FILE_ACCENT , name + ".mp3");
        } else {
            playSound.downFile(name,"http://dict.youdao.com/dictvoice?audio="+name,".mp3");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        setResult(RESULT_OK);
        return super.onKeyDown(keyCode, event);
    }
}