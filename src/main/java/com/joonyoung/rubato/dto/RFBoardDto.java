package com.joonyoung.rubato.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RFBoardDto {
	private int rfbnum;
	private String rfbname;
	private String rfbtitle;
	private String rfbcontent;
	private int rfbhit;
	private String rfbuserid;
	private int rfbreplycount;
	private String date;
}
