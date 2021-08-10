package com.sage.cabapp.ui.payment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Maroof Ahmed Siddique on 14-02-2020.
 * Mindcrew Technologies Pvt Ltd
 * maroofahmedsiddique@gmail.com
 */
public class AllCardsData implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("object")
    @Expose
    private String object;
    @SerializedName("billing_details")
    @Expose
    private AllCardsBillingDetails billingDetails;
    @SerializedName("card")
    @Expose
    private AllCards card;
    @SerializedName("created")
    @Expose
    private Integer created;
    @SerializedName("customer")
    @Expose
    private String customer;
    @SerializedName("livemode")
    @Expose
    private Boolean livemode;
    @SerializedName("metadata")
    @Expose
    private AllCardsMetadata metadata;
    @SerializedName("type")
    @Expose
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public AllCardsBillingDetails getBillingDetails() {
        return billingDetails;
    }

    public void setBillingDetails(AllCardsBillingDetails billingDetails) {
        this.billingDetails = billingDetails;
    }

    public AllCards getCard() {
        return card;
    }

    public void setCard(AllCards card) {
        this.card = card;
    }

    public Integer getCreated() {
        return created;
    }

    public void setCreated(Integer created) {
        this.created = created;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public Boolean getLivemode() {
        return livemode;
    }

    public void setLivemode(Boolean livemode) {
        this.livemode = livemode;
    }

    public AllCardsMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(AllCardsMetadata metadata) {
        this.metadata = metadata;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
