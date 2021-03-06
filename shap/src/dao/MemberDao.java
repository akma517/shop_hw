package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import common.DBUtil;
import vo.Member;

// 규칙 => 매개변수값은 첫줄에 디버깅
//actor에 따라 구분하여 메소드를 표기(주석으로)
public class MemberDao {
	
	/* [관리자] 회원 아이디 중복 여부 검증(select) */
	// input: String => memberId
	// output(success): 0
	// output(false): 1 or more than 1
	public int selectMemberIdByAdmin(Member member) throws ClassNotFoundException, SQLException {
		
		// 입력값 디버깅
		System.out.println("[debug] MemberDao.selectMemberIdByAdmin(Member member) => 중복 여부를 검사할 멤버 아이디 : " + member.getMemberId());
		
		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();
		
		// 별칭(alias)를 설정할 때, AS를 안붙여줘도 되고, 별칭이 영어일 경우, ""를 사용해주지 않아도 된다.
		String sql = "SELECT COUNT(*) FROM member WHERE member_id=?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		
		stmt.setString(1, member.getMemberId());
		
		// 쿼리 디버깅
		System.out.println("[debug] MemberDao.selectMemberIdByAdmin(Member member) => 쿼리문 : " + stmt);
	
		ResultSet rs = stmt.executeQuery();
		
		int countMember = 0;
		if (rs.next()) {
			countMember = rs.getInt(1);	
		}
					
		System.out.println("[debug] MemberDao.selectMemberIdByAdmin(Member member) => 입력받은 멤버 아이디와 일치하는 기존 멤버 아이디의 수 : " + countMember);

		rs.close();
		stmt.close();
		conn.close();
		
		return countMember;
		
	}
	
	/* [관리자] 회원 비밀번호 검증(select) */
	// input: Member => memberNo, memberPw
	// output(success): 1
	// output(false): 0
	public int selectMemberPwCompare(Member member) throws ClassNotFoundException, SQLException {
		
		// 입력값 디버깅
		System.out.println("[debug] MemberDao.selectMemberPwCompare(Member member) => 멤버 넘버 : " + member.getMemberId());
		System.out.println("[debug] MemberDao.selectMemberPwCompare(Member member) => 비교할 멤버 비밀번호 : " + member.getMemberPw());
		
		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();
		
		// 별칭(alias)를 설정할 때, AS를 안붙여줘도 되고, 별칭이 영어일 경우, ""를 사용해주지 않아도 된다.
		String sql = "SELECT COUNT(*) FROM member WHERE member_no=? AND member_pw=PASSWORD(?)";
		PreparedStatement stmt = conn.prepareStatement(sql);
		
		stmt.setInt(1, member.getMemberNo());
		stmt.setString(2, member.getMemberPw());
		
		// 쿼리 디버깅
		System.out.println("[debug] MemberDao.selectMemberPwCompare(Member member) => 쿼리문 : " + stmt);
	
		ResultSet rs = stmt.executeQuery();
		
		int countMember = 0;
		if (rs.next()) {
			countMember = rs.getInt(1);	
		}
					
		System.out.println("[debug] MemberDao.selectMemberPwCompare(Member member) => 입력받은 정보와 일치하는 멤버의 수 : " + countMember);

		rs.close();
		stmt.close();
		conn.close();
		
		return countMember;
		
	}
	
