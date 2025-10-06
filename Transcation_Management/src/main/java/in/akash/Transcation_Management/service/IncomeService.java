package in.akash.Transcation_Management.service;

import in.akash.Transcation_Management.entity.CategoryEntity;

import in.akash.Transcation_Management.entity.ExpenseEntity;
import in.akash.Transcation_Management.entity.IncomeEntity;
import in.akash.Transcation_Management.entity.ProfileEntity;

import in.akash.Transcation_Management.io.ExpenseDTO;
import in.akash.Transcation_Management.io.IncomeDTO;
import in.akash.Transcation_Management.repository.CategoryRepository;
import in.akash.Transcation_Management.repository.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IncomeService {

    private final CategoryService categoryService;

    private final IncomeRepository incomeRepository;

    private final ProfileService profileService;

    private final CategoryRepository categoryRepository;

    public IncomeDTO addIncome(IncomeDTO dto) {
        ProfileEntity profile= profileService.getCurrentProfile();
        CategoryEntity category= categoryRepository.findById(dto.getCategoryId()).
                orElseThrow(()->new RuntimeException("Category Not Found"));
        IncomeEntity newIncome=toEntity(dto,profile,category);
        newIncome=incomeRepository.save(newIncome);
        return toDTO(newIncome);
    }

    private IncomeEntity toEntity(IncomeDTO dto, ProfileEntity profileEntity, CategoryEntity categoryEntity){
        return IncomeEntity.builder()
                .name(dto.getName())
                .icon(dto.getIcon())
                .amount(dto.getAmount())
                .date(dto.getDate())
                .profile(profileEntity)
                .category(categoryEntity)
                .build();

    }

    private IncomeDTO toDTO(IncomeEntity entity){
      return  IncomeDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .icon(entity.getIcon())
                .categoryId(entity.getCategory()!=null?entity.getCategory().getId():null)
                .categoryName(entity.getCategory()!=null?entity.getCategory().getName():"N/A")
                .amount(entity.getAmount())
                .date(entity.getDate())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }


    public List<IncomeDTO> getCurrentMonthIncomeForCurrentUser(){
        ProfileEntity profile=profileService.getCurrentProfile();
        LocalDate now=LocalDate.now();
        LocalDate startDate=now.withDayOfMonth(1);
        LocalDate endDate=now.withDayOfMonth(now.lengthOfMonth());
        List<IncomeEntity> list=  incomeRepository.findByProfileIdAndDateBetween(profile.getId(),startDate,endDate);
        return list.stream().map(this::toDTO).toList();
    }

    public void deleteIncome(Long incomeId){
        ProfileEntity profile=  profileService.getCurrentProfile();
        IncomeEntity entity= incomeRepository.findById(incomeId)
                .orElseThrow(()-> new RuntimeException("Income Not Found"));
        if(!entity.getProfile().getId().equals(profile.getId())){
            throw new RuntimeException("Unauthorized to delete this message");
        }
        incomeRepository.delete(entity);
    }
}
