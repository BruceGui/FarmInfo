package com.getpoint.farminfomanager.utils.adapters.pointdetailadapter;

import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.getpoint.farminfomanager.R;
import com.getpoint.farminfomanager.entity.points.enumc.PointItemType;
import com.getpoint.farminfomanager.weights.expandablerecyclerview.ParentViewHolder;

/**
 * Created by Gui Zhou on 2016/10/26.
 */

public class PointContainerViewHolder extends ParentViewHolder{

    private static final float INITIAL_POSITION = 0.0f;
    private static final float ROTATED_POSITION = 180f;

    private TextView pointType;
    private final ImageView mArrowExpandImageView;

    public PointContainerViewHolder(@NonNull View itemView) {
        super(itemView);
        pointType = (TextView) itemView.findViewById(R.id.pointTypeTex);
        mArrowExpandImageView = (ImageView) itemView.findViewById(R.id.arrow_expand_imageview);
    }

    public void bind(@NonNull String type) {
        pointType.setText(type);
    }

    @Override
    public void setExpanded(boolean expanded) {
        super.setExpanded(expanded);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (expanded) {
                mArrowExpandImageView.setImageResource(R.drawable.ic_expand_less_black_24dp);
            } else {
                mArrowExpandImageView.setImageResource(R.drawable.ic_expand_more_black_24dp);
            }
        }
    }

    @Override
    public void onExpansionToggled(boolean expanded) {
        super.onExpansionToggled(expanded);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

            RotateAnimation rotateAnimation;

            if (expanded) {
                rotateAnimation = new RotateAnimation(ROTATED_POSITION,
                        INITIAL_POSITION,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            } else {
                rotateAnimation = new RotateAnimation(-1 * ROTATED_POSITION,
                        INITIAL_POSITION,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            }

            rotateAnimation.setDuration(200);
            rotateAnimation.setFillAfter(true);
        }
    }
}
