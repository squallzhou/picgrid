package com.mapath.android.gridpic.adapters;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;

import com.mapath.android.gridpic.activitys.PicGridViewActivity;
import com.mapath.android.gridpic.R;
import com.mapath.android.gridpic.utils.ViewHolder;

import java.util.List;

public class PicGridViewAdapter extends CommonAdapter<String> {

    private static final String TAG = "PicGridViewAdapter";

    protected Activity context;

    /**
     * 文件夹路径
     */
    protected String mDirPath;

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

        //设置图片
        helper.setImageResource(R.id.id_item_image, R.drawable.pictures_no); // 设置no_pic
        helper.setImageByUrl(R.id.id_item_image, mDirPath + "/" + item);// 设置图片
    }

}
