package com.example.beikeapp.Util;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


import com.example.beikeapp.Constant.GlobalConstant;
import com.example.beikeapp.Constant.StudentConstant;
import com.example.beikeapp.InitApp.MyApplication;
import com.example.beikeapp.StudentMain.Homework.StudentAllHomework;
import com.example.beikeapp.StudentMain.Homework.StudentHomework;
import com.example.beikeapp.StudentNotify.Assess.StudentAllAssess;
import com.example.beikeapp.StudentNotify.Notify.Notify;
import com.example.beikeapp.StudentNotify.Notify.StudentAllNotify;
import com.example.beikeapp.StudentNotify.Notify.StudentNotify;
import com.example.beikeapp.StudentRegister.StudentRegisterInfo;
import com.example.beikeapp.StudentRegister.StudentRegisterSuccess;
import com.example.beikeapp.TeacherMain.Homework.Homework;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * PushMesssageReceiver广播接收器

 PushMessageReceiver是一个抽象的BroadcastReceiver类，
  它的用途有两种:
    1、获取服务器推送的消息
    2、获取调用MiPushClient方法的返回结果
 */
public class MessageReceiver extends PushMessageReceiver {
    private String mRegId;
    private long mResultCode = -1;
    private String mReason;
    private String mCommand;
    private String mMessage;
    private String mTopic;
    private String mAlias;
    private String mUserAccount;
    private String mStartTime;
    private String mEndTime;

    private Pattern pattern;
    private Matcher matcher;


