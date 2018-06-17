package me.hades.yqword.view.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.util.DialogUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import me.hades.yqword.R;
import okhttp3.HttpUrl;
import retrofit2.http.HTTP;

/**
 * Created by dandelion1995 on 2018/6/17.
 */
/*
public class LoginActivity extends Activity{
    EditText etName,etPass;
    Button bnLogin,bnCancel;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //获取界面的两个编辑框
        etName=(EditText)findViewById(R.id.userEditText);
        etPass=(EditText)findViewById(R.id.pwdEditTetx);
        //获取界面中的两个按钮
        bnLogin=(Button)findViewById(R.id.bnLogin);
        bnCancel=(Button)findViewById(R.id.bnCancel);

        //为bnCancel按钮的单击事件绑定事件监听器
        bnCancel.setOnClickListener(new FinishListener(this));
        bnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //执行输入校验
                if(validate()){
                    //如果登录成功
                    if(loginPro()){
                        //启动Main Activity
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });

    }
    private boolean loginPro(){
        //获取用户输入的用户名和密码
        String username = etName.getText().toString();
        String pwd = etPass.getText().toString();
        JSONObject jsonObj;
        try {
            jsonObj = query(username, pwd);
            //如果userid大于0
            if (jsonObj.getInt("userId") > 0) {
                return true;
            }
        }catch(Exception e){
                DialogUtil.showDialog(this,"服务器响应异常，请稍后再试！",false);
                e.printStackTrace();
            }
        return false;
    }
    private boolean validate(){
        String username = etName.getText().toString().trim();
        if(username.equals("")){
            DialogUtil.showDialog(this,"用户账号是必填项!",false);
            return false;
        }
        String pwd = etPass.getText().toString().trim();
        if(pwd.equals("")){
            DialogUtil.showDialog(this,"用户密码是必填项！",false);
            return false;
        }
        return true;
    }
    private JSONObject query(String username,String password)throws Exception{
        Map<String,String> map = new HashMap<String,String>();
        map.put("user",username);
        map.put("passwd",password);

        //定义请求的URL
        String url = HttpUtil.Base_URL+"login.jsp";

        //发送请求
        return new JSONObject(HttpUtil.postRequest(url,map));
    }
}
*/