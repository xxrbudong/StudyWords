package com.example.big;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import DB.WordDao;
import com.example.big.Utils.ConstStringUtils;
import com.example.big.Utils.SpfUtils;
import com.example.big.Utils.WordJsonUtils;
import bean.WordBean;

public class MainActivity extends AppCompatActivity {
    private static final int WORD_REQUEST_CODE = 0x00;
    private static final int COLLECTION_WORD_REQUEST_CODE = 0x01;
    private static final int REVIEW_WORDS_REQUEST_CODE = 0x02;
    private static final int WORD_LIST_REQUEST_CODE = 0x04;
    private static final int WORD_ACTIVITY_REQUEST_CODE = 0x05;
    public static final int SEARCH_WORD_REQUEST_CODE = 0x06;
    private ListView listView;
    private TextView newStudyNumberTextView, reviewStudyNumberTextView, studyCollectionWordTextView, modifyProgram,relearnTextView, learningProcessTextView, showAll;
    private Button reviewWords, studyWord,search_word;
    //private int sumStudyWords, sumReviewWords;
    private int wordsSum, studyWordsSum,reviewWordsSum, sumStudyWords;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWordDB();
        initView();
        initViewListener();
        initWrongList();

    }
    /**
    * 将单词数据存入到数据库中
    * */
    private void initWordDB() {
        Boolean isInitWordDB = (Boolean) SpfUtils.getUserInfo(MainActivity.this,ConstStringUtils.IS_INIT_WORD_DB,false);
        if(isInitWordDB) {
            Log.i("INITWORDDB","已经加载过了");
        } else {
            WordJsonUtils wordJsonUtils = new WordJsonUtils();
            WordDao  wordDao = new WordDao(MainActivity.this);
            List<WordBean> list = wordJsonUtils.getWord(MainActivity.this);
            for (WordBean word : list) {
                wordDao.insertWord(word);
            }
            SpfUtils.saveUserInfo(MainActivity.this,ConstStringUtils.IS_INIT_WORD_DB, true);
            Log.i("INITWORDDB","已经加载过了" + list.size());
        }
    }

    /**
     *初始化控件
     */
    private void initView() {
        listView = findViewById(R.id.wrong_word_list);
        newStudyNumberTextView = findViewById(R.id.new_study_number);
        reviewStudyNumberTextView = findViewById(R.id.review_number);
        reviewWords = findViewById(R.id.reviewWords);
        studyWord = findViewById(R.id.studyWords);
        studyCollectionWordTextView = findViewById(R.id.study_collection_word);
        modifyProgram = findViewById(R.id.Modify_program);
        relearnTextView = findViewById(R.id.relearn);
        learningProcessTextView = findViewById(R.id.learning_process);
        showAll = findViewById(R.id.showAll);
        search_word = findViewById(R.id.search_word);
        //init变量
        WordDao wordDao = new WordDao(MainActivity.this);
        wordsSum = wordDao.allCaseNum();
        Log.i("WORDSSUM===================",  " "+ wordsSum);
        //startStudyWordsNumber = (int) SpfUtils.getUserInfo(MainActivity.this,ConstStringUtils.START_STUDY_NUMBER, 1);
        studyWordsSum = (int) SpfUtils.getUserInfo(MainActivity.this,ConstStringUtils.SUM_STUDY_WORDS_NUMBER, 0);
        //startReviewWordsNumber = (int) SpfUtils.getUserInfo(MainActivity.this,ConstStringUtils.REVIEW_NUMBER_START,1);
        reviewWordsSum = (int) SpfUtils.getUserInfo(MainActivity.this,ConstStringUtils.SUM_NEED_REVIEW_NUMBER,0);
        sumStudyWords = (int) SpfUtils.getUserInfo(MainActivity.this,ConstStringUtils.EVERY_STUDY_WORDS,20);
        //初始化界面
        newStudyNumberTextView.setText(String.valueOf(sumStudyWords) + " ");
        reviewStudyNumberTextView.setText(String.valueOf(reviewWordsSum) + " ");
        learningProcessTextView.setText("已学习：" + studyWordsSum +" / " + wordsSum);
    }

    /**
     *点击事件绑定
     */
    private void initViewListener() {
        studyWord.setOnClickListener(onClickListener);
        reviewWords.setOnClickListener(onClickListener);
        studyCollectionWordTextView.setOnClickListener(onClickListener);
        modifyProgram.setOnClickListener(onClickListener);
        relearnTextView.setOnClickListener(onClickListener);
        showAll.setOnClickListener(onClickListener);
        search_word.setOnClickListener(onClickListener);
    }

    /**
     * 点击事件
     * */
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.studyWords:
                    startStudyWords();
                    break;
                case R.id.reviewWords:
                    reviewWords();
                    break;
                case R.id.study_collection_word:
                    studyCollectionWords();
                    break;
                case R.id.Modify_program:
                    modifyProgram();
                    break;
                case R.id.relearn:
                    relearnProcess();
                    break;
                case R.id.showAll:
                    showAll();
                    break;
                case R.id.search_word:
                    searchWord();
                default:
                    break;
            }
        }
    };
    /**
     * 更新错误单词列表
     * */
    private void initWrongList() {
        WordDao  wordDao = new WordDao(MainActivity.this);
        String[] from = {"_id","word","mean_cn"};
        Cursor cursor = wordDao.getCursorWord(from,WordDao.COLLECTION);
        int[] to ={R.id._id,R.id.word,R.id.mean_cn};
        SimpleCursorAdapter simpleCursorAdapter = new
                SimpleCursorAdapter(this,R.layout.list_word,cursor,from,to);
        listView.setAdapter(simpleCursorAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, WordActivity.class);
                intent.putExtra("_id",(int)id);
                Log.i("MainActivity================="," " + (int)id);
                startActivityForResult(intent,WORD_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    /**
    * 开始学习单词
    * */
    public void startStudyWords() {
        Intent intent = new Intent(MainActivity.this, StudyWordActivity.class);
        //startStudyWordsNumber = (int) SpfUtils.getUserInfo(MainActivity.this,ConstStringUtils.START_STUDY_NUMBER,1);
        Log.i("STARTSTUDYWORDS=================="," " + reviewWordsSum + " " + studyWordsSum + " " + wordsSum + " " + sumStudyWords);
        if(reviewWordsSum >= 100) {
            Toast.makeText(MainActivity.this,"未复习的单词太多啦，先复习吧！",Toast.LENGTH_SHORT).show();
        }else if(studyWordsSum == wordsSum) {
            Toast.makeText(MainActivity.this,"全学完啦，可以重置学习！",Toast.LENGTH_SHORT).show();
        }else {
            //sumStudyWords = getNumber(newStudyNumberTextView.getText().toString());
            //intent.putExtra(ConstStringUtils.START_STUDY_NUMBER, startStudyWordsNumber);
            intent.putExtra(ConstStringUtils.SUM_STUDY_WORDS_NUMBER, sumStudyWords);
            intent.putExtra(ConstStringUtils.IS_COLLECTION,false);
            startActivityForResult(intent,WORD_REQUEST_CODE);
        }
    }

    /**
     * 开始学习收藏单词
     * */
    public void studyCollectionWords() {
        Intent intent = new Intent(MainActivity.this, StudyWordActivity.class);
        intent.putExtra(ConstStringUtils.IS_COLLECTION,true);
        startActivityForResult(intent,COLLECTION_WORD_REQUEST_CODE);
    }

    /**
     * 执行跳转回调
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case WORD_REQUEST_CODE:
                initWrongList();
                int haven_study_numbers = 0;
                if(resultCode == RESULT_OK&&data != null) {
                    haven_study_numbers = data.getIntExtra("studyWordsNum",0);
                }
                Log.i("IS_STUDY_ALL==================", " " + haven_study_numbers);
                reviewWordsSum += haven_study_numbers;
                studyWordsSum += haven_study_numbers;
                Log.i("OnActivityResult==============="," " + reviewWordsSum);
                SpfUtils.saveUserInfo(MainActivity.this,ConstStringUtils.SUM_NEED_REVIEW_NUMBER,reviewWordsSum);
                SpfUtils.saveUserInfo(MainActivity.this,ConstStringUtils.SUM_STUDY_WORDS_NUMBER,studyWordsSum);
                learningProcessTextView.setText("已学习：" + (studyWordsSum) + " / " + wordsSum);
                reviewStudyNumberTextView.setText(reviewWordsSum + " ");
                break;
            case WORD_LIST_REQUEST_CODE:
            case COLLECTION_WORD_REQUEST_CODE:
            case WORD_ACTIVITY_REQUEST_CODE:
            case SEARCH_WORD_REQUEST_CODE:
                initWrongList();
                break;
            case REVIEW_WORDS_REQUEST_CODE:
                reviewWordsSum -= data.getIntExtra("haven_review_nums",0);
                SpfUtils.saveUserInfo(MainActivity.this,ConstStringUtils.SUM_NEED_REVIEW_NUMBER,reviewWordsSum);
                reviewStudyNumberTextView.setText(reviewWordsSum + " ");
                break;
            default:
                break;
        }
    }

    /**
     * 调整计划
     */
    public void modifyProgram() {
        List<String> list = new ArrayList<>();
        for(int i = 0; i < 1; i++) {
            for(int j = 0; j < 6; j++) {
                list.add(String.valueOf(j*10*2 + i*100));
            }
        }
        list.remove("0");
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("请选择要学习的单词数量");
        Spinner spinner = new Spinner(MainActivity.this);
        String[] arr = new String[list.size()];
        RelativeLayout relativeLayout = new RelativeLayout(MainActivity.this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        relativeLayout.addView(spinner,layoutParams);
        list.toArray(arr);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,arr);
        spinner.setAdapter(arrayAdapter);
        builder.setView(relativeLayout);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sumStudyWords = Integer.parseInt(spinner.getSelectedItem().toString());
                newStudyNumberTextView.setText(sumStudyWords + " ");
                SpfUtils.saveUserInfo(MainActivity.this,ConstStringUtils.EVERY_STUDY_WORDS, sumStudyWords);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }

    /**
     * 复习单词
     */
    private void reviewWords() {
        if(reviewWordsSum >= 15) {
            Intent intent = new Intent(MainActivity.this,ReviewWordsActivity.class);
            startActivityForResult(intent,REVIEW_WORDS_REQUEST_CODE);
        } else {
            Toast.makeText(MainActivity.this,"单词太少啦，快继续学习！",Toast.LENGTH_SHORT).show();
        }

    }

    /**
     *获得数字
     * */
    /*private int getNumber(String s) {
        int num = 0;
        int n = s.length();
        for(int i = 0; i < n; i++) {
            if(s.charAt(i)>= '0' && s.charAt(i)<= '9') {
                num = num * 10 + s.charAt(i) - '0';
            }else {
                return num;
            }
        }
        return num;
    }*/

    /**
     * 重置学习
     * */
    private void relearnProcess() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("你确定要重置学习计划吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //startStudyWordsNumber = 1;
                //startReviewWordsNumber = 1;
                studyWordsSum = 0;
                reviewWordsSum = 0;
                //sumStudyWords = 0;
                WordDao wordDao = new WordDao(MainActivity.this);
                wordDao.reSetStudyAndReview();
                learningProcessTextView.setText("已学习：" + "0 / " + wordsSum);
                SpfUtils.saveUserInfo(MainActivity.this,ConstStringUtils.SUM_STUDY_WORDS_NUMBER,studyWordsSum);
                //SpfUtils.saveUserInfo(MainActivity.this,ConstStringUtils.REVIEW_NUMBER_START,startReviewWordsNumber);
                SpfUtils.saveUserInfo(MainActivity.this,ConstStringUtils.SUM_NEED_REVIEW_NUMBER,reviewWordsSum);
                Log.i("RELEARNPROCESS=============="," "  + " " + studyWordsSum + " " + reviewWordsSum );
                Toast.makeText(MainActivity.this,"重置成功",Toast.LENGTH_SHORT).show();
                reviewStudyNumberTextView.setText(reviewWordsSum + " ");
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

    /**
     * 查看全部单词
     * */
    private void showAll() {
        Intent intent = new Intent(this,WordListActivity.class);
        startActivityForResult(intent,WORD_LIST_REQUEST_CODE);
    }

    /**
     * 查询单词
     * */
    private void searchWord() {
        Intent intent = new Intent(MainActivity.this,LookUpActivity.class);
        startActivityForResult(intent,SEARCH_WORD_REQUEST_CODE);
    }
}