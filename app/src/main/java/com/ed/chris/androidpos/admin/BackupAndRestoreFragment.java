package com.ed.chris.androidpos.admin;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ed.chris.androidpos.R;
import com.ed.chris.androidpos.database.DatabaseHelper;

import java.io.File;


/**
 * A simple {@link Fragment} subclass.
 */
public class BackupAndRestoreFragment extends Fragment {


    private ImageView buttonBACKUP, buttonRESTORE;

    public BackupAndRestoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_backup_and_restore, container, false);

        buttonBACKUP = v.findViewById(R.id.buttonBackup);
        buttonRESTORE = v.findViewById(R.id.buttonRestore);

        buttonBACKUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //backup();
                final DatabaseHelper db = new DatabaseHelper(getActivity());
                String outFileName = "data/data/com.ed.chris.androidpos/databases/backup" + File.separator;
                performBackup(db,outFileName);

            }
        });

        buttonRESTORE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // restore();

                final DatabaseHelper db = new DatabaseHelper(getActivity());
                performRestore(db);

            }
        });

        return v;
    }

        public void performBackup(final DatabaseHelper db, final String outFileName) {

            File folder = new File("data/data/com.ed.chris.androidpos/databases/backup");

            boolean success = true;
            if (!folder.exists())
                success = folder.mkdirs();
            if (success) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Backup Name");
                final EditText input = new EditText(getActivity());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                            String m_Text = input.getText().toString();
                            String out = outFileName + m_Text + ".db";
                            db.backup(out);

                    }
                });


                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });
                builder.show();
            } else
                Toast.makeText(getActivity(), "Unable to create directory. Retry", Toast.LENGTH_SHORT).show();
        }


    public void performRestore(final DatabaseHelper db) {


        File folder = new File("data/data/com.ed.chris.androidpos/databases/backup");

        if (folder.exists()) {

            final File[] files = folder.listFiles();

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_item);
            for (File file : files)
                arrayAdapter.add(file.getName());

            AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
            builderSingle.setTitle("Restore:");
            builderSingle.setNegativeButton(
                    "cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            dialog.dismiss();
                        }
                    });

                    builderSingle.setAdapter(
                            arrayAdapter, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    try {
                                        db.importDB(files[which].getPath());
                                    } catch (Exception e) {
                                        Toast.makeText(getActivity(), "Unable to restore. Retry", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            builderSingle.show();
        } else
            Toast.makeText(getActivity(), "Backup folder not present.\nDo a backup before a restore!", Toast.LENGTH_SHORT).show();
    }

   /*   try {
        db.importDB(files[which].getPath());
    } catch (Exception e) {
        Toast.makeText(activity, "Unable to restore. Retry", Toast.LENGTH_SHORT).show();
    }

    */
}
