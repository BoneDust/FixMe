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

    public Instrument(String name, int quantity, double price)
    {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }
}
