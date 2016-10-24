package com.getpoint.farminfomanager.fragment.Mission;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.getpoint.farminfomanager.R;
import com.getpoint.farminfomanager.utils.adapters.IndexAdapter;
import com.getpoint.farminfomanager.utils.proxy.MissionItemProxy;
import com.getpoint.farminfomanager.utils.proxy.MissionProxy;
import com.getpoint.farminfomanager.weights.spinnerWheel.CardWheelHorizontalView;
import com.getpoint.farminfomanager.weights.spinnerWheel.adapters.NumericWheelAdapter;
import com.getpoint.farminfomanager.weights.spinners.SpinnerSelfSelect;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gui Zhou on 2016/10/22.
 */

public class BSPointFragment extends PointDetailFragment implements
        CardWheelHorizontalView.OnCardWheelChangedListener {

    private static final String TAG = "BSPoint";

    private TextView pointIndex;
    private EditText altitudeEdt;
    private Button delBSPoint;

    private List<String> pointNum = new ArrayList<>();
    private MissionItemProxy currBSP;

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

        delBSPoint = (Button) view.findViewById(R.id.delPointBtn);
        delBSPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(currBSP != null) {
                    missionProxy.getBaseStationProxies().remove(currBSP);
                    updatePointNum(missionProxy);
                    setPointIndex(missionProxy.getCurrentBaseNumber());
                    mapFragment.updateInfoFromMission(missionProxy);
                }

            }
        });

        final SpinnerSelfSelect mFPIndexSel = (SpinnerSelfSelect) view.findViewById(R.id.spinnerFPIndex);
        mFPIndexSel.setOnSpinnerItemSelectedListener(new SpinnerSelfSelect.OnSpinnerItemSelectedListener() {
            @Override
            public void onSpinnerItemSelected(Spinner spinner, int position) {

                setPointIndex(position + 1);

                if (position < missionProxy.getBaseStationProxies().size()) {
                    addNew = false;
                    currBSP = missionProxy.getBaseStationProxies().get(position);
                } else {
                    addNew = true;
                    currBSP = null;
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

        int length = m.getBaseStationProxies().size() + 1;

        for (int i = 0; i < length; i++) {
            pointNum.add(String.valueOf(i + 1));
        }
    }

    /**
     *  更新当前点的信息
     */
    public void updateCurrentBSP() {
        int fh =  0;

        currBSP.getPointInfo().getPosition().setAltitude(fh);

        addNew = true;
    }

    public float getAltitude() {

        float altitude = 0;

        return altitude;
    }

    public void setPointIndex(int index) {
        pointIndex.setText(String.valueOf(index));
    }

    @Override
    public void onChanged(CardWheelHorizontalView cardWheel, int oldValue, int newValue) {

    }

}
