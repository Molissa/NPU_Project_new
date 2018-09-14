package com.example.molissa.npu_project_new;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.login.LoginManager;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Objects;

public class EncyclopediaActivity2 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,YouTubePlayer.OnInitializedListener{

    String CrabName;
    String url;
    String image_Url;
    String crab_ID;
    TextView Name_txt;
    TextView CName_txt;
    TextView SName_txt;
    TextView Perch_txt;
    TextView CrabSubject;
    TextView Exterior_txt;
    TextView Spot_txt;
    TextView Color_txt;
    ImageView Crab_image;
    String YoutubeVideoID;
    String URL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encyclopedia2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        findview();
        searching();


    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵
            new AlertDialog.Builder(EncyclopediaActivity2.this)
                    .setTitle("確認視窗")
                    .setMessage("確定要關閉圖鑑嗎?")
                    .setIcon(R.mipmap.ic_icon)
                    .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                        }
                    }).show();
        }
        return true;
    }

    private void findview(){
        CrabSubject = (TextView) findViewById(R.id.Subject);
        Name_txt = (TextView) findViewById(R.id.Name_txt);
        CName_txt = (TextView) findViewById(R.id.CName_txt);
        SName_txt = (TextView) findViewById(R.id.SName_txt);
        Perch_txt = (TextView) findViewById(R.id.Perch_txt);
        Exterior_txt = (TextView)findViewById(R.id.Exterior_txt);
        Color_txt = (TextView)findViewById(R.id.Color_txt);
        Spot_txt = (TextView)findViewById(R.id.Spot_txt);
        Crab_image = (ImageView) findViewById(R.id.Crab_image);

    }

    private void searching(){
        RequestQueue queue = Volley.newRequestQueue(EncyclopediaActivity2.this);
        Bundle bundle_crab = this.getIntent().getExtras();
        CrabName = bundle_crab.getString("Name");
        String select_crab = null;
        try {
            select_crab = URLEncoder.encode(CrabName,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        url = "http://120.127.16.69/crab/link1.php?ca=select*from%20crabs_data%20Where%20Name=%22"+select_crab+"%22";
        JsonArrayRequest json = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++){
                        JSONObject crabs = (JSONObject) response.get(i);
                        crab_ID = crabs.getString("ID");
                        String Subject = crabs.getString("Subject");
                        String Name = crabs.getString("Name");
                        String CName = crabs.getString("CName");
                        String SName = crabs.getString("SName");
                        String Exterior = crabs.getString("Exterior");
                        String Color = crabs.getString("Color");
                        String Spot = crabs.getString("Spot");
                        String Perch = crabs.getString("Perch");
                        URL = crabs.getString("URL");

                        CrabSubject.setText("科名 ： "+Subject);
                        Name_txt.setText("名稱 ： "+Name);
                        if(Objects.equals(CName, "null")){CName_txt.setText("俗名 ： 無");}
                        else CName_txt.setText("俗名 ： "+CName);
                        SName_txt.setText("學名 ： <"+SName+">");
                        Perch_txt.setText("棲地 ： "+Perch);
                        if(Objects.equals(Exterior, "null")){Exterior_txt.setText("頭胸甲形狀 ： 無資料");}
                        else Exterior_txt.setText("頭胸甲形狀 ： "+Exterior);
                        if(Objects.equals(Color, "null")){Color_txt.setText("螯足顏色 ： 無資料");}
                        else Color_txt.setText("螯足顏色 ： "+Color);
                        if(Objects.equals(Spot, "null")){Spot_txt.setText("頭胸甲顏色 ： 無資料");}
                        else Spot_txt.setText("頭胸甲顏色 ： "+Spot);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: 查無螃蟹" + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }

                if (URL != null) {
                    YoutubeVideoID = URL;
                    YouTubePlayerFragment YouTubePlayerFragment = (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtube_fragment);
                    YouTubePlayerFragment.initialize("AIzaSyC8g3axyv7pj75KD5vtVa4gbnik8-if464",EncyclopediaActivity2.this);
                }
                else {
                    YoutubeVideoID = "";
                }
                image_Url = "http://120.127.16.69/crab/image_png/"+crab_ID+"-1.png";
                Picasso.with(EncyclopediaActivity2.this).load(image_Url).placeholder(R.mipmap.pic).into(Crab_image);

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(json);


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
            Intent it = new Intent(this, CameraActivity.class);
            startActivity(it);
            finish();
        }
        else if (id == R.id.nav_camera) {
            Intent it = new Intent(this, BiometricsActivity.class);
            startActivity(it);
            finish();
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Intent it = new Intent(this, EncyclopediaActivity.class);
            startActivity(it);
            finish();
        } else if (id == R.id.nav_map) {
            Intent it = new Intent(this, MapsActivity.class);
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

    //Youtube介面:
    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        youTubePlayer.loadVideo(YoutubeVideoID);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Toast.makeText(getApplicationContext(),"影片播放失敗",Toast.LENGTH_LONG).show();
    }
}
