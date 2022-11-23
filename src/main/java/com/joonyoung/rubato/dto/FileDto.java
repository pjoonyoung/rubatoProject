package com.joonyoung.rubato.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileDto {
	private int filenum;//파일의 고유번호(시퀀스)
	private int boardnum;//파일이 첨부된 원글의 게시판 글 번
	private String fileoriname;//첨부된 파일의 원래이름
	private String filename;//첨부된 파일의 변경된 이름
	private String fileextention;//첨부된 파일 확장자
	private String fileurl;//첨부된 파일의 저장경로
}
