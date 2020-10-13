package liuxiaohua.com;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OperationDatabaseUtil {


    private MyDBHelper mMyDBHelper;


    /**
     * dao类需要实例化数据库Help类,只有得到帮助类的对象我们才可以实例化 SQLiteDatabase
     *
     * @param context
     */
    public OperationDatabaseUtil(Context context) {
        mMyDBHelper = new MyDBHelper(context);
    }


    public boolean openDB() {
        SQLiteDatabase sqLiteDatabase = mMyDBHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select name from sqlite_master where type='table' order by name", null);
        Log.e("数据库名", mMyDBHelper.getDatabaseName());

        while (cursor.moveToNext()) {//获取所有表名
            //遍历出表名
            String name = cursor.getString(0);
            Log.e("表名", name);
        }
        if (!TextUtils.isEmpty(mMyDBHelper.getDatabaseName()) && cursor.getCount() > 1) {
            return true;
        } else {
            return false;
        }
    }


    // 将数据库打开帮帮助类实例化，然后利用这个对象
    // 调用谷歌的api去进行增删改查

    // 增加的方法吗，返回的的是一个long值
    public long addDate(StuBean bean) {
        // 增删改查每一个方法都要得到数据库，然后操作完成后一定要关闭
        // getWritableDatabase(); 执行后数据库文件才会生成
        // 数据库文件利用DDMS可以查看，在 data/data/包名/databases 目录下即可查看
        SQLiteDatabase sqLiteDatabase = mMyDBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(mMyDBHelper.STUID, bean.getStuId());
        contentValues.put(mMyDBHelper.NAME, bean.getName());
        contentValues.put(mMyDBHelper.CLASSNAME, bean.getClassName());
        contentValues.put(mMyDBHelper.AGE, bean.getAge());
        // 返回,显示数据添加在第几行
        // 加了现在连续添加了3行数据,突然删掉第三行,然后再添加一条数据返回的是4不是3
        // 因为自增长
        long rowid = sqLiteDatabase.insert(mMyDBHelper.tableName, null, contentValues);
        sqLiteDatabase.close();
        return rowid;
    }


    // 删除的方法，返回值是int
    public int deleteDate(long id) {
        SQLiteDatabase sqLiteDatabase = mMyDBHelper.getWritableDatabase();
        int deleteResult = sqLiteDatabase.delete(mMyDBHelper.tableName, mMyDBHelper.ID + "=?", new String[]{id + ""});
        sqLiteDatabase.close();
        return deleteResult;
    }

    /**
     * 修改的方法
     */
    public int updateData(long id, StuBean bean) {
        SQLiteDatabase sqLiteDatabase = mMyDBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(mMyDBHelper.STUID, bean.getStuId());
        contentValues.put(mMyDBHelper.NAME, bean.getName());
        contentValues.put(mMyDBHelper.CLASSNAME, bean.getClassName());
        contentValues.put(mMyDBHelper.AGE, bean.getAge());
        int updateResult = sqLiteDatabase.update(mMyDBHelper.tableName, contentValues, mMyDBHelper.ID + "=?", new String[]{id + ""});
        sqLiteDatabase.close();
        Log.e("修改数据", updateResult + "");
        return updateResult;
    }

    /**
     * 查询的方法
     *
     * @return
     */
    public List<StuBean> alterDate() {
        SQLiteDatabase readableDatabase = mMyDBHelper.getReadableDatabase();
        // 查询比较特别,涉及到 cursor
        Cursor cursor = readableDatabase.query(mMyDBHelper.tableName, null, null,
                null, null, null, null, null);
        List<StuBean> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            StuBean bean = new StuBean();
            bean.setId(cursor.getLong(cursor.getColumnIndex(mMyDBHelper.ID)));
            bean.setStuId(cursor.getString(cursor.getColumnIndex(mMyDBHelper.STUID)));
            bean.setName(cursor.getString(cursor.getColumnIndex(mMyDBHelper.NAME)));
            bean.setClassName(cursor.getString(cursor.getColumnIndex(mMyDBHelper.CLASSNAME)));
            bean.setAge(cursor.getString(cursor.getColumnIndex(mMyDBHelper.AGE)));
            list.add(bean);
        }
        cursor.close(); // 记得关闭 corsor
        readableDatabase.close(); // 关闭数据库
        return list;
    }


}
