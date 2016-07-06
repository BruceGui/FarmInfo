package com.getpoint.farminfomanager.fragment.Mission;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.getpoint.farminfomanager.fragment.MeasureFragment;
import com.getpoint.farminfomanager.utils.FarmInfoUtils;
import com.getpoint.farminfomanager.R;
import com.getpoint.farminfomanager.activity.MeasureActivity;
import com.getpoint.farminfomanager.activity.NewMissionActivity;

import java.io.File;
import java.util.ArrayList;

public class MissionManagerFragment extends Fragment {

    private ArrayList<String> mMissionFileNameList;
    private ArrayAdapter<String> mAdapter;
    private ListView mListView;


    public static MissionManagerFragment newInstance() {
        MissionManagerFragment fragment = new MissionManagerFragment();
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mission_fragment, container, false);

        scanMissionFile();
        mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mMissionFileNameList);

        mListView = (ListView) view.findViewById(R.id.id_mission_listview);
        mListView.setAdapter(mAdapter);
        registerForContextMenu(mListView);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_mission_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.id_add_mission:
                Intent intent = new Intent(getActivity(), NewMissionActivity.class);
                getActivity().startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.mission_list_item_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        String fileName = mAdapter.getItem(position);

        switch ( item.getItemId()) {
            case R.id.id_delete_mission:
                deleteFile(fileName);
                break;
            case R.id.id_goto_meausre:
                startMeasureActivity(fileName);
                break;
        }

        return super.onContextItemSelected(item);
    }

    private void scanMissionFile() {
        File fileDir = new File(FarmInfoUtils.MISSION_FILE_PATH);
        File[] missionFileArray = fileDir.listFiles();
        mMissionFileNameList = new ArrayList<>();
        for (File file : missionFileArray) {
            mMissionFileNameList.add(file.getName());
        }
    }

    private void deleteFile(String fileName) {
        File file = FarmInfoUtils.MissionFile(fileName);
        if (file.isFile() && file.exists()) {
            file.delete();
        }

        for (int i = 0; i < mMissionFileNameList.size(); i++ ) {
            if (fileName.equals(mMissionFileNameList.get(i))) {
                mMissionFileNameList.remove(i);
                break;
            }
        }

        mAdapter.notifyDataSetChanged();
    }

    private void startMeasureActivity(String fileName) {
        Intent intent = new Intent(getActivity(), MeasureActivity.class);
        intent.putExtra(MeasureFragment.MISSION_FILE_NAME, fileName);
        getActivity().startActivity(intent);
    }
}
