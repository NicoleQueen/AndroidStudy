package com.nicolep.android.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

/**
 * SearchViewActivity.java文件
 * @鑫鱻
 */

public class MainActivity extends AppCompatActivity {

    private SearchView mSearchView = null;
    private ListView mListView = null;
    private String[] mDatas = {"aaa", "bbb", "something"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchView = (SearchView) findViewById(R.id.search_view);
        mListView =(ListView) findViewById(R.id.list_view);
        mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mDatas));
        mListView.setTextFilterEnabled(true);

        /**
         * 设置搜索文本监听
         */
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            //当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            //当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText){
                if (!TextUtils.isEmpty(newText)) {
                    mListView.setFilterText(newText);
                } else {
                    mListView.clearTextFilter();
                }
                return false;
            }
        });
    }
}
