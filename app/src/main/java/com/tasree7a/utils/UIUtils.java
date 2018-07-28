package com.tasree7a.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.google.common.base.Strings;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.customcomponent.CustomSwitch;
import com.tasree7a.enums.Language;
import com.tasree7a.enums.Sizes;
import com.tasree7a.fragments.BaseFragment;
import com.tasree7a.managers.FragmentManager;
import com.tasree7a.models.LoadingViewModel;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by KhalidTaha on 4/20/17.
 */

public class UIUtils {

    static SweetAlertDialog loadingDialog;
    private final static String IMAGES_URL = "http://tasree7a.ps/services/public/uploads/";

    public static String[] images = {
            "http://www.jolie-shopping.com/wp-content/uploads/2016/02/cancela_barber_lo-skin-fade-pompadour-1.jpg",
            "http://bayyraq.s3.amazonaws.com/2016/06/hairstyles-3.jpg",
            "http://s3.amazonaws.com/mbc_actionha/uploads/101627/original.jpg",
            "http://s3.amazonaws.com/mbc_actionha/uploads/148473/original.jpg",
            "http://tasri7at.com/wp-content/uploads/2015/11/c7eef7a8b4db361220c6b4e68c7681f8.jpg",
            "http://www.alwasat.ly/get_img?ImageWidth=900&ImageHeight=1263&ImageId=3547",
            "https://dorar.at/imup2/2014-04/13527627181.jpg",
            "https://lh3.googleusercontent.com/-24gMfLZ2z9c/VCdSI2R0OcI/AAAAAAAAAT8/XHi52u3P_dY/s250/ZvNtiH.png",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTuHHedfImRFQiatV_rfg3F2PFbfbS1JO0YUmv-IPFwdQLhoxPA",
            "http://s3.amazonaws.com/mbc_actionha/uploads/148471/original.jpg",
            "http://www.jolie-shopping.com/wp-content/uploads/2016/02/cancela_barber_lo-skin-fade-pompadour-1.jpg",
            "https://s-media-cache-ak0.pinimg.com/originals/7c/5a/49/7c5a49f30440902f2b186d3a427297ef.jpg",
            "https://s-media-cache-ak0.pinimg.com/originals/8a/94/9c/8a949ca57a41b0fec132afffb6cff36c.jpg",
            "https://2.bp.blogspot.com/-E3eb_GDnlvo/WDmWeiE6uEI/AAAAAAAAcxY/gYNC9ZS39KswvngFlK34EyhUBpsbbgo4QCLcB/s1600/hair-men-short-2017%2B%25283%2529.jpg",
            "http://www.all-aboutfashion.com/wp-content/uploads/2016/07/4-54.jpg",
            "http://www.all-aboutfashion.com/wp-content/uploads/2016/07/5-55.jpg",
            "https://i.ytimg.com/vi/W2tK3F6RZPo/hqdefault.jpg",
            "http://www.sayidy.net/sites/default/files/styles/660xauto/public/main/articles/shutterstock_164435612.jpg?itok=0gRSlCNm",
            "https://i.ytimg.com/vi/bvF6ixC6zoM/maxresdefault.jpg",
            "http://img.soutalomma.com/ArticleImgs/2017/4/26/40797-%D9%82%D8%B5%D8%A9-%D8%B4%D8%B9%D8%B1-%D9%84%D8%A7%D8%B9%D8%A8%D9%88-%D9%83%D8%B1%D8%A9-%D8%A7%D9%84%D9%82%D8%AF%D9%85.jpg",
            "http://www.menshairstyletrends.com/wp-content/uploads/2015/08/blendz_barbershop_-_By__criztofferson_here_at__blendz_barbershop.jpg",
            "http://3.bp.blogspot.com/-1Wu6R0HMckM/U_3k5qyKGWI/AAAAAAAABvQ/7JXFdwEWNs0/s1600/%D8%AA%D8%B3%D8%B1%D9%8A%D8%AD%D8%A9-%D8%A7%D8%B4%D8%B1-%D8%A8%D9%88%D9%88%D9%83-%D8%A7%D9%84%D8%B4%D8%A7%D8%A6%D9%83%D8%A9%2B-%D9%84%D9%84%D8%B1%D8%AC%D8%A7%D9%84-%D9%84%D8%A8%D8%AF%D8%A7%D9%8A%D8%A9-%D8%A7%D9%84%D9%85%D9%88%D8%B3%D9%85-%D8%A7%D9%84%D8%AF%D8%B1%D8%A7%D8%B3%D9%8A.jpg",
            "http://www.allshorthairstyles.com/wp-content/uploads/2016/12/Spiky-Light-Brown-Hair-554x800.jpg",
            "https://lh3.googleusercontent.com/ABM6Tejc2Ny3t4VD5W04WcOexkhWFzOi0_VcPEkJN_MItzfjLU5SmmStgaOTIvIHACI=h900",
            "http://ift.tt/2cUdFZF",
            "https://s-media-cache-ak0.pinimg.com/736x/0c/c8/d0/0cc8d0cebf608371a44f5c61ce58bc26--joel-alexander-google-search.jpg",
            "http://www.sayidy.net/sites/default/files/styles/660xauto/public/main/articles/mad_men_hair_style.jpg?itok=PFgFtFLY",
            "http://nicee.cc/wp-content/uploads/2017/04/790-6.jpg",
            "http://n4hr.com/up/uploads/c015986765.jpg",
            "https://i.ytimg.com/vi/65oxK-DgnZs/maxresdefault.jpg",
            "https://barbarianstyle.net/wp-content/uploads/2016/01/Black-undercut-hairstyle-with-shaved-designs.jpg",
            "https://s-media-cache-ak0.pinimg.com/originals/8e/7f/7e/8e7f7e9dab33c48ba666878ffbdeeb93.jpg",
            "http://www.hiamag.com/sites/default/files/styles/ph2_960_600/public/article/24/04/2016/%20%D8%AC%D9%88%D8%A7%D9%86%D8%A8%20%D8%A7%D9%84%D8%B4%D8%B9%D8%B1%20%D9%85%D8%B9%20%D8%AA%D8%B1%D9%83%20%D8%B4%D8%B9%D8%B1%20%D8%A7%D9%84%D8%B1%D8%A7%D8%B3%20%D8%B7%D9%88%D9%8A%D9%84%D8%A7.jpg?itok=HQbaB6tR",
            "http://taplamdep.net/wp-content/uploads/2015/09/nhung-kieu-toc-nam-dep-vuot-nguoc-mang-phong-cac-sang-trong-lich-lam-cho-quy-ong-tuoi-trung-nien6.jpg",
            "http://www.shammil.com/wp-content/uploads/unnamed-file-498-500x500.jpg",
            "https://raqyi.com/wp-content/uploads/2016/12/%D8%A3%D8%AD%D8%AF%D8%AB-%D9%82%D8%B5%D8%A7%D8%AA-%D8%A7%D9%84%D8%B4%D8%B9%D8%B1-%D9%84%D9%84%D8%B1%D8%AC%D8%A7%D9%84-%D9%84%D9%87%D8%B0%D8%A7-%D8%A7%D9%84%D8%B9%D8%A7%D9%853.jpg",
            "https://elyana.info/images5/0117L/boys-haircut-2017/boys-haircut-2017-57_17.jpg",
            "http://lorrye.net/images5/0117/new-haircuts-2017-for-long-hair/new-haircuts-2017-for-long-hair-18_15.jpg",
            "https://s-media-cache-ak0.pinimg.com/736x/80/4b/43/804b4367a28b42db5bb437f2ec8dcf12.jpg",
            "https://lh3.googleusercontent.com/idnioTz-gra_hpTDGs3n-BNQRsmV759AycLxWqQH0mlCIPSrLahKJ3Z37eW9g-Za1g=h900",
            "http://www.shammil.com/wp-content/uploads/unnamed-file-499-500x500.jpg",
            "https://i.ytimg.com/vi/K77kKEU9tfs/maxresdefault.jpg",
            "https://s-media-cache-ak0.pinimg.com/originals/c8/d6/4e/c8d64ee7c89e9bd5f2c2f0492b0ad436.jpg",
            "http://www.mens-hairstylists.com/wp-content/uploads/2015/10/cool-summer-hairstyle-for-men.jpg",
            "http://www.menshairstyletrends.com/wp-content/uploads/2016/12/javi_thebarber_-medium-hairstyle-men.jpg",
            "https://s-media-cache-ak0.pinimg.com/236x/dd/94/21/dd94214942b10e838e8ab2973da132a7--men-beard-style-short-beard-styles-short.jpg",
            "http://4.bp.blogspot.com/-OCxrEGSO2nk/UjV0yN2R-uI/AAAAAAAAAgg/bSJ3UQRjR8M/s400/Mullet-Hairstyles-2013.jpg",
            "http://www.mens-hairstyle.com/wp-content/uploads/2016/12/Long-Top-Haircuts-for-Men.jpg",
            "https://s-media-cache-ak0.pinimg.com/originals/1b/b6/33/1bb6333801259d30457568ac3a58ae10.jpg",
            "https://s-media-cache-ak0.pinimg.com/736x/84/be/cc/84beccaff1e5f07b46814b4a04aa0d94--hipster-hairstyles-hairstyle-for-women.jpg",
            "http://hairstyleonpoint.com/wp-content/uploads/2015/08/11811288_937523026308702_756256385143542710_n.jpg",
            "http://www.jolie-shopping.com/wp-content/uploads/2016/02/cancela_barber_lo-skin-fade-pompadour-1.jpg",
            "http://bayyraq.s3.amazonaws.com/2016/06/hairstyles-3.jpg",
            "http://s3.amazonaws.com/mbc_actionha/uploads/101627/original.jpg",
            "http://s3.amazonaws.com/mbc_actionha/uploads/148473/original.jpg",
            "http://tasri7at.com/wp-content/uploads/2015/11/c7eef7a8b4db361220c6b4e68c7681f8.jpg",
            "http://www.alwasat.ly/get_img?ImageWidth=900&ImageHeight=1263&ImageId=3547",
            "https://dorar.at/imup2/2014-04/13527627181.jpg",
            "https://lh3.googleusercontent.com/-24gMfLZ2z9c/VCdSI2R0OcI/AAAAAAAAAT8/XHi52u3P_dY/s250/ZvNtiH.png",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTuHHedfImRFQiatV_rfg3F2PFbfbS1JO0YUmv-IPFwdQLhoxPA",
            "http://s3.amazonaws.com/mbc_actionha/uploads/148471/original.jpg",
            "http://www.jolie-shopping.com/wp-content/uploads/2016/02/cancela_barber_lo-skin-fade-pompadour-1.jpg",
            "https://s-media-cache-ak0.pinimg.com/originals/7c/5a/49/7c5a49f30440902f2b186d3a427297ef.jpg",
            "https://s-media-cache-ak0.pinimg.com/originals/8a/94/9c/8a949ca57a41b0fec132afffb6cff36c.jpg",
            "https://2.bp.blogspot.com/-E3eb_GDnlvo/WDmWeiE6uEI/AAAAAAAAcxY/gYNC9ZS39KswvngFlK34EyhUBpsbbgo4QCLcB/s1600/hair-men-short-2017%2B%25283%2529.jpg",
            "http://www.all-aboutfashion.com/wp-content/uploads/2016/07/4-54.jpg",
            "http://www.all-aboutfashion.com/wp-content/uploads/2016/07/5-55.jpg",
            "https://i.ytimg.com/vi/W2tK3F6RZPo/hqdefault.jpg",
            "http://www.sayidy.net/sites/default/files/styles/660xauto/public/main/articles/shutterstock_164435612.jpg?itok=0gRSlCNm",
            "https://i.ytimg.com/vi/bvF6ixC6zoM/maxresdefault.jpg",
            "http://img.soutalomma.com/ArticleImgs/2017/4/26/40797-%D9%82%D8%B5%D8%A9-%D8%B4%D8%B9%D8%B1-%D9%84%D8%A7%D8%B9%D8%A8%D9%88-%D9%83%D8%B1%D8%A9-%D8%A7%D9%84%D9%82%D8%AF%D9%85.jpg",
            "http://www.menshairstyletrends.com/wp-content/uploads/2015/08/blendz_barbershop_-_By__criztofferson_here_at__blendz_barbershop.jpg",
            "http://3.bp.blogspot.com/-1Wu6R0HMckM/U_3k5qyKGWI/AAAAAAAABvQ/7JXFdwEWNs0/s1600/%D8%AA%D8%B3%D8%B1%D9%8A%D8%AD%D8%A9-%D8%A7%D8%B4%D8%B1-%D8%A8%D9%88%D9%88%D9%83-%D8%A7%D9%84%D8%B4%D8%A7%D8%A6%D9%83%D8%A9%2B-%D9%84%D9%84%D8%B1%D8%AC%D8%A7%D9%84-%D9%84%D8%A8%D8%AF%D8%A7%D9%8A%D8%A9-%D8%A7%D9%84%D9%85%D9%88%D8%B3%D9%85-%D8%A7%D9%84%D8%AF%D8%B1%D8%A7%D8%B3%D9%8A.jpg",
            "http://www.allshorthairstyles.com/wp-content/uploads/2016/12/Spiky-Light-Brown-Hair-554x800.jpg",
            "https://lh3.googleusercontent.com/ABM6Tejc2Ny3t4VD5W04WcOexkhWFzOi0_VcPEkJN_MItzfjLU5SmmStgaOTIvIHACI=h900",
            "http://ift.tt/2cUdFZF",
            "https://s-media-cache-ak0.pinimg.com/736x/0c/c8/d0/0cc8d0cebf608371a44f5c61ce58bc26--joel-alexander-google-search.jpg",
            "http://www.sayidy.net/sites/default/files/styles/660xauto/public/main/articles/mad_men_hair_style.jpg?itok=PFgFtFLY",
            "http://nicee.cc/wp-content/uploads/2017/04/790-6.jpg",
            "http://n4hr.com/up/uploads/c015986765.jpg",
            "https://i.ytimg.com/vi/65oxK-DgnZs/maxresdefault.jpg",
            "https://barbarianstyle.net/wp-content/uploads/2016/01/Black-undercut-hairstyle-with-shaved-designs.jpg",
            "https://s-media-cache-ak0.pinimg.com/originals/8e/7f/7e/8e7f7e9dab33c48ba666878ffbdeeb93.jpg",
            "http://www.hiamag.com/sites/default/files/styles/ph2_960_600/public/article/24/04/2016/%20%D8%AC%D9%88%D8%A7%D9%86%D8%A8%20%D8%A7%D9%84%D8%B4%D8%B9%D8%B1%20%D9%85%D8%B9%20%D8%AA%D8%B1%D9%83%20%D8%B4%D8%B9%D8%B1%20%D8%A7%D9%84%D8%B1%D8%A7%D8%B3%20%D8%B7%D9%88%D9%8A%D9%84%D8%A7.jpg?itok=HQbaB6tR",
            "http://taplamdep.net/wp-content/uploads/2015/09/nhung-kieu-toc-nam-dep-vuot-nguoc-mang-phong-cac-sang-trong-lich-lam-cho-quy-ong-tuoi-trung-nien6.jpg",
            "http://www.shammil.com/wp-content/uploads/unnamed-file-498-500x500.jpg",
            "https://raqyi.com/wp-content/uploads/2016/12/%D8%A3%D8%AD%D8%AF%D8%AB-%D9%82%D8%B5%D8%A7%D8%AA-%D8%A7%D9%84%D8%B4%D8%B9%D8%B1-%D9%84%D9%84%D8%B1%D8%AC%D8%A7%D9%84-%D9%84%D9%87%D8%B0%D8%A7-%D8%A7%D9%84%D8%B9%D8%A7%D9%853.jpg",
            "https://elyana.info/images5/0117L/boys-haircut-2017/boys-haircut-2017-57_17.jpg",
            "http://lorrye.net/images5/0117/new-haircuts-2017-for-long-hair/new-haircuts-2017-for-long-hair-18_15.jpg",
            "https://s-media-cache-ak0.pinimg.com/736x/80/4b/43/804b4367a28b42db5bb437f2ec8dcf12.jpg",
            "https://lh3.googleusercontent.com/idnioTz-gra_hpTDGs3n-BNQRsmV759AycLxWqQH0mlCIPSrLahKJ3Z37eW9g-Za1g=h900",
            "http://www.shammil.com/wp-content/uploads/unnamed-file-499-500x500.jpg",
            "https://i.ytimg.com/vi/K77kKEU9tfs/maxresdefault.jpg",
            "https://s-media-cache-ak0.pinimg.com/originals/c8/d6/4e/c8d64ee7c89e9bd5f2c2f0492b0ad436.jpg",
            "http://www.mens-hairstylists.com/wp-content/uploads/2015/10/cool-summer-hairstyle-for-men.jpg",
            "http://www.menshairstyletrends.com/wp-content/uploads/2016/12/javi_thebarber_-medium-hairstyle-men.jpg",
            "https://s-media-cache-ak0.pinimg.com/236x/dd/94/21/dd94214942b10e838e8ab2973da132a7--men-beard-style-short-beard-styles-short.jpg",
            "http://4.bp.blogspot.com/-OCxrEGSO2nk/UjV0yN2R-uI/AAAAAAAAAgg/bSJ3UQRjR8M/s400/Mullet-Hairstyles-2013.jpg",
            "http://www.mens-hairstyle.com/wp-content/uploads/2016/12/Long-Top-Haircuts-for-Men.jpg",
            "https://s-media-cache-ak0.pinimg.com/originals/1b/b6/33/1bb6333801259d30457568ac3a58ae10.jpg",
            "https://s-media-cache-ak0.pinimg.com/736x/84/be/cc/84beccaff1e5f07b46814b4a04aa0d94--hipster-hairstyles-hairstyle-for-women.jpg",
            "http://hairstyleonpoint.com/wp-content/uploads/2015/08/11811288_937523026308702_756256385143542710_n.jpg"
    };

