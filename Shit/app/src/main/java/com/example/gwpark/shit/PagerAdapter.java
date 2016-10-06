package com.example.gwpark.shit;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import com.example.gwpark.shit.fragments.InfoFragment;
import com.example.gwpark.shit.fragments.PhotoFragment;
import com.example.gwpark.shit.fragments.ReviewFragment;

import net.daum.mf.map.api.MapPOIItem;

/**
 * Created by gwpark on 16. 8. 11..
 */
public class PagerAdapter extends FragmentPagerAdapter {
    private MapPOIItem selected;

    public enum TabItem {
        INFO(InfoFragment.class, R.string.tab_info),
        PHOTO(PhotoFragment.class, R.string.tab_photo),
        REVIEW(ReviewFragment.class, R.string.tab_review);

        private final Class<? extends Fragment> fragmentClass;
        private final int titleResId;

        TabItem(Class<? extends Fragment> fragmentClass, @StringRes int titleResId) {
            this.fragmentClass = fragmentClass;
            this.titleResId = titleResId;
        }
    }

    private final TabItem[] tabItems;
    private final Context context;
    private int[] imageResId = {
            R.drawable.ic_infotab,
            R.drawable.ic_phototab,
            R.drawable.ic_reviewtab
    };

    public PagerAdapter(FragmentManager fragmentManager, Context context, TabItem... tabItems) {
        super(fragmentManager);
        this.context = context;
        this.tabItems = tabItems;
    }

    @Override
    public Fragment getItem(int position) {
        return newInstance(tabItems[position].fragmentClass);
    }

    private Fragment newInstance(Class<? extends Fragment> fragmentClass) {
        try {
            return fragmentClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("fragment must have public no-arg constructor: " + fragmentClass.getName(), e);
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Drawable image = ContextCompat.getDrawable(context, imageResId[position]);
        image.setBounds(0, 0, 50, 50);
        SpannableString sb = new SpannableString(" ");
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }

    @Override
    public int getCount() {
        return tabItems.length;
    }

    @Override
    public int getItemPosition(Object object) {
        if (object instanceof InfoFragment) {
            ((InfoFragment) object).update(selected.getTag());
        } else if (object instanceof PhotoFragment) {
            ((PhotoFragment) object).update(selected.getTag());
        } else if (object instanceof ReviewFragment) {
            ((ReviewFragment) object).update(selected.getTag());
        }
        return super.getItemPosition(object);
    }

    public void notifyViewPagerDataSetChanged(MapPOIItem selected) {
        this.selected = selected;
        this.notifyDataSetChanged();
    }
}
