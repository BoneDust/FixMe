package market.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Transaction
{
    int brokerId;
    int marketId;
    String type;
    String status;
    String instrument;
    int quantity;
    double price;

    public Transaction(int broker, int market, String type, String status, String instrument, int quantity, double cost)
    {
        brokerId = broker;
        marketId = market;
        this.type = type;
        this.status = status;
        this.instrument = instrument;
        this.quantity = quantity;
        price = cost;
    }
}
