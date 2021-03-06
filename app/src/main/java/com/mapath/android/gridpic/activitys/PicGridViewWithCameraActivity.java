package com.mapath.android.gridpic.activitys;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;

import com.mapath.android.gridpic.adapters.PicGridViewWithCameraAdapter;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * Created by zhouxiaobo on 15/10/16.
 */
public class PicGridViewWithCameraActivity extends PicGridViewActivity {

    private static final int RESULT_CAMERA_CODE = 3;
    private Uri tempUri; //拍照后存放照片的位置

    @Override
    protected void initPicGridViewAdapter(Activity content, List<String> mImgs, int layoutId, String imgDirPath){
        mAdapter = new PicGridViewWithCameraAdapter(content, mImgs, layoutId, imgDirPath);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position == 0){
            Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if (checkSDCard()) {
                String tempFolder = Environment.getExternalStorageDirectory() + "/temp";

                File tempFilePath = new File(tempFolder);
                if(!tempFilePath.exists()){
                    tempFilePath.mkdirs();
                }

                tempUri = Uri.fromFile(new File(tempFolder, "demo_" + new Date().getTime() + ".jpg"));
                intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
            }
            startActivityForResult(intentFromCapture, RESULT_CAMERA_CODE);

        }else{
            super.onItemClick(parent,view,position,id);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(RESULT_CAMERA_CODE == requestCode && Activity.RESULT_OK == resultCode){
            Intent intent = new Intent(PicGridViewWithCameraActivity.this,ResultActivity.class);
            intent.putExtra(RESULT_KEY,tempUri.getPath());
            startActivity(intent);
        }
    }
}
