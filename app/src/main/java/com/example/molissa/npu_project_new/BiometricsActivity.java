package com.example.molissa.npu_project_new;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class BiometricsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    Uri imgUri;
    String url;
    ImageView imv;
    Button TakePicture;
    Button Picture;
    Button YesButton;
    Button NoButton;
    TextView QuestText;
    ImageButton pic01;
    ImageButton pic02;
    ImageButton pic03;
    ImageButton pic04;
    ImageButton pic05;
    ImageButton pic06;
    ImageButton pic07;
    ImageButton pic08;
    ImageButton pic09;
    ImageButton pic10;
    ImageButton pic11;
    String diaryID;
    String Map_Img_Url;
    BitmapFactory.Options options;
    Intent intent;
    Intent it;
    Bundle subject;


    public void findview(){
        QuestText = (TextView) findViewById(R.id.questText);
        imv = (ImageView) findViewById(R.id.imageView3);
        YesButton = (Button) findViewById(R.id.YesButton);
        NoButton = (Button) findViewById(R.id.NoButton);
        TakePicture = (Button) findViewById(R.id.button7);
        Picture = (Button) findViewById(R.id.button8);
        pic01 = (ImageButton)findViewById(R.id.pic01);
        pic02 = (ImageButton)findViewById(R.id.pic02);
        pic03 = (ImageButton)findViewById(R.id.pic03);
        pic04 = (ImageButton)findViewById(R.id.pic04);
        pic05 = (ImageButton)findViewById(R.id.pic05);
        pic06 = (ImageButton)findViewById(R.id.pic06);
        pic07 = (ImageButton)findViewById(R.id.pic07);
        pic08 = (ImageButton)findViewById(R.id.pic08);
        pic09 = (ImageButton)findViewById(R.id.pic09);
        pic10 = (ImageButton)findViewById(R.id.pic10);
        pic11 = (ImageButton)findViewById(R.id.pic11);
        pic01.setVisibility(View.INVISIBLE);
        pic02.setVisibility(View.INVISIBLE);
        pic03.setVisibility(View.INVISIBLE);
        pic04.setVisibility(View.INVISIBLE);
        pic05.setVisibility(View.INVISIBLE);
        pic06.setVisibility(View.INVISIBLE);
        pic07.setVisibility(View.INVISIBLE);
        pic08.setVisibility(View.INVISIBLE);
        pic09.setVisibility(View.INVISIBLE);
        pic10.setVisibility(View.INVISIBLE);
        pic11.setVisibility(View.INVISIBLE);

        intent = this.getIntent();
        Map_Img_Url = intent.getStringExtra("MapImage");
        if(Map_Img_Url != null)
            Picasso.with(BiometricsActivity.this).load(Map_Img_Url).into(imv);
        else {
            Map_Img_Url = "http://www.lfes.tc.edu.tw/www2/media_dvd/94CAI/E3_4-%E5%B0%8B%E6%89%BE%E5%A4%A7%E5%AE%89%E6%B5%B7%E6%BF%B1%E9%90%B5%E7%94%B2%E6%AD%A6%E5%A3%AB/index/con1.jpg";
        }

    }
    public void setbtnOnClick(){
        YesButton.setOnClickListener(new BtnOnClick());
        NoButton.setOnClickListener(new BtnOnClick());
        TakePicture.setOnClickListener(new BtnOnClick());
        Picture.setOnClickListener(new BtnOnClick());
        pic01.setOnClickListener(new BtnOnClick());
        pic02.setOnClickListener(new BtnOnClick());
        pic03.setOnClickListener(new BtnOnClick());
        pic04.setOnClickListener(new BtnOnClick());
        pic05.setOnClickListener(new BtnOnClick());
        pic06.setOnClickListener(new BtnOnClick());
        pic07.setOnClickListener(new BtnOnClick());
        pic08.setOnClickListener(new BtnOnClick());
        pic09.setOnClickListener(new BtnOnClick());
        pic10.setOnClickListener(new BtnOnClick());
        pic11.setOnClickListener(new BtnOnClick());
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_biometrics);

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
        setbtnOnClick();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵
            new AlertDialog.Builder(BiometricsActivity.this)
                    .setTitle("確認視窗")
                    .setMessage("確定要結束辨識精靈嗎?")
                    .setIcon(R.mipmap.ic_icon)
                    .setPositiveButton("確定",
                            new DialogInterface.OnClickListener() {

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
            Intent it = new Intent(BiometricsActivity.this, CameraActivity.class);
            startActivity(it);
            finish();
        }

        else if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Intent it = new Intent(BiometricsActivity.this, EncyclopediaActivity.class);
            startActivity(it);
            finish();
        } else if (id == R.id.nav_map) {
            Intent it = new Intent(BiometricsActivity.this, MapsActivity.class);
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

    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == Activity.RESULT_OK ){
            switch (requestCode){
                case 100:
                    Intent it = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,imgUri);
                    sendBroadcast(it);
                    break;
                case 101:
                    imgUri = converUri(data.getData());

                    break;
            }
            showImg();
        }
        else {
            Toast.makeText(this,requestCode==100 ? "拍照失敗":"選取失敗", Toast.LENGTH_LONG).show();
        }
    }

    protected void showImg(){
        options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imgUri.getPath() , options);
        int iw = options.outWidth;
        int ih = options.outHeight;
        if(iw > 400 || ih > 300){
            options.inSampleSize = 2;
        }
        options.inJustDecodeBounds = false;
        Bitmap bmp = BitmapFactory.decodeFile(imgUri.getPath());//將圖檔轉換為bitmap物件
        imv.setImageBitmap(bmp);//將照片顯示在ImageView中
    }

    Uri converUri(Uri uri){
        if(uri.toString().substring(0,7).equals("content"))
        {
            String[] colName = {MediaStore.MediaColumns.DATA};//宣告要查詢的欄位
            Cursor cursor = getContentResolver().query(uri,colName,null,null,null);//以Uri進行查詢
            assert cursor != null;
            cursor.moveToFirst();
            uri = Uri.parse("file://"+cursor.getString(0));//將路徑轉為Uri
        }
        return uri;
    }

    public void diaryRequest() {
        url = "http://120.127.16.69/crab/Delete_users_Myorder.php";
        RequestQueue mqueue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(),"回報成功",Toast.LENGTH_LONG).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        "回報失敗", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                diaryID = intent.getStringExtra("ID");
                if(diaryID != null){
                    params.put("item_ID",diaryID);
                }

                return params;
            }
        };
        // Adding request to request queue
        mqueue.add(postRequest);
    }


    private class BtnOnClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch( v.getId() ) {
                case R.id.button7:
                    String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
                    String fname = "p" + System.currentTimeMillis()+".jpg"; //利用時間建立不重複的檔案名
                    imgUri = Uri.parse("file://"+dir+"/"+fname);
                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //啟動相機
                    camera.putExtra(MediaStore.EXTRA_OUTPUT,imgUri);
                    startActivityForResult(camera,100);//啟動Intent,傳出圖片

                    break;
                case R.id.button8:
                    it = new Intent(Intent.ACTION_GET_CONTENT); //選取內容
                    it.setType("image/*"); //可選所有類型的圖片
                    startActivityForResult(it,101); //啟動Intent,傳回選取的圖片

                    break;

                case R.id.YesButton:
                    pic01.setVisibility(View.VISIBLE);
                    pic02.setVisibility(View.VISIBLE);
                    pic03.setVisibility(View.VISIBLE);
                    pic04.setVisibility(View.VISIBLE);
                    pic05.setVisibility(View.VISIBLE);
                    pic06.setVisibility(View.VISIBLE);
                    pic07.setVisibility(View.VISIBLE);
                    pic08.setVisibility(View.VISIBLE);
                    pic09.setVisibility(View.VISIBLE);
                    pic10.setVisibility(View.VISIBLE);
                    pic11.setVisibility(View.VISIBLE);
                    YesButton.setVisibility(View.INVISIBLE);
                    NoButton.setVisibility(View.INVISIBLE);
                    QuestText.setText("螃蟹頭胸甲外觀形狀為：");
                    break;
                case R.id.NoButton:
                    new AlertDialog.Builder(BiometricsActivity.this)
                            .setTitle("確認視窗")
                            .setMessage("確定不是螃蟹嗎?")
                            .setIcon(R.mipmap.ic_icon)
                            .setPositiveButton("確定",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            diaryRequest();
                                            finish();
                                        }
                                    })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub
                                }
                            }).show();

                    break;

                case R.id.pic01:
                    it = new Intent(BiometricsActivity.this, BiometricsActivity2.class);
                    subject = new Bundle();
                    it.putExtra("Subject","短指和尚蟹");
                    it.putExtra("MapImage",Map_Img_Url);
                    it.putExtra("Type",0);
                    it.putExtras(subject);
                    startActivity(it);
                    finish();
                    break;
                case R.id.pic02:
                    it = new Intent(BiometricsActivity.this, BiometricsActivity2.class);
                    subject = new Bundle();
                    subject.putInt("Type",1);
                    subject.putString("MapImage",Map_Img_Url);
                    it.putExtras(subject);
                    startActivity(it);
                    finish();
                    break;
                case R.id.pic03:
                    it = new Intent(BiometricsActivity.this, BiometricsActivity2.class);
                    subject = new Bundle();
                    subject.putInt("Type",2);
                    subject.putString("MapImage",Map_Img_Url);
                    it.putExtras(subject);
                    startActivity(it);
                    finish();
                    break;
                case R.id.pic04:
                    it = new Intent(BiometricsActivity.this, BiometricsActivity2.class);
                    subject = new Bundle();
                    subject.putString("Subject","遠海梭子蟹");
                    subject.putString("MapImage",Map_Img_Url);
                    subject.putInt("Type",0);
                    it.putExtras(subject);
                    startActivity(it);
                    finish();
                    break;
                case R.id.pic05:
                    it = new Intent(BiometricsActivity.this, BiometricsActivity2.class);
                    subject = new Bundle();
                    subject.putInt("Type",3);
                    subject.putString("MapImage",Map_Img_Url);
                    it.putExtras(subject);
                    startActivity(it);
                    finish();
                    break;
                case R.id.pic06:
                    it = new Intent(BiometricsActivity.this, BiometricsActivity2.class);
                    subject = new Bundle();
                    subject.putInt("Type",0);
                    subject.putString("Subject","長趾股蟹窗");
                    it.putExtras(subject);
                    startActivity(it);
                    finish();
                    break;
                case R.id.pic07:
                    it = new Intent(BiometricsActivity.this, BiometricsActivity2.class);
                    subject = new Bundle();
                    subject.putString("MapImage",Map_Img_Url);
                    subject.putInt("Type",4);
                    it.putExtras(subject);
                    startActivity(it);
                    finish();
                    break;
                case R.id.pic08:
                    it = new Intent(BiometricsActivity.this, BiometricsActivity2.class);
                    subject = new Bundle();
                    subject.putString("Subject","豆形拳蟹");
                    subject.putString("MapImage",Map_Img_Url);
                    subject.putInt("Type",0);
                    it.putExtras(subject);
                    startActivity(it);
                    finish();
                    break;
                case R.id.pic09:
                    it = new Intent(BiometricsActivity.this, BiometricsActivity2.class);
                    subject = new Bundle();
                    subject.putString("MapImage",Map_Img_Url);
                    subject.putInt("Type",5);
                    it.putExtras(subject);
                    startActivity(it);
                    finish();
                    break;
                case R.id.pic10:
                    it = new Intent(BiometricsActivity.this, BiometricsActivity2.class);
                    subject = new Bundle();
                    subject.putString("MapImage",Map_Img_Url);
                    subject.putInt("Type",6);
                    it.putExtras(subject);
                    startActivity(it);
                    finish();
                    break;
                case R.id.pic11:
                    it = new Intent(BiometricsActivity.this, BiometricsActivity2.class);
                    subject = new Bundle();
                    subject.putString("Subject","肝葉饅頭蟹");
                    subject.putString("MapImage",Map_Img_Url);
                    subject.putInt("Type",0);
                    it.putExtras(subject);
                    startActivity(it);
                    finish();
                    break;
            }
        }
    }
}
