package com.tyh.user_defined_view_demo;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.tyh.view.CircleImageView;

import java.io.File;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private CircleImageView mMainHeadimg;
    private Button mMainSetHeadImg;

    private TextView mHeadsetTackphoto;
    private TextView mHeadsetChoosephoto;

    private int TackPhoto_Request_Code = 10;
    private int ChoosePhoto_Request_Code = 20;

    private AlertDialog mAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        //设置默认头像
        mMainHeadimg.setImageResources(R.mipmap.header);
    }

    private void initView() {
        mMainHeadimg = (CircleImageView) findViewById(R.id.main_headimg);
        mMainSetHeadImg = (Button) findViewById(R.id.main_setHeadImg);

        mMainSetHeadImg.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_setHeadImg://设置头像
                showDialog();
                break;
            case R.id.headset_tackphoto://点击拍照
                mAlertDialog.dismiss();
                tackPhoto();
                break;
            case R.id.headset_choosephoto://点击图库
                mAlertDialog.dismiss();
                choosePhoto();
                break;
        }
    }

    /**
     * 拍照选择头像
     */
    private void tackPhoto() {
        //跳转到照相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //设置图片保存的位置
        Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "header.jpg"));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, TackPhoto_Request_Code);
    }

    /**
     * 从图库中选中头像
     */
    private void choosePhoto() {
        //设置跳转到图库
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //设置数据格式
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        //设置待返回值的跳转
        startActivityForResult(intent, ChoosePhoto_Request_Code);
    }

    /**
     * 显示设置头像的对话框
     */
    private void showDialog() {
        //创建对话框实例
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        mAlertDialog = builder.create();
        //获取view
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_headset, null);
        mHeadsetTackphoto = (TextView) view.findViewById(R.id.headset_tackphoto);
        mHeadsetChoosephoto = (TextView) view.findViewById(R.id.headset_choosephoto);
        mHeadsetTackphoto.setOnClickListener(this);
        mHeadsetChoosephoto.setOnClickListener(this);

        //设置对话框内容
        mAlertDialog.setView(view);

        //设置对话框点击外围可以取消
        mAlertDialog.setCanceledOnTouchOutside(true);
        //显示
        mAlertDialog.show();
        //设置window的宽和高 ------>在show之后设置才有效果
        mAlertDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //直接返回
        if (resultCode == 0) {
            return;
        }

        //拍照
        if (requestCode == TackPhoto_Request_Code) {
            File file = new File(Environment.getExternalStorageDirectory(), "header.jpg");
            Log.i("TAGName", "file----->" + file.getAbsolutePath());
            mMainHeadimg.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
            return;
        }

        //图库
        if (requestCode == ChoosePhoto_Request_Code) {
            if (data == null) {
                return;
            }

            Uri uri = data.getData();
            Log.i("TAGName", "uri----->" + uri);
            //通过uri查询图片真实路径
            ContentResolver contentResolver = this.getContentResolver();
            Cursor cursor = contentResolver.query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToNext()) {
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                Log.i("TAGName", "path----->" + path);
                mMainHeadimg.setImageBitmap(BitmapFactory.decodeFile(path));
                cursor.close();
            }
        }
    }
}
