package com.example.vivek_000.tictactoe20;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;



import java.util.ArrayList;
import java.util.List;

/**
 * Created by vivek_000 on 7/3/2015.
 */
public class TikTacToe extends Activity implements View.OnTouchListener {

    MediaPlayer start_tune;
    Paint paint, ColorForWin,textPaint;
    int[] canDraw = new int[9];
    String WhoWon = null;
    int[][] checkforwin = new int[3][3];
    Bitmap playAgain,ExitGame;
    Rect pA,ex;
    int i, j, c;
    float widthfactor, heightfactor, basefactor, crossfactor;
    boolean circleorcross = true, gameOver = false, PAClicked = false, ExitClicked = false, noOneWin = false;
    List<Point> circlePoints, circleWinPoints, crossWinPoints, crossPoints;
    SoundPool sp;
    int tick=0;
    class Base extends View {

        public Base(Context context) {
            super(context);
            paint = new Paint();
            textPaint = new Paint();
            ColorForWin = new Paint();
            ColorForWin.setColor(Color.BLUE);
            ColorForWin.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            circlePoints = new ArrayList<Point>();
            crossPoints = new ArrayList<Point>();
            circleWinPoints = new ArrayList<Point>();
            crossWinPoints = new ArrayList<Point>();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            //////  BASE  /////
            canvas.drawColor(Color.rgb(39,39,41));
            widthfactor = canvas.getWidth() / 3;
            basefactor = widthfactor / 2;
            heightfactor = canvas.getHeight() / 3;
            paint.setStrokeWidth(canvas.getWidth() / 90);
            paint.setColor(Color.rgb(82, 198, 255));
            ColorForWin.setStrokeWidth(canvas.getWidth() / 100 + 2);
            ColorForWin.setColor(Color.rgb(127, 255, 212));
            ColorForWin.setShadowLayer(30, 20, 20, Color.WHITE);
            canvas.drawLine(widthfactor, 0 + basefactor, widthfactor, 3 * widthfactor + basefactor, paint);
            canvas.drawLine(2 * widthfactor, 0 + basefactor, 2 * widthfactor, 3 * widthfactor + basefactor, paint);
            canvas.drawLine(10, widthfactor + basefactor, 3 * widthfactor-10, widthfactor + basefactor, paint);
            canvas.drawLine(10, 2 * widthfactor + basefactor, 3 * widthfactor-10, 2 * widthfactor + basefactor, paint);
            ///////////////////
            ///////// CIRCLES ////////////
            for (Point point : circlePoints)
                canvas.drawCircle(point.x, point.y, widthfactor / 2 - 30, paint);
            //////////////////
            ///////// CROSS ////////////
            crossfactor = widthfactor / 2 - 20;
            for (Point point : crossPoints) {
                canvas.drawLine(point.x - crossfactor, point.y - crossfactor, point.x + crossfactor, point.y + crossfactor, paint);
                canvas.drawLine(point.x + crossfactor, point.y - crossfactor, point.x - crossfactor, point.y + crossfactor, paint);
            }

            ////// Game Over ////////

            if (gameOver) {
                ///////// Win CIRCLES ////////////
                for (Point point : circleWinPoints)
                    canvas.drawCircle(point.x, point.y, widthfactor / 2 - 30, ColorForWin);
                //////////////////
                ///////// Win CROSS ////////////
                for (Point point : crossWinPoints) {
                    canvas.drawLine(point.x - crossfactor, point.y - crossfactor, point.x + crossfactor, point.y + crossfactor, ColorForWin);
                    canvas.drawLine(point.x + crossfactor, point.y - crossfactor, point.x - crossfactor, point.y + crossfactor, ColorForWin);
                }
                ////////////

                textPaint.setColor(Color.rgb(82,198,255));
                textPaint.setStrokeWidth(5);
                textPaint.setShadowLayer(10,3,5,Color.rgb(128,128,128));
                textPaint.setTextSize(widthfactor / 4);
                if (!noOneWin) {
                    canvas.drawText(WhoWon + " Won !!", widthfactor - crossfactor / 2, 2 * basefactor + 3 * widthfactor, textPaint);
                } else {
                    textPaint.setTextSize(widthfactor / 6);
                    canvas.drawText("Dono Hosiyaar Lagte Ho!!", widthfactor - crossfactor, 2 * basefactor + 3 * widthfactor, textPaint);
                }
                playAgain= BitmapFactory.decodeResource(getResources(), R.drawable.playagain);
                ExitGame=BitmapFactory.decodeResource(getResources(),R.drawable.exit);
                pA=new Rect((int)crossfactor/2,(int)(2 * basefactor + 3 * widthfactor+crossfactor),(int)((canvas.getWidth()/2)-(crossfactor/2)),(int)(2 * basefactor + 3 * widthfactor+2*crossfactor));
                ex=new Rect((int)(crossfactor+canvas.getWidth()/2),(int)(2 * basefactor + 3 * widthfactor+crossfactor),(int)((canvas.getWidth())-(crossfactor/2)),(int)(2 * basefactor + 3 * widthfactor+2*crossfactor));
                canvas.drawBitmap(playAgain, new Rect(0, 0, playAgain.getWidth(), playAgain.getHeight()), pA, null);
                canvas.drawBitmap(ExitGame, new Rect(0, 0, ExitGame.getWidth(), ExitGame.getHeight()), ex, null);

            }

            if(PAClicked){
               /*
                Intent intent=getIntent();
                finish();
                startActivity(intent);
                */
                recreate();
            }
            if(ExitClicked){
                finish();
            }


        }


    }

