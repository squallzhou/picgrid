package com.mapath.android.gridpic.adapters;

import android.app.Activity;

import com.mapath.android.gridpic.R;
import com.mapath.android.gridpic.utils.ViewHolder;

import java.util.List;

/**
 * Created by zhouxiaobo on 15/10/16.
 */
public class PicGridViewWithCameraAdapter extends PicGridViewAdapter {

    @Override
    public int getCount() {
        return mDatas.size() + 1;
    }

    @Override
    public String getItem(int position) {
        if(position == 0){
            return "";
        }else{
            return mDatas.get(position - 1);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public PicGridViewWithCameraAdapter(Activity context, List<String> mDatas, int itemLayoutId, String dirPath) {
        super(context, mDatas, itemLayoutId, dirPath);
    }

    @Override
    protected void setImage(ViewHolder helper,final String item, final int position){
        helper.setImageResource(R.id.id_item_image, R.drawable.pictures_no); // 设置no_pic
        if(position == 0){
            helper.setImageResource(R.id.id_item_image, R.drawable.photograph);
        }else{
            helper.setImageByUrl(R.id.id_item_image, mDirPath + "/" + item);// 设置图片
        }
    }
}
