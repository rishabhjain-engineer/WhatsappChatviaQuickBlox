package com.example.anupama.quickblox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.utils.DialogUtils;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;

public class ChatListActivity extends AppCompatActivity {

    private RecyclerView mContactRv ;
    private Button mCreateChatDialogBtn ;
    private LinearLayoutManager mLinearLayoutManager;
    private ChatListAdapter mChatListAdapter ;
    private ArrayList<QBUser> qbUserArrayList = new ArrayList<>();
    private ProgressBar mProgressBar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        initComponent();
        retrieveAllUser();
    }

    private void initComponent(){

        mContactRv = findViewById(R.id.contact_list);
        mProgressBar = findViewById(R.id.contact_pb);
        mCreateChatDialogBtn = findViewById(R.id.create_dialog_btn);

        mProgressBar.setVisibility(View.VISIBLE);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mContactRv.setLayoutManager(mLinearLayoutManager);
        mContactRv.setHasFixedSize(true);
        mChatListAdapter = new ChatListAdapter(qbUserArrayList);
        mContactRv.setAdapter(mChatListAdapter);

        mCreateChatDialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Checked Userlist, to create chat dialog.
                int value = mChatListAdapter.getCheckeduserList().size() ;
                if(value==0){
                    Toast.makeText(ChatListActivity.this,"Select atleast 1 user, to create chat dialog.",Toast.LENGTH_SHORT).show();
                }else if(value==1){
                    createPrivateChat();
                }else {
                    createGroupChat();
                }
            }
        });
    }

    private void retrieveAllUser(){
        QBUsers.getUsers(null).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {
               for(QBUser u : qbUsers){
                   if(!u.getLogin().equalsIgnoreCase(ChatSingleton.getChatInstance().chatService().getUser().getLogin())){
                       qbUserArrayList.add(u);
                   }
                }

                mChatListAdapter.notifyDataSetChanged();
                mProgressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onError(QBResponseException e) {
                Log.e("Rishabh","\n\n User QBResponseException: "+e.toString());
            }
        });

    }

    private void createPrivateChat(){

        QBUser qbUser = mChatListAdapter.getCheckeduserList().get(0);
        QBChatDialog dialog = DialogUtils.buildPrivateDialog(mChatListAdapter.getCheckeduserList().get(0).getId());
        QBRestChatService.createChatDialog(dialog).performAsync(new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                Toast.makeText(ChatListActivity.this,"Dialog created successfully.",Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(QBResponseException e) {
                Log.e("Rishabh","Dialog error. "+e.toString());
            }
        });
    }

    private void createGroupChat(){

    }




}
