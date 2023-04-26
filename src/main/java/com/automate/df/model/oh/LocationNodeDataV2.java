package com.automate.df.model.oh;

import java.util.Map;
import java.util.Objects;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor

@ToString
public class LocationNodeDataV2 {
	private int id;

	String cananicalName;

	String code;

	String name;

	String parentId;

	String type;

	String refParentId;

	Integer orgId;

	String active;

	Integer locationNodeDefId;
	
	Map<String,Object> childs;
	
	String disabled;
	
	int order;
	
		int branch;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LocationNodeDataV2 other = (LocationNodeDataV2) obj;
		return id == other.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	
}
