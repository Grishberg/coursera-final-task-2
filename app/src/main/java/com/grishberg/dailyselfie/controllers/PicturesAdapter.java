package com.grishberg.dailyselfie.controllers;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.grishberg.dailyselfie.common.controllers.BaseRecyclerAdapter;
import android.widget.TextView;

import com.grishberg.dailyselfie.R;
import com.grishberg.dailyselfie.common.interfaces.OnItemClickListener;
import com.grishberg.dailyselfie.common.db.ListResult;
import com.grishberg.dailyselfie.data.files.PictureManager;
import com.grishberg.dailyselfie.data.model.Pictures;

import java.util.Date;
import java.util.Locale;

/**
 * Created by grishberg on 28.04.16.
 */
public class PicturesAdapter extends BaseRecyclerAdapter<Pictures, PicturesAdapter.PictureViewHolder> {
    private static final String TAG = PicturesAdapter.class.getSimpleName();
    private final OnItemClickListener listener;
    private final PictureManager pictureManager;
    private int lastPosition;

    public PicturesAdapter(@NonNull Context context,
                           @NonNull ListResult<Pictures> listResult,
                           @NonNull PictureManager pictureManager,
                           OnItemClickListener listener) {
        super(context, listResult);
        this.pictureManager = pictureManager;
        this.listener = listener;
    }

    @Override
    public PictureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_cell_preview
                , parent, false);
        PictureViewHolder pvh = new PictureViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PictureViewHolder holder, int position) {
        Pictures item = getItem(position);

        holder.tvDate.setText(String.format(Locale.US, "dd.MM.yyyy HH.mm.ss",new Date(item.getLastupdate())));
        holder.id = item.getId();
        holder.path = item.getPath();
        loadPicture(holder);
        setAnimation(holder.container, position);

        holder.clickListener = listener;
    }

    private void loadPicture(final PictureViewHolder holder) {
        pictureManager.loadPicture(holder.path, new PictureManager.DecodeCompleteListener() {
            @Override
            public void onCompleted(Bitmap bitmap, String path) {
                if(holder.path.equals(path)){
                    holder.ivPreviewIcon.setImageBitmap(bitmap);
                    //TODO start animation
                }
            }
        });
    }

    @Override
    public void onViewDetachedFromWindow(PictureViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.clearAnimation();
    }

    private void setAnimation(View viewToAnimate, int position) {
        // Отображать анимацию только для новых элементов
        if (position > lastPosition) {
            Animation moveAnimation = AnimationUtils.loadAnimation(getContext(),
                    android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(moveAnimation);
            lastPosition = position;
        }
    }

    public static class PictureViewHolder extends RecyclerView.ViewHolder {
        long id;
        String path;
        View container;
        public ImageView ivPreviewIcon;
        TextView tvDate;
        TextView tvNewDay;
        View vNewDateContainer;
        OnItemClickListener clickListener;

        PictureViewHolder(View itemView) {
            super(itemView);
            container = itemView;
            ivPreviewIcon = (ImageView) itemView.findViewById(R.id.ivPreviewIcon);
            tvNewDay = (TextView) itemView.findViewById(R.id.tvNewDay);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            vNewDateContainer = itemView.findViewById(R.id.vNewDateContainer);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        int offsetTop = v.getTop();
                        int offsetBottom = v.getBottom();
                        Log.d(TAG, String.format("onClick: offsetTop = %d, offsetBottom = %d",
                                offsetTop, offsetBottom));
                        clickListener.onItemClicked(id);
                    }
                }
            });
        }

        // отключение анимации
        public void clearAnimation() {
            container.clearAnimation();
            ivPreviewIcon.clearAnimation();
        }
    }
}
