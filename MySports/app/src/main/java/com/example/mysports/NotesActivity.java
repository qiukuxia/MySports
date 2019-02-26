package com.example.mysports;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mysports.database.Notes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class NotesActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textViewTitle;
    private ListView notesList;
    private ImageView imageViewNew;

    private List<Notes> notesData = new ArrayList<>();

    private NoteAdapter noteAdapter;

    private App myApp;
    private EditText editTextName;
    private EditText editTextCon;
    private Button buttonSave;
    private Button buttonBack;
    private RelativeLayout newBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        myApp = (App) getApplication();
        notesData = Notes.find(Notes.class, "remark = ?",
                new String[]{myApp.getUserName()}, null, "time desc", null);
        noteAdapter = new NoteAdapter();

        initView();
    }

    private void initView() {
        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        notesList = (ListView) findViewById(R.id.notesList);
        imageViewNew = (ImageView) findViewById(R.id.imageViewNew);

        notesList.setAdapter(noteAdapter);

        imageViewNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newBox.setVisibility(View.VISIBLE);
            }
        });
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextCon = (EditText) findViewById(R.id.editTextCon);
        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(this);
        buttonBack = (Button) findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(this);
        newBox = (RelativeLayout) findViewById(R.id.newBox);
        newBox.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSave:
                String name = editTextName.getText().toString();
                String con = editTextCon.getText().toString();
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(con))
                {
                    Notes n = new Notes();
                    n.setCon(con);
                    n.setName(name);
                    n.setRemark(myApp.getUserName());
                    n.setTime(System.currentTimeMillis());
                    n.save();

                    notesData = Notes.find(Notes.class, "remark = ?",
                            new String[]{myApp.getUserName()}, null, "time desc", null);

                    noteAdapter.notifyDataSetChanged();
                    newBox.setVisibility(View.GONE);
                    editTextCon.setText("");
                    editTextName.setText("");
                }
                else
                {
                    Toast.makeText(NotesActivity.this,"输入有误",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.buttonBack:
                newBox.setVisibility(View.GONE);
                editTextCon.setText("");
                editTextName.setText("");
                break;
        }
    }

    private void submit() {
        // validate
        String editTextNameString = editTextName.getText().toString().trim();
        if (TextUtils.isEmpty(editTextNameString)) {
            Toast.makeText(this, "请输入标题", Toast.LENGTH_SHORT).show();
            return;
        }

        String editTextConString = editTextCon.getText().toString().trim();
        if (TextUtils.isEmpty(editTextConString)) {
            Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something


    }

    class NoteAdapter extends BaseAdapter {
        private LayoutInflater mInflater = LayoutInflater.from(NotesActivity.this);

        class ViewHolder {
            public TextView tvplace, tvFlag;

        }

        ViewHolder viewHolder = null;

        @Override
        public int getCount() {
            /**说明：     */

            return notesData.size();
        }

        @Override
        public Object getItem(int position) {
            /**说明：     */

            return null;
        }

        @Override
        public long getItemId(int position) {
            /**说明：     */

            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            /**说明：     */
            convertView = mInflater.inflate(
                    R.layout.item, null);

            viewHolder = new ViewHolder();


            viewHolder.tvplace = (TextView) convertView
                    .findViewById(R.id.textView1);
            viewHolder.tvFlag = (TextView) convertView
                    .findViewById(R.id.textView2);

            convertView.setTag(viewHolder);

            viewHolder.tvplace.setText(notesData.get(position).getName());
            viewHolder.tvFlag.setText(formatDateTime(notesData.get(position).getTime()) + "\n" + notesData.get(position).getCon());

            return convertView;
        }

    }


    public static String formatDateTime(long datelong) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(datelong);
    }
}