	/* [회원, 관리자] 회원 정보 출력(select) */
	// input: String => memberNo
	// output(success): Member => memberNo, memberId, memberName, memberAge, memberGender, createDate, updateDate, memberLevel
	// output(false): null
	public Member selectMemberOne(int memberNo) throws ClassNotFoundException, SQLException {
		
		// 입력값 디버깅
		System.out.println("[debug] MemberDao.selectMemberOne(int memberNo) => 멤버 넘버 : " + memberNo);
		
		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();
		
		// 별칭(alias)를 설정할 때, AS를 안붙여줘도 되고, 별칭이 영어일 경우, ""를 사용해주지 않아도 된다.
		String sql = "SELECT member_no memberNo, member_id memberId, member_name memberName, member_age memberAge, member_gender memberGender, create_date createDate, update_date updateDate, member_level memberLevel FROM member WHERE member_no=?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		
		stmt.setInt(1, memberNo);
		
		// 쿼리 디버깅
		System.out.println("[debug] MemberDao.selectMemberOne(int memberNo) => 쿼리문 : " + stmt);
	
		ResultSet rs = stmt.executeQuery();
		
		Member member = null;
		
		if(rs.next()) {
			
			member = new Member();
			
			member.setMemberNo(rs.getInt("memberNo"));
			member.setMemberId(rs.getString("memberId"));
			member.setMemberName(rs.getString("memberName"));
			member.setMemberAge(rs.getInt("memberAge"));
			member.setMemberGender(rs.getString("memberGender"));
			member.setCreateDate(rs.getString("createDate"));
			member.setUpdateDate(rs.getString("updateDate"));
			member.setMemberLevel(rs.getInt("memberLevel"));
			
			// 출력값 디버깅
			System.out.println("[debug] MemberDao.selectMemberOne(int memberNo) => 멤버 매개변수 : " + member.toString());
			
			rs.close();
			stmt.close();
			conn.close();
			
			return member;
			
		}
		
		rs.close();
		stmt.close();
		conn.close();
		
		return null;
		
	}
	
	
	/* [회원, 관리자] 회원 정보 출력(select) */
	// input: String => memberNo
	// output(success): Member => memberNo, memberId, memberName, memberAge, memberGender, createDate, updateDate, memberLevel
	// output(false): null
	public Member selectMemberOneByAdmin(String memberNo) throws ClassNotFoundException, SQLException {
		
		// 입력값 디버깅
		System.out.println("[debug] MemberDao.selectMemberOneByAdmin(String memberNo) => 멤버 넘버 : " + memberNo);
		
		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();
		
		// 별칭(alias)를 설정할 때, AS를 안붙여줘도 되고, 별칭이 영어일 경우, ""를 사용해주지 않아도 된다.
		String sql = "SELECT member_no memberNo, member_id memberId, member_name memberName, member_age memberAge, member_gender memberGender, create_date createDate, update_date updateDate, member_level memberLevel FROM member WHERE member_no=?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		
		stmt.setString(1, memberNo);
		
		// 쿼리 디버깅
		System.out.println("[debug] MemberDao.selectMemberOne(String memberNo) => 쿼리문 : " + stmt);
	
		ResultSet rs = stmt.executeQuery();
		
		Member member = null;
		
		if(rs.next()) {
			
			member = new Member();
			
			member.setMemberNo(rs.getInt("memberNo"));
			member.setMemberId(rs.getString("memberId"));
			member.setMemberName(rs.getString("memberName"));
			member.setMemberAge(rs.getInt("memberAge"));
			member.setMemberGender(rs.getString("memberGender"));
			member.setCreateDate(rs.getString("createDate"));
			member.setUpdateDate(rs.getString("updateDate"));
			member.setMemberLevel(rs.getInt("memberLevel"));
			
			// 출력값 디버깅
			System.out.println("[debug] MemberDao.selectMemberOneByAdmin(String memberNo) => 멤버 넘버 : " + member.getMemberNo());
			System.out.println("[debug] MemberDao.selectMemberOneByAdmin(String memberNo) => 멤버 아이디 : " + member.getMemberId());
			System.out.println("[debug] MemberDao.selectMemberOneByAdmin(String memberNo) => 멤버 이름 : " + member.getMemberName());
			System.out.println("[debug] MemberDao.selectMemberOneByAdmin(String memberNo) => 멤버 나이 : " + member.getMemberAge());
			System.out.println("[debug] MemberDao.selectMemberOneByAdmin(String memberNo) => 멤버 성별 : " + member.getMemberGender());
			System.out.println("[debug] MemberDao.selectMemberOneByAdmin(String memberNo) => 멤버 가입날짜 : " + member.getCreateDate());
			System.out.println("[debug] MemberDao.selectMemberOneByAdmin(String memberNo) => 멤버 수정날짜 : " + member.getUpdateDate());
			System.out.println("[debug] MemberDao.selectMemberOneByAdmin(String memberNo) => 멤버 등급 : " + member.getMemberLevel());
			
			rs.close();
			stmt.close();
			conn.close();
			
			return member;
			
		}
		
		rs.close();
		stmt.close();
		conn.close();
		
		return null;
		
	}
	
