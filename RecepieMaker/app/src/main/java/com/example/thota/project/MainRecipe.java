package com.example.thota.aseproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//This page shows the image, title, instructions of the selected recipe.

public class MainRecipe extends AppCompatActivity {
    int id;
    ImageView recipeimage;
    TextView recipetitle,instruction,ingr;
    String ingredientlists="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_recipe);
        Intent intent=getIntent();
        recipeimage=(ImageView)findViewById(R.id.recipeimage);
        recipetitle=(TextView)findViewById(R.id.recipetitle);
        instruction=(TextView)findViewById(R.id.instruction);
        id=intent.getIntExtra("id",0);
        ingr=(TextView)findViewById(R.id.ingr);
        if(id!=0){
            String url= "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/"+id+"/information";
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .addHeader("X-Mashape-Key", "aqReKEVTCsmshkwxFbj0Onzmjxymp1UP7N0jsnkNPIxWG2YLJb")
                    .addHeader("X-Mashape-Host", "spoonacular-recipe-food-nutrition-v1.p.mashape.com")
                    .url(url)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    final String reciperesult= response.body().string();
                    try {
                        Log.d("out",reciperesult);
                        JSONObject res= new JSONObject(reciperesult);

                        String instructions= res.getString("instructions");
                        Log.d("i",instructions);
                        if(instructions.isEmpty()){
                        String Sourceurl=res.getString("sourceUrl");
                        String url1="https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/extract?url="+Sourceurl; //api
                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .addHeader("X-Mashape-Key", "aqReKEVTCsmshkwxFbj0Onzmjxymp1UP7N0jsnkNPIxWG2YLJb")
                                    .addHeader("X-Mashape-Host", "spoonacular-recipe-food-nutrition-v1.p.mashape.com")
                                    .url(url1)
                                    .build();
                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    final String instructionresult= response.body().string();
                                    try {
                                        JSONObject insres=new JSONObject(instructionresult);
                                        String instructions= insres.getString("instructions");
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                instruction.setText(instructions.toString().trim());
                                            }
                                        });

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                        }
                        else{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    instruction.setText(instructions.toString());
                                }
                            });
                        }
                        String img=res.getString("image");
                        JSONArray ingredientslist = res.getJSONArray("extendedIngredients");
                        for(int i=0;i<ingredientslist.length();i++){
                            JSONObject ingres=new JSONObject(ingredientslist.get(i).toString());
                            String ingrname = ingres.getString("name");
                            String ingramount=ingres.getString("amount");
                            ingredientlists=ingredientlists+ingrname+"-"+ingramount+"\n";
                        }

                        String title=res.getString("title");
                        Log.d("title",title);
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                loadImageFromUrl(img);
                                recipetitle.setText(title);
                                ingr.setText(ingredientlists);
                            }
                        });
                        if(instructions.equals(null)){

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });

        }
    }
    private void loadImageFromUrl(String img) {
        Picasso.with(MainRecipe.this).load(img).placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(recipeimage,new com.squareup.picasso.Callback(){

                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });
    }
}
