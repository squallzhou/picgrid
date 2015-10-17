package com.mapath.android.gridpic.activitys;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;

import com.mapath.android.gridpic.adapters.PicGridViewAdapter;

import java.io.File;

/**
 * Created by zhouxiaobo on 15/10/16.
 */
public class PicGridViewCropActivity extends PicGridViewActivity {

    protected static final int RESULT_REQUEST_CODE = 2;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PicGridViewAdapter picGridViewAdapter = (PicGridViewAdapter)parent.getAdapter();
        String item = picGridViewAdapter.getItem(position);
        String path = mImgDir.getAbsolutePath() + "/" + item;
        startPhotoZoom(Uri.fromFile(new File(path)),PicGridViewCropActivity.RESULT_REQUEST_CODE);
    }

    protected void startPhotoZoom(Uri uri, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        // 设置裁剪
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(RESULT_REQUEST_CODE == requestCode){
            if (data != null) {
                setResult(Activity.RESULT_OK, data);
                finish();
            }
        }
    }
}
