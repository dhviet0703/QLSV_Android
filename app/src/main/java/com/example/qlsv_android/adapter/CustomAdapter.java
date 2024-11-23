package com.example.qlsv_android.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.example.qlsv_android.R;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<String> {
    private Context context;
    private List<String> dataList;
    private boolean isEditable = false;

    public CustomAdapter(Context context, List<String> dataList) {
        super(context, R.layout.list_item_info, dataList);
        this.context = context;
        this.dataList = dataList;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
        notifyDataSetChanged();
    }
    public void clear() {
        dataList.clear();
    }

    public void addAll(List<String> items) {
        dataList.addAll(items);
    }
    public List<String> getUpdatedData() {
        return dataList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_info, parent, false);
        }

        EditText editInfo = convertView.findViewById(R.id.text_info);
        editInfo.setText(dataList.get(position));
        editInfo.setEnabled(isEditable);

        editInfo.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                dataList.set(position, editInfo.getText().toString());
            }
        });

        return convertView;
    }
}

