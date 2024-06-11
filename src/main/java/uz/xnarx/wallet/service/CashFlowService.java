package uz.xnarx.wallet.service;

import jakarta.transaction.Transactional;
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


    @Transactional
    public ApiResponse saveCashFlow(CashFlowDto cashFlowDto) {
        try {
            CashFlow cashFlow = new CashFlow();
            if (cashFlowDto.getId() != null) {
                cashFlow = cashFlowRepository.findById(cashFlowDto.getId())
                        .orElseThrow(() -> new IllegalStateException("Credit with this ID not fount"));
            }
            cashFlow.setName(cashFlowDto.getName());
            cashFlow.setAmount(cashFlowDto.getAmount());
            cashFlow.setCashFlowType(cashFlowDto.getCash_flow_type());
            cashFlow.setCurType(cashFlowDto.getCur_type());
            cashFlow.setDate(cashFlowDto.getDate());
            cashFlowRepository.save(cashFlow);
            return new ApiResponse(cashFlowDto.getId() != null ? "Edited" : "Saved", true);
        } catch (Exception e) {
            return new ApiResponse("Error", false);
        }
    }

    @Transactional
    public ApiResponse getAllCashFlow(Integer type, Integer month, Integer year, Integer page, Integer size) {
        Page<CashFlow> flowPage;
        try {
            if (type == 1) {
                if (month == 0) {
                    flowPage = cashFlowRepository.findAllByCashFlowTypeOrderByDateDesc
                            (true, CommonUtills.simplePageable(page, size));
                } else {
                    flowPage = cashFlowRepository.findAllByCashFlowTypeAndMonthAndYearOrderByDateDesc
                            (true, month, year, CommonUtills.simplePageable(page, size));
                }
            } else if (type == -1) {
                if (month == 0) {
                    flowPage = cashFlowRepository.findAllByCashFlowTypeOrderByDateDesc
                            (false, CommonUtills.simplePageable(page, size));
                } else {
                    flowPage = cashFlowRepository.findAllByCashFlowTypeAndMonthAndYearOrderByDateDesc
                            (false, month, year, CommonUtills.simplePageable(page, size));
                }
            } else {
                if (month == 0) {
                    flowPage = cashFlowRepository.findAllByOrderByDateDesc
                            (CommonUtills.simplePageable(page, size));
                } else {
                    flowPage = cashFlowRepository.findAllByMonthAndYearOrderByDateDesc
                            (month, year, CommonUtills.simplePageable(page, size));
                }
            }
            Double rate = Double.parseDouble(currency.getRate());
            Double incomeSumUSD = (cashFlowRepository.sumOfIncomeInUSD() + (cashFlowRepository.sumOfIncomeInUZS() / rate));
            Double incomeSumUZS = incomeSumUSD * rate;
            Double outcomeSumUSD = (cashFlowRepository.sumOfOutcomeInUSD() + (cashFlowRepository.sumOfOutcomeInUZS() / rate));
            Double outcomeSumUZS = outcomeSumUSD * rate;
            return new ApiResponse("Success",
                    true,
                    flowPage.getTotalElements(),
                    flowPage.getTotalPages(),
                    incomeSumUSD,
                    incomeSumUZS,
                    outcomeSumUSD,
                    outcomeSumUZS,
                    flowPage.getContent().stream().map(this::getCashFlowDtoFromCashFlow).collect(Collectors.toList()));
        } catch (Exception e) {
            return new ApiResponse(e.getMessage(), false);
        }
    }

    public CashFlowDto getCashFlowDtoFromCashFlow(CashFlow cashFlow) {
        CashFlowDto flowDto = new CashFlowDto();
        flowDto.setId(cashFlow.getId());
        flowDto.setName(cashFlow.getName());
        flowDto.setAmount(cashFlow.getAmount());
        flowDto.setDate(cashFlow.getDate());
        flowDto.setCash_flow_type(cashFlow.getCashFlowType());
        flowDto.setCur_type(cashFlow.getCurType());
        return flowDto;
    }

    @Transactional
    public ApiResponse removeCashById(String id) {
        try {
            cashFlowRepository.deleteById(id);
            return new ApiResponse("Deleted", true);
        } catch (Exception e) {
            return new ApiResponse("Error in deleting", false);
        }
    }

    @Transactional
    public ApiResponse getCashByName(String name, Integer month, Integer year, Integer page, Integer size) {
        Page<CashFlow> flowPage;
        try {
            if (month == 0) {
                flowPage = cashFlowRepository.findAllByNameContainingIgnoreCase
                        (name, CommonUtills.simplePageable(page, size));
            } else {
                flowPage = cashFlowRepository.findAllByNameContainingIgnoreCaseAndMonthAndYear
                        (name, month, year, CommonUtills.simplePageable(page, size));
            }
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
