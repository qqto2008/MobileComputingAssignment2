package com.yang.mobilecomputingassignment2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/*
* This activity is used to display the single image
* that the user clicked on the grid view
* in this activity there are one image view
* and a button to get back to the main view
* */
public class ClickImage extends AppCompatActivity {

    //orignalbitmap that user pass to this activity with intent
    Bitmap orignalBitmap;
    //the fianl bitmap after edit
    Bitmap finalBitmap;
    //gray filter butn
    Button grayBtn;
    //go back the to main view btn
    Button gobackBtn;
    //crop btn
    Button cropBtn;
    //undo the filter btn
    Button undoBtn;
    //image store location
    String imageUrl;
    //save the image to some location
    Button saveBtn;
    // file to store image
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_image);
        final Intent intent = getIntent();
        imageUrl = String.valueOf(intent.getStringExtra("fileAddress"));
        orignalBitmap = BitmapFactory.decodeFile(String.valueOf(intent.getStringExtra("fileAddress")));
        final ImageView imageView = findViewById(R.id.editImage);
        imageView.setImageBitmap(orignalBitmap);
        grayBtn = findViewById(R.id.grayBtn);
        grayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalBitmap =gray(orignalBitmap);
                imageView.setImageBitmap(finalBitmap);
            }
        });
        gobackBtn = findViewById(R.id.backBtn);
        gobackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        undoBtn = findViewById(R.id.undoBtn);
        undoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalBitmap = orignalBitmap;
                imageView.setImageBitmap(finalBitmap);
            }
        });
        cropBtn = findViewById(R.id.cropBtn);
        cropBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input(imageView);

            }
        });
        saveBtn = findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                file = new File(imageUrl);
                if (file.exists()){
                    file.delete();
                }
                try{
                    FileOutputStream out = new FileOutputStream(file);
                    finalBitmap.compress(Bitmap.CompressFormat.JPEG,90,out);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                back();
            }
        });

    }
    public Bitmap gray(Bitmap originalBitmap)
    {
        int width, height;
        height = originalBitmap.getHeight();
        width = originalBitmap.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(originalBitmap, 0, 0, paint);
        return bmpGrayscale;
    }
    public void back(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }


    void input(final ImageView imageView){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("How you want to crop");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
// Set up the input
        final EditText x = new EditText(this);
        final EditText y = new EditText(this);
        final EditText width = new EditText(this);
        final EditText height = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        x.setInputType(InputType.TYPE_CLASS_NUMBER);
        y.setInputType(InputType.TYPE_CLASS_NUMBER);
        width.setInputType(InputType.TYPE_CLASS_NUMBER);
        height.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(x);
        layout.addView(y);
        layout.addView(height);
        layout.addView(width);
        builder.setView(layout);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int positionX = Integer.parseInt(x.getText().toString());
                int positionY = Integer.parseInt(y.getText().toString());
                int positionWidth = Integer.parseInt(width.getText().toString());
                int positionHeight = Integer.parseInt(height.getText().toString());
                finalBitmap = Bitmap.createBitmap(orignalBitmap,positionX,positionY,positionWidth,positionHeight);
                imageView.setImageBitmap(finalBitmap);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


}
