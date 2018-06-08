package com.example.kkccbd;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    private final OnItemClickListener listener;
    private List<Category> categoryList;

    public interface OnItemClickListener {
        void onItemClick(Category item);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageButton;

        public MyViewHolder(View view) {
            super(view);
            imageButton = (ImageView) view.findViewById(R.id.row_image);
        }
        public void bind(final Category item, final OnItemClickListener listener) {
            String category = item.getName()+"_tab";
            Context context = imageButton.getContext();
            int id = context.getResources().getIdentifier(category, "mipmap", context.getPackageName());
            imageButton.setImageResource(id);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }


    public CategoryAdapter(List<Category> categoryList,OnItemClickListener listener) {
        this.categoryList = categoryList;
        this.listener = listener;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.catergory_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        String category = categoryList.get(position).getName();

        holder.bind(categoryList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }



}
