package com.gfbusinessschool.activity;

import android.content.Intent;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gfbusinessschool.InterfaceUtils.MyDialogCallback;
import com.gfbusinessschool.InterfaceUtils.NetWorkCallback;
import com.gfbusinessschool.InterfaceUtils.OnClickCallBack;
import com.gfbusinessschool.R;
import com.gfbusinessschool.adapter.TestAdapter;
import com.gfbusinessschool.bean.CourseBean;
import com.gfbusinessschool.bean.TestAnswerEntity;
import com.gfbusinessschool.bean.TestAnswerResultEntity;
import com.gfbusinessschool.bean.TestBean;
import com.gfbusinessschool.bean.TestQuestionsBean;
import com.gfbusinessschool.databinding.ActivityTestBinding;
import com.gfbusinessschool.dialog.MyAlertDialog;
import com.gfbusinessschool.utils.ARouterPath;
import com.gfbusinessschool.utils.InterfaceClass;
import com.gfbusinessschool.utils.NetWorkUtils;
import com.gfbusinessschool.utils.ToastUtil;
import com.gfbusinessschool.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(path = ARouterPath.ACTIVITY_Test)
public class TestActivity extends BaseActivity<ActivityTestBinding> {
    @Autowired
    boolean isQuestionResearch;//是否是问卷调研
    @Autowired
    int testPoistion;
    @Autowired
    String testId;
    @Autowired
    String testName;
    @Autowired
    String courseType;//课程类型（1必修2精选）
    @Autowired
    String testType;//考试类型（1专题考核2单课考核3综合考核）
    @Autowired
    String rId;//关联id(testType=1专题id,testType=2单课id,testType=3分类id)
    private List<TestAnswerEntity> answerEntityList =new ArrayList<>();
    private List<String> strList =new ArrayList(Arrays.asList("a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"));

    @Override
    protected void initView() {
        viewBinding.titlebarTest.setTitle(Utils.getString(testName));
        viewBinding.testBottom.releaseShareBtn.setBackgroundColor(Utils.getThemeColor(getApplicationContext()));
        LinearLayoutManager manager =new LinearLayoutManager(getApplicationContext());
        manager.setOrientation(RecyclerView.VERTICAL);
        viewBinding.recyclerViewTest.setLayoutManager(manager);
        viewBinding.testBottom.releaseShareBtn.setText(getString(R.string.post_test_answer));
        viewBinding.testBottom.releaseShareBtn.setOnClickListener(v -> {
            if (isQuestionResearch){
                List<TestAnswerEntity> list = new ArrayList<>();
                for (TestAnswerEntity entity : answerEntityList){
                    TestAnswerEntity testAnswerEntity =new TestAnswerEntity();
                    testAnswerEntity.setId(entity.getId());
                    testAnswerEntity.setType(entity.getType());
                    testAnswerEntity.setUserKey("");
                    //type（1单选2多选3简答）
                    if (Utils.getString(entity.getType()).equals("3"))
                        testAnswerEntity.setUserKey(entity.getUserKey());
                    else {
                        if (!Utils.isEmpty(entity.getUserKey())){
                            for (int i=0;i<entity.getUserKey().length();i++){
                                int index =strList.indexOf(entity.getUserKey().substring(i,i+1));
                                if (i==0 && index>=0) {
                                    testAnswerEntity.setUserKey(entity.getQuestionSelectList().get(index));
                                } else {
                                    if (index>=0)
                                        testAnswerEntity.setUserKey(testAnswerEntity.getUserKey() + "@@" + entity.getQuestionSelectList().get(index));
                                }
                            }
                        }
                    }
                    list.add(testAnswerEntity);
                }
                postTestAnswers(list);
            }else {
                boolean isFinishTest =true;
                for (int i=0;i<answerEntityList.size();i++){
                    if (Utils.isEmpty(answerEntityList.get(i).getId()) || Utils.isEmpty(answerEntityList.get(i).getUserKey())){
                        isFinishTest =false;
                        ToastUtil.show(getApplicationContext(),String.format(getString(R.string.alert_test_answer),""+(i+1)));
                        break;
                    }
                }
                if (isFinishTest) postTestAnswers(answerEntityList);
            }
        });
    }

    @Override
    protected void initData() {
        if (isQuestionResearch)
            getResearchData();
        else
            getTestQuestion();
    }

