package com.example.thota.aseproject;

//This page is used to show the recipies from the available list of items.

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class recipe extends AppCompatActivity {
ListView list;

static  volatile JSONArray recipelist;
ArrayList<String> items= new ArrayList<String>();
List<Integer> itemids = new ArrayList<Integer>();
    CustomAdapter customadapter= new CustomAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        Intent intent=getIntent();
        items=intent.getStringArrayListExtra("items");
        list = (ListView)findViewById(R.id.itemslist);
        list = (ListView)findViewById(R.id.itemslist);
        Log.d("items",items.toString());
      String url= "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/findByIngredients?ingredients=" +items.toString()+"&number=5&ranking=1";
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
                final String result = response.body().string();
                try {
                    JSONArray res= new JSONArray(result);
                    recipelist=res;
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            //Log.d("output",recipelist.toString());


                            list.setAdapter(customadapter); // Stuff that updates the UI

                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        //list = (ListView)findViewById(R.id.list);
list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent recipepagemain= new Intent(recipe.this,MainRecipe.class);

       int x= (int) customadapter.getItemId(position);
        recipepagemain.putExtra("id",x);
        startActivity(recipepagemain);
    }
});
    }
class CustomAdapter extends BaseAdapter {
    ImageView imageView;
    TextView recipeid1;
    int id;
    @Override
    public int getCount() {
        return recipelist.length();
    }

    @Override
    public Object getItem(int position) {

        return null;
    }

    @Override
    public long getItemId(int position) {
        return itemids.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView =getLayoutInflater().inflate(R.layout.customlayout,null);
        imageView=(ImageView)convertView.findViewById(R.id.itemimage);
       // recipeid1= (TextView)convertView.findViewById(R.id.recipeid);

        TextView name = (TextView)convertView.findViewById(R.id.itemname);
        try {
            JSONObject item=(JSONObject) recipelist.get(position);
            String img=item.getString("image");
            String name1=item.getString("title");
            name.setText(name1);
            id=item.getInt("id");
            //recipeid1.setText(id);
            System.out.println(id);
            itemids.add(position,id);
            loadImageFromUrl(img);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    private void loadImageFromUrl(String img) {
        Picasso.with(recipe.this).load(img).placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(imageView,new com.squareup.picasso.Callback(){

                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });
    }
    }
}

