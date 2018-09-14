package com.example.molissa.npu_project_new;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class BiometricsActivity2 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String CrabSubject;
    String url;
    ArrayAdapter<String> Crablist;
    ArrayList<String> list;
    ArrayList<String> crabslist;
    ListView listView;
    String checked;
    TextView Quest_txt;
    int QuestType;
    int QuestOptionNum;
    String QuestNumber;
    boolean searched = false;
    Intent intent;
    Bundle bundle;
    Button YesButton;
    Button NoButton;
    ImageView BioImageView;
    String Map_Img_Url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biometrics2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        listView = (ListView) findViewById(R.id.listView2);
        Quest_txt = (TextView)findViewById(R.id.Quest_txt);
        YesButton = (Button) findViewById(R.id.YesBtn);
        NoButton = (Button) findViewById(R.id.NoBtn);
        BioImageView = (ImageView) findViewById(R.id.BioimageView);
        YesButton.setOnClickListener(new BiometricsActivity2.YesNoOnClick());
        NoButton.setOnClickListener(new BiometricsActivity2.YesNoOnClick());
        CheckQuestType();
    }

    private void CheckQuestType(){
        Intent intent = this.getIntent();
        Map_Img_Url = intent.getStringExtra("MapImage");
        CrabSubject = intent.getStringExtra("Subject");
        if(Map_Img_Url != null){
            Picasso.with(BiometricsActivity2.this).load(Map_Img_Url).into(BioImageView);
        }
        QuestType = getIntent().getExtras().getInt("Type");
        if (QuestType == 0){
            YesButton.setVisibility(View.INVISIBLE);
            NoButton.setVisibility(View.INVISIBLE);
        }
        switch (QuestType){
            case 0:
                searching();
                break;
            case 1:
                QuestOptionNum = 1;
                break;
            case 2:
                QuestOptionNum = 2;
                break;
            case 3:
                QuestOptionNum = 3;
                break;
            case 4:
                QuestOptionNum = 4;
                break;
            case 5:
                QuestOptionNum = 5;
                break;
            case 6:
                QuestOptionNum = 6;
                break;
        }
    }
    private void QuestOption(){
        QuestNumber = Quest_txt.getText().toString();
        Intent it = new Intent(BiometricsActivity2.this, EncyclopediaActivity2.class);
        Bundle subject = new Bundle();
        switch (QuestOptionNum){
            case 1:
                switch (QuestNumber){
                    case "螯是否對稱":
                        subject.putString("Name","平背蜞");
                        it.putExtras(subject);
                        startActivity(it);
                        finish();
                        break;
                    case "身體有無顆粒":
                        subject.putString("Name","小巧毛刺蟹");
                        it.putExtras(subject);
                        startActivity(it);
                        finish();
                        break;
                    case "身體有無毛":
                        Quest_txt.setText("身體有無斑點");
                        BioImageView.setImageResource(R.mipmap.yn_spot);
                        break;
                    case "身體有無斑點":
                        subject.putString("Name","黑點綠蟹");
                        it.putExtras(subject);
                        startActivity(it);
                        finish();
                        break;
                }
                break;
            case 2:
                switch (QuestNumber){
                    case "螯是否對稱":
                        subject.putString("Name","楊氏近扇蟹");
                        it.putExtras(subject);
                        startActivity(it);
                        finish();
                        break;
                    case "身體有無顆粒":
                        subject.putString("Name","火紅皺蟹");
                        it.putExtras(subject);
                        startActivity(it);
                        finish();
                        break;
                    case "身體有無泳足":
                        subject.putString("Name","光手滑面蟹");
                        it.putExtras(subject);
                        startActivity(it);
                        finish();
                        break;
                }
                break;
            case 3:
                switch (QuestNumber){
                    case "螯是否對稱":
                        subject.putString("Name","底棲短槳蟹");
                        it.putExtras(subject);
                        startActivity(it);
                        finish();
                        break;
                    case "身體有無顆粒":
                        subject.putString("Name","鋸緣青蟳");
                        it.putExtras(subject);
                        startActivity(it);
                        finish();
                        break;
                    case "身體有無尖刺":
                        subject.putString("Name","欖緣清蟳");
                        it.putExtras(subject);
                        startActivity(it);
                        finish();
                        break;
                }
                break;
            case 4:
                switch (QuestNumber){
                    case "螯是否對稱":
                        Quest_txt.setText("身體有無顆粒");
                        BioImageView.setImageResource(R.mipmap.yn_pellet);
                        break;
                    case "身體有無顆粒":
                        Quest_txt.setText("身體有無尖刺");
                        BioImageView.setImageResource(R.mipmap.yn_spiked);
                        break;
                    case "身體有無毛":
                        subject.putString("Name","莫氏毛刺蟹");
                        it.putExtras(subject);
                        startActivity(it);
                        finish();
                        break;
                    case "甲面平滑嗎":
                        subject.putString("Name","方形大額蟹");
                        it.putExtras(subject);
                        startActivity(it);
                        finish();
                        break;
                    case "身體有無尖刺":
                        subject.putString("Name","悅目大眼蟹");
                        it.putExtras(subject);
                        startActivity(it);
                        finish();
                        break;
                }
                break;
            case 5:
                switch (QuestNumber){
                    case "螯是否對稱":
                        subject.putString("Name","蛙型蟹");
                        it.putExtras(subject);
                        startActivity(it);
                        finish();
                        break;
                }
                break;
            case 6:
                switch (QuestNumber){
                    case "螯是否對稱":
                        Quest_txt.setText("身體有無斑點");
                        BioImageView.setImageResource(R.mipmap.yn_spot);
                        break;
                    case "身體有無斑點":
                        subject.putString("Name","粗甲裂額蟹");
                        it.putExtras(subject);
                        startActivity(it);
                        finish();
                        break;
                }
                break;
        }
    }
    private void QuestOptionFalse(){
        QuestNumber = Quest_txt.getText().toString();
        Intent it = new Intent(BiometricsActivity2.this, EncyclopediaActivity2.class);
        Bundle subject = new Bundle();
        switch (QuestOptionNum){
            case 1:
                    switch (QuestNumber){
                    case "螯是否對稱":
                        Quest_txt.setText("身體有無顆粒");
                        BioImageView.setImageResource(R.mipmap.yn_pellet);
                        break;
                    case "身體有無顆粒":
                        Quest_txt.setText("身體有無毛");
                        BioImageView.setImageResource(R.mipmap.yn_hair);
                        break;
                    case "身體有無毛":
                        subject.putString("Name","凶狠圓軸蟹");
                        it.putExtras(subject);
                        startActivity(it);
                        finish();
                        break;
                    case "身體有無斑點":
                        subject.putString("Name","雙齒毛足蟹");
                        it.putExtras(subject);
                        startActivity(it);
                        finish();
                        break;
                    }
                break;
            case 2:
                switch (QuestNumber){
                    case "螯是否對稱":
                        Quest_txt.setText("身體有無顆粒");
                        BioImageView.setImageResource(R.mipmap.yn_pellet);
                        break;
                    case "身體有無顆粒":
                        Quest_txt.setText("身體有無泳足");
                        BioImageView.setImageResource(R.mipmap.yn_swimfit);
                        break;
                    case "身體有無泳足":
                        subject.putString("Name","台灣厚蟹");
                        it.putExtras(subject);
                        startActivity(it);
                        finish();
                        break;
                }

                break;
            case 3:
                switch (QuestNumber){
                    case "螯是否對稱":
                        Quest_txt.setText("身體有無顆粒");
                        BioImageView.setImageResource(R.mipmap.yn_pellet);
                        break;
                    case "身體有無顆粒":
                        Quest_txt.setText("身體有無毛");
                        BioImageView.setImageResource(R.mipmap.yn_hair);
                        break;
                    case "身體有無毛":
                        subject.putString("Name","鈍齒短槳蟹");
                        it.putExtras(subject);
                        startActivity(it);
                        finish();
                        break;
                }

                break;
            case 4:
                switch (QuestNumber){
                    case "螯是否對稱":
                        Quest_txt.setText("身體有無毛");
                        BioImageView.setImageResource(R.mipmap.yn_hair);
                        break;
                    case "身體有無顆粒":
                        Quest_txt.setText("甲面平滑嗎");
                        BioImageView.setImageResource(R.mipmap.yn_smooth);
                        break;
                    case "身體有無毛":
                        subject.putString("Name","北方呼喚招潮");
                        it.putExtras(subject);
                        startActivity(it);
                        finish();
                        break;
                    case "甲面平滑嗎":
                        subject.putString("Name","斑點擬相手蟹");
                        it.putExtras(subject);
                        startActivity(it);
                        finish();
                        break;
                    case "身體有無尖刺":
                        subject.putString("Name","雙齒近相手蟹");
                        it.putExtras(subject);
                        startActivity(it);
                        finish();
                        break;
                }
                break;
            case 5:
                switch (QuestNumber){
                    case "螯是否對稱":
                        subject.putString("Name","字紋弓蟹");
                        it.putExtras(subject);
                        startActivity(it);
                        finish();
                        break;
                }
                break;
            case 6:
                switch (QuestNumber){
                    case "螯是否對稱":
                        subject.putString("Name","鈍額曲毛蟹");
                        it.putExtras(subject);
                        startActivity(it);
                        finish();
                        break;
                    case "身體有無斑點":
                        subject.putString("Name","拳折額蟹");
                        it.putExtras(subject);
                        startActivity(it);
                        finish();
                        break;
                }
                break;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵
            if(searched) {
                searching();
                searched = false;
            }
            else {
                    Intent it = new Intent(BiometricsActivity2.this, BiometricsActivity.class);
                    startActivity(it);
                    finish();
            }
        }
        return true;
    }

    private void searching() {
        Quest_txt.setText("頭胸甲外觀斑紋/顏色為：");
        RequestQueue queue = Volley.newRequestQueue(BiometricsActivity2.this);
        String select_crab = null;
        try {
            select_crab = URLEncoder.encode(CrabSubject, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        url = "http://120.127.16.69/crab/link1.php?ca=select`Spot`from%20crabs_data%20Where%20Name=%22" + select_crab + "%22";
        JsonArrayRequest json = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    list = new ArrayList<String>();
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject crabs = (JSONObject) response.get(i);
                        list.add(crabs.getString("Spot"));
                    }
                    Crablist = new ArrayAdapter<String>(BiometricsActivity2.this, android.R.layout.simple_list_item_1,list);
                    listView.setAdapter(Crablist);
                    listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                            AbsListView list = (AbsListView)adapterView;
                            int idx = list.getCheckedItemPosition();
                            checked = (String) adapterView.getAdapter().getItem(idx);
                            getCrab();

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(json);
    }
    protected void getCrab(){
        searched = true;
        Quest_txt.setText("您找到的螃蟹為：");
        RequestQueue mqueue = Volley.newRequestQueue(BiometricsActivity2.this);
        String checked_crab = null;
        try {
            checked_crab = URLEncoder.encode(checked,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        url = "http://120.127.16.69/crab/link1.php?ca=select*from%20crabs_data%20Where%20Spot=%22"+checked_crab+"%22";
        JsonArrayRequest crabjson = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    crabslist = new ArrayList<String>();

                    for (int i = 0; i < response.length(); i++){
                        JSONObject crabsname = (JSONObject) response.get(i);
                        crabslist.add(crabsname.getString("Name"));

                    }
                    Crablist = new ArrayAdapter<String>(BiometricsActivity2.this, android.R.layout.simple_list_item_1,crabslist);
                    listView.setAdapter(Crablist);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                            AbsListView list = (AbsListView)adapterView;
                            int idx = list.getCheckedItemPosition();
                            checked = (String) adapterView.getAdapter().getItem(idx);

                            intent = new Intent();
                            intent.setClass(BiometricsActivity2.this, EncyclopediaActivity2.class);

                            //new一個Bundle物件，並將要傳遞的資料傳入
                            bundle = new Bundle();
                            bundle.putString("Name", checked);
                            //將Bundle物件assign給intent
                            intent.putExtras(bundle);
                            //切換Activity
                            startActivity(intent);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error:"+error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        mqueue.add(crabjson);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_diary) {
            Intent it = new Intent(BiometricsActivity2.this, CameraActivity.class);
            startActivity(it);
            finish();
        }

        else if (id == R.id.nav_camera) {
            Intent it = new Intent(BiometricsActivity2.this, BiometricsActivity.class);
            startActivity(it);
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Intent it = new Intent(BiometricsActivity2.this, EncyclopediaActivity.class);
            startActivity(it);
            finish();
        } else if (id == R.id.nav_map) {
            Intent it = new Intent(BiometricsActivity2.this, MapsActivity.class);
            startActivity(it);
            finish();
        } else if (id == R.id.nav_manage) {
            Intent it = new Intent(this, SettingActivity.class);
            startActivity(it);
            finish();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_end) {
            LoginManager.getInstance().logOut();
            System.exit(0);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class YesNoOnClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.YesBtn:
                    QuestOption();
                    break;
                case R.id.NoBtn:
                    QuestOptionFalse();
                    break;
            }
        }
    }
}
