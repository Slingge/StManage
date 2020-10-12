package liuxiaohua.com;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //是否开启读写权限
    private boolean isPermissions = false;

    private OperationDatabaseUtil operationDatabaseUtil;

    private Button btn_ceateDatabase, btn_openDatabase;
    private Button btn_last, btn_next;
    private Button btn_add, btn_modify, btn_del, btn_close;

    private EditText et_id, et_name, et_class, et_age;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (PermissionUtil.ApplyPermission(this, 0)) {
            isPermissions = true;
            Toast("已获得权限");
        }

        init();

    }

    private void init() {
        btn_ceateDatabase = findViewById(R.id.btn_ceateDatabase);
        btn_ceateDatabase.setOnClickListener(this);
        btn_openDatabase = findViewById(R.id.btn_openDatabase);
        btn_openDatabase.setOnClickListener(this);
        btn_last = findViewById(R.id.btn_last);
        btn_last.setOnClickListener(this);
        btn_next = findViewById(R.id.btn_next);
        btn_next.setOnClickListener(this);

        btn_add = findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);
        btn_modify = findViewById(R.id.btn_modify);
        btn_modify.setOnClickListener(this);
        btn_del = findViewById(R.id.btn_del);
        btn_del.setOnClickListener(this);
        btn_close = findViewById(R.id.btn_close);
        btn_close.setOnClickListener(this);

        et_id = findViewById(R.id.et_id);
        et_name = findViewById(R.id.et_name);
        et_class = findViewById(R.id.et_class);
        et_age = findViewById(R.id.et_age);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ceateDatabase://创建数据库
                if (!isPermissions) {
                    Toast("请先开启读写权限");
                    return;
                }
                operationDatabaseUtil = new OperationDatabaseUtil(this);
                break;
            case R.id.btn_openDatabase://打开数据库

                break;
            case R.id.btn_last://上一条数据

                break;
            case R.id.btn_next://下一条数据

                break;
            case R.id.btn_add://添加一条数据

                break;
            case R.id.btn_modify://修改数据

                break;
            case R.id.btn_del://删除一条数据

                break;
            case R.id.btn_close://关闭

                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && requestCode == 0) {
            Toast("获得权限");
            isPermissions = true;
        } else {
            Toast("请开启读写权限");
            isPermissions = false;
        }
    }


    private void Toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


}
