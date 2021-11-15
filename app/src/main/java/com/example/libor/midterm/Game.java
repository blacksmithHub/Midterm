package com.example.libor.midterm;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sqlitelib.DataBaseHelper;
import com.sqlitelib.SQLite;

import java.util.ArrayList;

public class Game extends AppCompatActivity {

    ArrayList<String> hands, top;
    ImageView user, comp;
    Button summary, logout;
    TextView scoreUser, scoreComp, username;
    Animation onClick;
    LinearLayout handle;
    private GestureDetectorCompat gestureDetectorCompat;
    int handsUser[] = {R.drawable.rockleft1,R.drawable.paperleft1,R.drawable.scissorleft1};
    int handsComp[] = {R.drawable.rockright1,R.drawable.paperright1,R.drawable.scissorright1};
    int enemy=0, player = 0, scorePlayer =0, scoreEnemy =0, rock = 0, paper = 0, scissor = 0;
    private DataBaseHelper dbhelper = new DataBaseHelper(Game.this, "MidtermDatabase", 2);
    public String name, nameErr;
    Intent act, out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        user = (ImageView)findViewById(R.id.handUser);
        comp = (ImageView)findViewById(R.id.handComp);
        summary = (Button)findViewById(R.id.btnSummary);
        scoreComp = (TextView)findViewById(R.id.comp);
        scoreUser = (TextView)findViewById(R.id.user);
        logout = (Button)findViewById(R.id.btnLogout);
        handle = (LinearLayout)findViewById(R.id.gesture);
        username = (TextView)findViewById(R.id.player);

        onClick = AnimationUtils.loadAnimation(this, R.anim.alpha);

        name = getIntent().getStringExtra("name");
        hands = new ArrayList();
        top = new ArrayList();
        act = new Intent(Game.this, Summary.class);
        out = new Intent(Game.this, MainActivity.class);

