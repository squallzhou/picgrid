package com.mapath.android.gridpic.activitys;

import android.app.Activity;
import android.os.Bundle;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mapath.android.gridpic.R;
import com.mapath.android.gridpic.adapters.CommonAdapter;
import com.mapath.android.gridpic.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouxiaobo on 15/10/16.
 */
public class ResultActivity extends Activity {

    private ListView listView;

    private List<String> pathList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectresult);

        listView = (ListView)findViewById(R.id.item_resultSelect);

        String path = getIntent().getStringExtra(PicGridViewActivity.RESULT_KEY);  //单选
        if(path!=null){
            pathList.add(path);
        }

        String[] paths = getIntent().getStringArrayExtra(PicGridViewMultiSelectActivity.RESULT_ARR_KEY);  //多选
        if(paths != null && paths.length > 0){
            for(String p :paths){
                pathList.add(p);
            }
        }

        if(pathList == null || pathList.size() == 0){
            return;
        }

        BaseAdapter picSelectAdapter =  new CommonAdapter(this, pathList, R.layout.item_selectresult) {

            @Override
            public void convert(ViewHolder helper, Object item, int position) {
                TextView textView = helper.getView(R.id.txt_path);
                textView.setText((String)mDatas.get(position));
                helper.setImageByUrl(R.id.item_img, (String)mDatas.get(position));// 设置图片
            }
        };

        listView.setAdapter(picSelectAdapter);
    }
}
