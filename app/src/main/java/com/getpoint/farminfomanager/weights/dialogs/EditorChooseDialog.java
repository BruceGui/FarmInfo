package com.getpoint.farminfomanager.weights.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.getpoint.farminfomanager.R;

/**
 * Created by Gui Zhou on 2016/10/19.
 */

public class EditorChooseDialog extends DialogFragment {

    private Button mDeleteBtn;
    private Button mEditorBtn;
    private Button mCancelBtn;

    /**
     * 监听接口
     */
    public interface EditorListener {

        void onDelete();

        void onEditor();

        void onCancel();

    }

    public static EditorChooseDialog newInstance(EditorListener listener) {

        EditorChooseDialog f = new EditorChooseDialog();
        f.listener = listener;
        return f;

    }

    protected EditorListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.editor_choose_dialog, container, false);

        mDeleteBtn = (Button)rootView.findViewById(R.id.id_delete_point);
        mDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDelete();
            }
        });

        mEditorBtn = (Button)rootView.findViewById(R.id.id_editor_point);
        mEditorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onEditor();
            }
        });

        mCancelBtn = (Button)rootView.findViewById(R.id.id_cancel_point);
        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCancel();
            }
        });

        return rootView;
    }


}
