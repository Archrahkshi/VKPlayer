package com.firekost.app.VKPlayer;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class TabPopular extends Fragment {
    private ListView listView;
    private TabPopular context;
    private JSONArray items;
    private VKAdapter adapter;
    private ArrayList<ObjectItem> list;
    private String databaseName = "popular.db";

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public TabPopular() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_tab_popular, container, false);

        listView = (ListView) v.findViewById(R.id.listViewPopular);
        context = this;

        VKApi.audio().getPopular().executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);

                try {
                    items = response.json.getJSONArray("response");
                    adapter = new VKAdapter(context.getContext(), initData());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                listView.setAdapter(adapter);

                dbHelper = new DatabaseHelper(context.getContext(), databaseName);
                db = dbHelper.getWritableDatabase();
                ContentValues values;

                for (int i=0; i<items.length(); i++) {
                    values = new ContentValues();
                    try {
                        values.put(DatabaseHelper.TITLE, items.getJSONObject(i).getString("title"));
                    } catch (JSONException ignored) {}
                    try {
                        values.put(DatabaseHelper.ARTIST, items.getJSONObject(i).getString("artist"));
                    } catch (JSONException ignored) {}
                    try {
                        values.put(DatabaseHelper.DURATION, items.getJSONObject(i).getInt("duration"));
                    } catch (JSONException ignored) {}
                    try {
                        values.put(DatabaseHelper.LYRICS_ID, items.getJSONObject(i).getInt("lyrics_id"));
                    } catch (JSONException ignored) {}
                    try {
                        values.put(DatabaseHelper.AUDIO_ID, items.getJSONObject(i).getInt("id"));
                    } catch (JSONException ignored) {}
                    try {
                        values.put(DatabaseHelper.URL, items.getJSONObject(i).getString("url"));
                    } catch (JSONException ignored) {}
                    db.insert("audios", null, values);
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context.getContext(), Player.class);
                intent.putExtra("POSITION", position);
                intent.putExtra("DATABASE_NAME", databaseName);
                startActivity(intent);
            }
        });

        return v;
    }

    private ArrayList<ObjectItem> initData() throws JSONException {
        list = new ArrayList<>();

        ObjectItem objectItem;

        for (int i=0; i<items.length(); i++){
            objectItem = new ObjectItem(
                    items.getJSONObject(i).getString("title"),
                    items.getJSONObject(i).getString("artist"),
                    items.getJSONObject(i).getInt("duration"),
                    false);
            list.add(objectItem);
        }

        return list;
    }
}
