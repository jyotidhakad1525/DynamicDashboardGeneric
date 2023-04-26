package com.automate.df.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ButtonRes {

	//String pageIdentifier;
	//int roleIdentifier;
	List<ButtonControl> buttonControls;
}
