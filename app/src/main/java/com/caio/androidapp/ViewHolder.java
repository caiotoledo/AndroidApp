package com.caio.androidapp;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by caio on 03/03/17.
 */

public class ViewHolder {
    private View row;
    private TextView upperText = null, lowerText = null;
    private Button bDelete = null;

    public ViewHolder(View row){
        this.row = row;
    }

    public TextView getUpperText(){
        if (this.upperText == null){
            this.upperText = (TextView) row.findViewById(R.id.medName);
        }
        return this.upperText;
    }

    public TextView getLowerText(){
        if (this.lowerText == null){
            this.lowerText = (TextView) row.findViewById(R.id.timeParam);
        }
        return this.lowerText;
    }

    public Button getBDelete(){
        if (this.bDelete == null){
            this.bDelete = (Button) row.findViewById(R.id.bDelTime);
            this.bDelete.setOnClickListener( new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                }
            });
        }
        return this.bDelete;
    }
}
