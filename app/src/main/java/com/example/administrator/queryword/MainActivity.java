package com.example.administrator.queryword;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.queryword.model.Basic;
import com.example.administrator.queryword.model.Word;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import util.JsonUitl;

/**
 * 实现单词查询
 * @author thinkdoor
 *
 * 1.界面初始化
 * 2.创建handler
 * 3.实现上网查词
 *      1.创建OkClient和Request对象
 *      2.创建Call对象
 *      3.重写Call对象的enCall方法
 *          1.获取响应数据
 *          2.封装成json对象
 *          3.转为java对象
 *          4.创建message对象包裹信息
 *          5.发送给handler
 *  4.handler获取消息进行处理
 *      1.获取对象
 *      2.获取数据
 *      3.在界面显示
 */
public class MainActivity extends AppCompatActivity {

    //日志打印
    private String TAG = "MainActivity";

    //1.界面初始化
    EditText editText;
    TextView textView;

    //2.创建handler
    private Handler handler = new Handler(){
        //4.handler获取消息进行处理
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //1.获取对象
            Word word = (Word) msg.obj;
            //2.获取数据
            Basic basic = word.getBasic();
            String explains = null;
            explains = basic.getStrings(basic.getExplains());
            //3.在界面显示
            textView.setText(explains);
        }
    };

    /**
     * 界面创建
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.et);
        textView = (TextView) findViewById(R.id.tv);
    }

    /**
     * 查询按钮的监听方法
     * @param view
     */
    public void query(View view){
        String word = editText.getText().toString();
        //调用上网查词方法
        queryWord(word);
    }

    /**
     * 3.实现上网查词
         1.创建OkClient和Request对象
         2.创建Call对象
         3.重写Call对象的enCall方法
             1.获取响应数据
             2.封装成json对象
             3.转为java对象
             4.发送message给handler
     * @param s 要查询的单词
     */
    public void queryWord(String s){
        String url = "http://fanyi.youdao.com/openapi.do?keyfrom=lewe518&key=70654389&type=data&doctype=json&version=1.1&q="+s;
        //1.创建OkClient和Request对象
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        //2.创建Call对象
        Call call = okHttpClient.newCall(request);
        //3.重写Call对象的enqueue方法
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //1.获取响应数据
                String str = response.body().string();
                Log.d(TAG, "onResponse: " + str);
                try {
                    //2.封装成json对象
                    JSONObject jsonObject = new JSONObject(str);
                    //3.转为java对象
                    Word word = (Word) JsonUitl.stringToObject(jsonObject.toString(),Word.class);
                    //4.创建message,包裹信息
                    Message message = new Message();
                    message.obj = word;
                    //5.发送message给handler
                    handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
