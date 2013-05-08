package at.ticketline.entity;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = -2142194421439108940L;
    
    @Version
    protected int version = 0;

    public int getVersion() {
        return this.version;
    }
}
