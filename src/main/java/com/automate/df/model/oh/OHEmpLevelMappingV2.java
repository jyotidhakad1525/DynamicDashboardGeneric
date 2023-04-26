package com.automate.df.model.oh;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OHEmpLevelMappingV2 {
	

	Integer orgId;
	
	List<LevelMapping> levels;
}
