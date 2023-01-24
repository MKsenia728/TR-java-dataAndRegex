package dataRegex;

import lombok.*;

@Getter
@Setter
@ToString

public class BusCardContact {
    private String fulName;
    private Phone phone;
    private Address address;
    private Email email;
    private Web web;

    public BusCardContact(){
    }


}


