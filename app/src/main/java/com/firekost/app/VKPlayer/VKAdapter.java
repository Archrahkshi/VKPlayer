package com.firekost.app.VKPlayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class VKAdapter extends ArrayAdapter<String>{

    private List<ObjectItem> data;
    private Context context;

    public VKAdapter(Context context, List<ObjectItem> data) {
        super(context, R.layout.object_item);
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public String getItem(int position) {
        return data.get(position).getTitle();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.object_item, parent, false);

        TextView title = (TextView)view.findViewById(R.id.title);
        TextView artist = (TextView)view.findViewById(R.id.artist);
        TextView duration = (TextView)view.findViewById(R.id.duration);

        ObjectItem objectItem = data.get(position);

        title.setText(objectItem.getTitle());
        artist.setText(objectItem.getArtist());
        duration.setText(String.valueOf(objectItem.getDuration()));

        return view;
    }

}
