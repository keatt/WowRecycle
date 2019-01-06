package com.example.user.wowrecycle;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    Context mContext;
    List<Reward> mData;


    public RecyclerViewAdapter(Context mContext, List<Reward> data) {
        this.mContext = mContext;
        this.mData = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.item_reward,parent,false);
        MyViewHolder vHolder = new MyViewHolder(v);
        return vHolder;
    }

    public static final String FILE_NAME="com.example.user.wowrecycle";
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.tv_desc.setText(mData.get(position).getDesc());
        holder.btn_view.setText(mData.get(position).getView());
        holder.iv_reward.setImageResource(mData.get(position).getPhoto());
                holder.btn_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Fragment fr=new RewardDetails();
                        SharedPreferences pref = v.getContext().getSharedPreferences(FILE_NAME,0);
                        SharedPreferences.Editor editor=pref.edit();
                        editor.putString("tnc",mData.get(position).getTnc());
                        editor.putString("detail",mData.get(position).getDetail());
                        editor.commit();
                        ((FragmentActivity) v.getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.content,fr).commit();


            }
        });




    }

    @Override
    public int getItemCount() {

        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_desc;
        private Button btn_view;
        private ImageView iv_reward;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_desc = (TextView)itemView.findViewById(R.id.reward_desc);
            btn_view = (Button)itemView.findViewById(R.id.reward_view);
            iv_reward = (ImageView)itemView.findViewById(R.id.reward_image);
        }
    }
}
