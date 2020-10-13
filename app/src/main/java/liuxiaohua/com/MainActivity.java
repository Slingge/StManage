package liuxiaohua.com;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //是否开启读写权限
    private boolean isPermissions = false;

    private OperationDatabaseUtil operationDatabaseUtil;
    private List<StuBean> DBList;

    private EditText et_studentID, et_name, et_class, et_age;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (PermissionUtil.ApplyPermission(this, 0)) {
            isPermissions = true;
        }

        init();
    }

    private void init() {
        Button btn_ceateDatabase = findViewById(R.id.btn_ceateDatabase);
        btn_ceateDatabase.setOnClickListener(this);
        Button btn_openDatabase = findViewById(R.id.btn_openDatabase);
        btn_openDatabase.setOnClickListener(this);
        Button btn_last = findViewById(R.id.btn_last);
        btn_last.setOnClickListener(this);
        Button btn_next = findViewById(R.id.btn_next);
        btn_next.setOnClickListener(this);

        Button btn_add = findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);
        Button btn_modify = findViewById(R.id.btn_modify);
        btn_modify.setOnClickListener(this);
        Button btn_del = findViewById(R.id.btn_del);
        btn_del.setOnClickListener(this);
        Button btn_close = findViewById(R.id.btn_close);
        btn_close.setOnClickListener(this);

        et_studentID = findViewById(R.id.et_studentID);
        et_name = findViewById(R.id.et_name);
        et_class = findViewById(R.id.et_class);
        et_age = findViewById(R.id.et_age);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ceateDatabase://创建数据库
                if (!isPermissions) {
                    PermissionUtil.ApplyPermission(this, 0);
                    return;
                }
                operationDatabaseUtil = new OperationDatabaseUtil(this);
                Toast("数据库已创建");
                break;
            case R.id.btn_openDatabase://打开数据库
                if (operationDatabaseUtil == null) {
                    operationDatabaseUtil = new OperationDatabaseUtil(this);
                }
                if (operationDatabaseUtil.openDB()) {
                    Toast("数据库已打开");
                }
                DBList = new ArrayList<>();
                DBList.addAll(operationDatabaseUtil.alterDate());
                Log.e("表内容", new Gson().toJson(DBList));
                setStuInfo();
                break;
            case R.id.btn_last://上一条数据
                if(DBList==null){
                    Toast("请先打开数据库");
                    return;
                }
                if (DBList.isEmpty()) {
                    Toast("请先添加数据");
                    return;
                }
                if (index == 0) {
                    Toast("已经是第一条");
                    return;
                }
                index--;
                setStuInfo();
                break;
            case R.id.btn_next://下一条数据
                if(DBList==null){
                    Toast("请先打开数据库");
                    return;
                }
                if (DBList.isEmpty()) {
                    Toast("请先添加数据");
                    return;
                }
                if (index == DBList.size() - 1) {
                    Toast("已经是最后一条");
                    return;
                }
                index++;
                setStuInfo();
                break;
            case R.id.btn_add://添加一条数据
                if (DBList == null) {
                    Toast("请先打开数据库");
                    return;
                }
                StuBean bean = getStuInfo();
                if (bean == null) {
                    return;
                }
                for (int i = 0; i < DBList.size(); i++) {
                    if (DBList.get(i).getStuId().equals(bean.getStuId())) {
                        Toast("已存在");
                        return;
                    }
                }
                bean.setId(operationDatabaseUtil.addDate(bean));
                DBList.add(bean);
                Log.e("DBList.toString", new Gson().toJson(DBList));
                index = DBList.size()-1;
                Log.e("表内容", new Gson().toJson(DBList));
                setStuInfo();
                Toast("添加成功");
                break;
            case R.id.btn_modify://修改数据
                if(DBList==null){
                    Toast("请先打开数据库");
                    return;
                }
                bean = getStuInfo();
                if (bean == null) {
                    return;
                }
                if (DBList.isEmpty()) {
                    Toast("请先添加数据");
                    return;
                }
                long id = DBList.get(index).getId();
                operationDatabaseUtil.updateData(id, bean);
                DBList.set(index, bean);
                Toast("修改成功");
                break;
            case R.id.btn_del://删除一条数据
                if(DBList==null){
                    Toast("请先打开数据库");
                    return;
                }
                if (DBList.isEmpty()) {
                    Toast("请先添加数据");
                    return;
                }
                id = DBList.get(index).getId();
                operationDatabaseUtil.deleteDate(id);
                index = 0;
                if (!DBList.isEmpty()) {
                    DBList.clear();
                }
                DBList.addAll(operationDatabaseUtil.alterDate());
                Log.e("表内容", new Gson().toJson(DBList));
                setStuInfo();
                Toast("删除成功");
                break;
            case R.id.btn_close://关闭
                finish();
                break;
        }
    }


    private StuBean getStuInfo() {
        StuBean bean = new StuBean();
        String stuId = getEditText(et_studentID);
        if (TextUtils.isEmpty(stuId)) {
            Toast("请输入学号");
            return null;
        }

        bean.setStuId(stuId);
        String name = getEditText(et_name);
        if (TextUtils.isEmpty(name)) {
            Toast("请输入姓名");
            return null;
        }
        bean.setName(name);
        String className = getEditText(et_class);
        if (TextUtils.isEmpty(className)) {
            Toast("请输入班级");
            return null;
        }
        bean.setClassName(className);
        String age = getEditText(et_age);
        if (TextUtils.isEmpty(stuId)) {
            Toast("请输入年龄");
            return null;
        }
        bean.setAge(age);
        return bean;
    }

    private int index = 0;

    private void setStuInfo() {
        if (DBList.isEmpty()) {
            et_studentID.setText("");
            et_name.setText("");
            et_class.setText("");
            et_age.setText("");
        } else {
            StuBean bean = DBList.get(index);
            et_studentID.setText(bean.getStuId());
            et_name.setText(bean.getName());
            et_class.setText(bean.getClassName());
            et_age.setText(bean.getAge());
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && requestCode == 0) {
            isPermissions = true;
        } else {
            Toast("请开启读写权限");
            isPermissions = false;
        }
    }

    private String getEditText(EditText edit) {
        return edit.getText().toString().trim();
    }

    private void Toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


}