    public static void hideLoadingView(View viewToAttach, BaseFragment fragment) {
//        showHideLoadingView(viewToAttach, fragment, false, null);
    }

    public static void showLoadingView(View viewToAttach, BaseFragment fragment) {
//        showHideLoadingView(viewToAttach, fragment, true, null);
    }

    public static void showSweetLoadingDialog() {
//
//        if (loadingDialog == null) {
//
//            loadingDialog = new SweetAlertDialog(ThisApplication.getCurrentActivity(), SweetAlertDialog.PROGRESS_TYPE);
//
//            loadingDialog.getProgressHelper().setBarColor(getColor(R.color.WHITE));
//
//            loadingDialog.setTitleText(getString(R.string.LOADING));
//
//            loadingDialog.setCancelable(false);
//
//        }
//
//        loadingDialog.show();
    }

    private static void showHideLoadingView(View viewToAttach,
                                            BaseFragment fragment,
                                            boolean show,
                                            LoadingViewModel loadingViewModel) {
//
//        if (viewToAttach == null
//                || fragment == null
//                || fragment.getActivity() == null
//                || fragment.getActivity().isFinishing()
//                || fragment.isDestroyed()) {
//
//            return;
//
//        }
//
//        View loadingView = viewToAttach.findViewById(R.id.layout_loading_view);
//
//        if (loadingView == null) {
//
//            loadingView = LayoutInflater.from(ThisApplication.getCurrentActivity()).inflate(R.layout.layout_loading_view, (ViewGroup) viewToAttach, false);
//
//            loadingView.setBackgroundColor(Color.WHITE);
//
//            loadingView.setClickable(true);
//
//            ((ViewGroup) viewToAttach).addView(loadingView);
//
//        }
//
//        if (show) {
//
//            customizeLoadingView(viewToAttach, loadingView, loadingViewModel);
//
//        }
//
//        showHideLoadingViewInternal(loadingView, show);

    }

