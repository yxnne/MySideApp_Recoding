package com.yxnne.mysides.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ant.liao.GifView;
import com.yxnne.mysides.R;


/**
 * for face girdview
 * Created by Administrator on 2016/10/30.
 */

public class FaceGridViewAdapter extends BaseAdapter {
    Context context;
    //faces
    private int[] faceid = {
//            R.drawable.f001, R.drawable.f002, R.drawable.f003,
//            R.drawable.f004, R.drawable.f005, R.drawable.f006, R.drawable.f007,
//            R.drawable.f008, R.drawable.f009, R.drawable.f010, R.drawable.f011,
//            R.drawable.f012, R.drawable.f013, R.drawable.f014, R.drawable.f015,
//            R.drawable.f016, R.drawable.f017, R.drawable.f018, R.drawable.f019,
//            R.drawable.f020, R.drawable.f021, R.drawable.f022, R.drawable.f023,
//            R.drawable.f024, R.drawable.f025, R.drawable.f026, R.drawable.f027,
//            R.drawable.f028, R.drawable.f029, R.drawable.f030, R.drawable.f031,
//            R.drawable.f032, R.drawable.f033, R.drawable.f034, R.drawable.f035,
//            R.drawable.f036, R.drawable.f037, R.drawable.f038, R.drawable.f039,
//            R.drawable.f040, R.drawable.f041, R.drawable.f042, R.drawable.f043,
//            R.drawable.f044, R.drawable.f045, R.drawable.f046, R.drawable.f047,
//            R.drawable.f048, R.drawable.f049, R.drawable.f050, R.drawable.f051,
//            R.drawable.f052, R.drawable.f053, R.drawable.f054, R.drawable.f055,
//            R.drawable.f056, R.drawable.f057, R.drawable.f058, R.drawable.f059,
//            R.drawable.f060, R.drawable.f061, R.drawable.f062, R.drawable.f063,
//            R.drawable.f064, R.drawable.f065, R.drawable.f066, R.drawable.f067,
//            R.drawable.f068, R.drawable.f069, R.drawable.f070, R.drawable.f071,
//            R.drawable.f072, R.drawable.f073, R.drawable.f074, R.drawable.f075,
//            R.drawable.f076, R.drawable.f077, R.drawable.f078, R.drawable.f079,
//            R.drawable.f080, R.drawable.f081, R.drawable.f082, R.drawable.f083,
//            R.drawable.f084, R.drawable.f085, R.drawable.f086, R.drawable.f087,
//            R.drawable.f088, R.drawable.f089, R.drawable.f090, R.drawable.f091,
//            R.drawable.f092, R.drawable.f093, R.drawable.f094, R.drawable.f095,
//            R.drawable.f096, R.drawable.f097, R.drawable.f098, R.drawable.f099,
//            R.drawable.f100, R.drawable.f101, R.drawable.f102, R.drawable.f103,
//            R.drawable.f104, R.drawable.f105, R.drawable.f106, R.drawable.f107,
//            R.drawable.f108, R.drawable.f109, R.drawable.f110, R.drawable.f111,
//            R.drawable.f112, R.drawable.f113, R.drawable.f114, R.drawable.f115,
//            R.drawable.f116, R.drawable.f117, R.drawable.f118, R.drawable.f119,
//            R.drawable.f120, R.drawable.f121, R.drawable.f122, R.drawable.f123,
//            R.drawable.f124, R.drawable.f125, R.drawable.f126, R.drawable.f127,
//            R.drawable.f128, R.drawable.f129, R.drawable.f130, R.drawable.f131,
//            R.drawable.f132, R.drawable.f133, R.drawable.f134, R.drawable.f135,
//            R.drawable.f136, R.drawable.f137, R.drawable.f138, R.drawable.f139,
//            R.drawable.f140, R.drawable.f141, R.drawable.f142
    };

    public FaceGridViewAdapter(Context context) {
        super();
        this.context = context;
    }
    @Override
    public int getCount() {
        return faceid.length;
    }

    @Override
    public Object getItem(int position) {
        return faceid[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        GifImageView imageView = new GifImageView(context);
//        imageView.setImageResource(faceid[position]);
        GifView gifView = new GifView(context);
        gifView.setGifImage(faceid[position]);
        return gifView;
    }
}
