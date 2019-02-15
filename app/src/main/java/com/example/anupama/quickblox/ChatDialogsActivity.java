package com.example.anupama.quickblox;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.BaseService;
import com.quickblox.auth.session.QBSession;
import com.quickblox.auth.session.QBSessionManager;
import com.quickblox.auth.session.QBSessionParameters;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.BaseServiceException;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;

public class ChatDialogsActivity extends AppCompatActivity implements ChatDialogsAdapter.RowClicked {

    private RecyclerView mChatRv ;
    private RecyclerView.LayoutManager mLayoutManager ;
    private ChatDialogsAdapter mChatDialogsAdapter;
    private FloatingActionButton mFloatingActionButton ;
    private QBRequestGetBuilder mQBRequestGetBuilder ;
    private ArrayList<QBChatDialog> mQBChatDialogsList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_dialog);
        initComponent();
        createChatSession();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadChatDialogs();
    }

    void initComponent(){
        mChatRv = findViewById(R.id.chat_rv);
        mFloatingActionButton = findViewById(R.id.floating_btn);

        mLayoutManager = new LinearLayoutManager(this);
        mChatRv.setLayoutManager(mLayoutManager);
        mChatRv.setHasFixedSize(true);
        mChatDialogsAdapter = new ChatDialogsAdapter(mQBChatDialogsList,ChatDialogsActivity.this);
        mChatRv.setAdapter(mChatDialogsAdapter);

        mFloatingActionButton.setOnClickListener(onClickListener);
    }

    private void createChatSession() {

        //final QBUser qbUser = new QBUser("rishabhandroid", "quickblox");
        final QBUser qbUser = new QBUser("ayush", "quickblox");

        QBAuth.createSession(qbUser).performAsync(new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession qbSession, Bundle bundle) {
                qbUser.setId(qbSession.getUserId());
                try {
                    qbUser.setPassword(BaseService.getBaseService().getToken());
                } catch (BaseServiceException e) {
                    e.printStackTrace();
                }

                ChatSingleton.getChatInstance(ChatDialogsActivity.this).chatService().login(qbUser, new QBEntityCallback() {
                    @Override
                    public void onSuccess(Object o, Bundle bundle) {
                    }

                    @Override
                    public void onError(QBResponseException e) {
                    }
                });
            }

            @Override
            public void onError(QBResponseException e) {
            }
        });

    }

    private void loadChatDialogs(){

        mQBRequestGetBuilder = ChatSingleton.getChatInstance(ChatDialogsActivity.this).qbRequestGetBuilder();
        mQBRequestGetBuilder.setLimit(10);

        QBRestChatService.getChatDialogs(null,mQBRequestGetBuilder).performAsync(new QBEntityCallback<ArrayList<QBChatDialog>>() {
            @Override
            public void onSuccess(ArrayList<QBChatDialog> qbChatDialogs, Bundle bundle) {
                mQBChatDialogsList.addAll(qbChatDialogs);
                mChatDialogsAdapter.notifyDataSetChanged();
                // notify adapter
            }

            @Override
            public void onError(QBResponseException e) {
            }
        });

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){

                case R.id.floating_btn: {
                    Intent intent = new Intent(ChatDialogsActivity.this,ChatListActivity.class);
                    startActivity(intent);
                    break;
                }

                default: break;

            }
        }
    };

    @Override
    public void onTouch(QBChatDialog qbChatDialog) {
        Intent intent = new Intent(ChatDialogsActivity.this,ChatMessageActivity.class);
        intent.putExtra("chatDialog",qbChatDialog);
        startActivity(intent);
    }
}
