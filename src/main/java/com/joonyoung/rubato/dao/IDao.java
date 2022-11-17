package com.joonyoung.rubato.dao;

import com.joonyoung.rubato.dto.RMemberDto;

public interface IDao {
	public void joinMember(String mid, String mpw, String mname, String memail);
	public int checkUserId(String mid);
	public int checkUserIdAndPw(String mid, String mpw);
}
