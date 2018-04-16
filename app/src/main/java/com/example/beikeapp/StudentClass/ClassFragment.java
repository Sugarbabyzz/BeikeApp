package com.example.beikeapp.StudentClass;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.beikeapp.Adapter.GroupAdapter;
import com.example.beikeapp.R;
import com.example.beikeapp.Util.ChatUtil.AddGroupActivity;
import com.example.beikeapp.Util.ChatUtil.ChatActivity;
import com.example.beikeapp.Util.ChatUtil.NewGroupActivity;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClassFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClassFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<EMGroup> classGroupList;
    private ListView lvClassGroup;
    private GroupAdapter groupAdapter;

    private SwipeRefreshLayout swipeRefreshLayout;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            swipeRefreshLayout.setRefreshing(false);
            switch (msg.what) {
                case 0:
                    refresh();
                    break;
                case 1:
                    Toast.makeText(getActivity(), R.string.Failed_to_get_group_chat_information, Toast.LENGTH_LONG).show();
                    break;

                default:
                    break;
            }
        }
    };


    public ClassFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClassFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ClassFragment newInstance(String param1, String param2) {
        ClassFragment fragment = new ClassFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_jiaxiao, null);

        lvClassGroup = view.findViewById(R.id.lv_group_list);
        swipeRefreshLayout = view.findViewById(R.id.group_chat_swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);


        //下拉刷新群组列表
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
                            handler.sendEmptyMessage(0);
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            handler.sendEmptyMessage(1);
                        }
                    }
                }.start();
            }
        });

        //注册列表项的点击事件的监听
        lvClassGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    // 建群
                    startActivity(new Intent(getActivity(),NewGroupActivity.class));
                } else if (position == 1) {
                    // 加群
                    startActivity(new Intent(getActivity(), AddGroupActivity.class));
                } else {
                    // 进入对应群聊
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    intent.putExtra("chatType", EaseConstant.CHATTYPE_GROUP);
                    intent.putExtra("userId", groupAdapter.getItem(position - 2).getGroupId());
                    startActivityForResult(intent, 0);
                }
            }

        });

        getGroupsOnCreate();

        return view;
    }

    /**
     * 首次创建Fragment时从环信服务器获取群组列表。
     * 同步方法，线程中进行。
     */
    private void getGroupsOnCreate() {
        new Thread() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
                    handler.sendEmptyMessage(0);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(1);
                }
            }
        }.start();
        classGroupList = EMClient.getInstance().groupManager().getAllGroups();
        groupAdapter = new GroupAdapter(getActivity(), 1, classGroupList);
        lvClassGroup.setAdapter(groupAdapter);
        groupAdapter.notifyDataSetChanged();
    }

    /**
     * 刷新群组列表
     */
    private void refresh() {
        classGroupList = EMClient.getInstance().groupManager().getAllGroups();
        groupAdapter = new GroupAdapter(getActivity(), 1, classGroupList);
        lvClassGroup.setAdapter(groupAdapter);
        groupAdapter.notifyDataSetChanged();
    }

}
