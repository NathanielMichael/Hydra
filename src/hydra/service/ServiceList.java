package hydra.service;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum ServiceList
{
    CONNECTION(0xB732DB32);
    
    private int hash;
    
    private static final Map<Integer, ServiceList> lookup = new HashMap<Integer, ServiceList>();
    
    static {
        for (ServiceList s : EnumSet.allOf(ServiceList.class)) {
            lookup.put(s.getHash(), s);
        }
    }
    
    private ServiceList(int hash)
    {
        this.hash = hash;
    }
    
    public int getHash() { return hash; }
    
    public static ServiceList get(int hash)
    {
        return lookup.get(hash);
    }
}
