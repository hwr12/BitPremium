package com.planet.premium.utils;

public class CryptoIconURLBuilder {

    public String code;
    public String style;
    public int size;
    public String url;
    public String color;

    // Private
    private String baseURL =  "https://cryptoicons.org";

    // MARK: Initialization

    public String url(String style, String code, int size, String color)
    {
        this.style = style;
        this.code = code;
        this.size = size;
        this.color = color;

        url = baseURL + "/" + style + "/"  + code+ "/" + size;

        return url;
    }
}
