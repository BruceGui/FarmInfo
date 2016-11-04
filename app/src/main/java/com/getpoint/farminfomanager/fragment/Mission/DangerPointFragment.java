package com.getpoint.farminfomanager.fragment.Mission;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import static com.getpoint.farminfomanager.utils.PixDPConvert.*;

import com.baidu.mapapi.map.Marker;
import com.getpoint.farminfomanager.R;
import com.getpoint.farminfomanager.entity.markers.DangerPointMarker;
import com.getpoint.farminfomanager.entity.points.DangerPoint;
import com.getpoint.farminfomanager.entity.points.PointInfo;
import com.getpoint.farminfomanager.entity.points.enumc.DangerPointType;
import com.getpoint.farminfomanager.utils.adapters.IndexAdapter;
import com.getpoint.farminfomanager.utils.proxy.MissionItemProxy;
import com.getpoint.farminfomanager.utils.proxy.MissionProxy;
import com.getpoint.farminfomanager.weights.spinners.SpinnerSelfSelect;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Station on 2016/8/2.
 */

//TODO 对代码的结构进行整理，重新设计逻辑

public class DangerPointFragment extends PointDetailFragment {

    private static final String TAG = "DangerPoint";

    private TextView pointIndex;
    private RadioGroup dPointTypeSel;
    protected TextView innerIndex;
    protected List<Marker> markerToAdd;

    private EditText altitudeEdt;

    private List<String> pointIND = new ArrayList<>();
    private List<String> innerInList = new ArrayList<>();

    private MissionItemProxy currDP;
    private List<PointInfo> currInPlist = null;
    private PointInfo currInP;

    private boolean addInP = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = super.onCreateView(inflater, container, savedInstanceState);

        final Context context = getActivity().getApplicationContext();

        pointIndex = (TextView) view.findViewById(R.id.dangerPointIndex);
        innerIndex = (TextView) view.findViewById(R.id.dangerInnerIndex);
        innerIndex.setText(String.valueOf(0));

        altitudeEdt = (EditText) view.findViewById(R.id.altitudePickEdit);
        final Button getPointBtn = (Button) view.findViewById(R.id.getPointBtn);
        final Button delPointBtn = (Button) view.findViewById(R.id.delPointBtn);

        /**
         *   添加 删除障碍点 内部点的监听函数
         */
        getPointBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //if (addNew) {
                //Toast.makeText(getActivity(), getString(R.string.please_add_point_first),
                //        Toast.LENGTH_SHORT).show();
                if (currDP == null) {

                    List<PointInfo> p = new ArrayList<>();

                    final DangerPoint dangerPoint = new DangerPoint(p);
                    dangerPoint.setdPType(getCurrentDPType());
                    /**
                     *   把当前点添加到任务中去
                     */
                    MissionItemProxy newItem = new MissionItemProxy(missionProxy, dangerPoint);
                    missionProxy.addItem(newItem);

                    currDP = newItem;
                    currInPlist = p;

                }

                if (addInP) {
                    PointInfo p = new PointInfo(getNewCoord(),
                            getFlyHeight());

                    switch (getCurrentDPType()) {
                        case BYPASS:
                            p.setPointNum((short) 6);
                            break;
                        case CLIMB:
                            p.setPointNum((short) 7);
                            break;
                        case FORWARD:
                            p.setPointNum((short) 8);
                            break;
                        default:
                            break;
                    }

                    ((DangerPoint) currDP.getPointInfo()).getInnerPoints()
                            .add(p);
                    ((DangerPoint) currDP.getPointInfo()).setdPType(getCurrentDPType());

                    updateInnerIndex(currDP);
                    mapFragment.updateInfoFromMission(missionProxy);

                } else {
                    updateCurrentInP();
                }

                //} else {

