package com.sage.cabapp.ui.chatmodulenew;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.sage.cabapp.BR;
import com.sage.cabapp.R;
import com.sage.cabapp.SageApp;
import com.sage.cabapp.ViewModelProviderFactory;
import com.sage.cabapp.databinding.ActivityChatNewBinding;
import com.sage.cabapp.twilio.chat.ChatClientManager;
import com.sage.cabapp.twilio.chat.TaskCompletionListener;
import com.sage.cabapp.ui.base.BaseActivity;
import com.sage.cabapp.ui.chatmodulenew.model.JoinMemberRequest;
import com.sage.cabapp.ui.chatmodulenew.model.JoinMemberResponse;
import com.sage.cabapp.utils.ApiConstants;
import com.sage.cabapp.utils.Constant;
import com.sage.cabapp.utils.SharedData;
import com.twilio.chat.CallbackListener;
import com.twilio.chat.Channel;
import com.twilio.chat.ChatClient;
import com.twilio.chat.ChatClientListener;
import com.twilio.chat.ErrorInfo;
import com.twilio.chat.StatusListener;
import com.twilio.chat.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

/**
 * Created by Maroof Ahmed Siddique on 24-04-2020.
 * MCT
 * maroofahmedsiddique@gmail.com
 */
public class ChatModuleNewActivity extends BaseActivity<ActivityChatNewBinding, ChatModuleNewViewModel> implements ChatModuleNewNavigator, ChatClientListener {

    @Inject
    ViewModelProviderFactory factory;
    ChatModuleNewViewModel chatModuleNewViewModel;
    ActivityChatNewBinding activityChatNewBinding;

    private ChatClientManager chatClientManager;

    private MainChatFragment chatFragment;

    private String channelName = "";
    public static String driverName = "";
    private String driverID = "";
    public static String driverPic = "";

    @Override
    public int getBindingVariable() {
        return BR.chatNewViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_chat_new;
    }

