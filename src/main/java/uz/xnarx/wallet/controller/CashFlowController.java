package uz.xnarx.wallet.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.xnarx.wallet.payload.ApiResponse;
import uz.xnarx.wallet.payload.CashFlowDto;
import uz.xnarx.wallet.service.CashFlowService;
import uz.xnarx.wallet.utils.ApplicationConstants;

@RestController
@RequestMapping("/api/cash")
public class CashFlowController {

    @Autowired
    CashFlowService cashFlowService;

    @PostMapping("/save")
    public HttpEntity<?> saveCashFlow(@RequestBody CashFlowDto cashFlowDto) {
        ApiResponse apiResponse = cashFlowService.saveCashFlow(cashFlowDto);
        return ResponseEntity
                .status(apiResponse.isSuccess() ? apiResponse.getMessage().equals("Saved") ? 201 : 202 : 409)
                .body(apiResponse);
    }

    @CrossOrigin
    @GetMapping("/getAllCashFlow")//todo income in USD,UZS outcome also same
    public HttpEntity<?> getAllCashFlow(@RequestParam(value = "type",
            defaultValue = ApplicationConstants.DEFAULT_CASH_FLOW_TYPE) Integer type,
                                        @RequestParam(value = "page",
                                                defaultValue = ApplicationConstants.DEFAULT_PAGE_NUMBER) Integer page,
                                        @RequestParam(value = "size",
                                                defaultValue = ApplicationConstants.DEFAULT_PAGE_SIZE) Integer size
    ) {
        return ResponseEntity.ok(cashFlowService.getAllCashFlow(type,page, size));
    }

    @GetMapping("/byName/{name}")
    public HttpEntity<?> getCashById(@PathVariable(value = "name")
                                     String name,
                                     @RequestParam(value = "page",
                                             defaultValue = ApplicationConstants.DEFAULT_PAGE_NUMBER) Integer page,
                                     @RequestParam(value = "size",
                                             defaultValue = ApplicationConstants.DEFAULT_PAGE_SIZE) Integer size) {
        ApiResponse response = cashFlowService.getCashByName(name, page, size);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    @DeleteMapping("/delete/{id}")
    public HttpEntity<?> removeCashFlowById(@PathVariable(value = "id") String id) {
        ApiResponse apiResponse = cashFlowService.removeCashById(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
