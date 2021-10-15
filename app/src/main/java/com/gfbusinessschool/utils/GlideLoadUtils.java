package com.gfbusinessschool.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.gfbusinessschool.R;

public class GlideLoadUtils{
    public static final String TYPE_PLACE_HOLDER_LIST ="列表占位图";//列表占位图
    public static final String TYPE_PLACE_HOLDER_HEAD ="头像占位图";//头像占位图
    public static final String TYPE_PLACE_HOLDER_COMPANY_LOGO ="公司logo占位图";//头像占位图
    public static final String TYPE_PLACE_HOLDER_LIVE_LIST ="直播列表占位图";//直播列表占位图
    public static final String TYPE_PLACE_HOLDER_NO ="无占位图";
    public static final String TYPE_PLACE_HOLDER_MAXSOUCE ="原始大小显示（>2M，慎用）";

    /**
     * 设置站位图
     * @param context
     * @param url
     * @param view
     * @param typePlaceHolder
     */
    public static void load(Context context, String url, ImageView view, String typePlaceHolder){
        switch (typePlaceHolder){
            case TYPE_PLACE_HOLDER_LIST:
                Glide.with(context).load(url).placeholder(R.mipmap.placeholder_cover).error(R.mipmap.placeholder_cover)
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(view);
                break;
            case TYPE_PLACE_HOLDER_NO:
                Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).into(view);
                break;
            case TYPE_PLACE_HOLDER_HEAD:
                Glide.with(context).load(url).placeholder(R.mipmap.recruit_user_icon).error(R.mipmap.recruit_user_icon)
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(view);
                break;
            case TYPE_PLACE_HOLDER_LIVE_LIST:
                Glide.with(context).load(url).placeholder(R.mipmap.placeholder_cover).error(R.mipmap.placeholder_live)
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(view);
                break;
            case TYPE_PLACE_HOLDER_COMPANY_LOGO:
                Glide.with(context).load(url).placeholder(R.mipmap.logo_bg).error(R.mipmap.logo_bg)
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(view);
                break;
            case TYPE_PLACE_HOLDER_MAXSOUCE:
                Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL)
                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).into(view);
                break;
        }
    }

    /**
     *
     * @param context
     * @param url
     * @param view
     * @param resourceId 占位图id
     */
    public static void load(Context context, String url, ImageView view,int resourceId){
        Glide.with(context).load(url).placeholder(resourceId).error(resourceId)
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(view);
    }

    /**
     *  本地图片
     * @param context
     * @param resourceId 本地图片id
     * @param view
     */
    public static void load(Context context, int resourceId, ImageView view){
        Glide.with(context).load(resourceId).into(view);
    }

    public static void loadVideoCover(Context context,String url,ImageView imageView){
        Glide.with(context)
                .setDefaultRequestOptions(
                        new RequestOptions()
                                .frame(1000000)
                                .centerCrop()
                                .error(R.mipmap.placeholder_cover)//可以忽略
                                .placeholder(R.mipmap.placeholder_cover)//可以忽略
                )
                .load(url)
                .into(imageView);
    }
}