	/* [관리자] 회원 정보 수정(update) */
	// input: Member => memberNo, memberAge, memberGender, memberName
	// output(success): 1
	// output(false): 0
	public int updateMember(Member member) throws ClassNotFoundException, SQLException {
		
		// 입력값 디버깅
		System.out.println("[debug] MemberDao.updateMember(Member member) => 수정할 회원 넘버 : " + member.getMemberNo());
		System.out.println("[debug] MemberDao.updateMember(Member member) => 수정할 회원의 변경할 나이: " + member.getMemberAge());
		System.out.println("[debug] MemberDao.updateMember(Member member) => 수정할 회원의 변경할 성별: " + member.getMemberGender());
		System.out.println("[debug] MemberDao.updateMember(Member member) => 수정할 회원의 변경할 닉네임: " + member.getMemberName());
		
		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();
		
		// 별칭(alias)를 설정할 때, AS를 안붙여줘도 되고, 별칭이 영어일 경우, ""를 사용해주지 않아도 된다.
		String sql = "UPDATE member SET member_age=?, member_gender=?, member_name=? WHERE member_no=?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		
		stmt.setInt(1, member.getMemberAge());
		stmt.setString(2, member.getMemberGender());
		stmt.setString(3, member.getMemberName());
		stmt.setInt(4, member.getMemberNo());
		
		// 쿼리 디버깅
		System.out.println("[debug] MemberDao.updateMember(Member member) => 쿼리문 : " + stmt);
		
		// db 작업 결과 성공 여부 저장
		int confirm = stmt.executeUpdate();
		
		stmt.close();
		conn.close();
		
		return confirm;

	}
	
	
	/* [관리자] 회원 등급 수정(update) */
	// input: Member => memberNo, memberLevel
	// output(success): 1
	// output(false): 0
	public int updateMemberLevelByAdmin(Member member) throws ClassNotFoundException, SQLException {
		
		// 입력값 디버깅
		System.out.println("[debug] MemberDao.updateMemberLevelByAdmin(Member member) => 등급을 수정할 회원 넘버 : " + member.getMemberNo());
		System.out.println("[debug] MemberDao.updateMemberLevelByAdmin(Member member) => 등급을 수정할 회원의 변경할 등급: " + member.getMemberLevel());
		
		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();
		
		// 별칭(alias)를 설정할 때, AS를 안붙여줘도 되고, 별칭이 영어일 경우, ""를 사용해주지 않아도 된다.
		String sql = "UPDATE member SET member_level=? WHERE member_no=?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		
		stmt.setInt(1, member.getMemberLevel());
		stmt.setInt(2, member.getMemberNo());
		
		// 쿼리 디버깅
		System.out.println("[debug] MemberDao.updateMemberLevelByAdmin(Member member) => 쿼리문 : " + stmt);
		
		// db 작업 결과 성공 여부 저장
		int confirm = stmt.executeUpdate();
		
		stmt.close();
		conn.close();
		
		return confirm;

	}
	