    @Override
    public ChatModuleNewViewModel getViewModel() {
        chatModuleNewViewModel = ViewModelProviders.of(this, factory).get(ChatModuleNewViewModel.class);
        return chatModuleNewViewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityChatNewBinding = getViewDataBinding();
        chatModuleNewViewModel.setNavigator(this);

        chatFragment = new MainChatFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, chatFragment);
        fragmentTransaction.commit();

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("REQUEST_ID")) {
                channelName = intent.getStringExtra("REQUEST_ID");
                driverName = intent.getStringExtra("DRIVER_NAME");
                driverID = intent.getStringExtra("DRIVER_ID");
                driverPic = intent.getStringExtra("DRIVER_PIC");
            }
        }

        activityChatNewBinding.driverName.setText(driverName);

        checkTwilioClient();
    }

    private void checkTwilioClient() {

        showLoading("");

        chatClientManager = SageApp.get().getChatClientManager();
        if (chatClientManager.getChatClient() == null) {
            initializeClient();
        } else {
            populateChannels();
        }
    }

    private void initializeClient() {
        chatClientManager.connectClient(new TaskCompletionListener<Void, String>() {
            @Override
            public void onSuccess(Void aVoid) {
                populateChannels();
            }

            @Override
            public void onError(String errorMessage) {
                hideLoading();
            }
        });
    }

    private void populateChannels() {

        List<Channel> channels = chatClientManager.getChatClient().getChannels().getSubscribedChannels();

        Channel channelID = null;
        for (Channel channel : channels) {

            if (channel.getFriendlyName().equalsIgnoreCase(channelName)) {
                channelID = channel;
                break;
            }
        }

        if (channelID != null) {
            chatFragment.setCurrentChannel(channelID);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    hideLoading();
                }
            }, 3000);
        } else {
            if (isNetworkConnected()) {
                createChannelWithName(channelName, channelName);
            } else {
                hideLoading();
                vibrate();
                Constant.showErrorToast(getResources().getString(R.string.internet_not_available), this);
            }
        }
    }


    public void createChannelWithName(String name, String uniqueName) {
        chatClientManager.getChatClient().getChannels()
                .channelBuilder()
                .withFriendlyName(name)
                .withUniqueName(uniqueName)
                .withType(Channel.ChannelType.PRIVATE)
                .build(new CallbackListener<Channel>() {
                    @Override
                    public void onSuccess(final Channel channel) {
                        joinChannel(channel);
                    }

                    @Override
                    public void onError(ErrorInfo errorInfo) {
                        alreadyCreatedChannel();
                    }
                });
    }

    private void alreadyCreatedChannel() {

        List<Channel> channels = chatClientManager.getChatClient().getChannels().getSubscribedChannels();
        Channel channelID = null;
        for (Channel channel : channels) {

            if (channel.getFriendlyName().equalsIgnoreCase(channelName)) {
                channelID = channel;
                break;
            }
        }

        hideLoading();

        if (channelID != null) {
            chatFragment.setCurrentChannel(channelID);
        } else {
            vibrate();
            Constant.showErrorToast("No channel unavailable", this);
            return;
        }
    }

    private void joinChannel(final Channel channel) {
        channel.join(new StatusListener() {
            @Override
            public void onSuccess() {
                alreadyCreatedChannel();
            }

            @Override
            public void onError(ErrorInfo errorInfo) {
                hideLoading();
            }
        });

        if (isNetworkConnected()) {
            riderTwilioWS(this, channel);
        }
    }

    public String getTime() {
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat mdformat = new SimpleDateFormat("dd MMM yyyy HH:mm");
        return mdformat.format(calendar.getTime());
    }

    @Override
    public void onBack() {
        finish();
        slideTopToBottom();
    }

    @Override
    public void onBackPressed() {
        finish();
        slideTopToBottom();
    }

    @Override
    public void callPhone() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        new Handler().post(() -> {
            chatClientManager.shutdown();
            SageApp.get().getChatClientManager().setChatClient(null);
        });
    }

    @Override
    public void onChannelJoined(Channel channel) {

    }

    @Override
    public void onChannelInvited(Channel channel) {

    }

    @Override
    public void onChannelAdded(Channel channel) {

    }

    @Override
    public void onChannelUpdated(Channel channel, Channel.UpdateReason updateReason) {

    }

    @Override
    public void onChannelDeleted(Channel channel) {

    }

    @Override
    public void onChannelSynchronizationChange(Channel channel) {

    }

    @Override
    public void onError(ErrorInfo errorInfo) {

    }

    @Override
    public void onUserUpdated(User user, User.UpdateReason updateReason) {

    }

    @Override
    public void onUserSubscribed(User user) {

    }

    @Override
    public void onUserUnsubscribed(User user) {

    }

    @Override
    public void onClientSynchronization(ChatClient.SynchronizationStatus synchronizationStatus) {

    }

    @Override
    public void onNewMessageNotification(String s, String s1, long l) {

    }

    @Override
    public void onAddedToChannelNotification(String s) {

    }

    @Override
    public void onInvitedToChannelNotification(String s) {

    }

    @Override
    public void onRemovedFromChannelNotification(String s) {

    }

    @Override
    public void onNotificationSubscribed() {

    }

    @Override
    public void onNotificationFailed(ErrorInfo errorInfo) {

    }

    @Override
    public void onConnectionStateChange(ChatClient.ConnectionState connectionState) {

    }

    @Override
    public void onTokenExpired() {

    }

    @Override
    public void onTokenAboutToExpire() {

    }

    void riderTwilioWS(Context context, Channel channel) {

        String riderID = SharedData.getString(context, Constant.USERID);
        String riderName = SharedData.getString(context, Constant.FIRST_NAME);

        JoinMemberRequest joinMemberRequest = new JoinMemberRequest();

        joinMemberRequest.setRiderId(riderID);
        joinMemberRequest.setRiderName(riderName);
        joinMemberRequest.setDriverId(driverID);
        joinMemberRequest.setDriverName(driverName);
        joinMemberRequest.setJoinedMemberId(driverID);
        joinMemberRequest.setRequestId(channelName);
        joinMemberRequest.setChannelId(channel.getSid());
        joinMemberRequest.setChannelName(channel.getUniqueName());

        AndroidNetworking.post(ApiConstants.joinTwilio)
                .addApplicationJsonBody(joinMemberRequest)
                .setTag("joinTwilio")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsObject(JoinMemberResponse.class, new ParsedRequestListener<JoinMemberResponse>() {

                    @Override
                    public void onResponse(JoinMemberResponse response) {
                        joinTwilioChatResponse(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        errorAPI(anError);
                    }
                });
    }

    private void joinTwilioChatResponse(JoinMemberResponse response) {

    }

    public void errorAPI(ANError anError) {
        vibrate();
        Constant.showErrorToast(getResources().getString(R.string.toast_something_wrong), this);
    }
}