    private static void showHideLoadingViewInternal(View loadingView, boolean show) {

        //animate
        if (show) {

            loadingView.setVisibility(View.VISIBLE);

//
//                ObjectAnimator anim = ObjectAnimator.ofFloat(loadingView, "alpha", 0f, 1f);
//
//                anim.setDuration(YamsaferApplication.getmShortAnimationDuration());
//
//                anim.start();

        } else {

            loadingView.setVisibility(View.GONE);

//                if (true) return;
//
//                loadingView.setClickable(false);
//
//                ObjectAnimator anim = ObjectAnimator.ofFloat(loadingView, "alpha", 1f, 0f);
//
//                anim.setDuration(YamsaferApplication.getmShortAnimationDuration());
//
//                anim.addListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        super.onAnimationEnd(animation);
//
//                        loadingView.setVisibility(View.GONE);
//
//                    }
//                });
//
//                anim.start();

        }

    }

    private static void customizeLoadingView(View viewToAttach, View loadingView, LoadingViewModel loadingViewModel) {

        // Upper text
        TextView upperTextView = (TextView) loadingView.findViewById(R.id.upper_loading_layout_label);

        if (upperTextView != null) {

            upperTextView.setText(loadingViewModel == null || loadingViewModel.getUpperText() == null ? "" : loadingViewModel.getUpperText());

        }

        // Lower text
        TextView lowerTextView = (TextView) loadingView.findViewById(R.id.lower_loading_layout_label);

        if (lowerTextView != null) {

            lowerTextView.setText(loadingViewModel == null || loadingViewModel.getLowerText() == null ? "" : loadingViewModel.getLowerText());

        }

        // Layout params
        if (loadingViewModel != null && loadingViewModel.getLayoutParams() != null) {

            loadingView.setLayoutParams(loadingViewModel.getLayoutParams());

        } else if (viewToAttach instanceof RelativeLayout) {

            loadingView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        } else if (viewToAttach instanceof LinearLayout) {

            loadingView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        } else if (viewToAttach instanceof FrameLayout) {

            loadingView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        } else {

            loadingView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }

        // Alpha
        loadingView.setAlpha(loadingViewModel != null ? loadingViewModel.getAlpha() : 1.0f);

    }