	/* [관리자] 회원 비밀번호 수정(update) */
	// input: Member => memberNo, memberPw
	// output(success): 1
	// output(false): 0
	public int updateMemberPw(Member member) throws ClassNotFoundException, SQLException {
		
		// 입력값 디버깅
		System.out.println("[debug] MemberDao.updateMemberPwn(Member member) => 비밀번호를 수정할 회원 넘버 : " + member.getMemberNo());
		System.out.println("[debug] MemberDao.updateMemberPw(Member member) => 비밀번호를 수정할 회원의 변경할 비밀번호: " + member.getMemberPw());
		
		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();
		
		// 별칭(alias)를 설정할 때, AS를 안붙여줘도 되고, 별칭이 영어일 경우, ""를 사용해주지 않아도 된다.
		String sql = "UPDATE member SET member_pw=PASSWORD(?) WHERE member_no=?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		
		stmt.setString(1, member.getMemberPw());
		stmt.setInt(2, member.getMemberNo());
		
		// 쿼리 디버깅
		System.out.println("[debug] MemberDao.updateMemberPw(Member member) => 쿼리문 : " + stmt);
		
		// db 작업 결과 성공 여부 저장
		int confirm = stmt.executeUpdate();
		
		stmt.close();
		conn.close();
		
		return confirm;

	}
	
	/* [관리자] 회원 비밀번호 수정(update) */
	// input: Member => memberNo, memberPw
	// output(success): 1
	// output(false): 0
	public int updateMemberPwByAdmin(Member member) throws ClassNotFoundException, SQLException {
		
		// 입력값 디버깅
		System.out.println("[debug] MemberDao.updateMemberPwByAdmin(Member member) => 비밀번호를 수정할 회원 넘버 : " + member.getMemberNo());
		System.out.println("[debug] MemberDao.updateMemberPwByAdmin(Member member) => 비밀번호를 수정할 회원의 변경할 비밀번호: " + member.getMemberPw());
		
		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();
		
		// 별칭(alias)를 설정할 때, AS를 안붙여줘도 되고, 별칭이 영어일 경우, ""를 사용해주지 않아도 된다.
		String sql = "UPDATE member SET member_pw=PASSWORD(?) WHERE member_no=?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		
		stmt.setString(1, member.getMemberPw());
		stmt.setInt(2, member.getMemberNo());

		
		// 쿼리 디버깅
		System.out.println("[debug] MemberDao.updateMemberPwByAdmin(Member member) => 쿼리문 : " + stmt);
		
		// db 작업 결과 성공 여부 저장
		int confirm = stmt.executeUpdate();
		
		stmt.close();
		conn.close();
		
		return confirm;

	}
	
	/* [회원] 회원탈퇴(delete) */
	// input: Member => memberNo
	// output(success): 1
	// output(false):  0
	public int deleteMember(Member member) throws ClassNotFoundException, SQLException {
		
		// 입력값 디버깅
		System.out.println("[debug] MemberDao.deleteMember(Member member) => 삭제할 회원 넘버 : " + member.getMemberNo());
		System.out.println("[debug] MemberDao.deleteMember(Member member) => 삭제할 회원 넘버 : " + member.getMemberPw());

		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();
		
		// 별칭(alias)를 설정할 때, AS를 안붙여줘도 되고, 별칭이 영어일 경우, ""를 사용해주지 않아도 된다.
		String sql = "DELETE FROM member WHERE member_no=? AND member_pw=PASSWORD(?)";
		PreparedStatement stmt = conn.prepareStatement(sql);
		
		stmt.setInt(1, member.getMemberNo());
		stmt.setString(2, member.getMemberPw());
		
		// 쿼리 디버깅
		System.out.println("[debug] MemberDao.updateMember(Member member) => 쿼리문 : " + stmt);
		
		// db 작업 결과 성공 여부 저장
		int confirm = stmt.executeUpdate();
		
		stmt.close();
		conn.close();
		
		return confirm;
	}
	
