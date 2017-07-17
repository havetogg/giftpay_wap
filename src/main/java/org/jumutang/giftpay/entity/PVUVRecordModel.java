package org.jumutang.giftpay.entity;

import java.io.Serializable;

/**
 * Created by RuanYJ on 2017/3/28.
 */
public class PVUVRecordModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String pvToday;
    private String uvToday;
    private String pvTotal;
    private String uvTotal;

    @Override
    public String toString() {
        return "PVUVRecordModel{" +
                "id='" + id + '\'' +
                ", pvToday='" + pvToday + '\'' +
                ", uvToday='" + uvToday + '\'' +
                ", pvTotal='" + pvTotal + '\'' +
                ", uvTotal='" + uvTotal + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPvToday() {
        return pvToday;
    }

    public void setPvToday(String pvToday) {
        this.pvToday = pvToday;
    }

    public String getUvToday() {
        return uvToday;
    }

    public void setUvToday(String uvToday) {
        this.uvToday = uvToday;
    }

    public String getPvTotal() {
        return pvTotal;
    }

    public void setPvTotal(String pvTotal) {
        this.pvTotal = pvTotal;
    }

    public String getUvTotal() {
        return uvTotal;
    }

    public void setUvTotal(String uvTotal) {
        this.uvTotal = uvTotal;
    }
}
