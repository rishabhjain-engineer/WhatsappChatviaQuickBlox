package com.example.anupama.quickblox;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private EditText mUsernameET, mPasswordET ;
    private Button mSubmitBtn ;
    private MainViewModel mMainViewModel ;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponent();
        ChatSingleton.getChatInstance();
        mMainViewModel.getResponse().observe(this, new Observer<AsyncResponse>() {
            @Override
            public void onChanged(@Nullable AsyncResponse asyncResponse) {
                if(asyncResponse != null){
                    consumeResponse(asyncResponse);
                }
            }
        });
    }



    private void initComponent() {

        mUsernameET = findViewById(R.id.username_et);
        mPasswordET = findViewById(R.id.password_et);
        mSubmitBtn = findViewById(R.id.submit_btn);
        mProgressBar = findViewById(R.id.progressbar);

        mSubmitBtn.setOnClickListener(onClickListener);
        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
    }


    private void consumeResponse(AsyncResponse asyncResponse) {

        switch (asyncResponse.responseStatus){

            case LOADING:{
               break;
            }
            case SUCCESS:{
                mProgressBar.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(MainActivity.this,ChatDialogsActivity.class);
                intent.putExtra("username",mUsernameET.getEditableText().toString());
                intent.putExtra("password",mPasswordET.getEditableText().toString());
                startActivity(intent);
                finish();
                break;
            }
            case ERROR: {
                mProgressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this,asyncResponse.error.toString(),Toast.LENGTH_SHORT).show();
                break;
            }
            default: break;

        }

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()){

                case R.id.submit_btn: {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mMainViewModel.signInToChat(mUsernameET.getEditableText().toString(),
                                mPasswordET.getEditableText().toString());

                    break;
                }

                default: break;

            }
        }
    };


}
