package com.gfbusinessschool.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewbinding.ViewBinding;
import com.alibaba.fastjson.JSONArray;
import com.gfbusinessschool.R;
import com.gfbusinessschool.databinding.ActivitySearchBaseBinding;
import com.gfbusinessschool.utils.ToastUtil;
import com.gfbusinessschool.utils.Utils;
import java.util.ArrayList;

public class SearchBaseActivity<T extends ViewBinding> extends BaseActivity<ActivitySearchBaseBinding> implements SwipeRefreshLayout.OnRefreshListener ,
        View.OnClickListener {
    protected final String NAME_SAVE_DATA = "历史搜索";//SharedPreferences保存数据的名称
    protected final String TYPE_COURSE_SEARCH = "课程搜索";
    protected ArrayList<String> listHistory = new ArrayList<>();
    protected String typeView;
    protected int currentPage;

    public String getTypeView() {
        return typeView;
    }

    @Override
    protected void initView() {
        super.initView();
        viewBinding.tvCancle.setTextColor(Utils.getThemeColor(getApplicationContext()));
        showKeyBoard(viewBinding.etSearch);//默认弹出键盘
        viewBinding.swipeRefreshLayout.setOnRefreshListener(this);
        viewBinding.etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if ((actionId == EditorInfo.IME_ACTION_UNSPECIFIED || actionId == EditorInfo.IME_ACTION_SEARCH) && event != null) {
                String contentSearch = viewBinding.etSearch.getText().toString().trim();
                if (!Utils.isEmpty(contentSearch)) {
                    onClickKeywordSearch();
                    loadResult(false, contentSearch);
                    currentPage =1;
                    requestResultData(contentSearch,1);
                    hideKeyBoard();
                }else {
                    ToastUtil.show(getApplicationContext(),"请输入搜索内容");
                }
            }
            return false;
        });
        viewBinding.hotSearchRV.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
        viewBinding.historySearchRV.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
        viewBinding.recruitRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false));
        listHistory = getHistorySearch(getTypeView());

        viewBinding.tvCancle.setOnClickListener(this);
        viewBinding.deleteBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCancle:
                Utils.hideSoftInputFromWindow(SearchBaseActivity.this, viewBinding.etSearch);
                finish();
                break;
            case R.id.deleteBtn:
                listHistory.clear();
                saveHistorySearch(listHistory);
                deleteHistory();
                break;
        }
    }

    @Override
    public void onRefresh() {

    }

    /**
     * 点击了键盘搜索
     */
    protected  void onClickKeywordSearch(){

    }

    /**
     * 显示搜索结果
     *
     * @param isClickHistorySearch 是否点击的历史搜索（不做重复数据的存储）
     * @param contentSearch        搜索内容
     */
    protected void loadResult(boolean isClickHistorySearch, String contentSearch) {

    }

    protected void requestResultData(String searchContent,int page) {
    }

    /**
     * 删除所有历史记录
     */
    protected void deleteHistory() {

    }

    protected ArrayList<String> getHistorySearch(String type) {
        ArrayList<String> list = new ArrayList<>();
        try {
            SharedPreferences prefs = getSharedPreferences(NAME_SAVE_DATA, Context.MODE_PRIVATE);
            String jsonHistory = prefs.getString(type, "");
            JSONArray array = JSONArray.parseArray(jsonHistory);
            if (array != null && array.size() != 0) {
                for (int i = 0; i < array.size(); i++) {
                    list.add(array.get(i).toString());
                }
            }
        } catch (Exception e) {
            Utils.log(Utils.TAG_ERROR, " Exception=" + e.getMessage());
        } finally {
            return list;
        }
    }

    protected void saveHistorySearch(ArrayList<String> list) {
        //限制最多显示15条
        if (list.size() > 15) {
            for (int i = list.size() - 1; i > 14; i--) {
                list.remove(i);
            }
        }
        SharedPreferences prefs = getSharedPreferences(NAME_SAVE_DATA, Context.MODE_PRIVATE);
        JSONArray jsonArray = new JSONArray();
        for (String b : list) {
            jsonArray.add(b);
        }
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(getTypeView(), jsonArray.toString());
        editor.commit();
    }
}