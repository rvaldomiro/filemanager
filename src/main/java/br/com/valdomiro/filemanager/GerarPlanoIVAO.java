package br.com.valdomiro.filemanager;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GerarPlanoIVAO {

    public static final String CONTAINS_NUMBER = ".*\\d+.*";

    private final String ROUTE = "ROUTE=";

    private final String OTHER = "OTHER=";

    public GerarPlanoIVAO(final File srcFile) throws Exception {
        final List<String> lines = FileUtils.readLines(srcFile);
        final List<String> newLines = new ArrayList<String>();

        for (final String line : lines) {
            if (line.startsWith(ROUTE)) {
                newLines.add(tratarCampoRoute(line));
            } else if (line.startsWith(OTHER)) {
                newLines.add(tratarCampoOther(line));
            } else {
                newLines.add(line);
            }
        }

        FileUtils.writeLines(srcFile, newLines);
    }

    private String tratarCampoRoute(final String line) {
        String[] route = line.substring(line.indexOf("=") + 1).split(" ");

        final int firstStep = 0;
        final int lastStep = route.length - 1;

        if (route[lastStep].matches(CONTAINS_NUMBER)) {
            route = ArrayUtils.remove(route, lastStep);
        }

        if (route[firstStep].matches(CONTAINS_NUMBER) || route[firstStep].equals(route[1])) {
            route = ArrayUtils.remove(route, firstStep);
        }

        return ROUTE + StringUtils.join(route, " ");
    }

    private String tratarCampoOther(final String line) {
        return OTHER + line.substring(line.indexOf("=") + 1) + " RMK/LIVESTREAM";
    }

}
