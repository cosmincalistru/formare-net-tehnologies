package project.java.JAXB;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "proiecte")
public class ProiecteRoot {

    @XmlElement(name = "proiect")
    private List<ProiectElement> proiecte = null;

    public List<ProiectElement> getProiecte() {
        return proiecte;
    }

    public void setProiecte(List<ProiectElement> proiecte) {
        this.proiecte = proiecte;
    }
}
