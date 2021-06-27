package com.planet.premium.activity.sidesheet;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.planet.premium.adapter.AdapterAssetList;
import com.planet.premium.model.Asset;

import java.util.ArrayList;
import java.util.List;



public class AssetViewModel  extends ViewModel{
    public static MutableLiveData<List<Asset>> liveData = new MutableLiveData<>();
    private AdapterAssetList adapter;

    public AssetViewModel() {
        // call your Rest API in init method
        init();
    }

    public MutableLiveData<List<Asset>> getLiveData() {
        return liveData;
    }

    public void init(){
        populateList();
    }

    public void populateList(){
        List<Asset> items = new ArrayList<>();
        for (int i = 0; i <9; i++) {
            Asset obj = new Asset();
            obj.name = "-";
            obj.ticker = "-";
            obj.amount =   "0";
            obj.intoKRW = "₩-";
            obj.image_url = "-";
            //obj.gimp = price[i];
            //getBinancePrice(obj, ticker_arr[i]);
            items.add(obj);
        }


    }



    public static void update(List<Asset> assets) {
        liveData.setValue(assets);
    }

    public static void update_balance(Asset asset) {
        String targetticker = asset.ticker;
        Asset result = null;
        for (Asset a : liveData.getValue()) {
            if (targetticker.equals(a.ticker)) {

                liveData.getValue().indexOf(a.ticker);
                a.amount = asset.amount;
                break;
            }
        }

    }


    public AdapterAssetList getAdapter() {
        return adapter;
    }

    public AdapterAssetList getAssetList() {
        return adapter;
    }
}