	/* [관리자] 회원 삭제(delete) */
	// input: Member => memberNo
	// output(success): 1
	// output(false):  0
	public int deleteMemberByAdmin(Member member) throws ClassNotFoundException, SQLException {
		
		// 입력값 디버깅
		System.out.println("[debug] MemberDao.deleteMemberByAdmin(Member member) => 삭제할 회원 넘버 : " + member.getMemberNo());

		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();
		
		// 별칭(alias)를 설정할 때, AS를 안붙여줘도 되고, 별칭이 영어일 경우, ""를 사용해주지 않아도 된다.
		String sql = "DELETE FROM member WHERE member_no=?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		
		stmt.setInt(1, member.getMemberNo());
		
		// 쿼리 디버깅
		System.out.println("[debug] MemberDao.updateMemberPwByAdmin(Member member) => 쿼리문 : " + stmt);
		
		// db 작업 결과 성공 여부 저장
		int confirm = stmt.executeUpdate();
		
		stmt.close();
		conn.close();
		
		return confirm;
	}
	
	
	/* [관리자] 회원 목록 출력(select) */
	// input: int => beginRow, rowPerPage, String => searchMemberId
	// output(success): ArrayList<Member> => memberNo, memberId, memberName, memberAge, memberGender, createDate, updateDate, memberLevel
	// output(false): null
	public ArrayList<Member> selectMemberListBySearchMemberId(String searchMemberId, int beginRow, int rowPerPage) throws ClassNotFoundException, SQLException {
		
		// 입력값 디버깅
		System.out.println("[debug] selectMemberListBySearchMemberId(String serchMemberId, int beginRow, int rowPerPage) => 입력받은 검색한 멤버 아이디: " + searchMemberId);
		System.out.println("[debug] selectMemberListBySearchMemberId(String serchMemberId, int beginRow, int rowPerPage) => 입력받은 시작 행: " + beginRow);
		System.out.println("[debug] selectMemberListBySearchMemberId(String serchMemberId, int beginRow, int rowPerPage) => 입력받은 페이지 당 행 : " + rowPerPage);
		
		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();
		
		// 별칭(alias)를 설정할 때, AS를 안붙여줘도 되고, 별칭이 영어일 경우, ""를 사용해주지 않아도 된다.
		String sql = "SELECT member_no memberNo, member_id memberId, member_name memberName, member_age memberAge, member_gender memberGender, create_date createDate, update_date updateDate, member_level memberLevel FROM member WHERE member_id LIKE ? ORDER BY create_date DESC LIMIT ?,?";
		
		PreparedStatement stmt = conn.prepareStatement(sql);
			
		// 쿼리문 세팅				
		stmt.setString(1, "%" + searchMemberId + "%");
		stmt.setInt(2, beginRow);
		stmt.setInt(3, rowPerPage);
		
		// 쿼리 디버깅
		System.out.println("[debug] MemberDao.selectMemberListByPage(int beginRow, int rowPerPage) => 쿼리문 : " + stmt);
		
		ResultSet rs = stmt.executeQuery();
		
		ArrayList<Member> memberList = new ArrayList<Member>();
		
		int i = 1;
		
		while(rs.next()) {
			
			Member member = new Member();
			
			member.setMemberNo(rs.getInt("memberNo"));
			member.setMemberId(rs.getString("memberId"));
			member.setMemberName(rs.getString("memberName"));
			member.setMemberAge(rs.getInt("memberAge"));
			member.setMemberGender(rs.getString("memberGender"));
			member.setCreateDate(rs.getString("createDate"));
			member.setUpdateDate(rs.getString("updateDate"));
			member.setMemberLevel(rs.getInt("memberLevel"));
			
			// 출력값 디버깅
			System.out.println("[debug] MemberDao.selectMemberOne(String memberId) => " + i + "번째 멤버 넘버 : " + member.getMemberId());
			System.out.println("[debug] MemberDao.selectMemberOne(String memberId) => " + i + "번째 멤버 아이디 : " + member.getMemberId());
			System.out.println("[debug] MemberDao.selectMemberOne(String memberId) => " + i + "번째 멤버 이름 : " + member.getMemberName());
			System.out.println("[debug] MemberDao.selectMemberOne(String memberId) => " + i + "번째 멤버 나이 : " + member.getMemberAge());
			System.out.println("[debug] MemberDao.selectMemberOne(String memberId) => " + i + "번째 멤버 성별 : " + member.getMemberGender());
			System.out.println("[debug] MemberDao.selectMemberOne(String memberId) => " + i + "번째 멤버 가입날짜 : " + member.getCreateDate());
			System.out.println("[debug] MemberDao.selectMemberOne(String memberId) => " + i + "번째 멤버 가입날짜 : " + member.getUpdateDate());
			System.out.println("[debug] MemberDao.selectMemberOne(String memberId) => " + i + "번째 멤버 등급 : " + member.getMemberLevel());
			
			memberList.add(member);
			
			i += 1;
			
		}
		
		rs.close();
		stmt.close();
		conn.close();
		
		// 조회결과 아무런 값도 안 나오면 null을 반환
		if(memberList.isEmpty()) {
			return null;
		}
		
		return memberList;
	}
	
