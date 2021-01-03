package com.app.battleword.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.app.battleword.R;
import com.app.battleword.objects.Language;
import com.app.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LanguageSpinnerAdapter extends BaseAdapter {

    List<Language> languages = new ArrayList<>();
    Map<Integer,String> keys = Utils.getKeyString();

    public LanguageSpinnerAdapter(@NonNull Context context, int resource, @NonNull List<Language> objects) {
        languages = objects;
    }

    @Override
    public int getCount() {
        return languages.size();
    }

    @Override
    public Language getItem(int position) {
        return languages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        View v = view;
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.language_setting_items, null);
        ImageView imageView =  v.findViewById(R.id.country_flag);
        TextView language = v.findViewById(R.id.language);
        imageView.setImageResource(languages.get(position).getFlagImageResourceId());
        language.setText(languages.get(position).getLanguage());

        return v;

    }

}
