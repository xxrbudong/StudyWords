package com.example.big.Utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import bean.WordBean;

public class WordJsonUtils {
    /*
    * 获得单词数据
    * */
    public List<WordBean> getWord(Context context) {
        List<WordBean> wordList = new ArrayList<>();
        StringBuffer stringBuffer = new StringBuffer();
        try {
            InputStream inputStream = context.getResources().getAssets().open("list1.json");
            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(isr);
            String jsonLine;
            while ((jsonLine = reader.readLine()) != null) {
                stringBuffer.append(jsonLine);
            }
            reader.close();
            isr.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("JSON","ERROR");
        }
        String result =  stringBuffer.toString();

        Type listType = new TypeToken<List<WordBean>>() {
        }.getType();
        wordList = new Gson().fromJson(result, listType);
        //Log.d("json",String.valueOf(wordList.size()) +  wordList.get(wordList.size()-2).toString());
        return wordList;
    }
}