    public static void showConfirmLanguageChangeDialog(Context context, final CustomSwitch langSwitch) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(ThisApplication.getCurrentActivity().getApplicationContext().getResources().getString(R.string.CONFILM_LANG_CHANGE_TITLE))
                .setMessage(ThisApplication.getCurrentActivity().getApplicationContext().getResources().getString(R.string.CONFILM_LANG_CHANGE_TEXT))
                .setPositiveButton(ThisApplication.getCurrentActivity().getApplicationContext().getResources().getString(R.string.YES), (dialog, which) -> {
                    Language lang = UserDefaultUtil.isAppLanguageArabic() ? Language.EN : Language.AR;
                    langSwitch.setChecked(!langSwitch.isChecked());
                    UserDefaultUtil.setAppLanguage(lang);
                })
                .setNegativeButton(ThisApplication.getCurrentActivity().getApplicationContext().getResources().getString(R.string.NO), (dialog, which) -> dialog.dismiss()).setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public static Point getScreenSize() {

        Display display = ThisApplication.getCurrentActivity().getWindowManager().getDefaultDisplay();

        Point size = new Point();

        display.getSize(size);

        return size;
    }


    public static void hideSweetLoadingDialog() {

        if (loadingDialog != null && loadingDialog.isShowing()) {

            loadingDialog.hide();

        }
    }

