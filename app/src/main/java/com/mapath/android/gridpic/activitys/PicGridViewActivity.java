package com.mapath.android.gridpic.activitys;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mapath.android.gridpic.R;
import com.mapath.android.gridpic.adapters.PicGridViewAdapter;
import com.mapath.android.gridpic.models.ImageFolder;
import com.mapath.android.gridpic.views.ListImageDirPopupWindowView;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class PicGridViewActivity extends Activity implements ListImageDirPopupWindowView.OnImageDirSelected, AdapterView.OnItemClickListener {

    private static final String TAG = "PicGridViewActivity";

    //用来记录手机里，选中的文件夹的位置
    private static final String DEFAULT_PATH = "picselectpath";

    private int mScreenHeight;
    private int mScreenWidth;

    private ProgressDialog mProgressDialog;
    private GridView mGirdView;
    private TextView mChooseDir;
    private TextView mImageCount;
    private RelativeLayout mBottomLy;

    private ListImageDirPopupWindowView mListImageDirPopupWindowView;

    //选中的图片文件夹，默认是图片数量最多的文件夹
    protected File mImgDir;

    //临时的辅助类，用于防止同一个文件夹的多次扫描
    private HashSet<String> mDirPaths = new HashSet<String>();

    //图片总数
    int totalCount = 0;

    //扫描拿到所有的图片文件夹
    private List<ImageFolder> mImageFloders = new ArrayList<ImageFolder>();

    private List<String> mImgs;

    protected PicGridViewAdapter mAdapter;

    //文件类型选择器
    private FilenameFilter picFilter = new FilenameFilter(){
        @Override
        public boolean accept(File dir, String filename) {
            if (filename.endsWith(".jpg") || filename.endsWith(".png") || filename.endsWith(".jpeg"))
                return true;
            return false;
        }
    };

    @Override
    public void selected(ImageFolder floder) {
        mImgDir = new File(floder.getDir());

        //持久化目录选择
        if(!getDefaultDirPath().equals(mImgDir.getAbsolutePath())){
            keepDefaultDirPath(mImgDir.getAbsolutePath());
        }

        mImgs = listImgPath();

        /**
         * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
         */
        initPicGridViewAdapter(this, mImgs, R.layout.item_picgrid, mImgDir.getAbsolutePath());

        mGirdView.setAdapter(mAdapter);
        mImageCount.setText(floder.getCount() + "张");

        mChooseDir.setText(floder.getName());
        mListImageDirPopupWindowView.dismiss();
    }

    protected void initPicGridViewAdapter(Activity content, List<String> mImgs, int layoutId, String imgDirPath){
        mAdapter = new PicGridViewAdapter(content, mImgs, layoutId, imgDirPath);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picgrid);

        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        mScreenHeight = outMetrics.heightPixels;
        mScreenWidth = outMetrics.widthPixels;

        initView();
        getImages();
        initEvent();
    }

    private void initView() {
        mGirdView = (GridView) findViewById(R.id.id_gridView);
        mGirdView.setOnItemClickListener(this);

        mChooseDir = (TextView) findViewById(R.id.id_choose_dir);
        mImageCount = (TextView) findViewById(R.id.id_total_count);

        mBottomLy = (RelativeLayout) findViewById(R.id.id_bottom_ly);
    }

    private void initEvent() {
        /**
         * 为底部的布局设置点击事件，弹出popupWindow
         */
        mBottomLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListImageDirPopupWindowView.setAnimationStyle(R.style.anim_popup_dir);
                mListImageDirPopupWindowView.showAsDropDown(mBottomLy, 0, 0);

                // 设置背景颜色变暗
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = .3f;
                getWindow().setAttributes(lp);
            }
        });
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            mProgressDialog.dismiss();

            // 为View绑定数据
            data2View();

            // 初始化展示文件夹的popupWindw
            initListDirPopupWindw();
        }
    };

    /**
     * 初始化展示文件夹的popupWindw
     */
    private void initListDirPopupWindw() {
        mListImageDirPopupWindowView = new ListImageDirPopupWindowView(ViewGroup.LayoutParams.MATCH_PARENT, (int) (mScreenHeight * 0.7),
                mImageFloders, LayoutInflater.from(getApplicationContext()).inflate(R.layout.popup_list_dir, null));

        mListImageDirPopupWindowView.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                // 设置背景颜色变暗
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });
        // 设置选择文件夹的回调
        mListImageDirPopupWindowView.setOnImageDirSelected(this);
    }

    //为View绑定数据
    private void data2View() {
        if (mImgDir == null) {
            Toast.makeText(getApplicationContext(), "擦，一张图片没扫描到", Toast.LENGTH_SHORT).show();
            return;
        }

        mImgs = listImgPath();

        initPicGridViewAdapter(this, mImgs, R.layout.item_picgrid, mImgDir.getAbsolutePath());
        mGirdView.setAdapter(mAdapter);

        mImageCount.setText(totalCount + "张");
    };

    private void getImages() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
            return;
        }
        // 显示进度条
        mProgressDialog = ProgressDialog.show(this, null, "正在加载...");

        new Thread(new Runnable() {
            @Override
            public void run() {

                String firstImage = null;

                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = PicGridViewActivity.this.getContentResolver();

                // 只查询jpeg和png的图片
                Cursor mCursor = mContentResolver.query(mImageUri, null, MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?", new String[] { "image/jpeg", "image/png" },
                        MediaStore.Images.Media.DATE_MODIFIED + " desc");

                Log.d(TAG, "共" + mCursor.getCount() + "张图片");

                while (mCursor.moveToNext()) {
                    // 获取图片的路径
                    String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));

                    // 获取图片的修改时间
                    long modifiedDt = mCursor.getLong(mCursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED));

                    // 拿到第一张图片的路径
                    if (firstImage == null){
                        firstImage = path;
                    }

                    // 获取该图片的父路径名
                    File parentFile = new File(path).getParentFile();
                    if (parentFile == null)
                        continue;
                    String dirPath = parentFile.getAbsolutePath();

                    ImageFolder imageFloder = null;

                    // 利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
                    if (mDirPaths.contains(dirPath)) {
                        continue;
                    } else {
                        mDirPaths.add(dirPath);
                        // 初始化imageFloder
                        imageFloder = new ImageFolder();
                        imageFloder.setDir(dirPath);
                        imageFloder.setFirstImagePath(path);
                    }

                    int picSize = parentFile.list(picFilter).length;

                    totalCount += picSize;

                    imageFloder.setCount(picSize);
                    mImageFloders.add(imageFloder);

                    int mPicsSize = 0;
                    if (picSize > mPicsSize) {
                        mPicsSize = picSize;
                        mImgDir = parentFile;

                    }

                    if(!"".equals(getDefaultDirPath())){
                        mImgDir = new File(getDefaultDirPath());
                    }

                }
                mCursor.close();

                // 扫描完成，辅助的HashSet也就可以释放内存了
                mDirPaths = null;

                // 通知Handler扫描图片完成
                mHandler.sendEmptyMessage(0x110);

            }
        }).start();
    }

    public int getmScreenWidth() {
        return mScreenWidth;
    }

    //获取系统里记录的文件夹地址
    private String getDefaultDirPath(){
        String result = "";
        SharedPreferences picSelectedPath = getSharedPreferences(DEFAULT_PATH, Context.MODE_PRIVATE);
        Map<String, String> pathMap = (Map<String, String>) picSelectedPath.getAll();
        for (Map.Entry<String, String> entry : pathMap.entrySet()){
            if("pic_path".equals(entry.getKey())){
                result = entry.getValue();
            }
        }
        return result;
    }

    private void keepDefaultDirPath(String path){
        SharedPreferences picSelectedPath = getSharedPreferences(DEFAULT_PATH, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = picSelectedPath.edit();
        editor.putString("pic_path",path);
        editor.commit();
    }

    private List<String> listImgPath(){
        List<String> result = new ArrayList<String>();
        if(sortImgFiles() == null){
            return result;
        }

        for(File f: sortImgFiles()){
            result.add(f.getName());
        }

        return result;
    }

    private List<File> sortImgFiles(){
        List<File> tempImgList = Arrays.asList(mImgDir.listFiles(picFilter));
        if(tempImgList == null){
            return null;
        }

        Collections.sort(tempImgList, new Comparator<File>() {

            @Override
            public int compare(File lhs, File rhs) {
                if (lhs.lastModified() < rhs.lastModified()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });

        return tempImgList;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

}
