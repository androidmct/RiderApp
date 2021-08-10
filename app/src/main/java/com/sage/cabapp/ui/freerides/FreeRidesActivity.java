package com.sage.cabapp.ui.freerides;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.sage.cabapp.BR;
import com.sage.cabapp.BuildConfig;
import com.sage.cabapp.R;
import com.sage.cabapp.ViewModelProviderFactory;
import com.sage.cabapp.databinding.ActivityFreeRidesBinding;
import com.sage.cabapp.ui.base.BaseActivity;
import com.sage.cabapp.utils.Constant;
import com.sage.cabapp.utils.SharedData;

import java.util.Objects;

import javax.inject.Inject;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.SharingHelper;
import io.branch.referral.util.LinkProperties;
import io.branch.referral.util.ShareSheetStyle;

/**
 * Created by Maroof Ahmed Siddique on 14-01-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class FreeRidesActivity extends BaseActivity<ActivityFreeRidesBinding, FreeRidesViewModel> implements FreeRidesNavigator {

    @Inject
    ViewModelProviderFactory factory;
    FreeRidesViewModel freeRidesViewModel;
    ActivityFreeRidesBinding activityFreeRidesBinding;

    private String smsMessage = "";

    @Override
    public int getBindingVariable() {
        return BR.freeRidesViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_free_rides;
    }

    @Override
    public FreeRidesViewModel getViewModel() {
        freeRidesViewModel = ViewModelProviders.of(this, factory).get(FreeRidesViewModel.class);
        return freeRidesViewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityFreeRidesBinding = getViewDataBinding();
        freeRidesViewModel.setNavigator(this);

        smsMessage = "Use my referral code and get 10% off for 2 weeks.";
    }

    @Override
    public void onBackPressed() {
        finish();
        slideTopToBottom();
    }

    @Override
    public void onBack() {
        finish();
        slideTopToBottom();
    }

    @Override
    public void shareText() {
        // Constant.shareOnlySMS(this, smsMessage);
        branchShare("msg");
    }

    @Override
    public void shareEmail() {
        // Constant.shareOnlyEmail(this, smsMessage);

        branchShare("gmail");
    }

    @Override
    public void shareFacebook() {
        branchShare("fb");
    }

    @Override
    public void shareOtherApps() {
        // Constant.shareOtherApps(this, smsMessage);
        branchShare("");
    }

    private void branchShare(String preferred) {

        String myReferral = SharedData.getString(this, Constant.MY_REFERRAL_CODE);

        BranchUniversalObject branchUniversalObject = new BranchUniversalObject()
                .setCanonicalIdentifier("item/12345")
                .setTitle(getResources().getString(R.string.app_name))
                .setContentDescription("Sage Rider App")
                //.setContentImageUrl(getResources().getString(R.string.app_logo_link))
                .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC);

        LinkProperties linkProperties = new LinkProperties()
                .setChannel(getResources().getString(R.string.facebook_channel))
                .setFeature(getResources().getString(R.string.sharing_feature))
                .addControlParameter("$ios_url", "")
                .setStage(myReferral)
                .addControlParameter("$android_url", "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "&hl=en");

        ShareSheetStyle shareSheetStyle = new ShareSheetStyle(this, "Sage", "Get 10% off when they ride! Your friends will get 10% off too for 2 weeks.\n Using this link :-")
                .setMoreOptionStyle(getResources().getDrawable(android.R.drawable.ic_menu_search), "Show more");

        if (shareSheetStyle != null) {
            if (preferred.equalsIgnoreCase("fb")) {
                shareSheetStyle.addPreferredSharingOption(SharingHelper.SHARE_WITH.FACEBOOK);
            } else if (preferred.equalsIgnoreCase("gmail")) {
                shareSheetStyle.addPreferredSharingOption(SharingHelper.SHARE_WITH.EMAIL);
            } else if (preferred.equalsIgnoreCase("msg")) {
                shareSheetStyle.addPreferredSharingOption(SharingHelper.SHARE_WITH.MESSAGE);
            }
        }

        branchUniversalObject.showShareSheet(this, linkProperties, Objects.requireNonNull(shareSheetStyle),
                new Branch.BranchLinkShareListener() {
                    @Override
                    public void onShareLinkDialogLaunched() {
                    }

                    @Override
                    public void onShareLinkDialogDismissed() {
                    }

                    @Override
                    public void onLinkShareResponse(String sharedLink, String sharedChannel, BranchError error) {

                    }

                    @Override
                    public void onChannelSelected(String channelName) {
                    }
                });
    }
}