                //}
            }
        });

        delPointBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 *  首先删除障碍点内部点的信息，障碍点内部点为空
                 *  那么就删除整个障碍点。
                 */
                if (currInPlist != null) {
                    if (!currInPlist.isEmpty()) {
                        if (currInP != null) {
                            currInPlist.remove(currInP);
                            //删除后更新一些信息
                            updateInnerIndex(currDP);
                            setInPointIndex(currInPlist.size() + 1);
                            currInP = null;
                        }
                    } else {
                        missionProxy.getDangerItemProxies().remove(currDP);
                        updatePointIND(missionProxy);
                        setPointIndex(missionProxy.getCurrentDangerNumber());
                        if (missionProxy.getDangerItemProxies().isEmpty()) {
                            currDP = null;
                        } else {
                            currDP = missionProxy.getDangerItemProxies()
                                    .get(missionProxy.getCurrentDangerNumber());
                            updateCurrVar(currDP);
                        }
                    }

                    addInP = true;
                    // 删除点后更新 marker 的信息
                    mapFragment.updateInfoFromMission(missionProxy);
                }

            }
        });

        /**
         *  障碍点类型的选择
         */
        dPointTypeSel = (RadioGroup) view.findViewById(R.id.danger_point_type_rgroup);


        /**
         *   选择障碍点 及 内部点 的东西
         */
        final SpinnerSelfSelect pointINDSel = (SpinnerSelfSelect) view.findViewById(R.id.spinnerDPIndex);
        pointINDSel.setOnSpinnerItemSelectedListener(new SpinnerSelfSelect.OnSpinnerItemSelectedListener() {
            @Override
            public void onSpinnerItemSelected(Spinner spinner, int position) {

                addNew = false;

                updateInnerIndex(missionProxy.getDangerItemProxies().get(position));

                currDP = missionProxy.getDangerItemProxies()
                        .get(position);
                final DangerPoint dp = (DangerPoint) currDP.getPointInfo();
                currInPlist = dp.getInnerPoints();

                switch (dp.getdPType()) {
                    case BYPASS:
                        dPointTypeSel.check(R.id.bypass_point_radio);
                        break;
                    case CLIMB:
                        dPointTypeSel.check(R.id.climb_point_radio);
                        break;
                    case FORWARD:
                        dPointTypeSel.check(R.id.forward_point_radio);
                        break;
                    default:
                        break;
                }

                setPointIndex(position);

            }
        });


        final IndexAdapter pINDAdapter = new IndexAdapter(getActivity(), pointIND);
        pointINDSel.setAdapter(pINDAdapter);

        final SpinnerSelfSelect pointInnerSel = (SpinnerSelfSelect) view.findViewById(R.id.spinnerDPInnerIndex);
        pointInnerSel.setOnSpinnerItemSelectedListener(new SpinnerSelfSelect.OnSpinnerItemSelectedListener() {
            @Override
            public void onSpinnerItemSelected(Spinner spinner, int position) {

                setInPointIndex(position + 1);

                if (currInPlist != null) {
                    if (position < currInPlist.size()) {
                        addInP = false;
                        currInP = currInPlist.get(position);
                    } else {
                        addInP = true;
                        currInP = null;
                    }
                }

            }
        });
        final IndexAdapter innerSelAdapter = new IndexAdapter(getActivity(), innerInList);
        pointInnerSel.setAdapter(innerSelAdapter);

        markerToAdd = new ArrayList<>();
        return view;
    }

    /**
     * 当取消添加点的时候，然后就可以删除 marker
     */
    public void removeMarker() {

        for (Marker marker : markerToAdd) {
            marker.remove();
        }

        clearVar();
    }

    /**
     * 获取当前障碍点的 类型
     *
     * @return 障碍点类型
     */

    public DangerPointType getCurrentDPType() {

        switch (dPointTypeSel.getCheckedRadioButtonId()) {
            case R.id.bypass_point_radio:
                return DangerPointType.BYPASS;
            case R.id.forward_point_radio:
                return DangerPointType.FORWARD;
            case R.id.climb_point_radio:
                return DangerPointType.CLIMB;
            default:
                return null;
        }

    }

    /**
     * 更新障碍点的 索引
     *
     * @param m
     */
    public void updatePointIND(MissionProxy m) {
        pointIND.clear();

        int length = m.getDangerItemProxies().size();

        if (length == 0) {
            length++;
        }

        for (int i = 0; i < length; i++) {
            pointIND.add(String.valueOf(DangerPointMarker.IND[i]));
        }
    }

    public void updateInnerIndex(MissionItemProxy m) {

        innerInList.clear();

        List<PointInfo> p = ((DangerPoint) m.getPointInfo()).getInnerPoints();

        int length = p.size() + 1;

        for (int i = 0; i < length; i++) {
            innerInList.add(String.valueOf(i + 1));
        }

        setInPointIndex(length);
        Log.i(TAG, "length " + length);
    }

    /**
     * 更新当前障碍点 的信息
     */
    public void updateCurrentDP() {

        if (addInP) {

        } else {
            updateCurrentInP();
        }

        addNew = true;

    }

    public void updateCurrentInP() {

        addInP = true;
    }

    /**
     * 更新当前 障碍点 及 内部点参数信息
     */
    public void updateCurrVar(MissionItemProxy m) {

        currDP = m;
        currInPlist = ((DangerPoint) m.getPointInfo()).getInnerPoints();

        updateInnerIndex(m);
        final DangerPoint dp = (DangerPoint) currDP.getPointInfo();

        switch (dp.getdPType()) {
            case BYPASS:
                dPointTypeSel.check(R.id.bypass_point_radio);
                break;
            case CLIMB:
                dPointTypeSel.check(R.id.climb_point_radio);
                break;
            case FORWARD:
                dPointTypeSel.check(R.id.forward_point_radio);
                break;
            default:
                break;
        }
    }

    public void clearVar() {
        //markerToAdd.clear();
        currInPlist = null;
        currDP = null;

        if (innerInList != null) {
            innerInList.clear();
            innerInList.add(String.valueOf(1));
        }

        setInPointIndex(1);
        setPointIndex(0);
    }

    public void setPointIndex(int index) {
        pointIndex.setText(String.valueOf(DangerPointMarker.IND[index]));
    }

    public void setInPointIndex(int index) {
        innerIndex.setText(String.valueOf(index));
    }

    public float getFlyHeight() {

        float al = gps.alt - appPref.getMobileStationHeight();

        if (TextUtils.isEmpty(altitudeEdt.getText())) {

        } else {
            al += Float.valueOf(altitudeEdt.getText().toString());
        }

        return al;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_editor_detail_danger;
    }

}
