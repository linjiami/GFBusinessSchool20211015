package com.gfbusinessschool.activity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.gfbusinessschool.R;
import com.gfbusinessschool.databinding.ActivityIntegralRuleBinding;
import com.gfbusinessschool.utils.ARouterPath;

@Route(path = ARouterPath.ACTIVITY_IntegralRule)
public class IntegralRuleActivity extends BaseActivity<ActivityIntegralRuleBinding> {

    @Override
    protected void initView() {
        super.initView();
        viewBinding.titleBar.setTitle(getString(R.string.integralRule));
        viewBinding.ruleTv.setText("基于每位伙伴在线上学习行为数据，计算并评分；得到的积分作为公司优秀评比、营销政策享受的标准之一。\n" +
                "1、适用范围\n" +
                "所有参与培训的伙伴。\n" +
                "2、积分用处？\n" +
                "积分不仅是你坚持、自我提升的象征，还可以用于公司体系内发起的某些特定线下精英培训课程、奖励评选、营销政策权益等培训、活动、赛事的参与和奖励兑换。\n" +
                "3、积分获取\n" +
                "参与公司培训计划，线上APP课程学习、发起评论、参与分享等均可获得积分，个人页面显示的积分为以下积分总和。\n" +
                "   APP观看课程学习：每门课程首次观看完毕，可获得10积分。\n" +
                "   APP评论：每次评论可获得1积分，每日评论加分次数限20次。\n" +
                "   APP冠军分享：发起冠军分享并成功，30积分/次，次数不受限制。 ");
    }
}