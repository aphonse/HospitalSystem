package Controllers;

import java.util.IdentityHashMap;

public interface settings {
    public String siteHelp = "http://nanotechsoftwares.co.ke";
    IdentityHashMap<String, Boolean> login = new IdentityHashMap<>();
    IdentityHashMap<String, String> user = new IdentityHashMap<>();
    IdentityHashMap<String, String> hospital = new IdentityHashMap<>();
    IdentityHashMap<String, Boolean> changepassword = new IdentityHashMap<>();

    String[] des = {"jdbc:mysql://127.0.0.1/edoc_hospitals", "root", ""};


}
