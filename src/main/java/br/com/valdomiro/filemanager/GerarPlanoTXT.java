package br.com.valdomiro.filemanager;

import br.com.valdomiro.filemanager.data.AirportInfo;
import br.com.valdomiro.filemanager.flightplan.OFP;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.StringWriter;
import java.util.Map;

import static org.apache.commons.io.FileUtils.writeStringToFile;
import static org.apache.commons.lang3.StringUtils.rightPad;

public class GerarPlanoTXT {

    private static Map<String, Map> mapAirpots;

    public GerarPlanoTXT(final File xmlFile) throws Exception {
        final JAXBContext jaxbContext = JAXBContext.newInstance(OFP.class);
        final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        final OFP ofp = (OFP) unmarshaller.unmarshal(xmlFile);

        final String header = rightPad("FLIGHT", 8) +
                rightPad("DEP", 5) +
                rightPad("ARR", 5) +
                rightPad("FL", 4) +
                rightPad("CI", 3) +
                rightPad("ZFW", 6) +
                rightPad("FUEL", 5) +
                rightPad("CARGO", 6) +
                rightPad("PAYLOAD", 8) +
                rightPad("TOW", 6) +
                rightPad("BURN", 6) +
                rightPad("| PAX", 6) +
                rightPad("TOTAL", 6) +
                rightPad("ALT  |", 7) +
                "ROUTE";

        final String icaoOrigin = ofp.origin.icao_code;
        final String icaoDestination = ofp.destination.icao_code;
        final String icaoAlternate = ofp.alternate.icao_code;

        final String data = rightPad(ofp.general.icao_airline + ofp.general.flight_number, 8) +
                rightPad(icaoOrigin, 5) +
                rightPad(icaoDestination, 5) +
                rightPad(ofp.general.initial_altitude.substring(0, ofp.general.initial_altitude.length() - 2), 4) +
                rightPad(ofp.general.costindex, 3) +
                rightPad(ofp.weights.est_zfw, 6) +
                rightPad(ofp.fuel.plan_ramp, 5) +
                rightPad(ofp.weights.cargo, 6) +
                rightPad(ofp.weights.payload, 8) +
                rightPad(ofp.weights.est_tow, 6) +
                toTime(Integer.valueOf(ofp.times.est_time_enroute)) + " " +
                rightPad("| " + ofp.general.passengers, 6) +
                toTime(Integer.valueOf(ofp.times.endurance) + Integer.valueOf(ofp.times.contfuel_time)) + " " +
                rightPad(icaoAlternate + " | ", 7) +
                ofp.origin.plan_rwy + " " +
                ofp.api_params.route + " " +
                ofp.destination.plan_rwy + " // " +
                icaoAlternate + " " + ofp.alternate.plan_rwy;

        String metar = "METAR\n\n";
        metar += airportInfo(icaoOrigin, AirportInfo.Type.CITY) + " / " + airportInfo(icaoOrigin, AirportInfo.Type.NAME) + "\n" + airportInfo(icaoOrigin, AirportInfo.Type.IATA) + "/" + ofp.weather.orig_metar + "\n\n";
        metar += airportInfo(icaoDestination, AirportInfo.Type.CITY) + " / " + airportInfo(icaoDestination, AirportInfo.Type.NAME) + "\n" + airportInfo(icaoDestination, AirportInfo.Type.IATA) + "/" + ofp.weather.dest_metar + "\n\n";
        metar += airportInfo(icaoAlternate, AirportInfo.Type.CITY) + " / " + airportInfo(icaoAlternate, AirportInfo.Type.NAME) + "\n" + airportInfo(icaoAlternate, AirportInfo.Type.IATA) + "/" + ofp.weather.altn_metar + "\n\n";

        final String flightplan = header + "\n" + data + "\n\n" + metar;

        writeStringToFile(new File("flightplan.dat"), flightplan);

        xmlFile.delete();
    }

    private void initializeAirports() throws Exception {
        if (mapAirpots == null) {
            final StringWriter writer = new StringWriter();
            final String airportsJson = IOUtils.toString(getClass().getResourceAsStream("/airports.json"));
            mapAirpots = new Gson().fromJson(airportsJson, Map.class);
        }
    }

    private String airportInfo(final String icao, final AirportInfo.Type info) throws Exception {
        initializeAirports();
        return (String) mapAirpots.get(icao).get(info.toString().toLowerCase());
    }

    private String toTime(final int seconds) {
        int minutes = seconds / 60;

        if (minutes >= 60) {
            final int hours = minutes / 60;
            minutes %= 60;

            if (hours >= 24) {
                final int days = hours / 24;
                return String.format("%d days %02dh%02d", days, hours % 24, minutes);
            }

            return String.format("%02dh%02d", hours, minutes);
        }

        return String.format("00h%02d", minutes);
    }

}
