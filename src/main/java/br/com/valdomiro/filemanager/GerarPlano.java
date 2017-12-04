package br.com.valdomiro.filemanager;

import br.com.valdomiro.filemanager.flightplan.OFP;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;

import static org.apache.commons.io.FileUtils.writeStringToFile;
import static org.apache.commons.lang3.StringUtils.rightPad;

public class GerarPlano {

    public GerarPlano(final File xmlFile) throws Exception {
        final JAXBContext jaxbContext = JAXBContext.newInstance(OFP.class);
        final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        final OFP ofp = (OFP) unmarshaller.unmarshal(xmlFile);

        final String header = rightPad("FLIGHT", 8) +
                rightPad("DEP", 5) +
                rightPad("ARR", 5) +
                rightPad("FL", 4) +
                rightPad("CI", 3) +
                rightPad("ZFW", 6) +
                rightPad("FUEL", 6) +
                rightPad("CARGO",6) +
                rightPad("TOW", 6) +
                rightPad("BURN", 6) +
                rightPad("| PAX", 6) +
                rightPad("TOTAL", 6) +
                rightPad("ALT  |", 7) +
                "ROUTE";

        final String data = rightPad(ofp.general.icao_airline + ofp.general.flight_number, 8) +
                rightPad(ofp.origin.icao_code, 5) +
                rightPad(ofp.destination.icao_code, 5) +
                rightPad(ofp.general.initial_altitude.substring(0, ofp.general.initial_altitude.length() - 2), 4) +
                rightPad(ofp.general.costindex, 3) +
                rightPad(ofp.weights.est_zfw, 6) +
                rightPad(ofp.fuel.plan_ramp, 6) +
                rightPad(ofp.api_params.cargo,6) +
                rightPad(ofp.weights.est_tow, 6) +
                toTime(Integer.valueOf(ofp.times.est_time_enroute)) + " " +
                rightPad("| " + ofp.general.passengers, 6) +
                toTime(Integer.valueOf(ofp.times.endurance) + Integer.valueOf(ofp.times.contfuel_time)) + " " +
                rightPad(ofp.alternate.icao_code + " | ", 7) +
                ofp.origin.plan_rwy + " " +
                ofp.api_params.route + " " +
                ofp.destination.plan_rwy + " // " +
                ofp.alternate.icao_code + " " + ofp.alternate.plan_rwy;

        final String weather = ofp.weather.orig_metar + "\n" + ofp.weather.dest_metar + "\n" + ofp.weather.altn_metar;

        final String flightplan = header + "\n" + data + "\n\n" + weather;

        writeStringToFile(new File("flightplan.dat"), flightplan);

        xmlFile.delete();
    }

    private String toTime(final int seconds) {
        int minutes = seconds / 60;

        if (minutes >= 60) {
            int hours = minutes / 60;
            minutes %= 60;

            if (hours >= 24) {
                int days = hours / 24;
                return String.format("%d days %02dh%02d", days, hours % 24, minutes);
            }

            return String.format("%02dh%02d", hours, minutes);
        }

        return String.format("00h%02d", minutes);
    }

}