    /**
     * 问卷调研详情
     */
    private void getResearchData() {
        Map<String,String> map =new HashMap<>();
        map.put("questionnaireSurveyId",testId);
        NetWorkUtils.getResultArray(getApplicationContext(), InterfaceClass.RESEARCH_DETAILS, map, new NetWorkCallback() {
            @Override
            public void onResponse(String code, String response) {
                if (Utils.getString(code).equals("200")){
                    List<TestQuestionsBean> list = JSONArray.parseArray(response,TestQuestionsBean.class);
                    TestAdapter adapter =new TestAdapter(getApplicationContext(), new OnClickCallBack() {

                        @Override
                        public void onClickEdittext(int position, String content) {
                            answerEntityList.get(position).setUserKey(content);
                        }

                        @Override
                        public void onClickTestAnswer(int position, int indexAnswer, boolean isCheck) {
                            //判断试题类型单选多选
                            TestQuestionsBean bean =list.get(position);
                            if (bean==null) return;
                            answerEntityList.get(position).setId(bean.getId());
                            if (Utils.getString(bean.getType()).equals("1")) {//问题类型（1单选2多选）
                                answerEntityList.get(position).setUserKey(strList.get(indexAnswer));
                            }else if (Utils.getString(bean.getType()).equals("2")) {//问题类型（1单选2多选）
                                if (isCheck){
                                    String userKey =answerEntityList.get(position).getUserKey();
                                    if (Utils.isEmpty(userKey)){
                                        answerEntityList.get(position).setUserKey(strList.get(indexAnswer));
                                    }else {
                                        if (!userKey.contains(strList.get(indexAnswer))){
                                            userKey=userKey+strList.get(indexAnswer);
                                            char arr[] =userKey.toCharArray();
                                            Arrays.sort(arr);
                                            userKey ="";
                                            for (char _arr : arr){
                                                userKey+=_arr;
                                            }
                                            answerEntityList.get(position).setUserKey(userKey);
                                        }
                                    }
                                }else {
                                    String userKey =answerEntityList.get(position).getUserKey();
                                    answerEntityList.get(position).setUserKey(userKey.replace(strList.get(indexAnswer),""));
                                }
                            }
                        }
                    });
                    for (TestQuestionsBean bean : list){
                        List<Boolean> listCheck =new ArrayList<>();
                        for (int i=0; i<bean.getQuestionSelectList().size(); i++){
                            listCheck.add(false);
                        }
                        bean.setCheckList(listCheck);
                    }
                    adapter.setmList(list);
                    //有几个试题，创建几个试题答案实体类
                    for (int i=0;i<list.size();i++){
                        TestAnswerEntity entity =new TestAnswerEntity();
                        entity.setId(list.get(i).getId());
                        entity.setUserKey("");
                        entity.setType(list.get(i).getType());
                        entity.setQuestionSelectList(list.get(i).getQuestionSelectList());
                        answerEntityList.add(entity);
                    }
                    viewBinding.recyclerViewTest.setAdapter(adapter);
                }
            }
        });
    }