    public static void loadUrlIntoImageView(Context context, String urlString, final ImageView imageView, Sizes size) {

        if (Strings.isNullOrEmpty(urlString)) return;

        String urlStringWithSize = urlString.replace("[size]", size != null ? size.getValue() : Sizes.LARGE.getValue())
                .replace("/medium/", "/" + (size != null ? size.getValue() : Sizes.LARGE.getValue()) + "/")
                .replace("/large/", "/" + (size != null ? size.getValue() : Sizes.LARGE.getValue()) + "/");

        Uri uri;

        if (urlString.contains("http")) {
            if (urlStringWithSize.contains("uploads")) {
                //noinspection ResultOfMethodCallIgnored
                urlStringWithSize = urlStringWithSize.replace("http://tasree7a.ps/uploads/", IMAGES_URL);
            }

            uri = Uri.parse(urlStringWithSize);
        } else {
            uri = Uri.parse(IMAGES_URL + urlStringWithSize);
        }

        final DrawableRequestBuilder<Uri> imageRequest = Glide.with(context)
                .load(uri)
                .placeholder(R.drawable.default_image)
                .dontAnimate();

        if (size != Sizes.LARGE) {
            imageRequest.centerCrop();
        }

        Log.d("IMAGE_URL", uri.toString());

        if (context instanceof AppCompatActivity)
            ((AppCompatActivity) context).runOnUiThread(() -> imageRequest.into(imageView));
        else
            imageRequest.into(imageView);
    }


