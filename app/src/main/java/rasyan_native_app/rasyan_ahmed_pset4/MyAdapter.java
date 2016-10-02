package rasyan_native_app.rasyan_ahmed_pset4;

/**
 * Created by Rasyan on 1-10-2016.
 *
 * The adapter which regulates the views in the recyclerView and provides
 * functionality for clicking them, both long and short clicks
 */

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    public ArrayList<HashMap<String,String>> data;
    private View.OnClickListener listener;
    private View.OnLongClickListener longlistener;
    private DataBaseHelper dbhelper;
    private Context context;

    // constructor, mainly houses the two different listeners.
    public MyAdapter(ArrayList<HashMap<String,String>> tempData){
        data = tempData;

        // changes an items checked status when it is short clicked
        listener = new View.OnClickListener() {
            @Override
            public void onClick(View view){
                int position = ((ViewGroup) view.getParent()).indexOfChild(view);
                HashMap<String,String> currentViewData = data.get(position);
                dbhelper = new DataBaseHelper(context);  // cannot be declared above this listener

                // sets the checked status to the opposite of its current status in the database.
                // also does this for the arraylist data that this adapter uses
                // so that it can notify the recyclerview that this item has changed and needs to be reloaded.
                if (Objects.equals(currentViewData.get("checked"), "false")) {
                    dbhelper.update(Long.parseLong(currentViewData.get("id")),
                            currentViewData.get("text"),"true");
                    currentViewData.put("checked","true");
                    Toast.makeText(context, "checked off from TODO list", Toast.LENGTH_SHORT).show();
                } else {
                    dbhelper.update(Long.parseLong(currentViewData.get("id")),
                            currentViewData.get("text"),"false");
                    currentViewData.put("checked","false");
                    Toast.makeText(context, "returned check to TODO list", Toast.LENGTH_SHORT).show();
                }
                data.set(position,currentViewData);
                notifyItemChanged(position);
            }
        };

        // delets an item from the database when its long clicked
        // this is also done for the arraylist data that this adapter uses
        // so that it can notify the recycler view that this item needs to be removed from the list.
        longlistener = new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view){
                int position = ((ViewGroup) view.getParent()).indexOfChild(view);
                dbhelper = new DataBaseHelper(context);  // cannot be declared above this listener
                dbhelper.delete(Long.parseLong(data.get(position).get("id")));
                Toast.makeText(context, "deleted from TODO list", Toast.LENGTH_SHORT).show();
                data.remove(position);
                notifyItemRemoved(position);
                return true;
            }
        };
    }

    // a method that is called in main Actifity when an item is added to the database,
    // it swaps the data in this adapter with the new one provided
    // by deleting the old one and adding all the data from the new one.
    // so that the recyclerlist can be notified of a dataset change.
    public void swap(ArrayList<HashMap<String,String>> newData){
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }
    // make the viewholder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView todo;

        public ViewHolder(View itemView) {
            super(itemView);
            todo = (TextView) itemView.findViewById(R.id.todo);
        }
    }
    // choose singleview as viewholder
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singleview, parent, false);
        ViewHolder vh = new ViewHolder(view);
        context = view.getContext();
        return vh;
    }

    // sets the text and color of the items in the recyclerList based on the data in the database.
    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder vh, int i) {
        HashMap<String,String> currentViewData = data.get(i);
        vh.todo.setText(currentViewData.get("text"));

        // sets the text color based on its checked status.
        if (Objects.equals(currentViewData.get("checked"), "false")) {
            vh.todo.setTextColor(Color.BLACK);
        } else {
            vh.todo.setTextColor(Color.GRAY);
        }

        // sets up the listeners within them
        vh.itemView.setOnClickListener(listener);
        vh.itemView.setOnLongClickListener(longlistener);
    }

    // returns the size of the data it contains, which reflects the number of entrys in the recyclerList
    @Override
    public int getItemCount() {
        if (data != null) {
            return data.size();
        } else {
            return 0;
        }
    }
}