package com.sage.cabapp.ui.tripinquirypage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.sage.cabapp.R;
import com.sage.cabapp.ui.tripinquirypage.model.AdvancedProblemPOJO;

import java.util.List;

public class AdvancedProblemAdapter extends ArrayAdapter<AdvancedProblemPOJO> {
    private List<AdvancedProblemPOJO> items;
    private Context context;

    public AdvancedProblemAdapter(@NonNull Context context, int resource, @NonNull List<AdvancedProblemPOJO> objects) {
        super(context, resource, objects);
        this.items = objects;
        this.context = context;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        AdvancedProblemAdapter.ViewHolder holder;

        if (null == convertView) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert vi != null;
            convertView = vi.inflate(R.layout.advanced_problem_list_item, parent, false);
            holder = new AdvancedProblemAdapter.ViewHolder();
            holder.text = convertView.findViewById(R.id.custom_cell_text);
            holder.icon = convertView.findViewById(R.id.custom_cell_image);
            convertView.setTag(holder);
        } else {
            holder = (AdvancedProblemAdapter.ViewHolder) convertView.getTag();
        }

        if (null != holder) {
            holder.text.setText(items.get(position).getTitle());
            holder.icon.setImageResource(items.get(position).getIcon());
        }
        return convertView;
    }

    private class ViewHolder {
        AppCompatTextView text;
        AppCompatImageView icon;
    }

    @Nullable
    @Override
    public AdvancedProblemPOJO getItem(int position) {
        return super.getItem(position);
    }
}
