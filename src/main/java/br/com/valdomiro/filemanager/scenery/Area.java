package br.com.valdomiro.filemanager.scenery;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

public class Area implements Comparable<Area> {

    private Integer id;

    private String title;

    private String local;

    private Boolean active;

    private Boolean required;

    private Integer layer;

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(final String local) {
        this.local = local;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(final Boolean active) {
        this.active = active;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(final Boolean required) {
        this.required = required;
    }

    public Integer getLayer() {
        return layer;
    }

    public void setLayer(final Integer layer) {
        this.layer = layer;
    }

    @Override
    public int compareTo(final Area o) {
        return title.compareToIgnoreCase(o.title);
    }

    @Override
    public String toString() {
        return "[Area." + StringUtils.leftPad(id.toString(), 3, "0") + "]\n" +
                "Title=" + title + "\n" +
                "Local=" + local + "\n" +
                "Active=" + WordUtils.capitalize(active.toString()) + "\n" +
                "Required=" + WordUtils.capitalize(required.toString()) + "\n" +
                "Layer=" + layer + "\n";
    }

}