    public static int getResourceId(String pVariableName, String pResourcename) {

        try {

            return ThisApplication.getCurrentActivity().getResources().getIdentifier(pVariableName, pResourcename, ThisApplication.getCurrentActivity().getPackageName());

        } catch (Throwable t) {

            Log.e("status", "error in getting resource id ", t);

            return -1;
        }
    }

    public static int dpToPx(float dp) {

        float resultPix = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, ThisApplication.getCurrentActivity().getResources().getDisplayMetrics());

        return (int) resultPix;
    }


    public static int getColor(int colorResId) {

        return ThisApplication.getCurrentActivity().getResources().getColor(colorResId);

    }


    public static int changeAlpha(int color, float alpha) {

        int aChannel = (int) (alpha * 255);

        return Color.argb(aChannel, Color.red(color), Color.green(color), Color.blue(color));
    }


    public static void hideSoftKeyboard(EditText editText) {

        if (ThisApplication.getCurrentActivity() == null) {

            return;

        }

        // Hide soft keyboard if it is visible
        InputMethodManager inputManager = (InputMethodManager) ThisApplication.getCurrentActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        View v = editText != null ? editText : ThisApplication.getCurrentActivity().getCurrentFocus();

        if (v != null) {

            changeSoftKeyboardMode(FragmentManager.getCurrentVisibleFragment(), WindowManager.LayoutParams
                    .SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        }

    }


    public static void forceHideKeyboard(EditText editText) {

        Log.d("keyboard", "forceHide!");

        if (ThisApplication.getCurrentActivity() == null) {

            return;

        }

        // Hide soft keyboard if it is visible
        InputMethodManager inputManager = (InputMethodManager) ThisApplication.getCurrentActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        View v = editText != null ? editText : ThisApplication.getCurrentActivity().getCurrentFocus();

        if (v != null) {

            changeSoftKeyboardMode(FragmentManager.getCurrentVisibleFragment(), WindowManager.LayoutParams
                    .SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

        }


    }


    public static void forceHideKeyboard() {

        forceHideKeyboard(null);

    }


    public static void showSoftKeyboard(EditText text) {

        if (text == null) {

            return;

        }

        InputMethodManager imm = (InputMethodManager) ThisApplication.getCurrentActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        text.requestFocus();
    }


    public static void hideSoftKeyboard() {

        hideSoftKeyboard(null);

    }


    public static String getString(int id) {

        return ThisApplication.getCurrentActivity().getString(id);

    }


    public static GradientDrawable generateShapeBackground(int backgroundColor, int strokeColor, int shape, int radius) {

        GradientDrawable shapeDrawable = new GradientDrawable();

        shapeDrawable.setShape(shape);

        if (shape == GradientDrawable.RECTANGLE) {

            shapeDrawable.setCornerRadii(new float[]{dpToPx(radius), dpToPx(radius), dpToPx(radius), dpToPx(radius),
                    dpToPx(radius), dpToPx(radius), dpToPx(radius), dpToPx(radius)});

        }

        shapeDrawable.setColor(backgroundColor);

        shapeDrawable.setStroke(dpToPx(1f), strokeColor);

        return shapeDrawable;

    }


    public static int dpToPx(int dp) {

        float resultPix = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getApplicationContext().getResources().getDisplayMetrics());

        return (int) resultPix;
    }


    public static void changeSoftKeyboardMode(BaseFragment fragment, int keyboardMode) {

        if (fragment != null
                && !fragment.isDestroyed()
                && fragment.getActivity() != null
                && fragment.getActivity().getWindow() != null) {

            fragment.getActivity().getWindow().setSoftInputMode(keyboardMode);

        }
    }
}
