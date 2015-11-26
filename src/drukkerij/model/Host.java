package drukkerij.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Pol on 14/11/2015.
 */

@XmlRootElement(name = "Host")
public class Host {

    private String hostname;

    @XmlElement(name = "Hostname")
    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
}
