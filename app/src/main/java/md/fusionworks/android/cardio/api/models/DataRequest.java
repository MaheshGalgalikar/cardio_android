package md.fusionworks.android.cardio.api.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import md.fusionworks.android.cardio.models.Data;


/**
 * Created by admin on 13.08.2015.
 */

public class DataRequest {

    @SerializedName("data")
    private List<Data> dataList;
    @SerializedName("id")
    private long packetId;

    public DataRequest(List<Data> dataList, long packetId) {
        this.dataList = dataList;
        this.packetId = packetId;
    }
}
