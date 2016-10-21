package com.getpoint.farminfomanager.utils.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.getpoint.farminfomanager.R;
import com.getpoint.farminfomanager.fragment.Mission.FramePointFragment;

import java.util.List;

/**
 * Created by Gui Zhou on 2016/10/21.
 */

public class IndexAdapter extends BaseAdapter {

    static class ViewHolder {
        private TextView pointIndex;
    }

    private LayoutInflater mInflater;
    private List<String> pointNum;

    public IndexAdapter(Context c, List<String> p) {
        this.mInflater = LayoutInflater.from(c);
        this.pointNum = p;
    }

    @Override
    public int getCount() {
        if (pointNum != null) {
            return pointNum.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;

        if (convertView == null) {

            holder = new ViewHolder();

            convertView = mInflater.inflate(R.layout.point_index_adapter, null);

            holder.pointIndex = (TextView) convertView.findViewById(R.id.pointIndex);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.pointIndex.setText(pointNum.get(position));

        return convertView;
    }

}
