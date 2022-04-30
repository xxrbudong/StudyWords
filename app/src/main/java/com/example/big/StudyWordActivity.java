package com.example.big;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import DB.WordDao;

import com.example.big.Utils.ConstStringUtils;
import com.example.big.Utils.DownloadUtil;
import com.example.big.Utils.PlayWordsAccentUtils;

import bean.DBWordBean;

public class StudyWordActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<DBWordBean> list;
    private boolean isCollection;
    private MyAdapter myAdapter;
    private ImageView back;
    private  String FILE_DIR_ACCENT;
    private  String FILE_ACCENT;
    private Intent intent;
    private int  sum;
    //private int haven_study_number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_word);
        FILE_DIR_ACCENT = getExternalFilesDir(Environment.DIRECTORY_MUSIC).getPath();
        FILE_ACCENT = getExternalFilesDir(Environment.DIRECTORY_MUSIC).getPath() + "/";
        Log.i("OPEN====================",FILE_DIR_ACCENT + "=========================" + FILE_ACCENT);
        initViewId();//初始化View
        initOnClickListener();//初始化监听事件
        intent = getIntent();//获得Intent
        isCollection = intent.getBooleanExtra(ConstStringUtils.IS_COLLECTION,false);//获得传来的数据
        if(!isCollection) {
            //startId = intent.getIntExtra(ConstStringUtils.START_STUDY_NUMBER,0);
            sum = intent.getIntExtra(ConstStringUtils.SUM_STUDY_WORDS_NUMBER,20);
            Log.i("NUM=================="," " + " " + sum);
            //haven_study_number = startId;
        }
        initRecyclerView();//初始化recyclerview
    }
    /**
     * initView()
     */
    private void initViewId() {
        recyclerView = findViewById(R.id.recyclerview);
        back = findViewById(R.id.back);
    }

    /**
     * initOnclick
     * */
    private void initOnClickListener() {
        back.setOnClickListener(onClickListener);
    }

    /**
     * 监听对象
     */
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    if(!isCollection) {
                        Intent intent = new Intent();
                        intent.putExtra("studyWordsNum",myAdapter.getStudyNumber());
                        setResult(RESULT_OK,intent);
                    }else {
                        setResult(RESULT_OK);
                    }
                    finish();
                    break;
                default:
                    break;
            }
        }
    };
    /**
     * 设置recyclerView
     * */
    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        WordDao wordDao = new WordDao(StudyWordActivity.this);
        list = wordDao.getListWord(sum, isCollection?0:1);
        myAdapter = new MyAdapter(StudyWordActivity.this, list, new OnItemListener() {
            @Override
            public void onItemCheckedChangeListener(View v, String word, boolean isChecked,int position) {
                int collectionSelect = isChecked ? 1:0;
                wordDao.updateStatus(collectionSelect,word,WordDao.COLLECTION);
                if(isCollection) {
                    if(isChecked) {
                        //myAdapter.insertList();
                    }else {
                        myAdapter.deleteList(position);
                        myAdapter.notifyItemRemoved(position);
                        myAdapter.notifyDataSetChanged();
                    }
                }
            }
        }, isCollection);
        recyclerView.setAdapter(myAdapter);
        /**
         * SnapHelper这个辅助类，用于辅助RecyclerView在滚动结束时将Item对齐到某个位置
         */
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

    }
    /**
     * 自定义适配器
     * */
    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private List<DBWordBean> list; //数据列表
        private int studyNumber;
        private Context context;//上下文对象
        private View itemView;//选择的布局
        private OnItemListener listener;//checkbox点击事件
        private boolean isCollection;
        private PlayWordsAccentUtils playSound;
        //private MediaPlayer mMediaPlayer;

        public int getStudyNumber() {
            return studyNumber;
        }

        /**
         * @param context 上下文
         * @param list 数据列表
         * @param listener 事件监听器
         * */
        public MyAdapter(Context context,List<DBWordBean> list, OnItemListener listener, boolean isCollection) {
            this.context = context;
            this.list = list;
            this.listener = listener;
            this.isCollection = isCollection;
            this.studyNumber = 0;
            this.playSound = new PlayWordsAccentUtils(context);
        }

        /**
         * 创建ViewHolder对象
         * @param parent 可以简单理解为item的根ViewGroup，item的子控件加载在其中
         * @param viewType item的类型，可以根据viewType来创建不同的ViewHolder，来加载不同的类型的item
         * */
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            /**
             * public View inflate (int resource, ViewGroup root, boolean attachToRoot) 加载布局
             * 参数
             * @param resource 加载的布局对应的资源id
             * @param root 为该布局的外部再嵌套一层父布局，如果不需要的话，写null就可以了!
             * @param attachToRoot 是否为加载的布局添加一个root的外层容器
             * */
            itemView = LayoutInflater.from(context).inflate(R.layout.item_word,parent,false);
            return new MyViewHolder(itemView);
        }

        /**
         * 界面ui控件绑定
         * 实现点击事件方法
         * */
        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            if(!isCollection) {
                holder.haven_study_all.setVisibility(View.VISIBLE);
                /*if(position != list.size()) {*/
                    holder.haven_study_all.setText("学习");
                    holder.haven_study_all.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(holder.haven_study_all.getText().toString().equals("学习")) {
                                studyNumber++;
                                holder.haven_study_all.setText("已学习" + list.get(position).getWord());
                                WordDao wordDao = new WordDao(context);
                                wordDao.updateStatus(1,holder.word.getText().toString(),WordDao.STUDY);
                            }
                            if(studyNumber == list.size()) {
                                Toast.makeText(context,"学完全部单词啦！",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            }
            holder.word.setText(list.get(position).getWord());
            holder.accent.setText(list.get(position).getAccent());
            holder.mean_cn.setText(list.get(position).getMean_cn());
            holder.sentence.setText(list.get(position).getSentence());
            holder.sentence_trans.setText(list.get(position).getSentence_trans());
            holder.numbers.setText(String.valueOf(position+1) + "/" + String.valueOf(list.size()));
            holder.collection.setChecked(list.get(position).getIsCollection() == 1?true:false);
            holder.collection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemCheckedChangeListener(v,holder.word.getText().toString(),holder.collection.isChecked(),position);
                }
            });
            /*
            *
            * */
            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.showButton.getVisibility() == View.VISIBLE) {
                        holder.showButton.setVisibility(View.INVISIBLE);
                    }else {
                        holder.showButton.setVisibility(View.VISIBLE);
                    }
                }
            });

            holder.play_the_pronunciation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = holder.word.getText().toString();
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
            });

            holder.detailed_interpretation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*Intent intent = new Intent(context,YouDaoActivity.class);
                    intent.putExtra("word",holder.word.getText());
                    startActivity(intent);*/
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");

                    Uri content_url = Uri.parse("https://www.iciba.com/word?w=" + holder.word.getText());

                    intent.setData(content_url);

                    startActivity(intent);
                }
            });
        }
        /**
         * item数量
         */
        @Override
        public int getItemCount() {
            return list.size();
        }

        /**
         * 插入数据
         * */
        public void insertList(DBWordBean wordBean) {
            for(int i = 0; i < list.size(); i++) {
                if(list.get(i).get_id() > wordBean.get_id()) {
                    list.add(i,wordBean);
                    return;
                }
            }
            list.add(wordBean);
        }
        /*
        * 删除数据
        * */
        public void deleteList(int position) {
            list.remove(position);
        }
    }
    /**
     * MyViewHolder
     * ViewHolder保存了一个项目视图，以及在这个视图中的元数据。
     */
    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView numbers, word, accent, mean_cn,sentence, sentence_trans,showButton;
        private CheckBox collection;
        private RelativeLayout relativeLayout;
        private ImageView play_the_pronunciation;
        private TextView detailed_interpretation;
        private Button haven_study_all;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            numbers = itemView.findViewById(R.id.numbers);
            word = itemView.findViewById(R.id.word);
            accent = itemView.findViewById(R.id.accent);
            mean_cn = itemView.findViewById(R.id.mean_cn);
            sentence = itemView.findViewById(R.id.sentence);
            sentence_trans = itemView.findViewById(R.id.sentence_trans);
            collection = itemView.findViewById(R.id.isCheck);
            relativeLayout = itemView.findViewById(R.id.showLayout);
            showButton = itemView.findViewById(R.id.showButton);
            play_the_pronunciation = itemView.findViewById(R.id.play_the_pronunciation);
            detailed_interpretation = itemView.findViewById(R.id.detailed_interpretation);
            haven_study_all = itemView.findViewById(R.id.haven_study_all);
        }

    }

    /**
     *点击事件回调接口
     */
    public interface OnItemListener {
        void onItemCheckedChangeListener(View v, String word,boolean isChecked,int position);
        //void onDetailedInterpretationListener(View v, String word,boolean isChecked,int position);
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Intent intent = new Intent();
        intent.putExtra("studyWordsNum",myAdapter.getStudyNumber());
        setResult(RESULT_OK,intent);
        return super.onKeyDown(keyCode, event);
    }
}