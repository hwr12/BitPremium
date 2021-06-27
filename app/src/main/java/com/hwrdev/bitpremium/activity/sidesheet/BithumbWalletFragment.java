package com.hwrdev.bitpremium.activity.sidesheet;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hwrdev.bitpremium.BinanceApi;
import com.hwrdev.bitpremium.R;
import com.hwrdev.bitpremium.activity.BithumbApi;
import com.hwrdev.bitpremium.adapter.AdapterAssetList;
import com.hwrdev.bitpremium.adapter.AdapterAssetListBithumb;
import com.hwrdev.bitpremium.model.Asset;
import com.hwrdev.bitpremium.model.BithumbAsset;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BithumbWalletFragment extends Fragment {
    AdapterAssetListBithumb recyclerViewAdapter;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
    AssetViewModel viewModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bithumb_wallet, container, false);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        ////////////
        List<BithumbAsset> items = new ArrayList<>();


        recyclerViewAdapter = new AdapterAssetListBithumb(getContext(), items);
        recyclerViewAdapter.setOnItemClickListener((view1, obj, position) -> {

        });

        recyclerView.setAdapter(recyclerViewAdapter);
        viewModel = new ViewModelProvider(requireActivity(), new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication())).get(AssetViewModel.class);
        viewModel.getBithumbLiveData().observe(getViewLifecycleOwner(), assetList -> {
            recyclerViewAdapter.setData(assetList);
            recyclerView.smoothScrollToPosition(recyclerViewAdapter.getItemCount());
        });

        BithumbApi bithumbApi = new BithumbApi("fde3a5cb43231c71b721af072cca965e","85bf62303f6373d4b6b39ea7354844dc", getContext());
        //bithumbApi.requestListenKey();
        bithumbApi.walletSnapShot();
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);
        return view;
    }
}