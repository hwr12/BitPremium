package com.hwrdev.bitpremium.model;

public class BithumbAsset {
    public String image_url;
    public String name;
    public String ticker;
    public String amount;
    public String intoKRW;

    public BithumbAsset() {
    }

    public BithumbAsset(String image_url, String name, String ticker, String amount, String intoKRW) {
        this.image_url = image_url;
        this.name = name;
        this.ticker = ticker;
        this.amount = amount;
        this.intoKRW = intoKRW;

    }

}
