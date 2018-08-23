package project.java.JAXB;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "eroare")
@XmlAccessorType(XmlAccessType.FIELD)
public class EroareElement {

    @XmlAttribute (name = "proiect")
    private String proiect;

    @XmlAttribute (name = "utilizator")
    private String utilizator;

    @XmlValue
    private String descriere;

    public String getProiect() {
        return proiect;
    }

    public void setProiect(String proiect) {
        this.proiect = proiect;
    }

    public String getUtilizator() {
        return utilizator;
    }

    public void setUtilizator(String utilizator) {
        this.utilizator = utilizator;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }
}
