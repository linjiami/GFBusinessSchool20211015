package com.gfbusinessschool.cityselect;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.gfbusinessschool.R;
import com.gfbusinessschool.activity.BaseActivity;
import com.gfbusinessschool.databinding.ActivityCityselectBinding;
import com.gfbusinessschool.utils.ARouterPath;
import com.gfbusinessschool.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Route(path = ARouterPath.ACTIVITY_URL_CitySelect)
public class CitySelectActivity extends BaseActivity<ActivityCityselectBinding> implements AbsListView.OnScrollListener {
    public static final String KEY_CITY_SELECTED="选中的城市";
    //文件名称
    private final static String CityFileName = "allcity.json";
    private Handler handler;
    private TextView overlay; // 对话框首字母TextView
    private OverlayThread overlayThread; // 显示首字母对话框
    private boolean mReady = false;
    private boolean isScroll = false;

    private HashMap<String, Integer> alphaIndexer;// 存放存在的汉语拼音首字母和与之对应的列表位置

    protected List<CityEntity> hotCityList = new ArrayList<>();
    protected List<CityEntity> totalCityList = new ArrayList<>();
    protected List<CityEntity> curCityList = new ArrayList<>();
    protected List<CityEntity> searchCityList = new ArrayList<>();
    protected CityListAdapter cityListAdapter;
    protected SearchCityListAdapter searchCityListAdapter;
    @Autowired
    String locationCity;
    private String curSelCity;

