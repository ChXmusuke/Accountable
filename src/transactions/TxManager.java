package transactions;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import util.CheckConditions;
import util.Date;

public class TxManager {

    List<List<Transaction>> transactions = new ArrayList<>();
    Map<Byte, Float> balances = new HashMap<>();
    int startYear;

    public TxManager() {
        transactions.add(new ArrayList<>());
        startYear = 2022;  // TODO: set to current time
    }

    public void createTransaction(
            String _name,
            short _date,
            byte _from,
            byte _to,
            float _value,
            byte _category,
            List<Byte> _tags) {
        CheckConditions.checkArgument(balances.containsKey(_from) && balances.containsKey(_to));

        transactions.get(Date.extractYear(_date)-startYear).add(new Transaction(_name, _date, _from, _to, _value, _category, _tags));
        balances.put(_from, balances.get(_from) - _value);
        balances.put(_to, balances.get(_to) + _value);

        // TODO
    }
}
