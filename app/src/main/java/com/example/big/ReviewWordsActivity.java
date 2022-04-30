package com.example.big;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import DB.WordDao;
import bean.DBWordBean;

public class ReviewWordsActivity extends AppCompatActivity {
    private final int REVIEW_WORDS_NUMBERS = 15;
    private RelativeLayout relativeLayout_check, relativeLayout_input;
    private Button[] buttons;
    private Button commit;
    private TextView word, accent, mean_cn, hint_input, hint_check, next,show_word;
    private ImageView back_imageView;
    private EditText input_word;
    private List<DBWordBean> list;
    private int step_input,step_check;
    private int haven_review_nums;
    private Random random;
    private boolean answer_questions;
    private Map<Integer,Boolean> answer_Results;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_words);
        initView();
        next();
        initListener();
    }
    /**
     * 初始化控件
     */
    private void initView() {
        relativeLayout_check = findViewById(R.id.re_layout_check);
        relativeLayout_input = findViewById(R.id.re_layout_input);
        Button AButton = findViewById(R.id.A);
        Button BButton = findViewById(R.id.B);
        Button CButton = findViewById(R.id.C);
        Button DButton = findViewById(R.id.D);
        commit = findViewById(R.id.commit);
        word = findViewById(R.id.word);
        accent = findViewById(R.id.accent);
        mean_cn = findViewById(R.id.mean_cn);
        hint_check = findViewById(R.id.hint_check);
        hint_input = findViewById(R.id.hint_input);
        next = findViewById(R.id.next);
        show_word = findViewById(R.id.show_word);
        back_imageView = findViewById(R.id.back);
        input_word = findViewById(R.id.input_word);
        buttons = new Button[]{AButton,BButton,CButton,DButton};
        /**
         * 初始化变量
         * */
        WordDao wordDao = new WordDao(ReviewWordsActivity.this);
        list = wordDao.getListWord(REVIEW_WORDS_NUMBERS,WordDao.REVIEW);
        Log.i("List=============================", " " + list.size() + list.get(0).getMean_cn());
        step_input = 0;
        step_check = 0;
        haven_review_nums = 0;
        random = new Random();
        answer_questions = true;
        answer_Results = new HashMap<>();
    }

    /**
     * 点击事件初始化
     * */
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    Intent intent = new Intent();
                    intent.putExtra("haven_review_nums",haven_review_nums);
                    setResult(RESULT_OK,intent);
                    finish();
                    break;
                case R.id.next:
                    next();
                    break;
                case R.id.A:
                    checkButtons(R.id.A);
                    break;
                case R.id.B:
                    checkButtons(R.id.B);
                    break;
                case R.id.C:
                    checkButtons(R.id.C);
                    break;
                case R.id.D:
                    checkButtons(R.id.D);
                    break;
                case R.id.commit:
                    commitWords();
                    break;
            }
        }
    };
    /**
     * 初始化点击事件
     * */
    private void initListener() {
        back_imageView.setOnClickListener(onClickListener);
        next.setOnClickListener(onClickListener);
        buttons[0].setOnClickListener(onClickListener);
        buttons[1].setOnClickListener(onClickListener);
        buttons[2].setOnClickListener(onClickListener);
        buttons[3].setOnClickListener(onClickListener);
        commit.setOnClickListener(onClickListener);
    }
    /**
     * next点击事件
     * */
    private void next() {
        if(answer_questions) {
            if(step_check < list.size()) {
                DBWordBean dbWordBean = list.get(step_check);
                List<Integer> selectList = new ArrayList<>();
                word.setText(dbWordBean.getWord());
                accent.setText(dbWordBean.getAccent());
                int current = random.nextInt(4);
                buttons[current].setText(dbWordBean.getMean_cn());
                selectList.add(step_check);
                for(int i = 0; i < 4; i++) {
                    buttons[i].setCompoundDrawables(null,null,null,null);
                    if(i != current) {
                        boolean flag;
                        int index = 0;
                        do {
                            flag = false;
                            index = random.nextInt(15);
                            for(int j : selectList) {
                                if(j == index) {
                                    flag = true;
                                }
                            }
                        }while (flag);
                        buttons[i].setText(list.get(index).getMean_cn());
                        selectList.add(index);
                    }
                    buttons[i].setEnabled(true);
                }
                //next.setVisibility(View.GONE);
                answer_questions = false;
                hint_check.setVisibility(View.GONE);
            }else if(step_input < list.size()) {
                input_word.setCompoundDrawables(null,null,null,null);
                input_word.setText("");
                relativeLayout_check.setVisibility(View.GONE);
                relativeLayout_input.setVisibility(View.VISIBLE);
                mean_cn.setText(list.get(step_input).getMean_cn());
                hint_input.setVisibility(View.GONE);
                show_word.setVisibility(View.GONE);
                show_word.setText(list.get(step_input).getWord());
                input_word.setEnabled(true);
                answer_questions = false;
                commit.setEnabled(true);
            }else {
                Toast.makeText(ReviewWordsActivity.this,"已经复习完所有单词啦！",Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(ReviewWordsActivity.this,"完成此题，才能切换下一个！",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * checkButtons 点击事件
     * */
    private void checkButtons(int id) {
        for(int i = 0; i < 4; i++) {
            String s = buttons[i].getText().toString();
            Log.i("Buttons=======================",s);
            if (s.equals(list.get(step_check).getMean_cn())) {
                Drawable drawable = getDrawable(R.drawable.current2);
                drawable.setBounds(1,1,80,80);
                buttons[i].setCompoundDrawables(null, null, drawable, null);
                Log.i("Buttons=======================1",s);

            }
            if (buttons[i].getId() == id) {
                hint_check.setVisibility(View.VISIBLE);
                if (!s.equals(list.get(step_check).getMean_cn())) {
                    Drawable drawable = getDrawable(R.drawable.error2);
                    drawable.setBounds(1,1,80,80);
                    buttons[i].setCompoundDrawables(null, null, drawable, null);
                    Log.i("Buttons=======================2",s);
                    answer_Results.put(step_check,false);
                    hint_check.setText("很抱歉回答错误。");
                }else {
                    //hint_check.setText("很抱歉回答错误。");
                    answer_Results.put(step_check,true);
                    hint_check.setText("恭喜你，回答正确！");

                }
            }
            buttons[i].setEnabled(false);
        }
        step_check++;
        answer_questions = true;
    }

    /**
     * 提交单词
     * */
    private void commitWords() {
        hint_input.setVisibility(View.VISIBLE);
        commit.setEnabled(false);
        Log.i("commitWords================", show_word.getText().toString() + " " + input_word.getText().toString());
        input_word.setEnabled(false);
        show_word.setVisibility(View.VISIBLE);
        if(show_word.getText().toString().equals(input_word.getText().toString())) {
            Drawable drawable = getDrawable(R.drawable.current2);
            drawable.setBounds(1,1,80,80);
            input_word.setCompoundDrawables(null, null, drawable, null);
            if(answer_Results.get(step_input)) {
                WordDao wordDao = new WordDao(ReviewWordsActivity.this);
                wordDao.updateStatus(1,show_word.getText().toString(),WordDao.REVIEW);
                haven_review_nums++;
                hint_input.setText("恭喜你，回答正确！");
            }
        }else {
            Drawable drawable = getDrawable(R.drawable.error2);
            drawable.setBounds(1,1,80,80);
            input_word.setCompoundDrawables(null, null, drawable, null);
            hint_input.setText("很抱歉回答错误。");
        }
        step_input++;
        answer_questions = true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Intent intent = new Intent();
        intent.putExtra("haven_review_nums",haven_review_nums);
        setResult(RESULT_OK,intent);
        return super.onKeyDown(keyCode, event);
    }
}