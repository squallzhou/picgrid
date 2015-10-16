package com.mapath.android.gridpic.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mapath.android.gridpic.R;
import com.mapath.android.gridpic.adapters.CommonAdapter;
import com.mapath.android.gridpic.utils.ViewHolder;

import java.util.Arrays;
import java.util.List;

/**
 * Created by zhouxiaobo on 15/10/16.
 */
public class MainActivity extends Activity implements AdapterView.OnItemClickListener{

    private ListView listView;

    private String[] titles = {
            "图片单选",
            "图片多选",
            "图片单选带摄像头" ,
            "图片多选带摄像头"
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = null;
        switch (position){
            case 0:
                intent = new Intent(this,PicGridViewActivity.class);
                startActivity(intent);
                break;
            case 1:
                intent = new Intent(this,PicGridViewMultiSelectActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.item_typeofGridPicSelect);

        BaseAdapter picSelectAdapter =  new CommonAdapter(this, Arrays.asList(titles), R.layout.item_main_select) {

            @Override
            public void convert(ViewHolder helper, Object item, int position) {
                TextView textView = helper.getView(R.id.select_title);
                textView.setText((String)mDatas.get(position));
            }
        };

        listView.setAdapter(picSelectAdapter);
        listView.setOnItemClickListener(this);
    }


}
