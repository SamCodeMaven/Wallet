package uz.xnarx.wallet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import uz.xnarx.wallet.entity.Credit;
import uz.xnarx.wallet.payload.ApiResponse;
import uz.xnarx.wallet.payload.CreditDto;
import uz.xnarx.wallet.repository.CreditRepository;
import uz.xnarx.wallet.utils.CommonUtills;

import java.util.stream.Collectors;

@Service
public class CreditService {

    @Autowired
    CreditRepository creditRepository;

    public CreditDto getCreditDtoFromCredit(Credit credit){
        CreditDto dto=new CreditDto();
        dto.setId(credit.getId());
        dto.setName(credit.getName());
        dto.setAmount(credit.getAmount());
        dto.setStart_date(credit.getStartDate());
        dto.setEnd_date(credit.getEndDate());
        dto.setCur_type(credit.getCurType());
        dto.setStatus(credit.getStatus());
        return dto;
    }

    public ApiResponse saveOrEditCredit(CreditDto dto) {
        try {
            Credit credit=new Credit();
            if (dto.getId()!=null){
                credit=creditRepository.findById(dto.getId())
                        .orElseThrow(() -> new IllegalStateException("Credit with this ID not fount"));
            }
            credit.setName(dto.getName());
            credit.setAmount(dto.getAmount());
            credit.setStartDate(dto.getStart_date());
            credit.setEndDate(dto.getEnd_date());
            credit.setCurType(dto.getCur_type());
            credit.setStatus(dto.getStatus());
            creditRepository.save(credit);
            return new ApiResponse(dto.getId()!=null?"Edited":"Saved",true);
        }catch (Exception e){
            return new ApiResponse("Error",false);
        }
    }

    public ApiResponse getAllCredit(Integer page, Integer size) {
        Page<Credit> creditPage=creditRepository.findAllByOrderByEndDateDesc(CommonUtills.simplePageable(page,size));

        return new ApiResponse("Success",
                true,
                creditPage.getTotalElements(),
                creditPage.getTotalPages(),
                creditPage.getContent().stream().map(this::getCreditDtoFromCredit).collect(Collectors.toList()));
    }
    public ApiResponse removeCreditById(String id){
        try {
            System.out.println(creditRepository.findById(id));
            System.out.println(id);
            creditRepository.deleteById(id);
            return new ApiResponse("Deleted",true);
        }catch (Exception e){
            return new ApiResponse("Error in deleting",false);
        }
    }

    public ApiResponse getCreditByName(String name, Integer page, Integer size) {
        try {
            Page<Credit> flowPage = creditRepository.findAllByNameContainingIgnoreCase(name, CommonUtills.simplePageable(page, size));

            return new ApiResponse("Credit fount",
                    true,
                    flowPage.getTotalElements(),
                    flowPage.getTotalPages(),
                    flowPage.getContent().stream().map(this::getCreditDtoFromCredit).collect(Collectors.toList()));
        } catch (Exception e) {
            return new ApiResponse("Credit not fount", false);
        }
    }
}
