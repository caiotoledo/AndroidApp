package com.caio.androidapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.List;

/**
 * Created by caio on 03/03/17.
 */

public class ListViewAdapter extends ArrayAdapter<MedicineAlarm> {

    private List<MedicineAlarm> dataSource;

    public ListViewAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<MedicineAlarm> objects) {
        super(context, R.layout.my_listview_layout, objects);
        dataSource = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder holder = null;

        LayoutInflater inflater;
        final Context context= this.getContext();
        inflater = LayoutInflater.from(context);

        if (convertView == null){
            convertView = inflater.inflate(R.layout.my_listview_layout, null, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.getUpperText().setText(dataSource.get(position).getMedName());
        holder.getLowerText().setText("A partir de " + dataSource.get(position).getAlarmStartTime()
                + " a cada " + String.valueOf(dataSource.get(position).getAlarmIntervalStringTime()) );
        holder.getBDelete().setText("Excluir");

        final int pos = position;
        View.OnClickListener list = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Remove Medicine Alarm")
                        .setMessage("Deseja remover este alarme?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                removeItem(context, pos);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        };

        holder.getBDelete().setOnClickListener( list );

        return convertView;
    }

    public void updateDataSource(List<MedicineAlarm> data){
        dataSource.clear();
        dataSource.addAll(data);
        this.notifyDataSetChanged();
    }

    public void removeItem(Context context, int position) {
        MainActivity.removeMed(context, position);
        dataSource.remove(position);
        this.notifyDataSetChanged();
    }

}
