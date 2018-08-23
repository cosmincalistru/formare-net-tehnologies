package project.java.JAXB;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement
public class ProiectElement {

    @XmlAttribute(name = "nume")
    private String nume;

    @XmlValue
    private String client;

    @XmlValue
    private String data_inceput;

    @XmlValue
    private String data_sfarsit;

    @XmlElement(name = "angajati")
    private AngajatiRoot angajatiRoot = null;

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getData_inceput() {
        return data_inceput;
    }

    public void setData_inceput(String data_inceput) {
        this.data_inceput = data_inceput;
    }

    public String getData_sfarsit() {
        return data_sfarsit;
    }

    public void setData_sfarsit(String data_sfarsit) {
        this.data_sfarsit = data_sfarsit;
    }

    public AngajatiRoot getAngajatiRoot() {
        return angajatiRoot;
    }

    public void setAngajatiRoot(AngajatiRoot angajatiRoot) {
        this.angajatiRoot = angajatiRoot;
    }
}