        refreshall();
        viewSummary();
        logout();
        //gesture();
        gestureHandle();
    }

    private void gestureHandle()
    {
        handle.setOnTouchListener(new OnSwipeTouchListener(Game.this) {
            public void onSwipeTop() {
                //bottom to top
            }
            public void onSwipeRight() {
                //left to right
                player = 0;
                enemy =(int) (Math.random() * 3);
                user.setImageResource(handsUser[player]);
                comp.setImageResource(handsComp[enemy]);
                score(player,enemy);
            }
            public void onSwipeLeft() {
                //right to left
                player = 1;
                enemy =(int) (Math.random() * 3);
                user.setImageResource(handsUser[player]);
                comp.setImageResource(handsComp[enemy]);
                score(player,enemy);
            }
            public void onSwipeBottom() {
                //top to bottom
                player = 2;
                enemy =(int) (Math.random() * 3);
                user.setImageResource(handsUser[player]);
                comp.setImageResource(handsComp[enemy]);
                score(player,enemy);
            }
        });
    }

    private void gesture()
    {
        gestureDetectorCompat = new GestureDetectorCompat(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {

                return false;
            }

            @Override
            public void onLongPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {

                float angle = (float) Math.toDegrees(Math.atan2(motionEvent.getY() - motionEvent1.getY(),
                        motionEvent1.getX() - motionEvent.getX()));

                if (angle > -45 && angle <= 45) {
                    //Left to Right swipe performed
                    player = 0;
                    enemy =(int) (Math.random() * 3);
                    user.setImageResource(handsUser[player]);
                    comp.setImageResource(handsComp[enemy]);
                    score(player,enemy);
                    return true;
                }

                if (angle >= 135 && angle < 180 || angle < -135 && angle > -180) {
                    //Right to Left swipe performed
                    player = 1;
                    enemy =(int) (Math.random() * 3);
                    user.setImageResource(handsUser[player]);
                    comp.setImageResource(handsComp[enemy]);
                    score(player,enemy);
                    return true;
                }

                if (angle < -45 && angle >= -135) {
                    //Up to Down swipe performed
                    player = 2;
                    enemy =(int) (Math.random() * 3);
                    user.setImageResource(handsUser[player]);
                    comp.setImageResource(handsComp[enemy]);
                    score(player,enemy);
                    return true;
                }

//                if (angle > 45 && angle <= 135) {
//                    //Down to Up swipe performed");
//                    return true;
//                }

                return false;
            }
        });
    }

    private void logout()
    {
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout.startAnimation(onClick);
                startActivity(out);
                Game.super.finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });
    }

    private void score(int player, int enemy)
    {
        if (player == 0 && enemy == 1 ){
            scoreComp.setText(""+ ++scoreEnemy);
        }
        if (player == 0 && enemy == 2 ){
            scoreUser.setText(""+ ++scorePlayer);
            ++rock;
        }
        if (player == 1 && enemy == 0 ){
            scoreUser.setText(""+ ++scorePlayer);
            ++paper;
        }
        if (player == 1 && enemy == 2 ){
            scoreComp.setText(""+ ++scoreEnemy);
        }
        if (player == 2 && enemy == 0 ){
            scoreComp.setText(""+ ++scoreEnemy);
        }
        if (player == 2 && enemy == 1 ){
            scoreUser.setText(""+ ++scorePlayer);
            ++scissor;
        }
        update();
    }

    private void refreshall()
    {
        reload();
        reloadSummary();
        reloadTopList();
    }

    private void viewSummary()
    {
        summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                summary.startAnimation(onClick);
                sendData();
            }
        });
    }

    private GameInfo getGesture()
    {
        GameInfo gameInfo =new GameInfo();
        gameInfo.setGesture(hands);
        return gameInfo;
    }

    private GameInfo getTop()
    {
        GameInfo gameInfo =new GameInfo();
        gameInfo.setTop(top);
        return gameInfo;
    }

    private void sendData()
    {
        act.putExtra("gesture",this.getGesture());
        act.putExtra("top",this.getTop());
        act.putExtra("name",name);
        startActivity(act);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }

    private void update()
    {
        final SQLiteDatabase db = dbhelper.getWritableDatabase();
        try {
            String query = "Select username FROM tblGame WHERE username = '" + name + "'";
            Cursor midterm = db.rawQuery(query, null);

            if (midterm.moveToFirst()) {
                midterm.moveToFirst();
                nameErr = midterm.getString(0);
            }
        } catch (Exception e) {
            Toast.makeText(Game.this,""+e,Toast.LENGTH_LONG).show();
        }

        if(name.equals(nameErr))
        {
            try {
                String sqlStr = "UPDATE tblGame SET scissor = '" + scissor
                        + "', rock = '" + rock + "', paper = '" + paper
                        + "' where username = '" + name + "'";
                db.execSQL(sqlStr);
                refreshall();
            } catch (SQLException e) {
                Toast.makeText(Game.this,"update:"+e,Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            try {
                String sqlStr = "INSERT INTO tblGame (username, scissor, rock, paper) VALUES ('"
                        + name + "', '" + scissor + "', '" + rock + "', '" + paper + "')";
                db.execSQL(sqlStr);
                refreshall();
            } catch (SQLException e) {
                Toast.makeText(Game.this,"insert:"+e,Toast.LENGTH_LONG).show();
            }
        }
    }
    //    public boolean onTouchEvent(MotionEvent event) {
//        gestureDetectorCompat.onTouchEvent(event);
//        return true;
//    }
    private void reload()
    {
        try {
            SQLiteDatabase dbGame = dbhelper.getWritableDatabase();
            Cursor midterm = dbGame.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='tblGame'", null);
            midterm.moveToNext();
            if (midterm.getCount() == 0) {
                SQLite.FITCreateTable("MidtermDatabase", this, "tblGame", " username VARCHAR(90) PRIMARY KEY," +
                        "scissor INTEGER, rock INTEGER, paper INTEGER");

                String sqlStr = "INSERT INTO tblGame (username, scissor, rock, paper) VALUES ('"
                        + name + "', '" + scissor + "', '" + rock + "', '" + paper + "')";
                dbGame.execSQL(sqlStr);
            }
            else {

                String query = "Select * FROM tblGame WHERE username = '" + name + "'";
                Cursor cursor = dbGame.rawQuery(query, null);

                if (cursor.moveToFirst()) {
                    cursor.moveToFirst();
                    scissor = Integer.parseInt(cursor.getString(1));
                    rock = Integer.parseInt(cursor.getString(2));
                    paper = Integer.parseInt(cursor.getString(3));

                }

            }

            username.setText(name);

        } catch (Exception e) {
            Toast.makeText(Game.this,""+e,Toast.LENGTH_LONG).show();
        }
    }

    private void reloadSummary()
    {
        try {
            SQLiteDatabase dbSUm = dbhelper.getWritableDatabase();
            Cursor midterm = dbSUm.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='tblGame'", null);
            midterm.moveToNext();
            if (midterm.getCount() == 0) {
                SQLite.FITCreateTable("MidtermDatabase", this, "tblGame", " username VARCHAR(90) PRIMARY KEY," +
                        "scissor INTEGER, rock INTEGER, paper INTEGER");
            }
            midterm = dbSUm.rawQuery("SELECT scissor, rock, paper FROM tblGame WHERE username = '"+ name +"'", null);
            String valueSum[] = new String[midterm.getCount()];
            hands.clear();
            if(midterm.moveToNext())
            {
                for(int x = 0; x <= 2; x++)
                {
                    String strFor = "";
                    strFor += System.lineSeparator() + midterm.getColumnName(x);
                    strFor += System.lineSeparator() + "Total wins: " + midterm.getString(x);
                    valueSum[0] = strFor;
                    hands.add(strFor);
                }
            }
        } catch (Exception e) {
            Toast.makeText(Game.this,""+e,Toast.LENGTH_LONG).show();
        }
    }

    private void reloadTopList()
    {
        try {
            SQLiteDatabase dbSUm = dbhelper.getWritableDatabase();
            Cursor midterm = dbSUm.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='tblGame'", null);
            midterm.moveToNext();
            if (midterm.getCount() == 0) {
                SQLite.FITCreateTable("MidtermDatabase", this, "tblGame", " username VARCHAR(90) PRIMARY KEY," +
                        "scissor INTEGER, rock INTEGER, paper INTEGER");
            }
            midterm = dbSUm.rawQuery("SELECT * , SUM(scissor + rock + paper) as sum FROM tblGame " +
                    "GROUP BY username ORDER BY sum desc", null);
            String valueSum[] = new String[midterm.getCount()];
            int ctrl = 0;
            top.clear();
            while(midterm.moveToNext())
            {
                String strFor = "";
                strFor += System.lineSeparator() + midterm.getString(midterm.getColumnIndex("username"));
                strFor += System.lineSeparator() + "Scissor: " + midterm.getString(midterm.getColumnIndex("scissor"));
                strFor += System.lineSeparator() + "Rock: " + midterm.getString(midterm.getColumnIndex("rock"));
                strFor += System.lineSeparator() + "Paper: " + midterm.getString(midterm.getColumnIndex("paper"));
                strFor += System.lineSeparator() + "Total: " + midterm.getString(midterm.getColumnIndex("sum"));
                valueSum[ctrl] = strFor;
                top.add(strFor);
                ctrl++;
            }
        } catch (Exception e) {
            Toast.makeText(Game.this,""+e,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void finish()
    {
        startActivity(out);
        Game.super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}