package com.sage.cabapp.ui.setaddress.model;

/**
 * Created by Maroof Ahmed Siddique on 02-12-2019.
 * Lala
 * maroofahmedsiddique@gmail.com
 */
public class PlaceAutocomplete {

    public CharSequence placeId;
    public CharSequence address, area;

    public PlaceAutocomplete(CharSequence placeId, CharSequence area, CharSequence address) {
        this.placeId = placeId;
        this.area = area;
        this.address = address;
    }

    @Override
    public String toString() {
        return area.toString();
    }
}
