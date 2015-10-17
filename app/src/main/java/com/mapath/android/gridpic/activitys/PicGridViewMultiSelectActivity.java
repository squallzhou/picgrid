package com.mapath.android.gridpic.activitys;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mapath.android.gridpic.R;
import com.mapath.android.gridpic.adapters.PicGridViewMultiSelectAdapter;

import java.util.List;

/**
 * Created by zhouxiaobo on 15/10/16.
 */
public class PicGridViewMultiSelectActivity extends PicGridViewActivity {

    public static final String RESULT_ARR_KEY = "keepResultArrPath";

    protected static final int MAX_SELECT_NUMBER = 10;
    protected List<String> paths; //选中的图片的地址集合

    @Override
    protected void initView() {
        super.initView();
        titleRight = (TextView)findViewById(R.id.tv_title_right);
        titleRight.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(paths!=null && paths.size()>0){
                    Intent intent = new Intent(PicGridViewMultiSelectActivity.this,ResultActivity.class);

                    String[] pathArr = new String[paths.size()];
                    intent.putExtra(RESULT_ARR_KEY, paths.toArray(pathArr));

                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PicGridViewMultiSelectAdapter picGridViewAdapter = (PicGridViewMultiSelectAdapter)parent.getAdapter();

        String item = picGridViewAdapter.getItem(position);
        String path = mImgDir.getAbsolutePath() + "/" + item;

        ImageButton mSelected = (ImageButton)view.findViewById(R.id.id_item_select);
        ImageView mImg = (ImageView)view.findViewById(R.id.id_item_image);

        if(picGridViewAdapter.isContains(path)){
            picGridViewAdapter.removeSelected(path, mSelected, mImg, titleRight);
        } else {
            if(paths != null && paths.size() >= MAX_SELECT_NUMBER){
                Toast.makeText(PicGridViewMultiSelectActivity.this,"最多选择" + MAX_SELECT_NUMBER + "张图片。",Toast.LENGTH_SHORT).show();
                return;
            }else{
                picGridViewAdapter.addSelected(path, mSelected, mImg, titleRight);
            }
        }
    }

    @Override
    protected void initPicGridViewAdapter(Activity content, List<String> mImgs, int layoutId, String imgDirPath){
        mAdapter = new PicGridViewMultiSelectAdapter(content, mImgs, layoutId, imgDirPath);
        paths = ((PicGridViewMultiSelectAdapter)mAdapter).mSelectedImage;

        if(paths != null){
            paths.clear();
        }
    }


}
