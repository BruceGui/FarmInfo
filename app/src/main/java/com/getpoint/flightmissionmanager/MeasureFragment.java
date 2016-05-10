package com.getpoint.flightmissionmanager;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Text;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.getpoint.flightmissionmanager.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

//gps latlon convert to baidu latlon CoordinateConverter
public class MeasureFragment extends Fragment {

    private final static String LOG_TAG = "MeasureFragment";
    private final int INDEX_TEXT_SIZE = 80;

    private BaiduMap mBaiduMap;
    private MapView mMapView;
    private LocationClient mLocationClient;
    private ArrayList<LatLng> mMeasureLatLonList;       //实测经纬度
    private ArrayList<LatLng> mBadiDuMeasureLatLonList;     //实测经纬度对应的百度经纬度
    private ArrayList<WayPointInfo> mWayPointInfosList;
    private ArrayList<Text> mTextsList = new ArrayList<Text>();
    private Polyline mPolyline;

    private Button mConfirmButton;
    private Button mEndButton;
    private Button mGetButton;

    private EditText mDotIndexEditText;
    private TextView mDotSumTextView;
    private TextView mLatTextView;
    private TextView mLonTextView;
    private TextView mHeightTextView;
    private EditText mCharacterEditText;        //change to spinner
    private EditText mRadiusEditText;

    private int mDotSum = 0;
    private int mCurDotIndex = 0;
    private WayPointInfo mCurWayPointInfo;
    private GPSInfo mGPSInfo;


    LatLngBounds mLatLngBounds;

    private String mCurrentMissionFileName;
    public static String MISSION_FILE_NAME = "MISSION_FILE_NAME";
    public static MeasureFragment newInstance(String fileName) {
        Bundle args = new Bundle();
        args.putString(MISSION_FILE_NAME, fileName);
        MeasureFragment fragment  = new MeasureFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCurrentMissionFileName = getArguments().getString(MISSION_FILE_NAME);
        getActivity().setTitle(mCurrentMissionFileName);
        parseMeasureInfoFromMissionFile();  //test Code. delete it

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.measure_fragment, container, false);
        initView(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();

    }

