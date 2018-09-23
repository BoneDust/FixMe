package broker.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Instrument
{
    private String name;
    private int quantity;
    private double price;
}
