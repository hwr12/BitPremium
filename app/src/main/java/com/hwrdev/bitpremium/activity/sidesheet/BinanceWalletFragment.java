package com.planet.premium.activity.sidesheet;

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
import com.planet.premium.BinanceApi;
import com.planet.premium.R;
import com.planet.premium.adapter.AdapterAssetList;
import com.planet.premium.model.Asset;
import java.util.ArrayList;
import java.util.List;

public class BinanceWalletFragment extends Fragment {
    AdapterAssetList recyclerViewAdapter;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
    AssetViewModel viewModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_binance_wallet, container, false);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        ////////////
        List<Asset> items = new ArrayList<>();

    ////////////////////
        Log.d("리사이클러 아이템 수", String.valueOf(items.size()));
        recyclerViewAdapter = new AdapterAssetList(getContext(), items);
        recyclerViewAdapter.setOnItemClickListener(new AdapterAssetList.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Asset obj, int position) {

            }
        });

        recyclerView.setAdapter(recyclerViewAdapter);
        viewModel = new ViewModelProvider(requireActivity(), new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication())).get(AssetViewModel.class);
        viewModel.getLiveData().observe(getViewLifecycleOwner(), assetList -> {
            recyclerViewAdapter.setData(assetList);
            recyclerView.smoothScrollToPosition(recyclerViewAdapter.getItemCount());
        });

        BinanceApi binanceApi = new BinanceApi("AsuW8pjiAx6XVWsKGxTnscWS6xfaeJa97KOSGY3CHbw8MTml5E2V0jk5EhpoBLC3","k9xip6mFjadZWHcrpyqCe7vYW9HTCCtbUSCTE7WZZJSo43KqFiAD5El26y6ERSqK", getContext());
        binanceApi.requestListenKey();
        binanceApi.walletSnapShot();
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);
        return view;
    }




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataset();
    }

    private void initDataset() {
        //for Test/
    }

}