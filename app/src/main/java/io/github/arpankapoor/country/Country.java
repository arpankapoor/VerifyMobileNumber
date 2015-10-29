package io.github.arpankapoor.country;

import com.google.i18n.phonenumbers.PhoneNumberUtil;

import java.util.Locale;

public class Country {
    private String name;
    private String isoCode;
    private int callingCode;

    public Country(String isoCode) {
        this.isoCode = isoCode.toUpperCase();
        this.name = new Locale("", this.isoCode).getDisplayCountry();
        this.callingCode = PhoneNumberUtil.getInstance().getCountryCodeForRegion(this.isoCode);
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
}
