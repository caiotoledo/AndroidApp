package com.caio.androidapp;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.List;

/**
 * Created by caio on 03/03/17.
 */

public class ListViewAdapter extends ArrayAdapter<String> {

    List<String> dataSource;

    public ListViewAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<String> objects) {
        super(context, R.layout.my_listview_layout, objects);
        dataSource = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder holder = null;

        LayoutInflater inflater;
        Context context= this.getContext();
        inflater = LayoutInflater.from(context);

        if (convertView == null){
            convertView = inflater.inflate(R.layout.my_listview_layout, null, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.getUpperText().setText(dataSource.get(position));
        holder.getLowerText().setText(dataSource.get(position));
        holder.getBDelete().setText("Excluir");

        final int pos = position;
        View.OnClickListener list = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "cliquei! " + String.valueOf(pos), Toast.LENGTH_SHORT).show();
            }
        };

        holder.getBDelete().setOnClickListener( list );

        return convertView;
    }

}
