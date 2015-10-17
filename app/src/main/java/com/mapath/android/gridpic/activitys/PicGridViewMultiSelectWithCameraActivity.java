package com.mapath.android.gridpic.activitys;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.mapath.android.gridpic.adapters.PicGridViewMultiSelectAdapter;
import com.mapath.android.gridpic.adapters.PicGridViewMultiSelectWithCameraAdapter;
import com.mapath.android.gridpic.adapters.PicGridViewWithCameraAdapter;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * Created by zhouxiaobo on 15/10/17.
 */
public class PicGridViewMultiSelectWithCameraActivity extends PicGridViewMultiSelectActivity {

    private static final int RESULT_MULTI_CAMERA_CODE = 6;
    private Uri tempUri; //拍照后存放照片的位置

    @Override
    protected void initPicGridViewAdapter(Activity content, List<String> mImgs, int layoutId, String imgDirPath){
        mAdapter = new PicGridViewMultiSelectWithCameraAdapter(content, mImgs, layoutId, imgDirPath);
        paths = ((PicGridViewMultiSelectWithCameraAdapter)mAdapter).mSelectedImage;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position == 0){

            if(paths != null && paths.size() >= MAX_SELECT_NUMBER){
                Toast.makeText(PicGridViewMultiSelectWithCameraActivity.this, "最多选择" + MAX_SELECT_NUMBER + "张图片。", Toast.LENGTH_SHORT).show();
                return;
            }

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
            startActivityForResult(intentFromCapture, RESULT_MULTI_CAMERA_CODE);
        }else{
            super.onItemClick(parent,view,position,id);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(RESULT_MULTI_CAMERA_CODE == requestCode && Activity.RESULT_OK == resultCode){
            if(paths != null && tempUri != null){
                paths.add(tempUri.getPath());
                titleRight.setVisibility(View.VISIBLE);
                titleRight.setText("确定" + "(" + paths.size() + ")");
            }
        }
    }
}