    private void initView(View view) {
        mDotIndexEditText = (EditText)view.findViewById(R.id.id_dot_index);
        mDotSumTextView = (TextView)view.findViewById(R.id.id_dot_Sum);
        mLatTextView = (TextView)view.findViewById(R.id.id_lat_value);
        mLonTextView = (TextView)view.findViewById(R.id.id_lon_value);
        mHeightTextView = (TextView)view.findViewById(R.id.id_height_value);
        mCharacterEditText = (EditText)view.findViewById(R.id.id_characteristic_value);        //change to spinner
        mRadiusEditText = (EditText)view.findViewById(R.id.id_radius_value);

        mMapView = (MapView) view.findViewById(R.id.id_bmapView);
        mBaiduMap = mMapView.getMap();

        mBaiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                updateMapByBounds();
            }
        });

        mGetButton = (Button)view.findViewById(R.id.id_get_info_button);
        mConfirmButton = (Button)view.findViewById(R.id.id_confirm_button);
        mEndButton = (Button)view.findViewById(R.id.id_end_button);


        mGetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestGPSIInfo();
            }
        });

        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWayPoint();
            }
        });

        mEndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {       //将航路信息写回航路文件
                writeWaypointIntoFile();
            }
        });

        drawTrack();
        updateViewByFileParseResult();
    }

    private void parseMeasureInfoFromMissionFile() {
        File missionFile = new File(FlightMissionUtils.MISSION_FILE_PATH + mCurrentMissionFileName);
        mMeasureLatLonList = new ArrayList<>();
        mBadiDuMeasureLatLonList = new ArrayList<>();
        mWayPointInfosList= new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(missionFile));
            String line = br.readLine();
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            while (line != null) {
                WayPointInfo wayPointInfo = new WayPointInfo(line);
                mWayPointInfosList.add(wayPointInfo);

                LatLng latLng = new LatLng(wayPointInfo.getLat(), wayPointInfo.getLon());
                mMeasureLatLonList.add(latLng);
                builder.include(latLng);
                line = br.readLine();
            }
            mLatLngBounds = builder.build();
            br.close();
        } catch (FileNotFoundException e) {
            Log.e(LOG_TAG, "File Not found");
        } catch (IOException e) {
            Log.e(LOG_TAG, "IO Exception");
        }
    }

    private void updateViewByFileParseResult() {
        if (mWayPointInfosList != null && mMeasureLatLonList.size() > 0) {
            mCurWayPointInfo = mWayPointInfosList.get(mCurDotIndex);
            mDotSum = mMeasureLatLonList.size();
            updateViewByWayPointInfo(mCurWayPointInfo);
            mDotSumTextView.setText(""+mDotSum);
        } else {
            mDotIndexEditText.setText("1");
            mDotSumTextView.setText("1");
        }
    }

    private void updateMapByBounds() {
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLngBounds(mLatLngBounds);
        mBaiduMap.setMapStatus(mapStatusUpdate);
    }

    private void drawTrack() {
        clearTrack();
        for(int i = 0; i < mMeasureLatLonList.size(); i++) {
            TextOptions textOptions = new TextOptions().text(""+(i+1))
                    .position(mMeasureLatLonList.get(i))
                    .fontSize(INDEX_TEXT_SIZE)
                    .zIndex(8)
                    .fontColor(Color.BLUE)
                    .visible(true);

            Log.e(LOG_TAG, mMeasureLatLonList.get(i).toString());
            Text text = (Text)mBaiduMap.addOverlay(textOptions);
            mTextsList.add(text);
        }

        PolylineOptions polylineOptions = new PolylineOptions()
                .color(Color.RED)
                .points(mMeasureLatLonList)
                .visible(true)
                .zIndex(8);
        mPolyline = (Polyline) mBaiduMap.addOverlay(polylineOptions);
    }

    private void clearTrack() {
        for (int i = 0; i < mTextsList.size(); i++) {
            mTextsList.get(i).remove();
        }
        mTextsList.clear();
        if (mPolyline != null) {
            mPolyline.remove();
        }
    }

    private void updateViewByWayPointInfo(WayPointInfo wayPointInfo) {
        mDotIndexEditText.setText("" + wayPointInfo.getRouteIndex());
        mLonTextView.setText(String.format("%.6f", wayPointInfo.getLon()));
        mLatTextView.setText(String.format("%.6f", wayPointInfo.getLat()));
        mHeightTextView.setText("" +wayPointInfo.getHeight());
        mCharacterEditText.setText(""+wayPointInfo.getVdx1());
        mRadiusEditText.setText("" + wayPointInfo.getVdx2());
    }

    private WayPointInfo getWayPointInfoByView() {
        WayPointInfo wayPointInfo = new WayPointInfo(1,
                mCurDotIndex,
                Double.parseDouble(mLonTextView.getText().toString()),
                Double.parseDouble(mLatTextView.getText().toString()),
                (int)Double.parseDouble(mHeightTextView.getText().toString()),
                Integer.parseInt(mCharacterEditText.getText().toString()),
                Integer.parseInt(mRadiusEditText.getText().toString()), 0);
        Log.e(LOG_TAG, wayPointInfo.toString());
        return wayPointInfo;
    }

    private void requestGPSIInfo() {
        mGPSInfo = new NativeGPSInfo(getActivity());
        mGPSInfo.request();
        mGPSInfo.setOnRequestResultListener(new GPSInfo.OnRequestResultListener() {
            @Override
            public void onRequestResult() {
                mLonTextView.setText(String.format("%.6f", mGPSInfo.getGPSLon()));
                mLatTextView.setText(String.format("%.6f", mGPSInfo.getGPSLat()));
                mHeightTextView.setText("" + mGPSInfo.getHeight());
            }
        });
    }

    private void writeWaypointIntoFile() {
        File file = FlightMissionUtils.MissionFile(mCurrentMissionFileName);
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            for (int i = 0 ; i < mWayPointInfosList.size(); i++) {
                bufferedWriter.write(mWayPointInfosList.get(i).toString());
                bufferedWriter.newLine();
            }
            if (bufferedWriter != null) {
                bufferedWriter.flush();
                bufferedWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(getActivity(), R.string.write_to_file_successfully, Toast.LENGTH_SHORT).show();
    }

    private void addWayPoint() {
        int dotIndex = Integer.parseInt(mDotIndexEditText.getText().toString());

        Log.e(LOG_TAG, " mDOTSum = " + mDotSum + "    dotIndex = " + dotIndex);
        int index = dotIndex - 1;
        if (index > (mDotSum) || index < 0) {
            Toast.makeText(getActivity(), R.string.dot_out_of_range, Toast.LENGTH_SHORT).show();
            return;
        }

        WayPointInfo wayPointInfo = getWayPointInfoByView();
        if (index == mDotSum ) {
            mMeasureLatLonList.add(new LatLng(wayPointInfo.getLat(), wayPointInfo.getLon()));
            mWayPointInfosList.add(wayPointInfo);
            mDotSum++;
        } else {
            mMeasureLatLonList.set(index, new LatLng(wayPointInfo.getLat(), wayPointInfo.getLon()));
            mWayPointInfosList.set(index, wayPointInfo);
        }
        drawTrack();
    }
}
