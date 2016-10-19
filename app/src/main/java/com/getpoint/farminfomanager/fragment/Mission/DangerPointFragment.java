package com.getpoint.farminfomanager.fragment.Mission;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static com.getpoint.farminfomanager.utils.PixDPConvert.*;

import com.baidu.mapapi.map.Marker;
import com.getpoint.farminfomanager.R;
import com.getpoint.farminfomanager.weights.spinnerWheel.CardWheelHorizontalView;
import com.getpoint.farminfomanager.weights.spinnerWheel.adapters.NumericWheelAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Station on 2016/8/2.
 */
public class DangerPointFragment extends PointDetailFragment implements
        CardWheelHorizontalView.OnCardWheelChangedListener {

    private static final String TAG = "DangerPoint";

    private TextView pointIndex;
    protected TextView innerIndex;
    protected List<Marker> markerToAdd;
    private RelativeLayout titleContent;
    private CardWheelHorizontalView altitudePickerMeter;
    private CardWheelHorizontalView altitudePickerCentimeter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = super.onCreateView(inflater, container, savedInstanceState);

        final TextView pointType = (TextView) view.findViewById(R.id.WaypointType);
        pointType.setText(R.string.bypass_danger_poi);

        final Context context = getActivity().getApplicationContext();

        final NumericWheelAdapter altitudeAdapterMeter = new NumericWheelAdapter(context,
                R.layout.wheel_text_centered, MIN_ALTITUDE, MAX_ALTITUDE, "%d m");
        final NumericWheelAdapter altitudeAdapterCentimeter = new NumericWheelAdapter(context,
                R.layout.wheel_text_centered, MIN_CENTIMETER, MAX_CENTIMETER, "%d cm");
        altitudePickerMeter = (CardWheelHorizontalView) view.findViewById(R.id
                .altitudePickerMeter);
        altitudePickerMeter.setViewAdapter(altitudeAdapterMeter);
        altitudePickerMeter.setCurrentValue(0);
        altitudePickerMeter.addChangingListener(this);

        altitudePickerCentimeter = (CardWheelHorizontalView) view.findViewById(R.id
                .altitudePickerCentimeter);
        altitudePickerCentimeter.setViewAdapter(altitudeAdapterCentimeter);
        altitudePickerCentimeter.setCurrentValue(0);

        innerIndex = (TextView) view.findViewById(R.id.dangerInnerIndex);
        innerIndex.setText(String.valueOf(0));

        pointIndex = (TextView) view.findViewById(R.id.dangerPointIndex);
        pointIndex.setText(String.valueOf(missionProxy.getCurrentBypassNumber()));

        markerToAdd = new ArrayList<>();

        //TODO 动态调整 View 的宽度

        titleContent = (RelativeLayout) view.findViewById(R.id.title_content_danger);
        return view;
    }

    /**
     * 当取消添加点的时候，然后就可以删除 marker
     */
    public void removeMarker() {

        for (Marker marker : markerToAdd) {
            marker.remove();
        }

        clearInnerVar();
    }

    public void clearInnerVar() {
        markerToAdd.clear();
        innerIndex.setText(String.valueOf(0));
    }

    public void setPointIndex(int index) {
        pointIndex.setText(String.valueOf(index));
    }

    public float getFlyHeight() {

        float altitude;

        if (altitudePickerMeter.getCurrentValue() < 0) {
            altitude = altitudePickerMeter.getCurrentValue() * 100
                    - altitudePickerCentimeter.getCurrentValue();
        } else {
            altitude = altitudePickerMeter.getCurrentValue() * 100
                    + altitudePickerCentimeter.getCurrentValue();
        }

        return altitude;
    }

    public void setRelLayoutPar() {
        final Context context = getActivity().getApplicationContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Log.i(TAG, "width: " + size.x);

        float widthByDp = convertPixelToDp(size.x, context);
        Log.i(TAG, "width dp: " + widthByDp);
        ViewGroup.LayoutParams lp = titleContent.getLayoutParams();
        lp.width = (int)widthByDp - 128;
        titleContent.setLayoutParams(lp);
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_editor_detail_danger;
    }

    @Override
    public void onChanged(CardWheelHorizontalView cardWheel, int oldValue, int newValue) {

    }

}
