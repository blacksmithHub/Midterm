package com.example.libor.midterm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Summary extends AppCompatActivity {

    ListView lstView, toplist;
    TextView name;
    Intent out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        lstView = (ListView)findViewById(R.id.lstview);
        toplist = (ListView)findViewById(R.id.toplist);
        name = (TextView)findViewById(R.id.name);

        out = new Intent(Summary.this,Game.class);

        name.setText(getIntent().getStringExtra("name"));

        receiveData();

    }
    private void  receiveData()
    {
        Intent i=this.getIntent();
        GameInfo gesture = (GameInfo) i.getSerializableExtra("gesture");
        GameInfo top = (GameInfo) i.getSerializableExtra("top");
        lstView.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, gesture.getGesture()));
        toplist.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, top.getTop()));

    }

//    @Override
//    public void finish()
//    {
//        startActivity(out);
//        Summary.super.finish();
//        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
//    }

    @Override
    public void onBackPressed() {
        out.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(out);
        Summary.super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
