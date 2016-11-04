package com.getpoint.farminfomanager.fragment.Mission;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.baidu.mapapi.map.Text;
import com.getpoint.farminfomanager.R;
import com.getpoint.farminfomanager.entity.coordinate.LatLong;
import com.getpoint.farminfomanager.entity.markers.FramePointMarker;
import com.getpoint.farminfomanager.entity.points.FramePoint;
import com.getpoint.farminfomanager.utils.adapters.IndexAdapter;
import com.getpoint.farminfomanager.utils.proxy.MissionItemProxy;
import com.getpoint.farminfomanager.utils.proxy.MissionProxy;
import com.getpoint.farminfomanager.weights.spinnerWheel.CardWheelHorizontalView;
import com.getpoint.farminfomanager.weights.spinnerWheel.adapters.NumericWheelAdapter;
import com.getpoint.farminfomanager.weights.spinners.SpinnerSelfSelect;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Station on 2016/8/2.
 */
public class FramePointFragment extends PointDetailFragment implements
        CardWheelHorizontalView.OnCardWheelChangedListener {

    private static final String TAG = "FramePoint";

    private TextView pointIndex;
    private EditText altitudeEdt;
    private Button getFPoint;
    private Button delFPoint;

    private List<String> pointNum = new ArrayList<>();

    private MissionItemProxy currFP;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_editor_detail_frame;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = super.onCreateView(inflater, container, savedInstanceState);

        final Context context = getActivity().getApplicationContext();

        pointIndex = (TextView) view.findViewById(R.id.WaypointIndex);
        pointIndex.setText(String.valueOf(missionProxy.getCurrentFrameNumber()));

        altitudeEdt = (EditText) view.findViewById(R.id.altitudePickEdit);

        getFPoint = (Button) view.findViewById(R.id.getPointBtn);
        getFPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAddNew()) {
                    FramePoint framePoint = new FramePoint(getNewCoord(),
                            getAltitude());

                    /**
                     *   把当前点添加到任务中去
                     */
                    MissionItemProxy newItem = new MissionItemProxy(missionProxy, framePoint);
                    missionProxy.addItem(newItem);

                    /**
                     *  在地图上产生当前点的标志
                     */
                    FramePointMarker pointMarker = (FramePointMarker) newItem.getMarker();
                    pointMarker.setMarkerNum(missionProxy.getOrder(newItem));
                    mapFragment.updateMarker(pointMarker);
                    mapFragment.updateFramePointPath(missionProxy.getBoundaryItemProxies());
                    updatePointNum(missionProxy);


                } else {

                    updateCurrentFP();

                }

                setPointIndex(missionProxy.getCurrentFrameNumber());
            }
        });

        delFPoint = (Button) view.findViewById(R.id.delPointBtn);
        delFPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currFP != null) {
                    missionProxy.getBoundaryItemProxies().remove(currFP);
                    updatePointNum(missionProxy);
                    setPointIndex(missionProxy.getCurrentFrameNumber());
                    mapFragment.updateInfoFromMission(missionProxy);
                    addNew = true;
                }

            }
        });

        final SpinnerSelfSelect mFPIndexSel = (SpinnerSelfSelect) view.findViewById(R.id.spinnerFPIndex);
        mFPIndexSel.setOnSpinnerItemSelectedListener(new SpinnerSelfSelect.OnSpinnerItemSelectedListener() {
            @Override
            public void onSpinnerItemSelected(Spinner spinner, int position) {

                setPointIndex(position + 1);

                if (position < missionProxy.getBoundaryItemProxies().size()) {
                    addNew = false;
                    currFP = missionProxy.getBoundaryItemProxies()
                            .get(position);
                    int fh = (int) currFP.getPointInfo().getPosition().getAltitude();
                } else {
                    addNew = true;
                    currFP = null;
                }
            }
        });

        /**
         *  设置 mFPIndexSel 的适配器
         */
        final IndexAdapter p = new IndexAdapter(getActivity(), pointNum);
        mFPIndexSel.setAdapter(p);

        return view;
    }

    /**
     * 更新适配器 list 的信息
     *
     * @param m 任务
     */
    public void updatePointNum(MissionProxy m) {

        pointNum.clear();

        int length = m.getBoundaryItemProxies().size() + 1;

        for (int i = 0; i < length; i++) {
            pointNum.add(String.valueOf(i + 1));
        }
    }

    /**
     * 更新当前点的信息
     */
    public void updateCurrentFP() {

        currFP.getPointInfo().getPosition().setAltitude(getAltitude());
        currFP.getPointInfo().getPosition().setLatitude(gps.lat);
        currFP.getPointInfo().getPosition().setLatitude(gps.lon);

        addNew = true;
    }

    public float getAltitude() {

        return gps.alt - appPref.getMobileStationHeight();
    }

    public void setPointIndex(int index) {
        pointIndex.setText(String.valueOf(index));
    }

    public void clearVar() {
        currFP = null;
        pointNum.clear();
        setPointIndex(1);
    }

    @Override
    public void onChanged(CardWheelHorizontalView cardWheel, int oldValue, int newValue) {

    }

}