    /**
     * onReceivePassThroughMessage用来接收服务器发送的透传消息，
     * @param context
     * @param message
     */
    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage message) {
        mMessage = message.getContent();
        if(!TextUtils.isEmpty(message.getTopic())) {
            mTopic=message.getTopic();
        } else if(!TextUtils.isEmpty(message.getAlias())) {
            mAlias=message.getAlias();
        } else if(!TextUtils.isEmpty(message.getUserAccount())) {
            mUserAccount=message.getUserAccount();
        }

        //打印消息方便测试
        System.out.println("透传消息到达了");
        System.out.println("透传消息是"+message.toString());
    }

    /**
     * onNotificationMessageClicked用来接收服务器发来的通知栏消息（用户点击通知栏时触发）
     * @param context
     * @param message
     */
    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage message) {
        mMessage = message.getContent();
        if(!TextUtils.isEmpty(message.getTopic())) {
            mTopic=message.getTopic();
        } else if(!TextUtils.isEmpty(message.getAlias())) {
            mAlias=message.getAlias();
        } else if(!TextUtils.isEmpty(message.getUserAccount())) {
            mUserAccount=message.getUserAccount();
        }



        pattern = Pattern.compile("(.*)(<category>)(.*?)(</category>)(.*)");
        matcher = pattern.matcher(message.toString());
        if (matcher.matches()){
            String category = matcher.group(3);

            if (category.equals("notify")){
                //start 跳转通知活动
                Intent intent = new Intent(context, StudentAllNotify.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
            else if (category.equals("homework")){
                //start 跳转作业活动
                Intent intent = new Intent(context, StudentAllHomework.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
            else if (category.equals("assess")){
                //start 跳转评教活动
                Intent intent = new Intent(context, StudentAllAssess.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }




        //打印消息方便测试
        System.out.println("\n用户点击了通知消息");
        System.out.println("\n通知消息是" + message.toString());
        System.out.println("\n点击后,会进入应用" );

    }

    /**
     * onNotificationMessageArrived用来接收服务器发来的通知栏消息（消息到达客户端时触发，并且可以接收应用在前台时不弹出通知的通知消息）
     * @param context
     * @param message
     */
    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage message) {
        mMessage = message.getContent();
        if(!TextUtils.isEmpty(message.getTopic())) {
            mTopic=message.getTopic();
        } else if(!TextUtils.isEmpty(message.getAlias())) {
            mAlias=message.getAlias();
        } else if(!TextUtils.isEmpty(message.getUserAccount())) {
            mUserAccount=message.getUserAccount();
        }

        //分类
        String category = null;
        //通知
        String title = null, name = null, content = null, time = null;
        //作业
        String size = null, subject = null, optionA = null, optionB = null, optionC = null, optionD = null, key = null;
        //评教

        pattern = Pattern.compile("(.*)(<category>)(.*?)(</category>)(.*)");
        matcher = pattern.matcher(message.toString());
        if (matcher.matches()){
            category = matcher.group(3);

            /**
             * 处理通知推送
             */
            if (category.equals("notify")){

                /*
                 * 接收通知 title
                 */
                pattern = Pattern.compile("(.*)(<title>)(.*?)(</title>)(.*)");
                matcher = pattern.matcher(message.toString());
                if (matcher.matches()){
                    title = matcher.group(3);
                }

                /*
                 * 接收通知 name
                 */
                pattern = pattern.compile("(.*)(<name>)(.*?)(</name>)(.*)");
                matcher = pattern.matcher(message.toString());
                if (matcher.matches()){
                    name = matcher.group(3);
                }

                /*
                 * 接收通知 content
                 */
                pattern = pattern.compile("(.*)(<content>)(.*?)(</content>)(.*)");
                matcher = pattern.matcher(message.toString());
                if (matcher.matches()){
                    content = matcher.group(3);
                }

                /*
                 * 接收通知 time
                 */
                pattern = pattern.compile("(.*)(<time>)(.*?)(</time>)(.*)");
                matcher = pattern.matcher(message.toString());
                if (matcher.matches()){
                    time = matcher.group(3);
                }

                System.out.println("\n解析通知消息：  title:" + title + "  name:" + name + "  content:" + content + "  time:" + time);

                //通知入库
                try {
                    addNotify(title, name, content, time);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
            /**
             * 处理作业推送
             */
            else if (category.equals("homework")){
                //处理作业   title name time size subject optionA optionB optionC optionD key

                //生成作业id
                int hwId = (int)(Math.random() * 1000000);

                /*
                 * 接收作业 title
                 */
                pattern = Pattern.compile("(.*)(<title>)(.*?)(</title>)(.*)");
                matcher = pattern.matcher(message.toString());
                if (matcher.matches()){
                    title = matcher.group(3);
                }

                /*
                 * 接收作业 name
                 */
                pattern = Pattern.compile("(.*)(<name>)(.*?)(</name>)(.*)");
                matcher = pattern.matcher(message.toString());
                if (matcher.matches()){
                    name = matcher.group(3);
                }

                /*
                 * 接收作业 time
                 */
                pattern = Pattern.compile("(.*)(<time>)(.*?)(</time>)(.*)");
                matcher = pattern.matcher(message.toString());
                if (matcher.matches()){
                    time = matcher.group(3);
                }

                /*
                 * 接收作业 size
                 */
                pattern = Pattern.compile("(.*)(<size>)(.*?)(</size>)(.*)");
                matcher = pattern.matcher(message.toString());
                if (matcher.matches()){
                    size = matcher.group(3);
                }


                /*
                 * 接收作业 内容
                 */
                String hw;
                pattern = Pattern.compile("<hw>.*?</hw>");
                matcher = pattern.matcher(message.toString());
                while (matcher.find()){
                    hw = matcher.group();

                    Pattern p = Pattern.compile("(.*)(<subject>)(.*?)(</subject>)(.*)");
                    Matcher m = p.matcher(hw);
                    if (m.matches()){
                        subject = m.group(3);
                    }

                    p = Pattern.compile("(.*)(<optionA>)(.*?)(</optionA>)(.*)");
                    m = p.matcher(hw);
                    if (m.matches()){
                        optionA = m.group(3);
                    }

                    p = Pattern.compile("(.*)(<optionB>)(.*?)(</optionB>)(.*)");
                    m = p.matcher(hw);
                    if (m.matches()){
                        optionB = m.group(3);
                    }

                    p = Pattern.compile("(.*)(<optionC>)(.*?)(</optionC>)(.*)");
                    m = p.matcher(hw);
                    if (m.matches()){
                        optionC = m.group(3);
                    }

                    p = Pattern.compile("(.*)(<optionD>)(.*?)(</optionD>)(.*)");
                    m = p.matcher(hw);
                    if (m.matches()){
                        optionD = m.group(3);
                    }

                    p = Pattern.compile("(.*)(<key>)(.*?)(</key>)(.*)");
                    m = p.matcher(hw);
                    if (m.matches()){
                        key = m.group(3);
                    }

                    //作业细节入库
                    try {
                        addHomeworkDetail(hwId, subject, optionA, optionB, optionC, optionD, key);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }


                }

                System.out.println("\n解析作业消息：  title:" + title + "  name:" + name + "  time:" + time + "  size:" + size);

                //作业入库
                try {
                    addHomework(title, name, time, size, hwId);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
            /**
             * 处理评教推送
             */
            else if (category.equals("assess")){
                //处理评教 待写
            }

        }





        //打印消息方便测试
        System.out.println("\n通知消息到达了");
        System.out.println("\n通知消息是"+message.toString());

    }

    /**
     * onCommandResult用来接收客户端向服务器发送命令消息后返回的响应
     * @param context
     * @param message
     */
    @Override
    public void onCommandResult(Context context, MiPushCommandMessage message) {
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String cmdArg2 = ((arguments != null && arguments.size() > 1) ? arguments.get(1) : null);
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_SET_ALIAS.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mAlias = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_UNSET_ALIAS.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mAlias = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_SUBSCRIBE_TOPIC.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mTopic = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_UNSUBSCRIBE_TOPIC.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mTopic = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_SET_ACCEPT_TIME.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mStartTime = cmdArg1;
                mEndTime = cmdArg2;
            }
        }

        //test
        System.out.println(command );


        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {

                //打印信息便于测试注册成功与否
                System.out.println("注册成功");

            } else {
                System.out.println("注册失败");
            }
        }
    }

    /**
     * onReceiveRegisterResult用来接受客户端向服务器发送注册命令消息后返回的响应
     * @param context
     * @param message
     */
    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage message) {
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String cmdArg2 = ((arguments != null && arguments.size() > 1) ? arguments.get(1) : null);
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = cmdArg1;
            }
        }

        //test
        System.out.println(command );

        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {

                //打印日志：注册成功
                System.out.println("注册成功");
            } else {
                //打印日志：注册失败
                System.out.println("注册失败");
            }
        } else {
            System.out.println("其他情况"+message.getReason());
        }

    }


    /**
     * 通知入库
     *
     * @param title
     * @param name
     * @param content
     * @param time
     */

    private void addNotify(String title, String name, String content, String time) throws UnsupportedEncodingException {

        String addNotify = StudentConstant.addNotifyURL
                + "?stuId=" + EMClient.getInstance().getCurrentUser()
                + "&title=" + java.net.URLEncoder.encode(title, "utf-8")
                + "&name=" + java.net.URLEncoder.encode(name, "utf-8")
                + "&content=" + java.net.URLEncoder.encode(content, "utf-8")
                + "&time=" + time;

        MyAsyncTask a = new MyAsyncTask(MyApplication.getContext());
        a.execute(addNotify);
        a.setOnAsyncResponse(new AsyncResponse() {
            @Override
            public void onDataReceivedSuccess(List<String> listData) {

                if(listData.get(0).equals(GlobalConstant.FLAG_SUCCESS)){
                    //添加通知成功
                    System.out.println("通知入库成功");

                }else {
                    //添加通知失败
                    System.out.println("通知入库失败");

                }
            }
            @Override
            public void onDataReceivedFailed() {
            }
        });
    }

    /**
     * 作业细节入库
     *
     * @param hwId
     * @param subject
     * @param optionA
     * @param optionB
     * @param optionC
     * @param optionD
     * @param key
     * @throws UnsupportedEncodingException
     */
    private void addHomeworkDetail(int hwId, String subject, String optionA, String optionB, String optionC, String optionD, String key)
                                    throws UnsupportedEncodingException {
        //URL待修改
        String addNotify = StudentConstant.addHomeworkDetailURL
                + "?hwID=" + hwId
                + "&subject=" + java.net.URLEncoder.encode(subject, "utf-8")
                + "&optionA=" + java.net.URLEncoder.encode(optionA, "utf-8")
                + "&optionB=" + java.net.URLEncoder.encode(optionB, "utf-8")
                + "&optionC=" + java.net.URLEncoder.encode(optionC, "utf-8")
                + "&optionD=" + java.net.URLEncoder.encode(optionD, "utf-8")
                + "&key=" + java.net.URLEncoder.encode(key, "utf-8");

        MyAsyncTask a = new MyAsyncTask(MyApplication.getContext());
        a.execute(addNotify);
        a.setOnAsyncResponse(new AsyncResponse() {
            @Override
            public void onDataReceivedSuccess(List<String> listData) {

                if(listData.get(0).equals(GlobalConstant.FLAG_SUCCESS)){
                    //添加作业细节成功
                    System.out.println("作业细节入库成功");

                }else {
                    //添加作业细节失败
                    System.out.println("作业细节入库失败");

                }
            }
            @Override
            public void onDataReceivedFailed() {
            }
        });
    }


    /**
     * 作业入库
     *
     * @param title
     * @param name
     * @param time
     * @param size
     * @param hwId
     * @throws UnsupportedEncodingException
     */
    private void addHomework(String title, String name, String time, String size, int hwId)
                                    throws UnsupportedEncodingException {
        //URL待修改
        String addNotify = StudentConstant.addHomeworkURL
                + "?stuId=" + EMClient.getInstance().getCurrentUser()
                + "&title=" + java.net.URLEncoder.encode(title, "utf-8")
                + "&name=" + java.net.URLEncoder.encode(name, "utf-8")
                + "&time=" + java.net.URLEncoder.encode(time, "utf-8")
                + "&size=" + java.net.URLEncoder.encode(size, "utf-8")
                + "&hwId=" + hwId;

        MyAsyncTask a = new MyAsyncTask(MyApplication.getContext());
        a.execute(addNotify);
        a.setOnAsyncResponse(new AsyncResponse() {
            @Override
            public void onDataReceivedSuccess(List<String> listData) {

                if(listData.get(0).equals(GlobalConstant.FLAG_SUCCESS)){
                    //添加通知成功
                    System.out.println("作业入库成功");

                }else {
                    //添加通知失败
                    System.out.println("作业入库失败");

                }
            }
            @Override
            public void onDataReceivedFailed() {
            }
        });
    }

}