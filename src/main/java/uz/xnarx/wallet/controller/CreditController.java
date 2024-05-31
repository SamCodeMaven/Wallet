package uz.xnarx.wallet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.xnarx.wallet.payload.ApiResponse;
import uz.xnarx.wallet.payload.CreditDto;
import uz.xnarx.wallet.service.CreditService;
import uz.xnarx.wallet.utils.ApplicationConstants;

@RestController
@RequestMapping("/api/credit")
public class CreditController {

    @Autowired
    CreditService creditService;

    @PostMapping("/saveOrEdit")
    public HttpEntity<?> saveOrEditCredit(@RequestBody CreditDto dto){
        ApiResponse apiResponse = creditService.saveOrEditCredit(dto);
        return ResponseEntity.status(apiResponse.isSuccess()?
                        apiResponse.getMessage().equals("Saved")?201:202: 409)
                .body(apiResponse);
    }

    @GetMapping("/getAllCredit")//todo to'langan va to'lanmagan table
    public HttpEntity<?> getAllCredit(@RequestParam(value = "page",
            defaultValue = ApplicationConstants.DEFAULT_PAGE_NUMBER)Integer page,
                                      @RequestParam(value = "size",
                                              defaultValue = ApplicationConstants.DEFAULT_PAGE_SIZE)Integer size
    ) {
        return ResponseEntity.ok(creditService.getAllCredit(page, size));
    }

    @GetMapping("/byName/{name}")
    public HttpEntity<?>getCreditById(@PathVariable(value = "name")
                                    String name,
                                    @RequestParam(value = "page",
                                            defaultValue = ApplicationConstants.DEFAULT_PAGE_NUMBER)Integer page,
                                    @RequestParam(value = "size",
                                            defaultValue = ApplicationConstants.DEFAULT_PAGE_SIZE)Integer size){
        ApiResponse response=creditService.getCreditByName(name,page, size);
        return ResponseEntity.status(response.isSuccess()?200:409).body(response);
    }
    @DeleteMapping("/delete/{id}")
    public HttpEntity<?> deleteCreditById(@PathVariable(value = "id")String id){
        ApiResponse response=creditService.removeCreditById(id);
        return ResponseEntity.status(response.isSuccess()?200:409).body(response);
    }
}
