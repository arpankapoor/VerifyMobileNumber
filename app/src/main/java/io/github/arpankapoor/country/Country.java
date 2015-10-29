package io.github.arpankapoor.country;

import android.support.annotation.NonNull;

import com.google.i18n.phonenumbers.PhoneNumberUtil;

import java.io.Serializable;
import java.util.Locale;

public class Country implements Comparable<Country>, Serializable {
    private static final long serialVersionUID = 8116297477768564397L;
    private String name;
    private String isoCode;
    private int callingCode;

    public Country(String isoCode) {
        setIsoCode(isoCode.toUpperCase());
        setName(new Locale("", getIsoCode()).getDisplayCountry());
        setCallingCode(PhoneNumberUtil.getInstance().getCountryCodeForRegion(getIsoCode()));
    }

    @Override
    public String toString() {
        return name + " (+" + callingCode + ")";
    }

    public int getCallingCode() {
        return callingCode;
    }

    public void setCallingCode(int callingCode) {
        this.callingCode = callingCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsoCode() {
        return isoCode;
    }

    public void setIsoCode(String isoCode) {
        this.isoCode = isoCode;
    }

    @Override
    public int compareTo(@NonNull Country another) {
        return this.name.compareTo(another.name);
    }
}
