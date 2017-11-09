package br.com.valdomiro.filemanager.flightplan;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "OFP")
public class OFP {

    public General general;
    public Origin origin;
    public Destination destination;
    public Alternate alternate;
    public Fuel fuel;
    public Times times;
    public Weights weights;
    public ApiParams api_params;
    public Weather weather;

}
