package project.java.JAXB;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "anagajati")
public class AngajatiRoot {

    @XmlElement(name = "angajat")
    private List<AngajatElement> angajati = null;

    public List<AngajatElement> getAngajati() {
        return angajati;
    }

    public void setAngajati(List<AngajatElement> angajati) {
        this.angajati = angajati;
    }
}