	/* [관리자]  검색된 회원의 수 계산 (select) */
	// input:  int => rowPerPage, String => searchMemberId
	// output(success): int => countSearchedMemeber
	// output(false): 0
	public int selectCountSearchedMember(String searchMemberId, int rowPerPage) throws SQLException, ClassNotFoundException {
		
		// DBUtil 클래스로 connection을 만듬
		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();
		
		// 조건에 맞는 검색 결과의 총 게시글들의 수 계산하기
		String sql = null;
		PreparedStatement stmt = null;
					
		sql = 
				"SELECT "
				+ "COUNT(*) "
				+ "FROM "
				+ "member "
				+ "WHERE "
				+ "member_id LIKE ?";
		stmt = conn.prepareStatement(sql);

		ResultSet rs = stmt.executeQuery();
		
		int countSearchedMemeber = 0;
		if (rs.next()) {
			countSearchedMemeber = rs.getInt(1);	
		}
					
		System.out.println("[debug] MemberDao.selectCountMember(int rowPerPage) => 검색된 멤버의 수 : " + countSearchedMemeber);

		rs.close();
		stmt.close();
		conn.close();
		
		return countSearchedMemeber;
	}
	
	/* [관리자] 회원 목록 출력(select) */
	// input: int => beginRow, rowPerPage
	// output(success): ArrayList<Member> => memberId, memberName, memberDate, memberGender, memberAge
	// output(false): null
	public ArrayList<Member> selectMemberListByPage(int beginRow, int rowPerPage) throws ClassNotFoundException, SQLException {
		
		// 입력값 디버깅
		System.out.println("[debug] selectMemberListByPage(int beginRow, int rowPerPage) => 입력받은 시작 행: " + beginRow);
		System.out.println("[debug]selectMemberListByPage(int beginRow, int rowPerPage) => 입력받은 페이지 당 행 : " + rowPerPage);
		
		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();
		
		// 별칭(alias)를 설정할 때, AS를 안붙여줘도 되고, 별칭이 영어일 경우, ""를 사용해주지 않아도 된다.
		String sql =
				"SELECT "
				+ "member_no memberNo, "
				+ "member_id memberId, "
				+ "member_name memberName, "
				+ "member_age memberAge, "
				+ "member_gender memberGender, "
				+ "create_date createDate, "
				+ "update_date updateDate, "
				+ "member_level memberLevel "
				+ "FROM "
				+ "member "
				+ "ORDER BY "
				+ "create_date "
				+ "DESC LIMIT ?,?";
		
		PreparedStatement stmt = conn.prepareStatement(sql);
			
		// 쿼리문 세팅				
		stmt.setInt(1, beginRow);
		stmt.setInt(2, rowPerPage);
		
		// 쿼리 디버깅
		System.out.println("[debug] MemberDao.selectMemberListByPage(int beginRow, int rowPerPage) => 쿼리문 : " + stmt);
		
		ResultSet rs = stmt.executeQuery();
		
		ArrayList<Member> memberList = new ArrayList<Member>();
		
		int i = 1;
		
		while(rs.next()) {
			
			Member member = new Member();
			
			member.setMemberNo(rs.getInt("memberNo"));
			member.setMemberId(rs.getString("memberId"));
			member.setMemberName(rs.getString("memberName"));
			member.setMemberAge(rs.getInt("memberAge"));
			member.setMemberGender(rs.getString("memberGender"));
			member.setCreateDate(rs.getString("createDate"));
			member.setUpdateDate(rs.getString("updateDate"));
			member.setMemberLevel(rs.getInt("memberLevel"));
			
			// 출력값 디버깅
			System.out.println("[debug] MemberDao.selectMemberOne(String memberId) => " + i + "번째 멤버 넘버 : " + member.getMemberId());
			System.out.println("[debug] MemberDao.selectMemberOne(String memberId) => " + i + "번째 멤버 아이디 : " + member.getMemberId());
			System.out.println("[debug] MemberDao.selectMemberOne(String memberId) => " + i + "번째 멤버 이름 : " + member.getMemberName());
			System.out.println("[debug] MemberDao.selectMemberOne(String memberId) => " + i + "번째 멤버 나이 : " + member.getMemberAge());
			System.out.println("[debug] MemberDao.selectMemberOne(String memberId) => " + i + "번째 멤버 성별 : " + member.getMemberGender());
			System.out.println("[debug] MemberDao.selectMemberOne(String memberId) => " + i + "번째 멤버 가입날짜 : " + member.getCreateDate());
			System.out.println("[debug] MemberDao.selectMemberOne(String memberId) => " + i + "번째 멤버 가입날짜 : " + member.getUpdateDate());
			System.out.println("[debug] MemberDao.selectMemberOne(String memberId) => " + i + "번째 멤버 등급 : " + member.getMemberLevel());
			
			memberList.add(member);
			
			i += 1;
			
		}
		
		rs.close();
		stmt.close();
		conn.close();
		
		// 조회결과 아무런 값도 안 나오면 null을 반환
		if(memberList.isEmpty()) {
			return null;
		}
		
		return memberList;
	}
	
