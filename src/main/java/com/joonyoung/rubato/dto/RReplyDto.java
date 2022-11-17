package com.joonyoung.rubato.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RReplyDto {
	private int rrnum;
	private String rrcontent;
	private String rrid;
	private int rrorinum;// 댓글이 달린 원글의 게시판번호
	private String rrdate;
}
