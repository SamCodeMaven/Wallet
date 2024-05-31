package uz.xnarx.wallet.payload;

import lombok.Data;

import java.util.Date;

@Data
public class CreditDto {
    private String id;
    private String name; //from whom or where
    private Double amount;
    private Date start_date;
    private Date end_date;
    private Boolean cur_type;
    private Boolean status;

    public CreditDto() {
    }

    public CreditDto(String id, String name, Double amount, Date start_date, Date end_date, Boolean cur_type, Boolean status) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.start_date = start_date;
        this.end_date = end_date;
        this.cur_type = cur_type;
        this.status = status;
    }

}