	/* [관리자] 회원의 수 계산 (select) */
	// input:  int => rowPerPage
	// output(success): int => countMemeber
	// output(false): 0
	public int selectCountMember(int rowPerPage) throws SQLException, ClassNotFoundException {
		
		// DBUtil 클래스로 connection을 만듬
		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();
		
		// 조건에 맞는 검색 결과의 총 게시글들의 수 계산하기
		String sql = null;
		PreparedStatement stmt = null;
					
		sql = "SELECT COUNT(*) FROM member";
		stmt = conn.prepareStatement(sql);

		ResultSet rs = stmt.executeQuery();
		
		int countMember = 0;
		if (rs.next()) {
			countMember = rs.getInt(1);	
		}
					
		System.out.println("[debug] MemberDao.selectCountMember(int rowPerPage) => 총 멤버의 수 : " + countMember);

		rs.close();
		stmt.close();
		conn.close();
		
		return countMember;
	}
	
	/* [비회원] 로그인 */
	// input: Member => memberId, memberPw
	// output(success): Member => , memberNo, memberId, memberName, memberLevel
	// output(false): null
	public Member login(Member member) throws ClassNotFoundException, SQLException {
		
		// 입력값 디버깅
		System.out.println("[debug] MemberDao.login(Member member) => 입력받은 멤버 아이디 : " + member.getMemberId());
		System.out.println("[debug] MemberDao.login(Member member) => 입력받은 멤버 비밀번호 : " + member.getMemberPw());
		
		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();
		
		// 별칭(alias)를 설정할 때, AS를 안붙여줘도 되고, 별칭이 영어일 경우, ""를 사용해주지 않아도 된다.
		String sql = 
				"SELECT "
				+ "member_no memberNo, "
				+ "member_id memberId, "
				+ "member_name memberName, "
				+ "member_level memberLevel "
				+ "FROM "
				+ "member "
				+ "WHERE "
				+ "member_id=? and member_pw=PASSWORD(?)";
		
		PreparedStatement stmt = conn.prepareStatement(sql);
		
		stmt.setString(1,member.getMemberId());
		stmt.setString(2, member.getMemberPw());
		
		// 쿼리 디버깅
		System.out.println("[debug] MemberDao.login(Member member) => 쿼리문 : " + stmt);
		
		ResultSet rs = stmt.executeQuery();
		
		// 객체에 null을 넣으면 그 객체의 기본 구조도 없이 그냥 null 값이 된다. 객체에 null을 넣고 그 객체의 메소드를 호출하면 nullPointError가 발생한다.
		
		if(rs.next()) {
			
			Member returnedMember = new Member();
			
			returnedMember.setMemberNo(rs.getInt("memberNo"));
			returnedMember.setMemberId(rs.getString("memberId"));
			returnedMember.setMemberName(rs.getString("memberName"));
			returnedMember.setMemberLevel(rs.getInt("memberLevel"));
			
			// 출력값 디버깅
			System.out.println("[debug] MemberDao.login(Member member) => 멤버 아이디 : " + returnedMember.getMemberNo());
			System.out.println("[debug] MemberDao.login(Member member) => 멤버 아이디 : " + returnedMember.getMemberId());
			System.out.println("[debug] MemberDao.login(Member member) => 멤버 이름 : " + returnedMember.getMemberName());
			System.out.println("[debug] MemberDao.login(Member member) => 멤버 레벨 : " + returnedMember.getMemberLevel());
			
			rs.close();
			stmt.close();
			conn.close();
			
			return returnedMember;
			
		}
		
		rs.close();
		stmt.close();
		conn.close();
		
		return null;
	}
	
