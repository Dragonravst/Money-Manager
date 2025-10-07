package in.akash.Transcation_Management.service;

import in.akash.Transcation_Management.entity.CategoryEntity;
import in.akash.Transcation_Management.entity.ExpenseEntity;
import in.akash.Transcation_Management.entity.ProfileEntity;
import in.akash.Transcation_Management.io.ExpenseDTO;
import in.akash.Transcation_Management.repository.CategoryRepository;
import in.akash.Transcation_Management.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final CategoryRepository categoryRepository;

    private final ExpenseRepository expenseRepository;
    private final ProfileService profileService;


    public ExpenseDTO addExpense(ExpenseDTO dto) {
       ProfileEntity profile= profileService.getCurrentProfile();
       CategoryEntity category= categoryRepository.findById(dto.getCategoryId()).
               orElseThrow(()->new RuntimeException("Category Not Found"));
       ExpenseEntity newExpense=toEntity(dto,profile,category);
       newExpense=expenseRepository.save(newExpense);
       return toDTO(newExpense);
    }

    private ExpenseEntity toEntity(ExpenseDTO expenseDTO, ProfileEntity profileEntity, CategoryEntity categoryEntity){
        return ExpenseEntity.builder()
                .name(expenseDTO.getName())
                .icon(expenseDTO.getIcon())
                .amount(expenseDTO.getAmount())
                .date(expenseDTO.getDate())
                .profile(profileEntity)
                .category(categoryEntity)
                .build();

    }

    private ExpenseDTO toDTO(ExpenseEntity expenseEntity){
      return  ExpenseDTO.builder()
                .id(expenseEntity.getId())
                .name(expenseEntity.getName())
                .icon(expenseEntity.getIcon())
                .categoryId(expenseEntity.getCategory()!=null?expenseEntity.getCategory().getId():null)
                .categoryName(expenseEntity.getCategory()!=null?expenseEntity.getCategory().getName():"N/A")
                .amount(expenseEntity.getAmount())
                .date(expenseEntity.getDate())
                .createdAt(expenseEntity.getCreatedAt())
                .updatedAt(expenseEntity.getUpdatedAt())
                .build();
    }


    public List<ExpenseDTO> getCurrentMonthExpenseForCurrentUser(){
        ProfileEntity profile=profileService.getCurrentProfile();
        LocalDate now=LocalDate.now();
        LocalDate startDate=now.withDayOfMonth(1);
        LocalDate endDate=now.withDayOfMonth(now.lengthOfMonth());
      List<ExpenseEntity> list=  expenseRepository.findByProfileIdAndDateBetween(profile.getId(),startDate,endDate);
      return list.stream().map(this::toDTO).toList();
    }

    public void deleteExpense(Long expenseId){
      ProfileEntity profile=  profileService.getCurrentProfile();
       ExpenseEntity entity= expenseRepository.findById(expenseId)
                .orElseThrow(()-> new RuntimeException("Expense Not Found"));
       if(!entity.getProfile().getId().equals(profile.getId())){
           throw new RuntimeException("Unauthorized to delete this message");
       }
       expenseRepository.delete(entity);
    }

    public List<ExpenseDTO> getLatestFiveExpensesForCurrentUser(){
        ProfileEntity profile=profileService.getCurrentProfile();
        List<ExpenseEntity> list= expenseRepository.findByProfileIdOrderByDateDesc(profile.getId());
        return list.stream().map(this::toDTO).toList();
    }

    public BigDecimal getTotalExpensesForCurrentUser(){
        ProfileEntity profile=profileService.getCurrentProfile();
        BigDecimal total=expenseRepository.findTotalExpenseByProfileId(profile.getId());
        return total!=null?total:BigDecimal.ZERO;
    }

    public List<ExpenseDTO> filterExpenses(LocalDate startDate, LocalDate endDate, String keyword, Sort sort){
        ProfileEntity profile=profileService.getCurrentProfile();
        List<ExpenseEntity> list=expenseRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(),startDate,endDate,keyword,sort);
       return list.stream().map(this::toDTO).toList();
    }

    public List<ExpenseDTO> getExpensesForUserOnDate(Long profileId,LocalDate date){
        List<ExpenseEntity> list=expenseRepository.findByProfileIdAndDate(profileId,date);
        return list.stream().map(this::toDTO).toList();
    }
}
