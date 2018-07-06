package br.com.valdomiro.filemanager;

import br.com.valdomiro.filemanager.scenery.Area;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.util.Collections.reverse;
import static java.util.Collections.sort;
import static org.apache.commons.io.FileUtils.writeLines;

public class OrdenarCenarios {

    private final SimpleDateFormat SDF = new SimpleDateFormat("ddMMyy HHmmss");

    public OrdenarCenarios(final File srcFile) throws Exception {
        final List<String> lines = FileUtils.readLines(srcFile, "UTF-16LE");
        final ArrayList<Area> areas = new ArrayList<Area>();
        final ArrayList<Area> areasHeader = new ArrayList<Area>();
        final ArrayList<Area> areasOrdered = new ArrayList<Area>();
        final ArrayList<Area> areasFooter = new ArrayList<Area>();
        boolean process = false;
        Area area = null;

        for (final String line : lines) {
            if (StringUtils.isEmpty(line)) {
                process = false;
                continue;
            }

            final int pos = line.indexOf("[Area.");

            if (pos >= 0) {
                process = false;
            }

            if (process && area != null) {
                final int posEquals = line.indexOf("=");
                final String key = line.substring(0, posEquals);
                final String value = line.substring(posEquals + 1);

                if (key.equalsIgnoreCase("local")) {
                    area.setLocal(value);
                } else if (key.equalsIgnoreCase("title")) {
                    area.setTitle(value);
                } else if (key.equalsIgnoreCase("required")) {
                    area.setRequired(Boolean.valueOf(value));
                } else if (key.equalsIgnoreCase("active")) {
                    area.setActive(Boolean.valueOf(value));
                } else if (key.equalsIgnoreCase("layer")) {
                    area.setLayer(Integer.valueOf(value));
                }
            }

            if (pos >= 0 && !process) {
                area = new Area();
                area.setId(Integer.valueOf(line.substring(pos + 6, pos + 9)));
                areas.add(area);
                process = true;
            }
        }

        for (final Area area1 : areas) {
            final String local = area1.getLocal().toLowerCase();

            if (local.startsWith("scenery") || local.startsWith("orbx\\ftx_vector")) {
                areasHeader.add(area1);
            } else if (local.startsWith("addon scenery") || local.startsWith("tropicalsim") || local.startsWith("latinvfr") || local.startsWith("ecosystem") || local.startsWith("flytampa") || local.startsWith("simmarket")) {
                areasOrdered.add(area1);
            } else if (local.startsWith("orbx")) {
                areasFooter.add(area1);
            }
        }

        Integer id = areasHeader.size() + 1;
        sort(areasOrdered);
        reverse(areasOrdered);

        for (final Area area1 : areasOrdered) {
            area1.setId(id);
            area1.setLayer(id);
            id++;
        }

        for (final Area area1 : areasFooter) {
            area1.setId(id);
            area1.setLayer(id);
            id++;
        }

        areas.clear();
        areas.addAll(areasHeader);
        areas.addAll(areasOrdered);
        areas.addAll(areasFooter);

        final List<String> writer = new ArrayList<String>();

        final File file = new File("scenery.cfg.new");
        writer.add("[General]");
        writer.add("Title=Prepar3D Scenery");
        writer.add("Description=Prepar3D Scenery Areas Data");
        writer.add("Clean_on_Exit=TRUE");
        writer.add("");

        for (final Area area1 : areasHeader) {
            writer.add(area1.toString());
        }

        for (final Area area1 : areasOrdered) {
            writer.add(area1.toString());
        }

        for (final Area area1 : areasFooter) {
            writer.add(area1.toString());
        }

        writeLines(file, writer);
        srcFile.delete();
        System.out.println(SDF.format(new Date()) + " | Arquivo scenery.cfg.new gerado com sucesso!");
    }

}