	/* [비회원] 회원 가입(insert) */
	// input:  Member => memberId, memberPw, MemberName, MemberAge, MemberGender
	// output(success): 1
	// output(false): 0
	public int insertMember(Member member) throws ClassNotFoundException, SQLException {
		
		// 입력값 디버깅
		System.out.println("[debug] MemberDao.insertMember(Member member) => 가입자 아이디 : " + member.getMemberId());
		System.out.println("[debug] MemberDao.insertMember(Member member) => 가입자 비밀번호 : " + member.getMemberPw());
		System.out.println("[debug] MemberDao.insertMember(Member member) => 가입자 이름 : " + member.getMemberName());
		System.out.println("[debug] MemberDao.insertMember(Member member) => 가입자 나이 : " + member.getMemberAge());
		System.out.println("[debug] MemberDao.insertMember(Member member) => 가입자 성별 : " + member.getMemberGender());
		
		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();
		
		// 별칭(alias)를 설정할 때, AS를 안붙여줘도 되고, 별칭이 영어일 경우, ""를 사용해주지 않아도 된다.
		String sql = 
				"INSERT INTO member("
				+ "member_id, "
				+ "member_pw, "
				+ "member_level, "
				+ "member_name, "
				+ "member_age, "
				+ "member_gender,"
				+ "update_date, "
				+ "create_date"
				+ ") VALUES("
				+ "?, PASSWORD(?), 0, ?, ?, ?, NOW(), NOW()"
				+ ")";
		
		PreparedStatement stmt = conn.prepareStatement(sql);
		
		stmt.setString(1, member.getMemberId());
		stmt.setString(2, member.getMemberPw());
		stmt.setString(3, member.getMemberName());
		stmt.setInt(4, member.getMemberAge());
		stmt.setString(5, member.getMemberGender());
		
		// 쿼리 디버깅
		System.out.println("[debug] MemberDao.insertMember(Member member) => 쿼리문 : " + stmt);
		
		// db 작업 결과 성공 여부 저장
		int confirm = stmt.executeUpdate();
		
		stmt.close();
		conn.close();
		
		return confirm;
	}
	
}
