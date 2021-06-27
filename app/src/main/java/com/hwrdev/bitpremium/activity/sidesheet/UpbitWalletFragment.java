package com.planet.premium.activity.sidesheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.planet.premium.R;
import com.planet.premium.adapter.AdapterAssetList;
import com.planet.premium.model.Asset;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpbitWalletFragment extends Fragment {
    AdapterAssetList recyclerViewAdapter;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_binance_wallet, null);

        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(linearLayoutManager);

        ////////////
        List<Asset> assets = new ArrayList<>();
        Asset asset = new Asset();
        asset.ticker = "BTC";
        asset.amount = "100";
        assets.add(asset);

    ////////////////////

        recyclerViewAdapter = new AdapterAssetList(getContext(), assets);
        recyclerViewAdapter.setOnItemClickListener(new AdapterAssetList.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Asset obj, int position) {
                final Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://118.67.129.104/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

            }
        }) ;
        //recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setAdapter(recyclerViewAdapter);
        return inflater.inflate(R.layout.fragment_binance_wallet,container,false);
    }

}