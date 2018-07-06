package br.com.valdomiro.filemanager;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import static org.apache.commons.io.FileUtils.*;

public class MoverArquivosJob implements Job {

    private final SimpleDateFormat SDF = new SimpleDateFormat("ddMMyy HHmmss");

    @Override
    public void execute(final JobExecutionContext arg0) throws JobExecutionException {
        try {
            final File pFile = new File("filemanager.properties");

            if (!pFile.exists()) {
                new File("teste.flp").createNewFile();

                pFile.createNewFile();
                writeStringToFile(pFile, "flp=/tmp;rnteste.txt;cp");
            }

            final PropertiesConfiguration p = new PropertiesConfiguration();
            p.read(new FileReader(pFile));

            final File[] files = new File(".").listFiles();

            if (files != null) {
                for (final File srcFile : files) {
                    if (srcFile.isDirectory()) {
                        continue;
                    }

                    String targetDir = null;
                    String fName = srcFile.getName();
                    final String fExtension = fName.substring(fName.lastIndexOf(".") + 1).toLowerCase();

                    if (fName.equalsIgnoreCase("scenery.cfg")) {
                        new OrdenarCenarios(srcFile);
                        continue;
                    }

                    if (fExtension.equals("xml") && fName.toLowerCase().contains("_xml_")) {
                        new GerarPlanoTXT(srcFile);
                        System.out.println(SDF.format(new Date()) + " | Arquivo flightplan.dat gerado com sucesso!");
                        continue;
                    }

                    if (fExtension.equals("fpl")) {
                        new GerarPlanoIVAO(srcFile);
                    }

                    for (final String hash : p.getStringArray(fExtension)) {
                        if (hash == null) {
                            continue;
                        }

                        final StringTokenizer st = new StringTokenizer(hash, ";");

                        boolean copyFile = false;

                        while (st.hasMoreTokens()) {
                            final String t = st.nextToken();

                            if (t.startsWith("cp")) {
                                copyFile = true;
                            } else if (t.startsWith("rn")) {
                                fName = t.substring(2);
                            } else {
                                targetDir = t;
                            }
                        }

                        if (fExtension.equals("pdf") && fName.toLowerCase().contains("_pdf_")) {
                            fName = fName.substring(0, fName.toLowerCase().indexOf("_pdf_")) + ".pdf";
                        }

                        final File destFile = new File(targetDir + "/" + fName);

                        if (destFile.exists()) {
                            destFile.delete();
                        }

                        if (!copyFile) {
                            moveFile(srcFile, destFile);
                        } else {
                            copyFile(srcFile, destFile);
                        }

                        System.out.println(SDF.format(new Date()) + " | " + String.format("Arquivo %s %s para -> %s",
                                fName, copyFile ? "copiado" : "movido", targetDir));
                    }
                }
            }
        } catch (final Exception e) {
            throw new JobExecutionException(e);
        }
    }

}
