package com.mapath.android.gridpic.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mapath.android.gridpic.R;
import com.mapath.android.gridpic.utils.ViewHolder;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by zhouxiaobo on 15/10/16.
 */
public class PicGridViewMultiSelectAdapter extends PicGridViewAdapter {

    private static final String TAG = "PicGridViewMultiSelectAdapter";

    /**
     * 用户选择的图片，存储为图片的完整路径
     */
    public static List<String> mSelectedImage = new LinkedList<String>();

    public PicGridViewMultiSelectAdapter(Activity context, List<String> mDatas, int itemLayoutId, String dirPath) {
        super(context, mDatas, itemLayoutId,dirPath);
    }

    @Override
    public void convert(ViewHolder helper, final String item, final int position) {
        super.convert(helper, item, position);

        final ImageView mImageView = helper.getView(R.id.id_item_image);
        final ImageView mSelect = helper.getView(R.id.id_item_select);

        // 设置no_selected
        helper.setImageResource(R.id.id_item_select, R.drawable.picture_unselected);
        mImageView.setColorFilter(null);

        handleSelectFlag(helper,position);

        /**
         * 已经选择过的图片，Ø显示出选择过的效果
         */
        if (mSelectedImage.contains(mDirPath + "/" + item)) {
            mSelect.setImageResource(R.drawable.pictures_selected);
            mImageView.setColorFilter(Color.parseColor("#77000000"));
        }
    }

    protected void handleSelectFlag(ViewHolder helper,int position){
        final ImageView mSelect = helper.getView(R.id.id_item_select);
        mSelect.setVisibility(View.VISIBLE);
    }

    public boolean isContains(String path) {
        return mSelectedImage.contains(path);
    }

    public void removeSelected(String path, ImageButton mSelected, ImageView mImageView, TextView titleRight) {
        mSelectedImage.remove(path);
        mSelected.setImageResource(R.drawable.picture_unselected);
        mImageView.setColorFilter(null);

        handTitleRight(titleRight);

    }

    public void addSelected(String path, ImageButton mSelected, ImageView mImageView, TextView titleRight) {
        mSelectedImage.add(path);
        mSelected.setImageResource(R.drawable.pictures_selected);
        mImageView.setColorFilter(Color.parseColor("#77000000"));

        handTitleRight(titleRight);
    }

    protected void handTitleRight(TextView titleRight){
        if(mSelectedImage.size()!=0){
            titleRight.setVisibility(View.VISIBLE);
            titleRight.setText("");
            titleRight.setText("确定" + "(" + mSelectedImage.size() + ")");
        }else{
            titleRight.setVisibility(View.GONE);
        }
    }
}
