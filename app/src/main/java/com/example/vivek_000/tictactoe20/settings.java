package com.example.vivek_000.tictactoe20;

import android.app.Activity;
import android.app.Service;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * Created by Vivek Singh on 28-10-15.
 */
public class settings extends Activity implements RadioGroup.OnCheckedChangeListener{

    Typeface bold,regular;
    TextView t1,t2,t3;
    RadioGroup sound_group;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        //start_tune = MediaPlayer.create(settings.this,R.raw.ac);
        bold=Typeface.createFromAsset(getAssets(),"Bold.ttf");
        regular=Typeface.createFromAsset(getAssets(),"Regular.ttf");
        t1=(TextView)findViewById(R.id.tv_settings);
        t2=(TextView)findViewById(R.id.tv_sound);
        t3=(TextView)findViewById(R.id.tv_level);
        sound_group=(RadioGroup)findViewById(R.id.sound_group);
        t1.setTypeface(bold);
        t2.setTypeface(regular);
        t3.setTypeface(regular);

    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if(checkedId==R.id.rb_off)
        {

        }
    }
}
