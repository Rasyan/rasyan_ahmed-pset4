package rasyan_native_app.rasyan_ahmed_pset4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    DataBaseHelper dbhelper;
    Button add;
    EditText edit;
    private ArrayList<HashMap<String,String>> data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbhelper = new DataBaseHelper(this);
        data = dbhelper.read();
        //dbhelper.deletAll();
        mRecyclerView = (RecyclerView) findViewById(R.id.list);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // use the adapter
        mAdapter = new MyAdapter(data);
        mRecyclerView.setAdapter(mAdapter);

        add = (Button) findViewById(R.id.add);
        edit = (EditText) findViewById(R.id.edit);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbhelper.create(String.valueOf(edit.getText()));
                data = dbhelper.read();
                mAdapter.swap(data);
                Toast.makeText(MainActivity.this, "added to TODO list", Toast.LENGTH_SHORT).show();
                edit.getText().clear();

                // code below simply hides the virtual keyboard after pressing the button,
                // i personally found it extremely anoying when the keyboard was still there after i pressed add.
                // source : http://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(MainActivity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

            }
        });
    }
}
