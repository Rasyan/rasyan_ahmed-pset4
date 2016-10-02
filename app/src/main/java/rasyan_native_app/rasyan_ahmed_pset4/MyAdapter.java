package rasyan_native_app.rasyan_ahmed_pset4;

/**
 * Created by Rasyan on 1-10-2016.
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

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    public ArrayList<HashMap<String,String>> data;
    private View.OnClickListener listener;
    private View.OnLongClickListener longlistener;
    private DataBaseHelper dbhelper;
    private Context context;

    public MyAdapter(ArrayList<HashMap<String,String>> data2){

        data = data2;
        // if an item in the list is clicked, go to its info page
        listener = new View.OnClickListener() {
            @Override
            public void onClick(View view){
                TextView text = (TextView) view.findViewById(R.id.todo);
                if(text.getCurrentTextColor() == -16777216) {
                    text.setTextColor(Color.GRAY);
                } else {
                    System.out.println("test color" + text.getCurrentTextColor());
                    text.setTextColor(Color.BLACK);
                }
            }
        };

        longlistener = new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view){
                int position = ((ViewGroup) view.getParent()).indexOfChild(view);
                System.out.println("test " +  Long.parseLong(data.get(position).get("id")));
                dbhelper = new DataBaseHelper(context);
                dbhelper.delete(Long.parseLong(data.get(position).get("id")));
                Toast.makeText(context, "deleted from TODO list", Toast.LENGTH_SHORT).show();
                data.remove(position);
                notifyItemRemoved(position);
                return true;
            }
        };
    }

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

    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder vh, int i) {
        vh.todo.setText(data.get(i).get("todo"));
        vh.itemView.setOnClickListener(listener);
        vh.itemView.setOnLongClickListener(longlistener);

    }

    @Override
    public int getItemCount() {
        if (data != null) {
            return data.size();
        } else {
            return 0;
        }
    }
}