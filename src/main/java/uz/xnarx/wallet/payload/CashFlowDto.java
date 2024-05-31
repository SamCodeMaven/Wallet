package uz.xnarx.wallet.payload;

import lombok.Data;

import java.util.Date;

@Data
public class CashFlowDto {

    private String id;

    private String name; //from whom or where

    private Double amount;

    private Date date;

    private Boolean cash_flow_type;

    private Boolean cur_type;

    public CashFlowDto() {
    }

    public CashFlowDto(String id, String name, Double amount, Date date, Boolean cash_flow_type, Boolean cur_type) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.cash_flow_type = cash_flow_type;
        this.cur_type = cur_type;
    }
}

