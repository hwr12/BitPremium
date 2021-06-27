package com.hwrdev.bitpremium.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.hwrdev.bitpremium.R;
import com.hwrdev.bitpremium.activity.sidesheet.AssetViewModel;
import com.hwrdev.bitpremium.model.Asset;
import com.hwrdev.bitpremium.model.BithumbAsset;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdapterAssetList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_ITEM = 1;
    private final int VIEW_SECTION = 0;

    private List<Asset> items = new ArrayList<>();
    private ArrayList<Asset> arrayList;
    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    private AssetViewModel mainViewModel;

    public interface OnItemClickListener {
        void onItemClick(View view, Asset obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterAssetList(Context context, List<Asset> items) {
        this.items = items;
        ctx = context;
        arrayList = new ArrayList<Asset>();
        arrayList.addAll(items);
        Log.d("리사이클러", "AdapterAssetList" + getItemCount());

    }


    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView ticker;
        public TextView name;
        public TextView amount;
        public TextView intoKRW;
        public TextView imageurl;

        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.image);
            ticker = (TextView) v.findViewById(R.id.ticker);
            name = (TextView) v.findViewById(R.id.name);
            amount = (TextView) v.findViewById(R.id.amount);
            intoKRW = (TextView) v.findViewById(R.id.intoKRW);
            lyt_parent=  v.findViewById(R.id.lyt_parent);
            Log.d("리사이클러", "OriginalViewHolder");
        }
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        mainViewModel = new ViewModelProvider((ViewModelStoreOwner) ctx).get(AssetViewModel.class);
        Log.d("리사이클러", "onCreateViewHolder");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.asset_coin_list_item, parent, false);
        vh = new OriginalViewHolder(v);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        OriginalViewHolder view = (OriginalViewHolder) holder;
        Asset a = items.get(position);
        view.ticker.setText(a.ticker);
        view.name.setText(a.name);
        view.amount.setText(a.amount);
        view.intoKRW.setText(a.intoKRW);
        //iew.imageurl.setText(a.image_url);
        Log.d("리사이클러", a.ticker);


//        mainViewModel.getLiveData().observe((LifecycleOwner) ctx, new Observer<List<Asset>>() {
//            @Override
 //           public void onChanged(List<Asset> asset) {
//                Asset a = asset.get(position);
//                view.amount.setText(a.amount);
//                view.intoKRW.setText(a.intoKRW);
//                //notifyDataSetChanged();
//            }
//        });


        view.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, items.get(position), position);
                }
            }
        });



    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        items.clear();
        if (charText.length() == 0) {
            items.addAll(arrayList);
        } else {
            for (Asset data : arrayList) {
                String name = data.name;
                if (name.toLowerCase().contains(charText)) {
                    items.add(data);
                }
            }
        }
        notifyDataSetChanged();
    }



    @Override
    public int getItemCount() {
        return items.size();
    }



    public void insertItem(int index, Asset people){
        items.add(index, people);
        notifyItemInserted(index);
    }

    public void updateData(List<Asset> items) {
        this.items.clear();
        this.items.addAll(items);


        notifyDataSetChanged();
    }

    public void setData(List<Asset> items) {
        this.items = items;
        notifyDataSetChanged();
    }



}