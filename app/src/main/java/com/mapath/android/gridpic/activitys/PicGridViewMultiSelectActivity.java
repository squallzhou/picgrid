package com.mapath.android.gridpic.activitys;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.mapath.android.gridpic.R;
import com.mapath.android.gridpic.adapters.PicGridViewMultiSelectAdapter;
import com.mapath.android.gridpic.models.ImageFolder;

import java.util.List;

/**
 * Created by zhouxiaobo on 15/10/16.
 */
public class PicGridViewMultiSelectActivity extends PicGridViewActivity {

    @Override
    public void selected(ImageFolder floder) {
        super.selected(floder);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PicGridViewMultiSelectAdapter picGridViewAdapter = (PicGridViewMultiSelectAdapter)parent.getAdapter();
        String item = picGridViewAdapter.getItem(position);
        String path = mImgDir.getAbsolutePath() + "/" + item;

        ImageButton mSelected = (ImageButton)view.findViewById(R.id.id_item_select);
        ImageView mImg = (ImageView)view.findViewById(R.id.id_item_image);

        if(picGridViewAdapter.isContains(path)){
            picGridViewAdapter.removeSelected(path, mSelected, mImg);
        } else {
            picGridViewAdapter.addSelected(path, mSelected, mImg);
        }
    }

    @Override
    protected void initPicGridViewAdapter(Activity content, List<String> mImgs, int layoutId, String imgDirPath){
        mAdapter = new PicGridViewMultiSelectAdapter(content, mImgs, layoutId, imgDirPath);

        if(((PicGridViewMultiSelectAdapter)mAdapter).mSelectedImage!=null){
            ((PicGridViewMultiSelectAdapter)mAdapter).mSelectedImage.clear();
        }
    }


}