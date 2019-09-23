package com.yang.mobilecomputingassignment2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;


import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA_PERMISSION = 200;
    File[] imagesHolder;
    ArrayList<String> imagePaths = new ArrayList<>();
    GridView gridView;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCameraView(view);
            }
        });
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            },REQUEST_CAMERA_PERMISSION); }
        gridView = findViewById(R.id.gridview);
        File images = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +"/imagesFolder");
        if(images.isDirectory()){
            imagesHolder=images.listFiles();
            assert imagesHolder != null;
            for (File file : imagesHolder) {
                imagePaths.add(file.getAbsolutePath());
            }

        }
        ImageAdapter imageAdapter = new ImageAdapter();
        gridView.setAdapter(imageAdapter);


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void openCameraView(View view){
        Intent intent = new Intent(this,Camera.class);
        startActivity(intent);
    }
    public class ImageAdapter extends BaseAdapter{
        private LayoutInflater mInflater;

        public ImageAdapter() {
            this.mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return imagePaths.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            final int p = i;
            if(view==null){
                holder = new ViewHolder();
                view=mInflater.inflate(R.layout.griditem,null);
                holder.imageView = view.findViewById(R.id.imageThumb);
                view.setTag(holder);

            }else {
                holder = (ViewHolder) view.getTag();
            }
            Bitmap bitmap = BitmapFactory.decodeFile(imagePaths.get(i));
            holder.imageView.setImageBitmap(bitmap);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openEditImageView(imagePaths.get(p));
                }
            });

            return view;
        }


    }
    class ViewHolder{
        ImageView imageView;
    }
    void openEditImageView(String fileAddress){
        Intent intent = new Intent(this,ClickImage.class);
        intent.putExtra("fileAddress",fileAddress);
        startActivity(intent);
    }
}

