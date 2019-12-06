package com.example.thota.aseproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.Login;
import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifiedImages;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

//This page is used for scanning the items using camera app and displaying the items name and adding it to the list.

public class Scan extends AppCompatActivity {
    ImageView imageView;
    InputStream finalImageStream;
    Bitmap photo;
    Single<ClassifiedImages> observable;
    TextView lblResult;
    int TAKE_PHOTO_CODE = 0;
    String imagepath="";
    Button scan,log,add2;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        imageView=(ImageView)findViewById(R.id.imageView);
        lblResult=(TextView)findViewById(R.id.Result);
        imageView=(ImageView)findViewById(R.id.imageView);
        lblResult=(TextView)findViewById(R.id.Result);
        scan=(Button)findViewById(R.id.Scan);
        log=(Button)findViewById(R.id.log);
        add2=(Button)findViewById(R.id.add2);
        add2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addlistpage=new Intent(Scan.this,addList.class);
                addlistpage.putExtra("item",lblResult.getText().toString());
                startActivity(addlistpage);
            }
        });
        log.setOnClickListener(v -> {
            Intent logout1 = new Intent(Scan.this,MainActivity.class);
            startActivity(logout1);
      });
     //  add2.setOnClickListener(v -> {
       //    Intent movetoadd =new Intent(Scan.this,addList.class);
         //  movetoadd.putExtra("item",lblResult.getText().toString());
           //startActivity(movetoadd);
       //});
        observable = Single.create((SingleOnSubscribe<ClassifiedImages>) emitter -> {
            InputStream imageStream = null;
            try {
                imageStream = new FileInputStream(imagepath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            InputStream finalImageStream = imageStream;
            IamOptions options = new IamOptions.Builder()
                    .apiKey("vwW-hdaBs80bdEkGeULbFPmr46S_CD7J10qj79VgYTvT")
                    .build();

            VisualRecognition visualRecognition = new VisualRecognition("2018-03-19", options);
            ClassifyOptions classifyOptions = new ClassifyOptions.Builder()
                    .imagesFile(finalImageStream)
                    .imagesFilename("fruitbowl.jpg")
                    .classifierIds(Collections.singletonList("default"))
                    .threshold((float) 0.7)
                    .owners(Collections.singletonList("me"))
                    .build();
            ClassifiedImages classifiedImages = visualRecognition.classify(classifyOptions).execute();
            emitter.onSuccess(classifiedImages);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }
    public void getResult() {
        observable.subscribe(new SingleObserver<ClassifiedImages>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(ClassifiedImages classifiedImages) {
                // System.out.println(classifiedImages.toString());
                //Log.d("result",classifiedImages.toString());
                String res=classifiedImages.toString();
                //parse json objects
                try {
                    JSONObject  reader = new JSONObject(res);

                    JSONArray images = reader.getJSONArray("images");

                    JSONObject classifier = new JSONObject(images.get(0).toString());
                    JSONArray classifiers1 = classifier.getJSONArray("classifiers");
                    JSONObject classes= new JSONObject(classifiers1.get(0).toString());

                    JSONArray x=classes.getJSONArray("classes");

                    JSONObject y=new JSONObject(x.get(0).toString());

                    String classname=y.getString("class");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            lblResult.setText(classname);//set the result value to the textview
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // TextView result = findViewById(R.id.Result);
                //List<ClassResult> resultList = classifiedImages.getImages().get(0).getClassifiers().get(0).getClasses();
                //String displayText = "";
                //for (ClassResult resultL : resultList) {
                //  displayText = displayText + " Class : " + resultL.getClassName() + " Score : " + resultL.getScore() + "\n";
                //  }

                // result.setText(displayText);
            }
            @Override
            public void onError(Throwable e) {
                System.out.println(e.getMessage());
            }
        });
    }
    public void openCamera(View v){
        Intent camera=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, 100);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("camera","onResult");
        if (requestCode == 100) {
            photo = (Bitmap) data.getExtras().get("data");
            Log.d("reply","camera working");
            imageView.setImageBitmap(photo);
            imagepath = savePath();
            getResult();
            Log.d("CameraDemo", "Pic saved");

        }
    }

    private String savePath() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());   //image saving
        String filename = "sample" + timeStamp + ".jpg";
        File file = new File(getFilesDir(), filename);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            photo.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            ;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException io) {
            io.printStackTrace();
        }
        return file.getAbsolutePath();
    }

/*public void logout(View v){
    Intent logout1 = new Intent(Scan.this,Login.class);
    startActivity(logout1);
}*/
}
