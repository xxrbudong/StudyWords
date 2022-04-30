package com.example.big.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SpfUtils {
    /*
    * 保存到手机中的文件名
    * */
    private static final String SPF_FILE_NAME = "fileName";
    //private static SharedPreferences prefs;
    /**
     * 初始化
     * @Param context Context
     *
     * */
    /*public static void init(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SPF_FILE_NAME, Context.MODE_PRIVATE);
    }*/
    /**
    * 保存用户数据
    * @Param Context context
    * @Param key String 数据名
    * @Param value Object 数据
     * */
    public static void saveUserInfo(Context context, String key, Object value) {
        SharedPreferences prefs = context.getSharedPreferences(SPF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        if(value == null) {
            editor.putString(key,"null");
        }else {
            String type = value.getClass().getSimpleName();//获得存储类型
            if(type.equals("String")) {
                editor.putString(key, (String)value);
            }else if(type.equals("Integer")) {
                editor.putInt(key,(Integer) value);
            }else if(type.equals("Boolean")) {
                editor.putBoolean(key,(Boolean) value);
            }else if(type.equals("Float")) {
                editor.putFloat(key, (Float) value);
            }else if(type.equals("Long")) {
                editor.putLong(key,(Long) value);
            }
        }
        editor.apply();
    }
    /**
     * 获取用户数据
     * @param
     * @Param key String 数据名
     * @Param value Object 默认数据
     * */
    public static Object getUserInfo(Context context, String key, Object value) {
        SharedPreferences prefs = context.getSharedPreferences(SPF_FILE_NAME, Context.MODE_PRIVATE);
        String type = value.getClass().getSimpleName();
        if ("String".equals(type)) {
            return prefs.getString(key, (String) value);
        } else if ("Integer".equals(type)) {
            return prefs.getInt(key, (Integer) value);
        } else if ("Boolean".equals(type)) {
            return prefs.getBoolean(key, (Boolean)value);
        } else if ("Float".equals(type)) {
            return prefs.getFloat(key, (Float) value);
        } else if ("Long".equals(type)) {
            return prefs.getLong(key, (Long) value);
        }
        return null;
    }
}
