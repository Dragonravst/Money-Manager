package in.akash.Transcation_Management.io;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
public class FilterDTO {

    private String type;
    private LocalDate startDate;
    private LocalDate endDate;
    private String keyword;
    private String sortField;
    private String sortOrder;
}
