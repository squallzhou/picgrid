package com.mapath.android.gridpic.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;

import com.mapath.android.gridpic.PicGridViewActivity;
import com.mapath.android.gridpic.R;
import com.mapath.android.gridpic.utils.ViewHolder;

import java.util.LinkedList;
import java.util.List;

public class PicGridViewAdapter extends CommonAdapter<String> {

    private static final String TAG = "PicGridViewAdapter";

    private Activity context;

    /**
     * 用户选择的图片，存储为图片的完整路径
     */
    public static List<String> mSelectedImage = new LinkedList<String>();

    /**
     * 文件夹路径
     */
    private String mDirPath;

    public PicGridViewAdapter(Activity context, List<String> mDatas, int itemLayoutId, String dirPath) {
        super(context, mDatas, itemLayoutId);
        this.mDirPath = dirPath;
        this.context = context;
    }

    @Override
    public void convert(ViewHolder helper, final String item, final int position) {
        final ImageView mImageView = helper.getView(R.id.id_item_image);

        LayoutParams param = (LayoutParams) mImageView.getLayoutParams();
        param.width = (((PicGridViewActivity) context).getmScreenWidth() - 2) / 3;
        param.height = (((PicGridViewActivity) context).getmScreenWidth() - 2) / 3;
        mImageView.setLayoutParams(param);

        final ImageView mSelect = helper.getView(R.id.id_item_select);

        //设置图片
        helper.setImageResource(R.id.id_item_image, R.drawable.pictures_no); // 设置no_pic
        helper.setImageByUrl(R.id.id_item_image, mDirPath + "/" + item);// 设置图片
        mSelect.setVisibility(View.VISIBLE);

        // 设置no_selected
        helper.setImageResource(R.id.id_item_select, R.drawable.picture_unselected);
        mImageView.setColorFilter(null);

        /**
         * 已经选择过的图片，显示出选择过的效果
         */
        if (mSelectedImage.contains(mDirPath + "/" + item)) {
            mSelect.setImageResource(R.drawable.pictures_selected);
            mImageView.setColorFilter(Color.parseColor("#77000000"));
        }
    }

    private void handleSelectPic(String item,ImageView mSelect,ImageView mImageView){
        // 已经选择过该图片
        if (mSelectedImage.contains(mDirPath + "/" + item)) {
            mSelectedImage.remove(mDirPath + "/" + item);
            mSelect.setImageResource(R.drawable.picture_unselected);
            mImageView.setColorFilter(null);
        } else { // 未选择该图片
            mSelectedImage.add(mDirPath + "/" + item);
            mSelect.setImageResource(R.drawable.pictures_selected);
            mImageView.setColorFilter(Color.parseColor("#77000000"));
        }
    }

    public boolean isContains(String path) {
        return mSelectedImage.contains(path);
    }

    public void removeSelected(String path, ImageButton mSelected) {
        mSelectedImage.remove(path);
        mSelected.setImageResource(R.drawable.picture_unselected);
    }

    public void addSelected(String path, ImageButton mSelected) {
        mSelectedImage.add(path);
        mSelected.setImageResource(R.drawable.pictures_selected);
    }
}
