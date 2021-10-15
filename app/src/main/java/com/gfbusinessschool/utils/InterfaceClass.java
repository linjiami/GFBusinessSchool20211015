package com.gfbusinessschool.utils;

public class InterfaceClass {
//    public static final String YM_NEWVERSION="http://api.imcfc.cn";//正式
    public static final String YM_NEWVERSION="http://test.bs-api.imcfc.cn";//测试

    public static final String READCOLLECTION_NEW =YM_NEWVERSION+"/bs-api/book/bookshare/newList";//读书汇上新
    public static final String CHANPIONSHARE_NEW =YM_NEWVERSION+"/bs-api/champion/championshare/newList";//冠军分享上新
    public static final String LOGIN_CHANGE_PASSWORD =YM_NEWVERSION+"/bs-api/app/updatePassword";//修改密码
    public static final String AUDIO_DETAILS =YM_NEWVERSION+"/bs-api/sptool/audiocoursepool/audioCourseDetail";//音频详情
    public static final String AUDIO_LIST =YM_NEWVERSION+"/bs-api/sptool/audiocoursepool/audioList";//音频中心列表
    public static final String AUDIO_CLASSIFY =YM_NEWVERSION+"/bs-api/sptool/audiocoursepool/AudioClassifyList";//音频中心分类
    public static final String APP_UPDATE_URL ="http://www.dajiajuvip.com/app/企业云教育.apk";//app下载地址（强制更新）
    public static final String APP_UPDATE =YM_NEWVERSION+"/bs-api/sys/appversion/info";//app版本管理（强制更新）
    public static final String COURSEWARE_LIST =YM_NEWVERSION+"/bs-api/course/courseware/courseWareList";//课件列表
    public static final String COURSECENTER_CLASSIFY =YM_NEWVERSION+"/bs-api/course/selectedspecialsubject/courseCenterClassifyList";//课程中心分类
    public static final String HOMEPAGER_CLASSIFY_ICONS =YM_NEWVERSION+"/bs-api/gf/iconmanage/list";//首页分类图标
    public static final String STUDYMAP_ADDSTUDYINFO =YM_NEWVERSION+"/bs-api/course/mapspecialsubject/addUserMapStudy";//添加用户地图学习记录
    public static final String STUDYMAP_COURSE_DETAILS =YM_NEWVERSION+"/bs-api/course/mapspecialsubject/mapCourseDetail";//学习地图课程详情
    public static final String STUDYMAP_CLASS_CATALOG =YM_NEWVERSION+"/bs-api/course/mapspecialsubject/mapSpecialCourseList";//学习地图专题目录
    public static final String STUDYMAP_CLASS_DETAILS =YM_NEWVERSION+"/bs-api/course/mapspecialsubject/mapSpecialDetail";//学习地图专题详情
    public static final String STUDYMAP =YM_NEWVERSION+"/bs-api/course/mapspecialsubject/studyMap";//学习地图
    public static final String SHOPMALL_ICON ="http://www.dajiajuvip.com/images/pointsmall.png";//积分商城图片
    public static final String SIGIN =YM_NEWVERSION+"/bs-api/mine/usersignrecord/sign";//签到
    public static final String SHOPMALL_ORDER_CONFIRM_RECEIPT =YM_NEWVERSION+"/bs-api/shop/shoporder/confirmReceipt";//积分商城订单确认收货
    public static final String SHOPMALL_ORDER_LIST =YM_NEWVERSION+"/bs-api/shop/shoporder/list";//积分商城订单列表
    public static final String SHOPMALL_ADDRESS_LIST =YM_NEWVERSION+"/bs-api/shop/shopaddress/list";//积分商城收货地址列表
    public static final String SHOPMALL_ADDRESS_ADD =YM_NEWVERSION+"/bs-api/shop/shopaddress/save";//积分商城薪资地址
    public static final String SHOPMALL_ADDRESS_DELETE =YM_NEWVERSION+"/bs-api/shop/shopaddress/removeAddress";//积分商城删除地址
    public static final String SHOPMALL_GOODS_BUY =YM_NEWVERSION+"/bs-api/shop/shopgoods/buyGoods";//积分商城商品购买
    public static final String SHOPMALL_GOODS_DETAILS =YM_NEWVERSION+"/bs-api/shop/shopgoods/info";//积分商城商品详情
    public static final String SHOPMALL_GOODS_LIST =YM_NEWVERSION+"/bs-api/shop/shopgoods/list";//积分商城商品列表
    public static final String SHOPMALL_CLASSIFY =YM_NEWVERSION+"/bs-api/shop/shopcategory/list";//积分商城分类
    //考核未通过课程排行
    public static final String RANKLIST_TESTNOPASS =YM_NEWVERSION+"/bs-api/gf/manager/noPassCourseRank";
    //问卷调研消息提醒
    public static final String RESEARCH_NOTICE =YM_NEWVERSION+"/bs-api/wq/questionnairesurvey/wqRemind";
    //问卷调研提交问卷
    public static final String RESEARCH_COMMIT =YM_NEWVERSION+"/bs-api/wq/questionnairesurvey/commitSurvey";
    //问卷调研详情
    public static final String RESEARCH_DETAILS =YM_NEWVERSION+"/bs-api/wq/questionnairesurvey/questionSurveyDetail";
    //问卷调研列表
    public static final String RESEARCH_LIST =YM_NEWVERSION+"/bs-api/wq/questionnairesurvey/list";
    //我的问卷调研列表
    public static final String RESEARCH_MY_LIST =YM_NEWVERSION+"/bs-api/wq/questionnairesurvey/mySurvey";
    //首页热门投票
    public static final String VOTE_ACTIVE_HOT =YM_NEWVERSION+"/bs-api/active/activeinfo/hotActive";
    //投票活动数据统计
    public static final String VOTE_ACTIVE_INFO =YM_NEWVERSION+"/bs-api/active/activeinfo/info";
    //投票活动给选手投票
    public static final String VOTE_ACTIVE_POST =YM_NEWVERSION+"/bs-api/active/activeinfo/vote";
    //投票活动选手列表
    public static final String VOTE_ACTIVE_USER_LIST =YM_NEWVERSION+"/bs-api/active/activeinfo/playerList";
    //投票活动列表
    public static final String VOTE_ACTIVE_LIST =YM_NEWVERSION+"/bs-api/active/activeinfo/list";
    //我的证书
    public static final String MY_CERTIFICATION =YM_NEWVERSION+"/bs-api/cert/usercert/myCertList";
    //系统常量
    public static final String SYSTEM_CONSTANT =YM_NEWVERSION+"/bs-api/sys/sysbasicparam/list";
    //登录
    public static final String LOGIN =YM_NEWVERSION+"/bs-api/app/login";
    //企业职位
    public static final String POSITION_LIST =YM_NEWVERSION+"/bs-api/gf/position/list";
    //门店地址分布
    public static final String STORE_ADDRESS =YM_NEWVERSION+"/bs-api/gf/storeaddress/list";
    //门店信息
    public static final String STORE_INFO =YM_NEWVERSION+"/bs-api/gf/store/list";
    //完善用户信息
    public static final String UPDATE_USERINFO =YM_NEWVERSION+"/bs-api/app/updateUserInfo";
    //获取用户信息
    public static final String GET_USERINFO =YM_NEWVERSION+"/bs-api/app/info";
    //优秀员工
    public static final String GOOD_STAFF =YM_NEWVERSION+"/bs-api/app/excellentUser";
    //首页banner
    public static final String FIRSTPAGE_BANNER =YM_NEWVERSION+"/bs-api/gf/bannerinfo/list";
    //企业公告
    public static final String COMPANY_NOTICE =YM_NEWVERSION+"/bs-api/gf/companynotice/list";
    //企业公告详情
    public static final String COMPANY_NOTICE_DETAILS =YM_NEWVERSION+"/bs-api/gf/companynotice/info";
    //课程推荐
    public static final String COURSE_RECOMMEND =YM_NEWVERSION+"/bs-api/index/indexPage/indexPageCourseRec";
    //课程上新
    public static final String COURSE_NEW =YM_NEWVERSION+"/bs-api/index/indexPage/indexPageCourseNew";
    //大家在学
    public static final String COURSE_STUDY =YM_NEWVERSION+"/bs-api/index/indexPage/indexPageEveryStu";
    //课程中心/必修课程分类列表
    public static final String COURSE_MUST_STUDY =YM_NEWVERSION+"/bs-api/course/requiredspecialsubject/requiredClassifyList";
    //课程中心/必修课程专题列表
    public static final String COURSE_MUST_SPECIAL =YM_NEWVERSION+"/bs-api/course/requiredspecialsubject/requiredList";
    //课程中心/必修课程专题列表/必修课程列表
    public static final String COURSE_MUST_SPECIAL_LIST =YM_NEWVERSION+"/bs-api/course/requiredspecialsubject/requiredCourseList";
    //课程中心/精选课程分类列表
    public static final String COURSE_SPECIAL_SELECTED =YM_NEWVERSION+"/bs-api/course/selectedspecialsubject/selectedClassifyList";
    //课程中心/精选课程专题列表
    public static final String COURSE_SPECIAL_SELECTED_SPECIAL =YM_NEWVERSION+"/bs-api/course/selectedspecialsubject/selectedList";
    //课程中心/精选课程专题列表/精选课程列表
    public static final String COURSE_SPECIAL_SELECTED_LIST =YM_NEWVERSION+"/bs-api/course/selectedspecialsubject/selectedCourseList";
    //图片文件上传
    public static final String UPLOAD_IMG_FILE=YM_NEWVERSION+"/bs-api/sys/oss/upload";
    //冠军分享分类
    public static final String CHAMPIONSHARING_CLASSIFY=YM_NEWVERSION+"/bs-api/champion/championsharetag/list";
    //冠军分享列表
    public static final String CHAMPIONSHARING_LIST =YM_NEWVERSION+"/bs-api/champion/championshare/list";
    //我的分享
    public static final String SHARING_MANAGER=YM_NEWVERSION+"/bs-api/champion/championshare/myShareList";
    //冠军分享模板列表
    public static final String CHAMPIONSHARING_COVER=YM_NEWVERSION+"/bs-api/champion/championsharecover/list";
    //上传分享
    public static final String CHAMPIONSHARING_RELEASE=YM_NEWVERSION+"/bs-api/champion/championshare/save";
    //门店排行-->全员学习通关率排行/详情页
    public static final String STORERANKLIST_PASSRATE=YM_NEWVERSION+"/bs-api/gf/storerank/passRateRank";
    //门店排行-->学习在线平均时长排行/详情页
    public static final String STORERANKLIST_STUDYTIME=YM_NEWVERSION+"/bs-api/gf/storerank/studyHoursRank";
    //门店排行-->学员积分平均排名
    public static final String STORERANKLIST_POINT=YM_NEWVERSION+"/bs-api/gf/storerank/jFRank";
    //个人排行-->一次性通关率排行
    public static final String STORERANKLIST_ONETIMEPASSRATE=YM_NEWVERSION+"/bs-api/gf/storerank/oneTimesPassRateRank";
    //个人排行-->在线习时长排行
    public static final String STORERANKLIST_ONLINESTUDYTIME=YM_NEWVERSION+"/bs-api/gf/storerank/personalStudyHoursRank";
    //个人排行-->个人通关率排行
    public static final String STORERANKLIST_PERSONALPASSRATE=YM_NEWVERSION+"/bs-api/gf/storerank/personalPassRateRank";
    //个人排行-->积分排行
    public static final String STORERANKLIST_PERSONALPOINT=YM_NEWVERSION+"/bs-api/gf/storerank/personalJFRank";
    //课程排行-->观看次数排行
    public static final String STORERANKLIST_LOOKCOUNT=YM_NEWVERSION+"/bs-api/gf/storerank/courseStudyTimesRank";
    //课程排行-->评论次数排行
    public static final String STORERANKLIST_COMMENTCOUNT=YM_NEWVERSION+"/bs-api/gf/storerank/courseRemarkTimesRank";
    //考试校验获取试卷
    public static final String TEST_GET_QUESTION=YM_NEWVERSION+"/bs-api/course/testpool/test";
    //提交考试
    public static final String POST_TEST=YM_NEWVERSION+"/bs-api/course/testpool/commitTest";
    //直播回放列表
    public static final String LIVE_LIST=YM_NEWVERSION+"/bs-api/broadcast/broadcastinfo/list";
    //我的收藏
    public static final String MY_COLLECTION=YM_NEWVERSION+"/bs-api/mine/usercollection/myCollection";
    //我的浏览历史
    public static final String MY_LOOK_HISTORY=YM_NEWVERSION+"/bs-api/mine/userbrowsehistory/myBrowseHistory";
    //积分列表
    public static final String MY_INTEGRAL=YM_NEWVERSION+"/bs-api/gf/integralrecord/list";
    //管理员首页
    public static final String MANAGER_FIRSTPAGER =YM_NEWVERSION+"/bs-api/gf/manager/dataBoard";
    //管理首页今日/累计学习时间排行榜
    public static final String MANAGER_FIRSTPAGER_STUDYTIME =YM_NEWVERSION+"/bs-api/gf/manager/personalStudyLength";
    //管理门店今日/累计学习时间排行榜
    public static final String MANAGER_SORE_STUDYTIME =YM_NEWVERSION+"/bs-api/gf/manager/storeStudyLength";
    //管理门店分享排行榜
    public static final String MANAGER_SORE_SHARE_RANKLIST =YM_NEWVERSION+"/bs-api/gf/manager/storeShareRank";
    //管理首页-->门店积分排行/详情
    public static final String MANAGER_SORE_POINT_RANKLIST =YM_NEWVERSION+"/bs-api/gf/manager/storeIntegralRank";
    //员工列表
    public static final String STAFF_LIST =YM_NEWVERSION+"/bs-api/app/userList";
    //学习时长统计
    public static final String STUDY_TIME =YM_NEWVERSION+"/bs-api/gf/userstudyrecord/myStudy";
    //7天学习列表
    public static final String SEVENDAY_STUDY_TIME =YM_NEWVERSION+"/bs-api/gf/userstudyrecord/list";
    //学习的课程
    public static final String STUDY_COURSE =YM_NEWVERSION+"/bs-api/course/coursepool/myStudyCourse";
    //课程详细信息
    public static final String COURSE_DETAILS =YM_NEWVERSION+"/bs-api/course/coursepool/courseDetail";
    //收藏课程
    public static final String COURSE_COLLECT =YM_NEWVERSION+"/bs-api/course/coursepool/courseCollection";
    //课程评价列表
    public static final String COURSE_COMMENT_LIST =YM_NEWVERSION+"/bs-api/course/courseremark/getCourseRemarkList";
    //发表课程评价
    public static final String COURSE_COMMENT_SEND =YM_NEWVERSION+"/bs-api/course/courseremark/saveCourseRemark";
    //添加用户学习记录（观看时间）
    public static final String COURSE_ADD_STUDYRECORD =YM_NEWVERSION+"/bs-api/course/coursepool/addUserStudy";
    //冠军分享详情
    public static final String CHAMPIONSHARE_COURSE_DETAILS =YM_NEWVERSION+"/bs-api/champion/championshare/info";
    //隐私协议
    public static final String PRIVACY_AGREEMENT_URL="http://third.allgf.com.cn/files/html/xieyi.html";
    //隐私政策
    public static final String PRIVACY_POLICY_URL="http://third.allgf.com.cn/files/html/zhengce.html";
    //员工信息统计
    public static final String STAFF_INFO_TABLE=YM_NEWVERSION+"/bs-api/app/usersNew";
    //课程搜索
    public static final String SEARCH_COURSE=YM_NEWVERSION+"/bs-api/index/indexPage/indexPageSearch";
    //获取上传凭证
    public static final String UPLOAD_TOKEN=YM_NEWVERSION+"/bs-api/sys/oss/uploadToken";
    //读书汇分享
    public static final String UPLOAD_READCOLLECTION=YM_NEWVERSION+"/bs-api/book/bookshare/save";
    //读书汇分享列表
    public static final String READCOLLECTION_LIST=YM_NEWVERSION+"/bs-api/book/bookshare/list";
    //读书汇分享详情
    public static final String READCOLLECTION_INFO=YM_NEWVERSION+"/bs-api/book/bookshare/info";
    //读书汇分享点赞收藏
    public static final String READCOLLECTION_LIKE_FOLLOW=YM_NEWVERSION+"/bs-api/book/bookshare/likeCollection";
    //读书汇分享取消收藏
    public static final String READCOLLECTION_CANCLEFOLLOW=YM_NEWVERSION+"/bs-api/book/bookshare/delCollection";
    //读书汇发表评论
    public static final String READCOLLECTION_COMMENT=YM_NEWVERSION+"/bs-api/book/bookshare/comment";
    //读书汇评论列表
    public static final String READCOLLECTION_COMMENT_LIST=YM_NEWVERSION+"/bs-api/book/bookshare/commentList";
    //必修专题详情
    public static final String CLASS_DETAILS_BIXIU=YM_NEWVERSION+"/bs-api/course/requiredspecialsubject/bxSpecialDetail";
    //精选专题详情
    public static final String CLASS_DETAILS_JINGXUAN=YM_NEWVERSION+"/bs-api/course/selectedspecialsubject/jxSpecialDetail";
}