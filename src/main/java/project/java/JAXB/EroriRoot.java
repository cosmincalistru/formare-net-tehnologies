package project.java.JAXB;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "erori")
@XmlAccessorType(XmlAccessType.FIELD)
public class EroriRoot {

    @XmlElement(name = "eroare")
    private List<EroareElement> erori = new ArrayList<>();

    public List<EroareElement> getErori() {
        return erori;
    }

    public void setErori(List<EroareElement> erori) {
        this.erori = erori;
    }
}
