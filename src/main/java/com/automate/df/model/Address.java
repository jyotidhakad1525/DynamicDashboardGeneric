package com.automate.df.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Address {

	String city;
	String state;
	List<Contact> contacts;
}
