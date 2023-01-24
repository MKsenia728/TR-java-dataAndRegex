package dataRegex;

import lombok.*;
@Getter
@Setter
@ToString

public class Address {
    public Type type;
    private String street;
    private String city;
    private String country;
}
