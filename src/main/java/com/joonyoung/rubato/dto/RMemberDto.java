package com.joonyoung.rubato.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RMemberDto {
	private String mid;
	private String mpw;
	private String mname;
	private String memail;
	private String mdate;
}