    private void getTestQuestion() {
        Map<String,String> map =new HashMap<>();
        map.put("courseType",courseType);
        map.put("testId",testId);
        //testType：考试类型（1专题考核2单课考核3综合考核）
        map.put("testType",testType);
        //rId：关联id(testType=1专题id,testType=2单课id,testType=3分类id)
        map.put("rId",rId);
        NetWorkUtils.getRequest(getApplicationContext(), InterfaceClass.TEST_GET_QUESTION, map, new NetWorkCallback() {
            @Override
            public void onResponse(String code, String response) {
                if (Utils.getString(code).equals("200")){
                    List<TestQuestionsBean> list = JSONArray.parseArray(response,TestQuestionsBean.class);
                    if (list==null) list =new ArrayList<>();
                    List<TestQuestionsBean> finalList = list;
                    TestAdapter adapter =new TestAdapter(getApplicationContext(), new OnClickCallBack() {

                        @Override
                        public void onClickTestAnswer(int position, int indexAnswer, boolean isCheck) {
                            //判断试题类型单选多选
                            TestQuestionsBean bean = finalList.get(position);
                            if (bean==null) return;
                            answerEntityList.get(position).setId(bean.getId());
                            if (Utils.getString(bean.getType()).equals("1")) {//问题类型（1单选2多选）
                                answerEntityList.get(position).setUserKey(strList.get(indexAnswer));
                            }else if (Utils.getString(bean.getType()).equals("2")) {//问题类型（1单选2多选）
                                if (isCheck){
                                    String userKey =answerEntityList.get(position).getUserKey();
                                    if (Utils.isEmpty(userKey)){
                                        answerEntityList.get(position).setUserKey(strList.get(indexAnswer));
                                    }else {
                                        if (!userKey.contains(strList.get(indexAnswer))){
                                            userKey=userKey+strList.get(indexAnswer);
                                            char arr[] =userKey.toCharArray();
                                            Arrays.sort(arr);
                                            userKey ="";
                                            for (char _arr : arr){
                                                userKey+=_arr;
                                            }
                                            answerEntityList.get(position).setUserKey(userKey);
                                        }
                                    }
                                }else {
                                    String userKey =answerEntityList.get(position).getUserKey();
                                    answerEntityList.get(position).setUserKey(userKey.replace(strList.get(indexAnswer),""));
                                }
                            }
                        }
                    });
                    for (TestQuestionsBean bean : list){
                        List<Boolean> listCheck =new ArrayList<>();
                       for (int i=0; i<bean.getQuestionSelectList().size(); i++){
                           listCheck.add(false);
                       }
                        bean.setCheckList(listCheck);
                    }
                    adapter.setmList(list);
                    //有几个试题，创建几个试题答案实体类
                    for (int i=0;i<list.size();i++){
                        answerEntityList.add(new TestAnswerEntity());
                    }
                    viewBinding.recyclerViewTest.setAdapter(adapter);
                }
            }
        });
    }

    private void postTestAnswers(List<TestAnswerEntity> list){
       Map<String,String> map =new HashMap<>();
        String url =InterfaceClass.POST_TEST;
       if (isQuestionResearch){
           map.put("questionnaireSurveyId",testId);
           url =InterfaceClass.RESEARCH_COMMIT;
       }else {
           map.put("courseType",courseType);
           map.put("testType",testType);
           map.put("rId",rId);
       }
       map.put("testStr", JSONObject.toJSONString(list));
       NetWorkUtils.postRequest(getApplicationContext(), url, map, new NetWorkCallback() {
           @Override
           public void onResponse(String code, String response) {
               if (Utils.getString(code).equals("200")){
                   MyAlertDialog myAlertDialog = new MyAlertDialog(TestActivity.this);
                   String result ="";
                   if (isQuestionResearch){
                       myAlertDialog.setMessage(getString(R.string.research_success));
                       myAlertDialog.setPositiveText(result);
                       myAlertDialog.setMyDialogCallback(new MyDialogCallback() {
                           @Override
                           public void onPositiveClick(MyAlertDialog dialog) {
                               super.onPositiveClick(dialog);
                               setResult(RESULT_OK);
                               finish();
                           }
                       });
                   }else {
                       TestAnswerResultEntity entity =JSONObject.parseObject(response,TestAnswerResultEntity.class);
                       if (Utils.getString(entity.getCode()).equals("201")) {//未通过
                           result =getString(R.string.goto_study);
                           myAlertDialog.setMessage(String.format(getString(R.string.test_pass_not), entity.getRightNum(), entity.getErrorNum()));
                       }else if (Utils.getString(entity.getCode()).equals("200")) {
                           result =getString(R.string.next_course);
                           myAlertDialog.setMessage(getString(R.string.test_pass_yes));
                       }
                       myAlertDialog.setPositiveText(result);
                       myAlertDialog.setMyDialogCallback(new MyDialogCallback() {
                           @Override
                           public void onPositiveClick(MyAlertDialog dialog) {
                               super.onPositiveClick(dialog);
                               if (Utils.getString(entity.getCode()).equals("200")){
                                   Intent intent =new Intent();
                                   intent.putExtra("index",testPoistion);
                                   setResult(RESULT_OK,intent);
                               }
                               finish();
                           }
                       });
                   }
                   myAlertDialog.setNegativeButtonGone();
                   myAlertDialog.setCanceledOnTouchOutside(false);
                   myAlertDialog.show();
               }
           }
       });
    }
}