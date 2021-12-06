package com.venumadhav.mumo.network;

import android.content.Context;
import android.os.AsyncTask;
import android.text.PrecomputedText;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import com.venumadhav.mumo.chat.ChatAdapter;
import com.venumadhav.mumo.mood;

import java.io.IOException;
import java.util.Objects;

import javax.xml.transform.Result;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetworkRequestUtil extends AsyncTask<Dummyclass,Void,PeekaLinkResponse> {


    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    public static String imguri = "";





    public static PeekaLinkResponse post(String url, RequestPeekalink json) throws IOException {

//            if (response.isSuccessful())
//            return gson.fromJson(Objects.requireNonNull(response.body()).string(), PeekaLinkResponse.class);
//
        return new PeekaLinkResponse();
    }


    @Override
    protected PeekaLinkResponse doInBackground(Dummyclass... dummyclasses) {
        GsonBuilder builder = new GsonBuilder();
        OkHttpClient client = new OkHttpClient.Builder().build();
        Gson gson = builder.create();
        RequestBody body = RequestBody.create(JSON, gson.toJson(dummyclasses[0].getRequestPeekalink()));
        Request request = new Request.Builder()
                .url(dummyclasses[0].getUrl())
                .addHeader("X-API-Key", "5bd900ca-7897-4fcb-a98e-349f1e6dc4ae")
                .post(body)
                .build();
        try(Response response = client.newCall(request).execute()){
            Log.i("hi","hi");
            return gson.fromJson(Objects.requireNonNull(response.body()).string(), PeekaLinkResponse.class);
           //Log.i("Tag",peekaLinkResponse.getImage().getUrl());
//            imguri = peekaLinkResponse.getImage().getUrl();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new PeekaLinkResponse();

    }

    @Override
    protected void onPostExecute(PeekaLinkResponse peekaLinkResponse) {
        super.onPostExecute(peekaLinkResponse);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://mumo-91024-default-rtdb.firebaseio.com/");

        Picasso.get().load(peekaLinkResponse.getImage().getUrl()).into(ChatAdapter.MyViewHolder.myimg);
    }
}
