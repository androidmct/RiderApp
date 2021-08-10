package com.sage.cabapp.ui.chatmodulenew;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sage.cabapp.R;
import com.sage.cabapp.utils.Constant;
import com.sage.cabapp.utils.SharedData;
import com.shrikanthravi.chatview.widget.ChatView;
import com.twilio.chat.CallbackListener;
import com.twilio.chat.Channel;
import com.twilio.chat.ChannelListener;
import com.twilio.chat.ErrorInfo;
import com.twilio.chat.Member;
import com.twilio.chat.Message;
import com.twilio.chat.Messages;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import timber.log.Timber;


/**
 * Created by Maroof Ahmed Siddique on 24-04-2020.
 * MCT
 * maroofahmedsiddique@gmail.com
 */
public class MainChatFragment extends Fragment implements ChannelListener {

    Context context;
    Activity mainActivity;
    Channel currentChannel;
    ChatView chatView;

    String name = "";
    String personImage = "";

    public MainChatFragment() {
    }

    public static MainChatFragment newInstance() {
        return new MainChatFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getActivity();
        mainActivity = this.getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_chat, container, false);
        chatView = view.findViewById(R.id.chatView);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        name = SharedData.getString(Objects.requireNonNull(getActivity()), Constant.FIRST_NAME);
        personImage = SharedData.getString(Objects.requireNonNull(getActivity()), Constant.PROFILE_PIC);

        chatView.setOnClickSendButtonListener(new ChatView.OnClickSendButtonListener() {
            @Override
            public void onSendButtonClick(String body) {

                if (body != null && !body.equalsIgnoreCase("")) {

                    sendMessages(body);

                }
            }
        });
    }

    public void setCurrentChannel(Channel currentChannel) {
        if (currentChannel == null) {
            this.currentChannel = null;
            return;
        }
        this.currentChannel = currentChannel;
        this.currentChannel.addListener(this);
        allMessages();
    }

    private void allMessages() {

        final Messages[] messagesObject = {null};

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                messagesObject[0] = currentChannel.getMessages();

                String userid = SharedData.getString(Objects.requireNonNull(getActivity()), Constant.USERID);

                if (messagesObject[0] != null) {
                    messagesObject[0].getLastMessages(100, new CallbackListener<List<Message>>() {
                        @Override
                        public void onSuccess(List<com.twilio.chat.Message> messageList) {
                            if (messageList != null && messageList.size() > 0) {
                                for (int i = 0; i < messageList.size(); i++) {

                                    com.shrikanthravi.chatview.data.Message message1 = new com.shrikanthravi.chatview.data.Message();
                                    if (messageList.get(i).getMember().getIdentity().equalsIgnoreCase(userid)) {

                                        message1.setMessageType(com.shrikanthravi.chatview.data.Message.MessageType.RightSimpleImage);
                                        message1.setUserIcon(personImage);
                                    } else {
                                        message1.setMessageType(com.shrikanthravi.chatview.data.Message.MessageType.LeftSimpleMessage);
                                        message1.setUserIcon(ChatModuleNewActivity.driverPic);
                                    }
                                    //message1.setUserName(messageList.get(i).getMember().getIdentity());
                                    message1.setUserName("");
                                    message1.setBody(messageList.get(i).getMessageBody());
                                    message1.setTime(getTime());
                                    chatView.addMessage(message1);
                                }
                            }
                        }
                    });
                }
            }
        }, 1000);
    }

    public String getTime() {
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat mdformat = new SimpleDateFormat("dd MMM yyyy HH:mm");
        String time = mdformat.format(calendar.getTime());
        return time;
    }

    private void sendMessages(String body) {
        Messages messagesObject;
        messagesObject = currentChannel.getMessages();

        com.twilio.chat.Message.Options messageOptions = com.twilio.chat.Message.options().withBody(body);
        messagesObject.sendMessage(messageOptions, new CallbackListener<com.twilio.chat.Message>() {

            @Override
            public void onSuccess(com.twilio.chat.Message message) {
                Timber.d("Message sent successfully %s", message.getMessageBody());
            }

            @Override
            public void onError(ErrorInfo errorInfo) {
                Timber.d("Error sending message %s", errorInfo.toString());
            }
        });
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onMessageAdded(com.twilio.chat.Message message) {

        String userid = SharedData.getString(Objects.requireNonNull(getActivity()), Constant.USERID);

        com.shrikanthravi.chatview.data.Message message1 = new com.shrikanthravi.chatview.data.Message();
        if (message.getMember().getIdentity().equalsIgnoreCase(userid)) {

            message1.setMessageType(com.shrikanthravi.chatview.data.Message.MessageType.RightSimpleImage);
            message1.setUserIcon(personImage);
        } else {
            message1.setMessageType(com.shrikanthravi.chatview.data.Message.MessageType.LeftSimpleMessage);
            message1.setUserIcon(ChatModuleNewActivity.driverPic);
        }
        // message1.setUserName(message.getMember().getIdentity());
        message1.setUserName("");
        message1.setBody(message.getMessageBody());
        message1.setTime(getTime());
        chatView.addMessage(message1);
    }

    @Override
    public void onMessageUpdated(com.twilio.chat.Message message, com.twilio.chat.Message.UpdateReason updateReason) {

    }

    @Override
    public void onMessageDeleted(com.twilio.chat.Message message) {

    }

    @Override
    public void onMemberAdded(Member member) {

    }

    @Override
    public void onMemberUpdated(Member member, Member.UpdateReason updateReason) {

    }

    @Override
    public void onMemberDeleted(Member member) {

    }

    @Override
    public void onTypingStarted(Channel channel, Member member) {

    }

    @Override
    public void onTypingEnded(Channel channel, Member member) {

    }

    @Override
    public void onSynchronizationChanged(Channel channel) {

    }
}