    @Override
    protected void initView() {
        super.initView();
        // 默认软键盘不弹出
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        if (Utils.isEmpty(locationCity)){
            locationCity =getIntent().getStringExtra(KEY_CITY_SELECTED);
        }
        if (Utils.isEmpty(locationCity)){
            locationCity ="北京";
        }
        curSelCity = locationCity;
        handler = new Handler();
        overlayThread = new OverlayThread();
        searchCityListAdapter = new SearchCityListAdapter(this, searchCityList);
        viewBinding.searchCityLv.setAdapter(searchCityListAdapter);
        viewBinding. searchCityLv.setOnItemClickListener((parent, view, position, id) -> {
            CityEntity cityEntity = searchCityList.get(position);
            curSelCity =cityEntity.getName();
            setResultCallbackData();
        });
        viewBinding.searchLocateContentEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = viewBinding.searchLocateContentEt.getText().toString().trim().toLowerCase();
                setSearchCityList(content);
            }
        });

        viewBinding.searchLocateContentEt.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideSoftInput(viewBinding.searchLocateContentEt.getWindowToken());
                String content = viewBinding.searchLocateContentEt.getText().toString().trim().toLowerCase();
                setSearchCityList(content);
                return true;
            }
            return false;
        });
    }

    @Override
    protected void initData() {
        super.initData();
        initTotalCityList();
        cityListAdapter = new CityListAdapter(this, totalCityList, hotCityList);
        viewBinding.totalCityLv.setAdapter(cityListAdapter);
        viewBinding.totalCityLv.setOnScrollListener(this);
        viewBinding.totalCityLv.setOnItemClickListener((parent, view, position, id) -> {
            if (position > 1) {
                CityEntity cityEntity = totalCityList.get(position);
                curSelCity =cityEntity.getName();
                setResultCallbackData();
            }

        });
        viewBinding.totalCityLettersLv.setOnTouchingLetterChangedListener(new LetterListViewListener());
        initOverlay();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            setResultCallbackData();
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 设置搜索数据展示
     */
    private void setSearchCityList(String content) {
        searchCityList.clear();
        if (TextUtils.isEmpty(content)) {
            viewBinding.totalCityLv.setVisibility(View.VISIBLE);
            viewBinding.totalCityLettersLv.setVisibility(View.VISIBLE);
            viewBinding.searchCityLv.setVisibility(View.GONE);
            viewBinding.noSearchResultTv.setVisibility(View.GONE);
        } else {
            viewBinding.totalCityLv.setVisibility(View.GONE);
            viewBinding.totalCityLettersLv.setVisibility(View.GONE);
            for (int i = 0; i < curCityList.size(); i++) {
                CityEntity cityEntity = curCityList.get(i);
                if (cityEntity.getName().contains(content) || cityEntity.getPinyin().contains(content)
                        || cityEntity.getFirst().contains(content)) {
                    searchCityList.add(cityEntity);
                }
            }

            if (searchCityList.size() != 0) {
                viewBinding.noSearchResultTv.setVisibility(View.GONE);
                viewBinding.searchCityLv.setVisibility(View.VISIBLE);
            } else {
                viewBinding.noSearchResultTv.setVisibility(View.VISIBLE);
                viewBinding.searchCityLv.setVisibility(View.GONE);
            }

            searchCityListAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 初始化全部城市列表
     */
    public void initTotalCityList() {
        hotCityList.clear();
        totalCityList.clear();
        curCityList.clear();

        String cityListJson = JsonReadUtil.getJsonStr(this, CityFileName);
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(cityListJson);
            JSONArray array = jsonObject.getJSONArray("City");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                String name = object.getString("name");
                String key = object.getString("key");
                String pinyin = object.getString("full");
                String first = object.getString("first");
                String cityCode = object.getString("code");

                CityEntity cityEntity = new CityEntity();
                cityEntity.setName(name);
                cityEntity.setKey(key);
                cityEntity.setPinyin(pinyin);
                cityEntity.setFirst(first);
                cityEntity.setCityCode(cityCode);

                if (key.equals("热门")) {
                    hotCityList.add(cityEntity);
                } else {
                    if (!cityEntity.getKey().equals("0") && !cityEntity.getKey().equals("1")) {
                        curCityList.add(cityEntity);
                    }
                    totalCityList.add(cityEntity);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        isScroll = scrollState == SCROLL_STATE_TOUCH_SCROLL
                || scrollState == SCROLL_STATE_FLING;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        if (!isScroll) {
            return;
        }

        if (mReady) {
            String key = getAlpha(totalCityList.get(firstVisibleItem).getKey());
            overlay.setText(key);
            overlay.setVisibility(View.VISIBLE);
            handler.removeCallbacks(overlayThread);
            // 延迟让overlay为不可见
            handler.postDelayed(overlayThread, 700);
        }
    }

    /**
     * 总城市适配器
     */
    private class CityListAdapter extends BaseAdapter {
        private Context context;

        private List<CityEntity> totalCityList;
        private List<CityEntity> hotCityList;
        private LayoutInflater inflater;
        final int VIEW_TYPE = 3;

        CityListAdapter(Context context,
                        List<CityEntity> totalCityList,
                        List<CityEntity> hotCityList) {
            this.context = context;
            this.totalCityList = totalCityList;
            this.hotCityList = hotCityList;
            inflater = LayoutInflater.from(context);

            alphaIndexer = new HashMap<>();

            for (int i = 0; i < totalCityList.size(); i++) {
                // 当前汉语拼音首字母
                String currentStr = totalCityList.get(i).getKey();

                String previewStr = (i - 1) >= 0 ? totalCityList.get(i - 1).getKey() : " ";
                if (!previewStr.equals(currentStr)) {
                    String name = getAlpha(currentStr);
                    alphaIndexer.put(name, i);
                }
            }
        }

        @Override
        public int getViewTypeCount() {
            return VIEW_TYPE;
        }

        @Override
        public int getItemViewType(int position) {
            return position < 2 ? position : 2;
        }

        @Override
        public int getCount() {
            return totalCityList == null ? 0 : totalCityList.size();
        }

        @Override
        public Object getItem(int position) {
            return totalCityList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final TextView curCityNameTv;
            ViewHolder holder;
            int viewType = getItemViewType(position);
            if (viewType == 0) { // 定位
                convertView = inflater.inflate(R.layout.select_city_location_item, null);

                LinearLayout noLocationLl = convertView.findViewById(R.id.cur_city_no_data_ll);
                TextView getLocationTv = convertView.findViewById(R.id.cur_city_re_get_location_tv);
                curCityNameTv = convertView.findViewById(R.id.cur_city_name_tv);

                if (TextUtils.isEmpty(locationCity)) {
                    noLocationLl.setVisibility(View.VISIBLE);
                    curCityNameTv.setVisibility(View.GONE);
                    getLocationTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //获取定位

                        }
                    });
                } else {
                    noLocationLl.setVisibility(View.GONE);
                    curCityNameTv.setVisibility(View.VISIBLE);

                    curCityNameTv.setText(locationCity);
                    curCityNameTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!locationCity.equals(curSelCity)) {
                                //设置城市代码
                                String cityCode = "";
                                for (CityEntity cityEntity : curCityList) {
                                    if (cityEntity.getName().equals(locationCity)) {
                                        cityCode = cityEntity.getCityCode();
                                        break;
                                    }
                                }
                                curSelCity =locationCity;
                                setResultCallbackData();
                            } else {
                                setResultCallbackData();
                            }
                        }
                    });
                }
            } else if (viewType == 1) { //热门城市
                convertView = inflater.inflate(R.layout.recent_city_item, null);
                GridView hotCityGv = convertView.findViewById(R.id.recent_city_gv);
                hotCityGv.setAdapter(new HotCityListAdapter(context, this.hotCityList));
                hotCityGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        CityEntity cityEntity = hotCityList.get(position);
                        curSelCity =cityEntity.getName();
                        setResultCallbackData();
                    }
                });
            } else {
                if (null == convertView) {
                    holder = new ViewHolder();
                    convertView = inflater.inflate(R.layout.city_list_item_layout, null);
                    ViewBinder.bind(holder, convertView);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                CityEntity cityEntity = totalCityList.get(position);
                holder.cityKeyTv.setVisibility(View.VISIBLE);
                holder.cityKeyTv.setText(getAlpha(cityEntity.getKey()));
                holder.cityNameTv.setText(cityEntity.getName());

                if (position >= 1) {
                    CityEntity preCity = totalCityList.get(position - 1);
                    if (preCity.getKey().equals(cityEntity.getKey())) {
                        holder.cityKeyTv.setVisibility(View.GONE);
                    } else {
                        holder.cityKeyTv.setVisibility(View.VISIBLE);
                    }
                }
            }

            return convertView;
        }

        private class ViewHolder {
            TextView cityNameTv =findViewById(R.id.city_name_tv);
            TextView cityKeyTv =findViewById(R.id.city_key_tv);
        }
    }

    /**
     * 热门城市适配器
     */
    private class HotCityListAdapter extends BaseAdapter {

        private List<CityEntity> cityEntities;
        private LayoutInflater inflater;

        HotCityListAdapter(Context mContext, List<CityEntity> cityEntities) {
            this.cityEntities = cityEntities;
            inflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            return cityEntities == null ? 0 : cityEntities.size();
        }

        @Override
        public Object getItem(int position) {
            return cityEntities.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (null == convertView) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.city_list_grid_item_layout, null);
                ViewBinder.bind(holder, convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            CityEntity cityEntity = cityEntities.get(position);
            holder.cityNameTv.setText(cityEntity.getName());

            return convertView;
        }

        private class ViewHolder {
            @Bind(R.id.city_list_grid_item_name_tv)
            TextView cityNameTv;
        }
    }

    /**
     * 搜索城市列表适配器
     */
    private class SearchCityListAdapter extends BaseAdapter {

        private List<CityEntity> cityEntities;
        private LayoutInflater inflater;

        SearchCityListAdapter(Context mContext, List<CityEntity> cityEntities) {
            this.cityEntities = cityEntities;
            inflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            return cityEntities == null ? 0 : cityEntities.size();
        }

        @Override
        public Object getItem(int position) {
            return cityEntities.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (null == convertView) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.city_list_item_layout, null);
                ViewBinder.bind(holder, convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            CityEntity cityEntity = cityEntities.get(position);
            holder.cityKeyTv.setVisibility(View.GONE);
            holder.cityNameTv.setText(cityEntity.getName());

            return convertView;
        }


        private class ViewHolder {
            @Bind(R.id.city_name_tv)
            TextView cityNameTv;
            @Bind(R.id.city_key_tv)
            TextView cityKeyTv;
        }
    }

    /**
     * 获得首字母
     */
    private String getAlpha(String key) {
        if (key.equals("0")) {
            return "定位";
        } else if (key.equals("1")) {
            return "热门";
        } else {
            return key;
        }
    }


    /**
     * 初始化汉语拼音首字母弹出提示框
     */
    private void initOverlay() {
        mReady = true;
        LayoutInflater inflater = LayoutInflater.from(this);
        overlay = (TextView) inflater.inflate(R.layout.overlay, null);
        overlay.setVisibility(View.INVISIBLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);
        WindowManager windowManager = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(overlay, lp);
    }


    private class LetterListViewListener implements
            LetterListView.OnTouchingLetterChangedListener {

        @Override
        public void onTouchingLetterChanged(final String s) {
            isScroll = false;
            if (alphaIndexer.get(s) != null) {
                int position = alphaIndexer.get(s);
                viewBinding.totalCityLv.setSelection(position);
                overlay.setText(s);
                overlay.setVisibility(View.VISIBLE);
                handler.removeCallbacks(overlayThread);
                // 延迟让overlay为不可见
                handler.postDelayed(overlayThread, 700);
            }
        }
    }

    /**
     * 设置overlay不可见
     */
    private class OverlayThread implements Runnable {
        @Override
        public void run() {
            overlay.setVisibility(View.GONE);
        }
    }

    /**
     * 隐藏软件盘
     */
    public void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void setResultCallbackData(){
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_CITY_SELECTED, curSelCity);
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);
        finish();
    }
}