    Base base;
    float x, y;
    PowerManager.WakeLock wakeLock;

    public void play()
    {
        if(tick!=0)
        sp.play(tick,1,1,0,0,1);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ///////// Wake Lock   ////////////
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "");
        super.onCreate(savedInstanceState);
        wakeLock.acquire();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sp = new SoundPool(1, AudioManager.STREAM_MUSIC,0);
        tick = sp.load(this,R.raw.tick,0);
        start_tune = MediaPlayer.create(TikTacToe.this, R.raw.ac);


        base = new Base(this);
        setContentView(base);
        base.setOnTouchListener(this);
        /////// To Prevent Overlapping of CrossAndCircle /////
        for (i = 0; i < 9; i++) {
            canDraw[i] = 0;
        }
        //////    To Check for Win     For Circle->1  Cross->2  EmptyArea->0    /////
        for (i = 0; i < 3; i++) {

            for (j = 0; j < 3; j++) {
                checkforwin[i][j] = 0;
            }
        }

    }

    //////////// Check Button Click  /////////
    void checkButtonClick(float x, float y) {
        if(pA.contains((int)x,(int)y)){
            PAClicked=true;
        }
        if(ex.contains((int)x,(int)y)){
            ExitClicked=true;
        }
    }


    ////////////  Function For Checking WIN ///////////
    void CheckWinner(){
        ///// Vertical Check /////
        for(i=0;i<=2;i++)
        {
            if((checkforwin[0][i]==2)&&(checkforwin[1][i]==2)&&(checkforwin[2][i]==2))
            {   WhoWon="Cross"; gameOver=true;
                /////////// For Coloring Winning elements
                switch (i){
                    case 0:
                        crossWinPoints.add(new Point(Math.round(widthfactor / 2),Math.round(widthfactor / 2+basefactor)));
                        crossWinPoints.add(new Point(Math.round(widthfactor / 2),Math.round(widthfactor / 2+basefactor+widthfactor)));
                        crossWinPoints.add(new Point(Math.round(widthfactor / 2),Math.round(widthfactor / 2+basefactor+2*widthfactor)));
                        break;
                    case 1:
                        crossWinPoints.add(new Point(Math.round(widthfactor / 2+widthfactor),Math.round(widthfactor / 2+basefactor)));
                        crossWinPoints.add(new Point(Math.round(widthfactor / 2+widthfactor),Math.round(widthfactor / 2+basefactor+widthfactor)));
                        crossWinPoints.add(new Point(Math.round(widthfactor / 2+widthfactor),Math.round(widthfactor / 2+basefactor+2*widthfactor)));
                        break;
                    case 2:
                        crossWinPoints.add(new Point(Math.round(widthfactor / 2+2*widthfactor),Math.round(widthfactor / 2+basefactor)));
                        crossWinPoints.add(new Point(Math.round(widthfactor / 2+2*widthfactor),Math.round(widthfactor / 2+basefactor+widthfactor)));
                        crossWinPoints.add(new Point(Math.round(widthfactor / 2+2*widthfactor),Math.round(widthfactor / 2+basefactor+2*widthfactor)));
                        break;
                }
                break;
            }
            if((checkforwin[0][i]==1)&&(checkforwin[1][i]==1)&&(checkforwin[2][i]==1))
            {   WhoWon="Circle"; gameOver=true;
                switch (i){
                    case 0:
                        circleWinPoints.add(new Point(Math.round(widthfactor / 2),Math.round(widthfactor / 2+basefactor)));
                        circleWinPoints.add(new Point(Math.round(widthfactor / 2),Math.round(widthfactor / 2+basefactor+widthfactor)));
                        circleWinPoints.add(new Point(Math.round(widthfactor / 2),Math.round(widthfactor / 2+basefactor+2*widthfactor)));
                        break;
                    case 1:
                        circleWinPoints.add(new Point(Math.round(widthfactor / 2+widthfactor),Math.round(widthfactor / 2+basefactor)));
                        circleWinPoints.add(new Point(Math.round(widthfactor / 2+widthfactor),Math.round(widthfactor / 2+basefactor+widthfactor)));
                        circleWinPoints.add(new Point(Math.round(widthfactor / 2+widthfactor),Math.round(widthfactor / 2+basefactor+2*widthfactor)));
                        break;
                    case 2:
                        circleWinPoints.add(new Point(Math.round(widthfactor / 2+2*widthfactor),Math.round(widthfactor / 2+basefactor)));
                        circleWinPoints.add(new Point(Math.round(widthfactor / 2+2*widthfactor),Math.round(widthfactor / 2+basefactor+widthfactor)));
                        circleWinPoints.add(new Point(Math.round(widthfactor / 2+2*widthfactor),Math.round(widthfactor / 2+basefactor+2*widthfactor)));
                        break;
                }
                break;
            }
        }
        //// Horizontal Check /////
        for(i=0;i<=2;i++)
        {
            if((checkforwin[i][0]==2)&&(checkforwin[i][1]==2)&&(checkforwin[i][2]==2))
            { WhoWon="Cross"; gameOver=true;
                switch (i){
                    case 0:
                        crossWinPoints.add(new Point(Math.round(widthfactor / 2),Math.round(widthfactor / 2+basefactor)));
                        crossWinPoints.add(new Point(Math.round(widthfactor / 2+widthfactor),Math.round(widthfactor / 2+basefactor)));
                        crossWinPoints.add(new Point(Math.round(widthfactor / 2+2*widthfactor),Math.round(widthfactor / 2+basefactor)));
                        break;
                    case 1:
                        crossWinPoints.add(new Point(Math.round(widthfactor / 2),Math.round(widthfactor / 2+basefactor+widthfactor)));
                        crossWinPoints.add(new Point(Math.round(widthfactor / 2+widthfactor),Math.round(widthfactor / 2+basefactor+widthfactor)));
                        crossWinPoints.add(new Point(Math.round(widthfactor / 2+2*widthfactor),Math.round(widthfactor / 2+basefactor+widthfactor)));
                        break;
                    case 2:
                        crossWinPoints.add(new Point(Math.round(widthfactor / 2),Math.round(widthfactor / 2+basefactor+2*widthfactor)));
                        crossWinPoints.add(new Point(Math.round(widthfactor / 2+widthfactor),Math.round(widthfactor / 2+basefactor+2*widthfactor)));
                        crossWinPoints.add(new Point(Math.round(widthfactor / 2+2*widthfactor),Math.round(widthfactor / 2+basefactor+2*widthfactor)));
                        break;
                }
                break;
            }
            if((checkforwin[i][0]==1)&&(checkforwin[i][1]==1)&&(checkforwin[i][2]==1))
            { WhoWon="Circle"; gameOver=true;
                switch (i){
                    case 0:
                        circleWinPoints.add(new Point(Math.round(widthfactor / 2),Math.round(widthfactor / 2+basefactor)));
                        circleWinPoints.add(new Point(Math.round(widthfactor / 2+widthfactor),Math.round(widthfactor / 2+basefactor)));
                        circleWinPoints.add(new Point(Math.round(widthfactor / 2+2*widthfactor),Math.round(widthfactor / 2+basefactor)));
                        break;
                    case 1:
                        circleWinPoints.add(new Point(Math.round(widthfactor / 2),Math.round(widthfactor / 2+basefactor+widthfactor)));
                        circleWinPoints.add(new Point(Math.round(widthfactor / 2+widthfactor),Math.round(widthfactor / 2+basefactor+widthfactor)));
                        circleWinPoints.add(new Point(Math.round(widthfactor / 2+2*widthfactor),Math.round(widthfactor / 2+basefactor+widthfactor)));
                        break;
                    case 2:
                        circleWinPoints.add(new Point(Math.round(widthfactor / 2),Math.round(widthfactor / 2+basefactor+2*widthfactor)));
                        circleWinPoints.add(new Point(Math.round(widthfactor / 2+widthfactor),Math.round(widthfactor / 2+basefactor+2*widthfactor)));
                        circleWinPoints.add(new Point(Math.round(widthfactor / 2+2*widthfactor),Math.round(widthfactor / 2+basefactor+2*widthfactor)));
                        break;
                }
                break;
            }
        }
        //// Diagonal Check /////
        if((checkforwin[0][0]==2)&&(checkforwin[1][1]==2)&&(checkforwin[2][2]==2))
        {  WhoWon="Cross"; gameOver=true;
            crossWinPoints.add(new Point(Math.round(widthfactor / 2),Math.round(widthfactor / 2+basefactor)));
            crossWinPoints.add(new Point(Math.round(widthfactor / 2+widthfactor),Math.round(widthfactor / 2+basefactor+widthfactor)));
            crossWinPoints.add(new Point(Math.round(widthfactor / 2+2*widthfactor),Math.round(widthfactor / 2+basefactor+2*widthfactor)));
        }
        if((checkforwin[0][0]==1)&&(checkforwin[1][1]==1)&&(checkforwin[2][2]==1))
        {  WhoWon="Circle"; gameOver=true;
            circleWinPoints.add(new Point(Math.round(widthfactor / 2),Math.round(widthfactor / 2+basefactor)));
            circleWinPoints.add(new Point(Math.round(widthfactor / 2+widthfactor),Math.round(widthfactor / 2+basefactor+widthfactor)));
            circleWinPoints.add(new Point(Math.round(widthfactor / 2+2*widthfactor),Math.round(widthfactor / 2+basefactor+2*widthfactor)));
        }
        if((checkforwin[2][0]==2)&&(checkforwin[1][1]==2)&&(checkforwin[0][2]==2))
        {  WhoWon="Cross"; gameOver=true;
            crossWinPoints.add(new Point(Math.round(widthfactor / 2+2*widthfactor),Math.round(widthfactor / 2+basefactor)));
            crossWinPoints.add(new Point(Math.round(widthfactor / 2+widthfactor),Math.round(widthfactor / 2+basefactor+widthfactor)));
            crossWinPoints.add(new Point(Math.round(widthfactor / 2),Math.round(widthfactor / 2+basefactor+2*widthfactor)));
        }
        if((checkforwin[2][0]==1)&&(checkforwin[1][1]==1)&&(checkforwin[0][2]==1))
        {  WhoWon="Circle"; gameOver=true;
            circleWinPoints.add(new Point(Math.round(widthfactor / 2+2*widthfactor),Math.round(widthfactor / 2+basefactor)));
            circleWinPoints.add(new Point(Math.round(widthfactor / 2+widthfactor),Math.round(widthfactor / 2+basefactor+widthfactor)));
            circleWinPoints.add(new Point(Math.round(widthfactor / 2),Math.round(widthfactor / 2+basefactor+2*widthfactor)));
        }

        ////////////////// NO One Win  //////////
        c=0;
        for (i=0;i<9;i++)
        {
            if(canDraw[i]==1)
                c++;
        }
        if((c==9)&&(WhoWon==null)){gameOver=true; noOneWin=true;}

        if(gameOver)
        {
            if(!start_tune.isPlaying())
            start_tune.start();
        }
        else
        {
            if(start_tune.isPlaying())
              start_tune.release();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        start_tune.release();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        x = event.getX();
        y = event.getY();
        if(event.getAction()==MotionEvent.ACTION_UP) {
            if(!gameOver) {
                play();
                if ((x > 0) && (x < widthfactor)) {
                    if ((y > basefactor) && (y < basefactor + widthfactor)) {
                        x = widthfactor / 2;
                        y = widthfactor / 2 + basefactor;
                        if (canDraw[0] == 0) {
                            ///////
                            if (circleorcross) {
                                circlePoints.add(new Point(Math.round(x), Math.round(y)));
                                checkforwin[0][0] = 1;
                                circleorcross = !circleorcross;
                            } else {
                                crossPoints.add(new Point(Math.round(x), Math.round(y)));
                                checkforwin[0][0] = 2;
                                circleorcross = !circleorcross;
                            }
                            //////
                            canDraw[0] = 1;
                        }
                    }
                    if ((y > basefactor + widthfactor) && (y < basefactor + 2 * widthfactor)) {
                        x = widthfactor / 2;
                        y = widthfactor / 2 + basefactor + widthfactor;
                        if (canDraw[1] == 0) {
                            ///////
                            if (circleorcross) {
                                circlePoints.add(new Point(Math.round(x), Math.round(y)));
                                checkforwin[1][0] = 1;
                                circleorcross = !circleorcross;
                            } else {
                                crossPoints.add(new Point(Math.round(x), Math.round(y)));
                                checkforwin[1][0] = 2;
                                circleorcross = !circleorcross;
                            }
                            //////
                            canDraw[1] = 1;
                        }
                    }
                    if ((y > basefactor + 2 * widthfactor) && (y < basefactor + 3 * widthfactor)) {
                        x = widthfactor / 2;
                        y = widthfactor / 2 + basefactor + 2 * widthfactor;
                        if (canDraw[2] == 0) {
                            ///////
                            if (circleorcross) {
                                circlePoints.add(new Point(Math.round(x), Math.round(y)));
                                checkforwin[2][0] = 1;
                                circleorcross = !circleorcross;
                            } else {
                                crossPoints.add(new Point(Math.round(x), Math.round(y)));
                                checkforwin[2][0] = 2;
                                circleorcross = !circleorcross;
                            }
                            //////
                            canDraw[2] = 1;
                        }
                    }

                } else if ((x > widthfactor) && (x < 2 * widthfactor)) {
                    if ((y > basefactor) && (y < basefactor + widthfactor)) {
                        x = widthfactor / 2 + widthfactor;
                        y = widthfactor / 2 + basefactor;
                        if (canDraw[3] == 0) {
                            ///////
                            if (circleorcross) {
                                circlePoints.add(new Point(Math.round(x), Math.round(y)));
                                checkforwin[0][1] = 1;
                                circleorcross = !circleorcross;
                            } else {
                                crossPoints.add(new Point(Math.round(x), Math.round(y)));
                                checkforwin[0][1] = 2;
                                circleorcross = !circleorcross;
                            }
                            //////
                            canDraw[3] = 1;
                        }
                    }
                    if ((y > basefactor + widthfactor) && (y < basefactor + 2 * widthfactor)) {
                        x = widthfactor / 2 + widthfactor;
                        y = widthfactor / 2 + basefactor + widthfactor;
                        if (canDraw[4] == 0) {
                            ///////
                            if (circleorcross) {
                                circlePoints.add(new Point(Math.round(x), Math.round(y)));
                                checkforwin[1][1] = 1;
                                circleorcross = !circleorcross;
                            } else {
                                crossPoints.add(new Point(Math.round(x), Math.round(y)));
                                checkforwin[1][1] = 2;
                                circleorcross = !circleorcross;
                            }
                            //////
                            canDraw[4] = 1;
                        }
                    }
                    if ((y > basefactor + 2 * widthfactor) && (y < basefactor + 3 * widthfactor)) {
                        x = widthfactor / 2 + widthfactor;
                        y = widthfactor / 2 + basefactor + 2 * widthfactor;
                        if (canDraw[5] == 0) {
                            ///////
                            if (circleorcross) {
                                circlePoints.add(new Point(Math.round(x), Math.round(y)));
                                checkforwin[2][1] = 1;
                                circleorcross = !circleorcross;
                            } else {
                                crossPoints.add(new Point(Math.round(x), Math.round(y)));
                                checkforwin[2][1] = 2;
                                circleorcross = !circleorcross;
                            }
                            //////
                            canDraw[5] = 1;
                        }
                    }
                } else {
                    if ((y > basefactor) && (y < basefactor + widthfactor)) {
                        x = widthfactor / 2 + 2 * widthfactor;
                        y = widthfactor / 2 + basefactor;

                        if (canDraw[6] == 0) {
                            ///////
                            if (circleorcross) {
                                circlePoints.add(new Point(Math.round(x), Math.round(y)));
                                checkforwin[0][2] = 1;
                                circleorcross = !circleorcross;
                            } else {
                                crossPoints.add(new Point(Math.round(x), Math.round(y)));
                                checkforwin[0][2] = 2;
                                circleorcross = !circleorcross;
                            }
                            //////
                            canDraw[6] = 1;
                        }
                    }
                    if ((y > basefactor + widthfactor) && (y < basefactor + 2 * widthfactor)) {
                        x = widthfactor / 2 + 2 * widthfactor;
                        y = widthfactor / 2 + basefactor + widthfactor;
                        if (canDraw[7] == 0) {
                            ///////
                            if (circleorcross) {
                                circlePoints.add(new Point(Math.round(x), Math.round(y)));
                                checkforwin[1][2] = 1;
                                circleorcross = !circleorcross;
                            } else {
                                crossPoints.add(new Point(Math.round(x), Math.round(y)));
                                checkforwin[1][2] = 2;
                                circleorcross = !circleorcross;
                            }
                            //////
                            canDraw[7] = 1;
                        }
                    }
                    if ((y > basefactor + 2 * widthfactor) && (y < basefactor + 3 * widthfactor)) {
                        x = widthfactor / 2 + 2 * widthfactor;
                        y = widthfactor / 2 + basefactor + 2 * widthfactor;
                        if (canDraw[8] == 0) {
                            ///////
                            if (circleorcross) {
                                circlePoints.add(new Point(Math.round(x), Math.round(y)));
                                checkforwin[2][2] = 1;
                                circleorcross = !circleorcross;
                            } else {
                                crossPoints.add(new Point(Math.round(x), Math.round(y)));
                                checkforwin[2][2] = 2;
                                circleorcross = !circleorcross;
                            }
                            //////
                            canDraw[8] = 1;
                        }
                    }
                }

                CheckWinner();

            }else {
                checkButtonClick(x,y);
            }
        }

        base.invalidate();
        return true;
    }


}
