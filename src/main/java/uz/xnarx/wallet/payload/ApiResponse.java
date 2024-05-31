package uz.xnarx.wallet.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse {

    private String message;
    private boolean success;
    private Long totalElements;
    private Integer totalPages;

    private Double incomeSumUSD;

    private Double incomeSumUZS;

    private Double outcomeSumUSD;

    private Double outcomeSumUZS;
    private Object object;



    public ApiResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public ApiResponse(String message, boolean success, Object object) {
        this.message = message;
        this.success = success;
        this.object = object;
    }

    public ApiResponse(String message, boolean success, Long totalElements, Integer totalPages, Object object) {
        this.message = message;
        this.success = success;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.object = object;
    }
}
