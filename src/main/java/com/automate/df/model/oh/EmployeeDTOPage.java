package com.automate.df.model.oh;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class EmployeeDTOPage {
    public List<EmployeeContent> content;
   
    public boolean last;
    public int totalPages;
    public int totalElements;
    public boolean first;

    public int numberOfElements;
    public int size;
    public int number;
}
