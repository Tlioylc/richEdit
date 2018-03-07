package com.tlioylc.myricheditor;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


/**
 * Created by Administrator on 2017/11/6.
 */

public class IconAdapter extends Adapter {
    private List<String> list;
    private SetIcon setIcon;

    public IconAdapter(List<String> list, SetIcon setIcon){
        this.list = list;
        this.setIcon = setIcon;
    }

    public interface SetIcon{
        public void setIcon(String imageName);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.emoticon_grid_adapter,parent, false);
        return new RichWordHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((RichWordHolder)holder).updateView(list.get(position));
    }

    @Override
    public int getItemCount() {
        if(list == null)
            return 0;
        return list.size();
    }

    private class RichWordHolder extends RecyclerView.ViewHolder{
        private ImageView iconImage;
        public RichWordHolder(View itemView) {
            super(itemView);
            iconImage = (ImageView)itemView.findViewById(R.id.imageView);
        }

        public void updateView(final String iconName){
            Bitmap image = null;
            AssetManager am = iconImage.getResources().getAssets();
            try
            {
                InputStream is = am.open("emoticon/"+iconName);
                image = BitmapFactory.decodeStream(is);
                is.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            if(image != null){
                iconImage.setImageBitmap(image);
            }

            iconImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String iconNameArray = iconName.substring(0,iconName .lastIndexOf("."));
                    setIcon.setIcon("["+ CommunityIconExchange.PicNameToCn(iconNameArray)+"]");
                }
            });
        }
    }
}
