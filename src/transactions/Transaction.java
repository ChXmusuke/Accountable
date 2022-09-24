package transactions;

import java.util.ArrayList;
import java.util.List;

public class Transaction {

    final int id;
    static int currentId;
    String name;
    short date;
    byte from;
    byte to;
    float value;
    byte category;
    List<Byte> tags = new ArrayList<>();


    public Transaction(String _name, short _date, byte _from, byte _to, float _value, byte _category, List<Byte> _tags) {

        id = currentId++;  // assigns an Id to the Tx and increases the global Id counter

        name = _name;
        date = _date;
        from = _from;
        to = _to;
        value = _value;
        category = _category;
        tags.addAll(_tags);
    }
}
