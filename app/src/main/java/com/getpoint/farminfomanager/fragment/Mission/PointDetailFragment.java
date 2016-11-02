package com.getpoint.farminfomanager.fragment.Mission;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.getpoint.farminfomanager.FarmInfoManagerApp;
import com.getpoint.farminfomanager.R;
import com.getpoint.farminfomanager.entity.coordinate.LatLong;
import com.getpoint.farminfomanager.entity.points.enumc.PointItemType;
import com.getpoint.farminfomanager.fragment.BaiduMapFragment;
import com.getpoint.farminfomanager.utils.GPS;
import com.getpoint.farminfomanager.utils.adapters.AdapterMissionItems;
import com.getpoint.farminfomanager.utils.proxy.MissionProxy;
import com.getpoint.farminfomanager.weights.spinners.SpinnerSelfSelect;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Gui Zhou on 2016-07-05.
 */

public class PointDetailFragment extends Fragment implements SpinnerSelfSelect.
        OnSpinnerItemSelectedListener {

    private static final String TAG = "PointDetailFragment";

    protected boolean addNew = true;
    protected GPS gps;

    public static String getFragmentTag(PointItemType itemType) {
        switch (itemType) {
            case FRAMEPOINT:
                return "FRAMEPOINT";
            case DANGERPOINT:
                return "DANGERPOINT";
            case STATIONPOINT:
                return "STATIONPOINT";
            default:
                return null;
        }
    }

    public static PointDetailFragment newInstance(PointItemType itemType) {

        PointDetailFragment fragment;
        switch (itemType) {
            case FRAMEPOINT:
                fragment = new FramePointFragment();
                break;
            case DANGERPOINT:
                fragment = new DangerPointFragment();
                break;
            case STATIONPOINT:
                fragment = new BSPointFragment();
                break;
            default:
                fragment = new FramePointFragment();
                break;
        }

        return fragment;

    }

    public interface OnPointDetailListener {

        /**
         * 通知监听者 点的类型已经改变
         *
         * @param newType
         */
        void onPointTypeChanged(PointItemType newType);

    }

    protected static final int MIN_ALTITUDE = -200;
    protected static final int MAX_ALTITUDE = +200;
    protected static final int MIN_CENTIMETER = 0;
    protected static final int MAX_CENTIMETER = 99;

    protected TextView pointType;
    protected MissionProxy missionProxy;
    protected FarmInfoManagerApp farmApp;
    protected SpinnerSelfSelect typeSpinner;
    protected OnPointDetailListener mListener;
    protected AdapterMissionItems commandAdapter;

    protected BaiduMapFragment mapFragment;

    public PointItemType getPointType() {


        if (pointType != null) {
            final String currentType = pointType.getText().toString();

            if (currentType.equals(PointItemType.FRAMEPOINT.getLabel())) {
                return PointItemType.FRAMEPOINT;
            } else if (currentType.equals(PointItemType.DANGERPOINT.getLabel())) {
                return PointItemType.DANGERPOINT;
            } else if(currentType.equals(PointItemType.STATIONPOINT.getLabel())) {
                return PointItemType.STATIONPOINT;
            }

        }

        return PointItemType.FRAMEPOINT;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        farmApp = (FarmInfoManagerApp) getActivity().getApplication();
        missionProxy = farmApp.getMissionProxy();
        gps = farmApp.getGps();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(getLayoutResource(), container, false);

        PointItemType.STATIONPOINT.setLabel(getActivity().getApplicationContext().getResources().getString(
                R.string.base_station_point
        ));
        PointItemType.FRAMEPOINT.setLabel(getActivity().getApplicationContext().getResources().getString(
                R.string.frame_point
        ));
        PointItemType.DANGERPOINT.setLabel(getActivity().getApplicationContext().getResources().getString(
                R.string.danger_point
        ));

        List<PointItemType> list = new LinkedList<>(Arrays.asList(PointItemType.values()));

        commandAdapter = new AdapterMissionItems(getActivity(),
                android.R.layout.simple_list_item_1, list.toArray(new PointItemType[list.size()]));

        typeSpinner = (SpinnerSelfSelect) view.findViewById(R.id.spinnerWaypointType);
        typeSpinner.setAdapter(commandAdapter);
        typeSpinner.setOnSpinnerItemSelectedListener(this);

        pointType = (TextView) view.findViewById(R.id.WaypointType);

        return view;
    }

    public boolean isAddNew() {
        return addNew;
    }

    public void setPointType(String type) {
        pointType.setText(type);
    }

    public void setMapFragment(BaiduMapFragment mapFragment) {
        this.mapFragment = mapFragment;
    }

    public LatLong getNewCoord() {
        return new LatLong(gps.lat, gps.lon);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof OnPointDetailListener)) {
            throw new IllegalStateException("Parent activity must be an instance of"
                    + OnPointDetailListener.class.getName());
        }

        mListener = (OnPointDetailListener) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    protected int getLayoutResource() {
        return R.layout.fragment_editor_detail_generic;
    }

    @Override
    public void onSpinnerItemSelected(Spinner spinner, int position) {

        final PointItemType selectType = commandAdapter.getItem(position);
        pointType.setText(selectType.getLabel());

        mListener.onPointTypeChanged(getPointType());

    }
}
