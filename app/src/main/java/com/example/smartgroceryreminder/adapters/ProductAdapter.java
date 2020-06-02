package com.example.smartgroceryreminder.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.smartgroceryreminder.R;
import com.example.smartgroceryreminder.model.GroceryItems;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> {
    private List<GroceryItems> items;
    private Context context;

    public ProductAdapter(Context c) {
        items = new ArrayList<>();
        context = c;
    }

    public void setItems(List<GroceryItems> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        GroceryItems item = items.get(position);
        if (item.getImage() != null && item.getImage().length() > 0) {
            Glide.with(context).load(item.getImage()).into(holder.image);
        } else {
            holder.image.setVisibility(View.GONE);
        }
        holder.productName.setText(item.getName());
        if (item.getBrand() != null && item.getBrand().length() > 0) {
            holder.brand.setText(item.getBrand());
        } else {
            holder.brandUpper.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ProductHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView productName, brand;
        RelativeLayout brandUpper;

        public ProductHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            productName = itemView.findViewById(R.id.productName);
            brand = itemView.findViewById(R.id.brand);
            brandUpper = itemView.findViewById(R.id.brandUpper);
        }
    }
}
