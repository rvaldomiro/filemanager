package br.com.valdomiro.filemanager.data;

import java.io.Serializable;

public class AirportInfo implements Serializable, Comparable<AirportInfo> {

    private String iata;

    public String getIata() {
        return iata;
    }

    public void setIata(final String iata) {
        this.iata = iata;
    }

    @Override
    public int compareTo(final AirportInfo o) {
        return iata.compareTo(o.iata);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final AirportInfo that = (AirportInfo) o;

        return iata.equals(that.iata);
    }

    @Override
    public int hashCode() {
        return iata.hashCode();
    }

    public enum Type {
        IATA, NAME, CITY
    }

}
