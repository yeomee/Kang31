package com.eastflag.kang.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eastflag.kang.R;
import com.eastflag.kang.dto.MemberVO;

import java.util.ArrayList;

/**
 * Created by oyg on 2016-01-07.
 */
public class Adaptor012 extends BaseAdapter {
    private Context mContext;
    private ArrayList<MemberVO> mMemberList;

    public Adaptor012(Context mContext, ArrayList<MemberVO> mMemberList) {
        this.mContext = mContext;
        this.mMemberList = mMemberList;
    }

    public void setData(ArrayList<MemberVO> mMemberList) {
        this.mMemberList = mMemberList;
    }

    @Override
    public int getCount() {
        return mMemberList.size();
    }

    @Override
    public Object getItem(int position) {
        return mMemberList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.adaptor012, null);

            holder.number = (TextView) convertView.findViewById(R.id.tv_Num);
            holder.name = (TextView) convertView.findViewById(R.id.tv_Name);
            holder.position = (TextView) convertView.findViewById(R.id.tv_position);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.number.setText(String.valueOf(position + 1));
        holder.name.setText(mMemberList.get(position).getMb_name());
        holder.position.setText(mMemberList.get(position).getMb_position());

        return convertView;
    }

    class ViewHolder {
        TextView number;
        TextView name;
        TextView position;
    }
}