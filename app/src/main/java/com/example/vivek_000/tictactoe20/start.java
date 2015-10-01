package com.example.vivek_000.tictactoe20;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Typeface;

import android.os.Bundle;

import android.view.View;

import android.widget.Button;
import android.widget.ImageView;

import android.widget.TextView;




public class start extends Activity implements View.OnClickListener{

    Button newGame,settings,exit;
    ImageView fb,tw,g;
    TextView play_with_frnd;
    Typeface bold,light,regular,heavy;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        newGame=(Button)findViewById(R.id.button);
        settings=(Button)findViewById(R.id.button2);
        exit=(Button)findViewById(R.id.button3);
        fb=(ImageView)findViewById(R.id.fb);
        tw=(ImageView)findViewById(R.id.tw);
        g=(ImageView)findViewById(R.id.g);
        play_with_frnd=(TextView)findViewById(R.id.play_with_frnd);
        newGame.setOnClickListener(this);
        settings.setOnClickListener(this);
        exit.setOnClickListener(this);
        fb.setOnClickListener(this);
        tw.setOnClickListener(this);
        g.setOnClickListener(this);
        bold=Typeface.createFromAsset(getAssets(),"Bold.ttf");
        newGame.setTypeface(bold);
        settings.setTypeface(bold);
        exit.setTypeface(bold);
        regular=Typeface.createFromAsset(getAssets(),"Regular.ttf");
        play_with_frnd.setTypeface(regular);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button2:
                break;
            case R.id.button:
                AlertDialog.Builder choose=new AlertDialog.Builder(this);

                choose.setMessage("  Play With ..   ").setCancelable(false).setPositiveButton("Friend", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent("com.example.vivek_000.tictactoe20.TIKTACTOE");
                        startActivity(intent);
                    }
                });

                choose.setNegativeButton("Computer <AI>", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent("com.example.vivek_000.tictactoe20.COMPPLAYER");
                        startActivity(intent);
                    }
                });
                AlertDialog alert=choose.create();
                alert.show();


                break;
            case R.id.button3: finish();
                break;
            case R.id.g:
                break;
            case R.id.fb:


                break;
            case R.id.tw:
                break;
        }
    }



    @Override
    protected void onStop() {
        super.onStop();
        finish();

    }
}
