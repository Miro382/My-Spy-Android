package com.myspy.myspyandroid.Weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Miroslav Murin on 05.01.2017.
 */

public class PlaceInfo {
    @SerializedName("place_id")
    @Expose
    public String placeId;
    @SerializedName("licence")
    @Expose
    public String licence;
    @SerializedName("osm_type")
    @Expose
    public String osmType;
    @SerializedName("osm_id")
    @Expose
    public String osmId;
    @SerializedName("boundingbox")
    @Expose
    public List<String> boundingbox = null;
    @SerializedName("lat")
    @Expose
    public String lat;
    @SerializedName("lon")
    @Expose
    public String lon;
    @SerializedName("display_name")
    @Expose
    public String displayName;
    @SerializedName("class")
    @Expose
    public String _class;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("importance")
    @Expose
    public Float importance;
    @SerializedName("icon")
    @Expose
    public String icon;

}
