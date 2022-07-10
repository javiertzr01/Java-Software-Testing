import java.util.ArrayList;
import java.util.List;

public class bankAcc {
    private String customerID;
    private String accountNum;
    private String currency;
    private String type;
    private int balance;

    public bankAcc(String customerID, String accountNum, String currency, String type, int balance){
        this.customerID = customerID;
        this.accountNum = accountNum;
        this.currency = currency;
        this.type = type;
        this.balance = balance;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj){
            return true;
        }
        bankAcc o = (bankAcc) obj;
        return accountNum.equals(o.accountNum) && currency.equals(o.currency) && type.equals(o.type) && balance == o.balance;
    }

    public List<String> asList(){
        ArrayList<String> ret = new ArrayList<>();
        ret.add(customerID);
        ret.add(accountNum);
        ret.add(currency);
        ret.add(type);
        ret.add(Integer.toString(balance));

        return ret;
    }
}
