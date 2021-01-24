package org.oszimt.fa83.definition;

public enum District {

    NEUKOELLN("Neuköln", 1100000008L, "/neukoelln"),
    MITTE("Mitte", 1100000001L, "/mitte"),
    KREUZBERG("Kreuzberg", 1100000002L,"/friedrichshain-kreuzberg"),
    PRENZLAUERBERG( "Prenzlauer Berg", 110000000301L, "/pankow/prenzlauer-berg"),
    TEMPELHOF_SCHOENEBERG("Tempelhof Schöneberg", 1100000007L ,"/tempelhof-schoeneberg"),
    WEDDING("Wedding",110000000105L , "/mitte/wedding"),
    RUMMELSBUCHT("Rummelsbucht", 110000001112L, "/lichtenberg/rummelsburg");

    private String district;
    private long zipCode;
    private String locationURL;

    District(String district, long zipCode, String locationURL) {
        this.district = district;
        this.zipCode = zipCode;
        this.locationURL = locationURL;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public long getZipCode() {
        return zipCode;
    }

    public void setZipCode(Integer zipCode) {
        this.zipCode = zipCode;
    }

    public String getLocationURL() {
        return locationURL;
    }

    public void setLocationURL(String locationURL) {
        this.locationURL = locationURL;
    }

    public String getDistrictFromZipCode(Long zipCode){
        for (District district : values()){
            if (Long.valueOf(district.getZipCode()).equals(zipCode)){
                return district.getLocationURL();
            }
        }
        return null;
    }
}
