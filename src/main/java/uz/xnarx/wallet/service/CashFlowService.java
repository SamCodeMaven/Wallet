package uz.xnarx.wallet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import uz.xnarx.wallet.entity.CashFlow;
import uz.xnarx.wallet.payload.ApiResponse;
import uz.xnarx.wallet.payload.CashFlowDto;
import uz.xnarx.wallet.payload.Currency;
import uz.xnarx.wallet.repository.CashFlowRepository;
import uz.xnarx.wallet.utils.CommonUtills;

import java.util.stream.Collectors;

@Service
public class CashFlowService {

    @Autowired
    CashFlowRepository cashFlowRepository;

    @Autowired
    Currency currency;


    public ApiResponse saveCashFlow(CashFlowDto cashFlowDto) {
        try {
            CashFlow cashFlow=new CashFlow();
            if (cashFlowDto.getId()!=null){
                cashFlow=cashFlowRepository.findById(cashFlowDto.getId())
                        .orElseThrow(() ->  new IllegalStateException("Credit with this ID not fount"));
            }
            cashFlow.setName(cashFlowDto.getName());
            cashFlow.setAmount(cashFlowDto.getAmount());
            cashFlow.setCashFlowType(cashFlowDto.getCash_flow_type());
            cashFlow.setCurType(cashFlowDto.getCur_type());
            cashFlow.setDate(cashFlowDto.getDate());
            cashFlowRepository.save(cashFlow);
            return new ApiResponse(cashFlowDto.getId()!=null?"Edited":"Saved",true);
        }catch (Exception e){
            return new ApiResponse("Error",false);
        }
    }

    public ApiResponse getAllCashFlow(Integer type, Integer page, Integer size) {
        Page<CashFlow> flowPage;
        if (type==1) {
            flowPage = cashFlowRepository.findAllByCashFlowTypeAndOrderByDateDesc(true,CommonUtills.simplePageable(page, size));
        }else if (type==-1){
            flowPage = cashFlowRepository.findAllByCashFlowTypeAndOrderByDateDesc(false,CommonUtills.simplePageable(page, size));
        }else {
            flowPage = cashFlowRepository.findAllByOrderByDateDesc(CommonUtills.simplePageable(page, size));
        }
        Double rate=Double.parseDouble(currency.getRate());
        Double incomeSumUSD= (cashFlowRepository.sumOfIncomeInUSD()+ (cashFlowRepository.sumOfIncomeInUZS()/rate));
        Double incomeSumUZS= incomeSumUSD*rate;
        Double outcomeSumUSD= (cashFlowRepository.sumOfOutcomeInUSD()+ (cashFlowRepository.sumOfOutcomeInUZS()/rate));
        Double outcomeSumUZS= outcomeSumUSD*rate;
        return new ApiResponse("Success",
                true,
                flowPage.getTotalElements(),
                flowPage.getTotalPages(),
                incomeSumUSD,
                incomeSumUZS,
                outcomeSumUSD,
                outcomeSumUZS,
                flowPage.getContent().stream().map(this::getCashFlowDtoFromCashFlow).collect(Collectors.toList()));
    }
    public CashFlowDto getCashFlowDtoFromCashFlow(CashFlow cashFlow){
        CashFlowDto flowDto=new CashFlowDto();
        flowDto.setId(cashFlow.getId());
        flowDto.setName(cashFlow.getName());
        flowDto.setAmount(cashFlow.getAmount());
        flowDto.setDate(cashFlow.getDate());
        flowDto.setCash_flow_type(cashFlow.getCashFlowType());
        flowDto.setCur_type(cashFlow.getCurType());
        return flowDto;
    }

    public ApiResponse removeCashById(String id) {
            try {
                cashFlowRepository.deleteById(id);
                return new ApiResponse("Deleted",true);
            }catch (Exception e){
                return new ApiResponse("Error in deleting", false);
            }
    }

    public ApiResponse getCashByName(String name,Integer page, Integer size) {

        try {
            Page<CashFlow> flowPage = cashFlowRepository.findAllByNameContainingIgnoreCase(name, CommonUtills.simplePageable(page, size));

            return new ApiResponse("CashFlow fount",
                    true,
                    flowPage.getTotalElements(),
                    flowPage.getTotalPages(),
                    flowPage.getContent().stream().map(this::getCashFlowDtoFromCashFlow).collect(Collectors.toList()));
        } catch (Exception e) {
            return new ApiResponse("CashFlow not fount", false);
        }
    }
}
