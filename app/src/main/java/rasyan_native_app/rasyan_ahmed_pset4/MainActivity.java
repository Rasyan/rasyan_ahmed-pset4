package rasyan_native_app.rasyan_ahmed_pset4;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;

/**
  this is the main and only activity of this program,
  it sets up the recyclerview in it and adds functionality to the button and edit text within it.
 */

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DataBaseHelper dbhelper;
    private Button add;
    private EditText edit;
    private ArrayList<HashMap<String,String>> data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setup the helper and read the data in the database.
        dbhelper = new DataBaseHelper(this);
        data = dbhelper.read();

        // if the database is empty then add some tips as items on the list.
        if(data.size() == 0){
            addTips();
        }

        setUpRecyclerView();

        //find the views necesairy and set up the listener
        add = (Button) findViewById(R.id.add);
        edit = (EditText) findViewById(R.id.edit);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = String.valueOf(edit.getText());

                if (!input.equals("")) {

                    // when clicked add the text entered in the edit text as an item to the database
                    dbhelper.create(String.valueOf(edit.getText()));
                    data = dbhelper.read();

                    // this function notifies the adapter that the data has changed
                    // and as such the recyclerView needs to be changed
                    mAdapter.swap(data);
                    Toast.makeText(MainActivity.this, "added to TODO list", Toast.LENGTH_SHORT).show();
                    edit.getText().clear();

                    // code below simply hides the virtual keyboard after pressing the button,
                    // i personally found it extremely annoying when the keyboard was still there after i pressed add.
                    // source : http://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(MainActivity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                } else {
                    // if the input is empty simply givea toast that notifying the user, without adding it to the database.
                    Toast.makeText(MainActivity.this, "Nothing entered", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    // adds some tips on how to use the app to the database and updates data to reflect that.
    private void addTips() {
        dbhelper.create("short click to check off (gray out) an item");
        dbhelper.create("long click to delete an item");
        dbhelper.create("new items will be added below the old ones");
        data = dbhelper.read();
    }

    // sets up the recyclerview and connects the layoutmanager and adapter to it.
    private void setUpRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());


        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // use the adapter
        mAdapter = new MyAdapter(data);
        mRecyclerView.setAdapter(mAdapter);
    }
}
