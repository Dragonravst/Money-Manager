package in.akash.Transcation_Management.service;

import in.akash.Transcation_Management.entity.CategoryEntity;
import in.akash.Transcation_Management.entity.ProfileEntity;
import in.akash.Transcation_Management.io.CategoryDTO;
import in.akash.Transcation_Management.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final ProfileService profileService;
    private final CategoryRepository categoryRepository;


    public CategoryDTO saveCategory(CategoryDTO categoryDTO) {
        ProfileEntity profile=profileService.getCurrentProfile();
        if (categoryRepository.existsByNameAndProfileId(categoryDTO.getName(),profile.getId())) {
            throw new RuntimeException("Category already exists");
        }
        CategoryEntity newCategory=toEntity(categoryDTO,profile);
        categoryRepository.save(newCategory);
        return toDTO(newCategory);
    }

    public List<CategoryDTO> getCategoriesForCurrentUser() {
        ProfileEntity profile=profileService.getCurrentProfile();
        List<CategoryEntity> categories=categoryRepository.findByProfileId(profile.getId());
        return categories.stream().map(this::toDTO).toList();

    }
    public List<CategoryDTO> getCategoriesByTypeFotCurrentUser(String type) {
        ProfileEntity profile=profileService.getCurrentProfile();
        List<CategoryEntity> entity= categoryRepository.findByTypeAndProfileId(type,profile.getId());
        return  entity.stream().map(this::toDTO).toList();
    }

    public CategoryDTO updateCategory(Long categoryId,CategoryDTO categoryDTO) {
        ProfileEntity profile=profileService.getCurrentProfile();
      CategoryEntity existingCategory=  categoryRepository.findByIdAndProfileId(categoryId,profile.getId())
                .orElseThrow(()->new RuntimeException("Category not found or not accessible"));
      existingCategory.setName(categoryDTO.getName());
      existingCategory.setIcon(categoryDTO.getIcon());
      existingCategory.setType(categoryDTO.getType());
      existingCategory=categoryRepository.save(existingCategory);
      return toDTO(existingCategory);
    }

    private CategoryEntity toEntity(CategoryDTO categoryDTO,ProfileEntity profileEntity) {
        return CategoryEntity.builder()
                .name(categoryDTO.getName())
                .icon(categoryDTO.getIcon())
                .profile(profileEntity)
                .type(categoryDTO.getType())
                .build();
    }

    private CategoryDTO toDTO(CategoryEntity categoryEntity) {
        return CategoryDTO.builder()
                .id(categoryEntity.getId())
                .name(categoryEntity.getName())
                .profileId(categoryEntity.getProfile()!=null?categoryEntity.getProfile().getId():null)
                .icon(categoryEntity.getIcon())
                .createdAt(categoryEntity.getCreatedAt())
                .updatedAt(categoryEntity.getUpdatedAt())
                .type(categoryEntity.getType())
                .build();
    }
}
