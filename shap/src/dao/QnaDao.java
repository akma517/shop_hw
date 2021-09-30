package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import common.DBUtil;
import vo.Ebook;
import vo.Member;
import vo.Qna;
import vo.QnaMember;

public class QnaDao {
	
	/* [관리자] 댓글을 안 달은 qna 목록 출력(select) */
	// input: int => beginRow, rowPerPage, 
	//		  String => searchQnaTitle, searchQnaCategory
	// output(success): ArrayList<QnaMember> => Qna => qnaNo, qnaTitle, qnaSecret, qnaCategory, createDate
	//										 => Member => memberNo, memberId, memberName
	// output(false): ArrayList<QnaMember> => null;
	public ArrayList<QnaMember> selectQnaListByAdmin(int beginRow, int rowPerPage, String searchQnaTitle, String searchQnaCategory) throws ClassNotFoundException, SQLException {
		
		// 입력값 디버깅
		System.out.println("[debug] QnakDao.selectQnaListByAdmi(int beginRow, int rowPerPage, String searchQnaTiitle, String searchQnaCategory) => 입력받은 검색한 Qna 제목 : " + beginRow);
		System.out.println("[debug] QnakDao.selectQnaListByAdmi(int beginRow, int rowPerPage, String searchQnaTiitle, String searchQnaCategory) => 입력받은 검색한 Qna 종류 : " + rowPerPage);
		System.out.println("[debug] QnakDao.selectQnaListByAdmi(int beginRow, int rowPerPage, String searchQnaTiitle, String searchQnaCategory) => 입력받은 시작 행 : " + searchQnaTitle);
		System.out.println("[debug] QnakDao.selectQnaListByAdmi(int beginRow, int rowPerPage, String searchQnaTiitle, String searchQnaCategory) => 입력받은 페이지 당 행 : " + searchQnaCategory);
		
		// DB 자원 연결
		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();
		
		String sql = null;
		PreparedStatement stmt = null;
		
		
		/* 카테고리가 있고 없고에 따라(ALL or 특정 카테고리) 쿼리문을 다르게 구성 */
		// 입력받은 카테고리와 qna 제목이 없을 경우(전체검색)
		if ( (searchQnaCategory.equals("ALL")) && (searchQnaTitle.equals("ALL") || searchQnaTitle.equals("")) ) {
			sql = "SELECT q.qnaNo, q.qnaTitle, q.qnaSecret, q.qnaCategory, q.createDate, m.member_no memberNo, m.member_id memberId, m.member_name memberName FROM (SELECT q.qna_no qnaNo, q.qna_title qnaTitle, q.qna_secret qnaSecret, q.qna_category qnaCategory, q.create_date createDate, q.member_no MemberNo, t.qna_no tQnaNo FROM qna q LEFT JOIN (SELECT q.qna_no FROM qna q JOIN qna_comment qc ON q.qna_no=qc.qna_no) t ON q.qna_no=t.qna_no) q JOIN member m ON q.memberNo=m.member_no WHERE q.tQnaNo IS NULL ORDER BY q.createDate DESC LIMIT ?,?";
			stmt = conn.prepareStatement(sql);
			
			stmt.setInt(1, beginRow);
			stmt.setInt(2, rowPerPage);
						
		// 입력받은 카테고리만 있을 경우
		} else if ( (!searchQnaCategory.equals("ALL")) && (searchQnaTitle.equals("ALL") || searchQnaTitle.equals(""))){
			sql = "SELECT q.qnaNo, q.qnaTitle, q.qnaSecret, q.qnaCategory, q.createDate, m.member_no memberNo, m.member_id memberId, m.member_name memberName FROM (SELECT q.qna_no qnaNo, q.qna_title qnaTitle, q.qna_secret qnaSecret, q.qna_category qnaCategory, q.create_date createDate, q.member_no MemberNo, t.qna_no tQnaNo FROM qna q LEFT JOIN (SELECT q.qna_no FROM qna q JOIN qna_comment qc ON q.qna_no=qc.qna_no) t ON q.qna_no=t.qna_no) q JOIN member m ON q.memberNo=m.member_no WHERE q.tQnaNo IS NULL AND q.qnaCategory=? ORDER BY q.createDate DESC LIMIT ?,?";
			stmt = conn.prepareStatement(sql);
			
			// 쿼리문 세팅
			stmt.setString(1, searchQnaCategory);
			stmt.setInt(2, beginRow);
			stmt.setInt(3, rowPerPage);
			
		// 입력받은 qna 제목만 있을 경우	
		} else if ( (searchQnaCategory.equals("ALL")) && (!searchQnaTitle.equals("ALL") || !searchQnaTitle.equals(""))){
			sql = "SELECT q.qnaNo, q.qnaTitle, q.qnaSecret, q.qnaCategory, q.createDate, m.member_no memberNo, m.member_id memberId, m.member_name memberName FROM (SELECT q.qna_no qnaNo, q.qna_title qnaTitle, q.qna_secret qnaSecret, q.qna_category qnaCategory, q.create_date createDate, q.member_no MemberNo, t.qna_no tQnaNo FROM qna q LEFT JOIN (SELECT q.qna_no FROM qna q JOIN qna_comment qc ON q.qna_no=qc.qna_no) t ON q.qna_no=t.qna_no) q JOIN member m ON q.memberNo=m.member_no WHERE q.tQnaNo IS NULL AND q.qnaTitle LIKE ? ORDER BY q.createDate DESC LIMIT ?,?";
			stmt = conn.prepareStatement(sql);
			
			// 쿼리문 세팅
			stmt.setString(1, "%" + searchQnaTitle + "%");
			stmt.setInt(2, beginRow);
			stmt.setInt(3, rowPerPage);
			
		// 입력받은 카테고리와 qna 제목이 있을 경우
		} else if ( (!searchQnaCategory.equals("ALL")) && (!searchQnaTitle.equals("ALL") || !searchQnaTitle.equals(""))){
			sql = "SELECT q.qnaNo, q.qnaTitle, q.qnaSecret, q.qnaCategory, q.createDate, m.member_no memberNo, m.member_id memberId, m.member_name memberName FROM (SELECT q.qna_no qnaNo, q.qna_title qnaTitle, q.qna_secret qnaSecret, q.qna_category qnaCategory, q.create_date createDate, q.member_no MemberNo, t.qna_no tQnaNo FROM qna q LEFT JOIN (SELECT q.qna_no FROM qna q JOIN qna_comment qc ON q.qna_no=qc.qna_no) t ON q.qna_no=t.qna_no) q JOIN member m ON q.memberNo=m.member_no WHERE q.tQnaNo IS NULL AND q.qnaCategory=? AND q.qnaTitle LIKE ? ORDER BY q.createDate DESC LIMIT ?,?";
			stmt = conn.prepareStatement(sql);
			
			// 쿼리문 세팅
			stmt.setString(1, searchQnaCategory);
			stmt.setString(2, "%" + searchQnaTitle + "%");
			stmt.setInt(3, beginRow);
			stmt.setInt(4, rowPerPage);
		}
		
		// 쿼리 디버깅
		System.out.println("[debug] QnakDao.selectQnaListByAdmi(int beginRow, int rowPerPage, String searchQnaTiitle, String searchQnaCategory) => 쿼리문 : " + stmt);
		
		// 쿼리 실행
		ResultSet rs = stmt.executeQuery();
		
		// 반환할 리스트 생성
		ArrayList<QnaMember> qnaMemberList = new ArrayList<QnaMember>();
		
		int i = 1;
		
		while(rs.next()) {
			
			// QnaMember 객체 생성
			QnaMember qnaMember = new QnaMember();
			
			// Qna 객체 생성
			Qna qna = new Qna();
			
			// Qna와 관련된 조회 결과 담기
			qna.setQnaNo(rs.getInt("qnaNo"));
			qna.setQnaTitle(rs.getString("qnaTitle"));
			qna.setQnaSecret(rs.getString("qnaSecret"));
			qna.setQnaCategory(rs.getString("qnaCategory"));
			qna.setCreateDate(rs.getString("createDate"));
			
			// 출력값 디버깅
			System.out.println("[debug] QnakDao.selectQnaList(int beginRow, int rowPerPage, String searchQnaTiitle, String searchQnaCategory) => " + i + "번째 qna 넘버 : " + qna.getQnaNo());
			System.out.println("[debug] QnakDao.selectQnaList(int beginRow, int rowPerPage, String searchQnaTiitle, String searchQnaCategory) => " + i + "번째 qna 제목 : " + qna.getQnaTitle());
			System.out.println("[debug] QnakDao.selectQnaList(int beginRow, int rowPerPage, String searchQnaTiitle, String searchQnaCategory) => " + i + "번째 qna 비밀글 여부 : " + qna.getQnaSecret());
			System.out.println("[debug] QnakDao.selectQnaList(int beginRow, int rowPerPage, String searchQnaTiitle, String searchQnaCategory) => " + i + "번째 qna 종류 : " + qna.getQnaCategory());
			System.out.println("[debug] QnakDao.selectQnaList(int beginRow, int rowPerPage, String searchQnaTiitle, String searchQnaCategory) => " + i + "번째 qna 등록날짜 : " + qna.getCreateDate());
			
			//QnaMember 객체에 Qna 객체 담기
			qnaMember.setQna(qna);
			
			// 회원 객체 생성
			Member m = new Member();
			
			// 회원 관련 조회 결과 담기
			m.setMemberNo(rs.getInt("memberNo"));
			m.setMemberName(rs.getString("memberName"));
			m.setMemberId(rs.getString("memberId"));
			
			// 출력값 디버깅
			System.out.println("[debug] QnakDao.selectQnaList(int beginRow, int rowPerPage, String searchQnaTiitle, String searchQnaCategory) => " + i + "번째 qna 작성자 넘버 :" + m.getMemberNo());
			System.out.println("[debug] QnakDao.selectQnaList(int beginRow, int rowPerPage, String searchQnaTiitle, String searchQnaCategory) => " + i + "번째 qna 작성자 이름 :" + m.getMemberName());
			System.out.println("[debug] QnakDao.selectQnaList(int beginRow, int rowPerPage, String searchQnaTiitle, String searchQnaCategory) => " + i + "번째 qna 작성자 아이디 :" + m.getMemberId());
			
			//QnaMember 객체에 회원 객체 담기
			qnaMember.setMember(m);
			
			// 반환할 리스트에 QnaMember 객체 담기
			qnaMemberList.add(qnaMember);
			
			i += 1;
			
		}
		
		// DB 자원 해제
		rs.close();
		stmt.close();
		conn.close();
		return null;
	}
	
	
	/* [관리자] 댓글을 안 달은 qna 개수 구하기(select) */
	// input: String => searchQnaTitle, searchQnaCategory
	// output(success): int => CountQnaList
	// output(false): 0
	public int selectCountQnaListByAdmin(String searchQnaTitle, String searchQnaCategory) throws ClassNotFoundException, SQLException {
		
		// 입력값 디버깅
		System.out.println("[debug] QnakDao.selectCountQnaListByAdmin(String searchQnaTitle, String searchQnaCategory) => 입력받은 시작 행 : " + searchQnaTitle);
		System.out.println("[debug] QnakDao.selectCountQnaListByAdmin(String searchQnaTitle, String searchQnaCategory) => 입력받은 페이지 당 행 : " + searchQnaCategory);
		
		// DB 자원 연결
		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();
		
		String sql = null;
		PreparedStatement stmt = null;
		
		/* 카테고리가 있고 없고에 따라(ALL or 특정 카테고리) 쿼리문을 다르게 구성 */
		// 입력받은 카테고리와 qna 제목이 없을 경우(전체검색)
		if ( (searchQnaCategory.equals("ALL")) && (searchQnaTitle.equals("ALL") || searchQnaTitle.equals("")) ) {
			sql = "SELECT COUNT(*) FROM (SELECT q.qna_no qnaNo, q.qna_title qnaTitle, q.qna_secret qnaSecret, q.qna_category qnaCategory, q.create_date createDate, q.member_no MemberNo, t.qna_no tQnaNo FROM qna q LEFT JOIN (SELECT q.qna_no FROM qna q JOIN qna_comment qc ON q.qna_no=qc.qna_no) t ON q.qna_no=t.qna_no) q JOIN member m ON q.memberNo=m.member_no WHERE q.tQnaNo IS NULL";
			stmt = conn.prepareStatement(sql);
						
		// 입력받은 카테고리만 있을 경우
		} else if ( (!searchQnaCategory.equals("ALL")) && (searchQnaTitle.equals("ALL") || searchQnaTitle.equals(""))){
			sql = "SELECT COUNT(*) FROM (SELECT q.qna_no qnaNo, q.qna_title qnaTitle, q.qna_secret qnaSecret, q.qna_category qnaCategory, q.create_date createDate, q.member_no MemberNo, t.qna_no tQnaNo FROM qna q LEFT JOIN (SELECT q.qna_no FROM qna q JOIN qna_comment qc ON q.qna_no=qc.qna_no) t ON q.qna_no=t.qna_no) q JOIN member m ON q.memberNo=m.member_no WHERE q.tQnaNo IS NULL AND q.qnaCategory=?";
			stmt = conn.prepareStatement(sql);
			
			// 쿼리문 세팅
			stmt.setString(1, searchQnaCategory);
		// 입력받은 qna 제목만 있을 경우	
		} else if ( (searchQnaCategory.equals("ALL")) && (!searchQnaTitle.equals("ALL") || !searchQnaTitle.equals(""))){
			sql = "SELECT COUNT(*) FROM (SELECT q.qna_no qnaNo, q.qna_title qnaTitle, q.qna_secret qnaSecret, q.qna_category qnaCategory, q.create_date createDate, q.member_no MemberNo, t.qna_no tQnaNo FROM qna q LEFT JOIN (SELECT q.qna_no FROM qna q JOIN qna_comment qc ON q.qna_no=qc.qna_no) t ON q.qna_no=t.qna_no) q JOIN member m ON q.memberNo=m.member_no WHERE q.tQnaNo IS NULL AND q.qnaCategory LIKE ?";
			stmt = conn.prepareStatement(sql);
			
			// 쿼리문 세팅
			stmt.setString(1, "%" + searchQnaTitle + "%");
			
		// 입력받은 카테고리와 qna 제목이 있을 경우
		} else if ( (!searchQnaCategory.equals("ALL")) && (!searchQnaTitle.equals("ALL") || !searchQnaTitle.equals(""))){
			sql = "SELECT COUNT(*) FROM (SELECT q.qna_no qnaNo, q.qna_title qnaTitle, q.qna_secret qnaSecret, q.qna_category qnaCategory, q.create_date createDate, q.member_no MemberNo, t.qna_no tQnaNo FROM qna q LEFT JOIN (SELECT q.qna_no FROM qna q JOIN qna_comment qc ON q.qna_no=qc.qna_no) t ON q.qna_no=t.qna_no) q JOIN member m ON q.memberNo=m.member_no WHERE q.tQnaNo IS NULL AND q.qnaCategory=? AND q.qnaCategory LIKE ?";
			stmt = conn.prepareStatement(sql);
			
			// 쿼리문 세팅
			stmt.setString(1, searchQnaCategory);
			stmt.setString(2, "%" + searchQnaTitle + "%");
		}
		
		// 쿼리 디버깅
		System.out.println("[debug] QnakDao.selectCountQnaListByAdmin(String searchQnaTitle, String searchQnaCategory) => 쿼리문 : " + stmt);
		
		// 쿼리 실행
		ResultSet rs = stmt.executeQuery();
		
		// 반환할 주문 개수를 담을 변수 생성
		int countQnaList = 0;
		
		if (rs.next()) {
			
			// 조회 결과 담기
			countQnaList = rs.getInt(1);
			
		}
		
		// DB 자원 해제
		rs.close();
		stmt.close();
		conn.close();
		
		return countQnaList;
		
	}
	
	/* [회원, 비회원, 관리자] qna 목록 출력 */
	// input: int => beginRow, rowPerPage, 
	//		  String => searchQnaTitle, searchQnaCategory
	// output(success): ArrayList<QnaMember> => Qna => qnaNo, qnaTitle, qnaSecret, qnaCategory, createDate
	//										 => Member => memberNo, memberId, memberName
	// output(false): ArrayList<Notice> => null;
	public ArrayList<QnaMember> selectQnaList(int beginRow, int rowPerPage, String searchQnaTitle, String searchQnaCategory) throws ClassNotFoundException, SQLException {
		
		// 입력값 디버깅
		System.out.println("[debug] QnakDao.selectQnaList(int beginRow, int rowPerPage, String searchQnaTiitle, String searchQnaCategory) => 입력받은 검색한 Qna 제목 : " + beginRow);
		System.out.println("[debug] QnakDao.selectQnaList(int beginRow, int rowPerPage, String searchQnaTiitle, String searchQnaCategory) => 입력받은 검색한 Qna 종류 : " + rowPerPage);
		System.out.println("[debug] QnakDao.selectQnaList(int beginRow, int rowPerPage, String searchQnaTiitle, String searchQnaCategory) => 입력받은 시작 행 : " + searchQnaTitle);
		System.out.println("[debug] QnakDao.selectQnaList(int beginRow, int rowPerPage, String searchQnaTiitle, String searchQnaCategory) => 입력받은 페이지 당 행 : " + searchQnaCategory);
		
		// DB 자원 연결
		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();
		
		String sql = null;
		PreparedStatement stmt = null;
		
		/* 카테고리가 있고 없고에 따라(ALL or 특정 카테고리) 쿼리문을 다르게 구성 */
		// 입력받은 카테고리와 qna 제목이 없을 경우(전체검색)
		if ( (searchQnaCategory.equals("ALL")) && (searchQnaTitle.equals("ALL") || searchQnaTitle.equals("")) ) {
			sql = "SELECT q.qna_no qnaNo, q.qna_title qnaTitle, q.qna_secret qnaSecret, q.qna_category qnaCategory, q.create_date createDate, m.member_no memberNo, m.member_id memberId, m.memberName memberName FROM qna q JOIN member m ON q.member_no=m.member_no ORDER BY q.create_date DESC LIMIT ?,?";
			stmt = conn.prepareStatement(sql);
			
			stmt.setInt(1, beginRow);
			stmt.setInt(2, rowPerPage);
						
		// 입력받은 카테고리만 있을 경우
		} else if ( (!searchQnaCategory.equals("ALL")) && (searchQnaTitle.equals("ALL") || searchQnaTitle.equals(""))){
			sql = "SELECT q.qna_no qnaNo, q.qna_title qnaTitle, q.qna_secret qnaSecret, q.qna_category qnaCategory, q.create_date createDate, m.member_no memberNo, m.member_id memberId, m.memberName memberName FROM qna q JOIN member m ON q.member_no=m.member_no WHERE q.qna_category=? ORDER BY q.create_date DESC LIMIT ?,?";
			stmt = conn.prepareStatement(sql);
			
			// 쿼리문 세팅
			stmt.setString(1, searchQnaCategory);
			stmt.setInt(2, beginRow);
			stmt.setInt(3, rowPerPage);
			
		// 입력받은 qna 제목만 있을 경우	
		} else if ( (searchQnaCategory.equals("ALL")) && (!searchQnaTitle.equals("ALL") || !searchQnaTitle.equals(""))){
			sql = "SELECT q.qna_no qnaNo, q.qna_title qnaTitle, q.qna_secret qnaSecret, q.qna_category qnaCategory, q.create_date createDate, m.member_no memberNo, m.member_id memberId, m.memberName memberName FROM qna q JOIN member m ON q.member_no=m.member_no WHERE q.qna_title LIKE ? ORDER BY q.create_date DESC LIMIT ?,?";
			stmt = conn.prepareStatement(sql);
			
			// 쿼리문 세팅
			stmt.setString(1, "%" + searchQnaTitle + "%");
			stmt.setInt(2, beginRow);
			stmt.setInt(3, rowPerPage);
			
		// 입력받은 카테고리와 qna 제목이 있을 경우
		} else if ( (!searchQnaCategory.equals("ALL")) && (!searchQnaTitle.equals("ALL") || !searchQnaTitle.equals(""))){
			sql = "SELECT q.qna_no qnaNo, q.qna_title qnaTitle, q.qna_secret qnaSecret, q.qna_category qnaCategory, q.create_date createDate, m.member_no memberNo, m.member_id memberId, m.memberName memberName FROM qna q JOIN member m ON q.member_no=m.member_no WHERE q.qna_category = ? AND q.qna_title LIKE ? ORDER BY q.create_date DESC LIMIT ?,?";
			stmt = conn.prepareStatement(sql);
			
			// 쿼리문 세팅
			stmt.setString(1, searchQnaCategory);
			stmt.setString(2, "%" + searchQnaTitle + "%");
			stmt.setInt(3, beginRow);
			stmt.setInt(4, rowPerPage);
		}
		
		// 쿼리 디버깅
		System.out.println("[debug] QnakDao.selectQnaList(int beginRow, int rowPerPage, String searchQnaTiitle, String searchQnaCategory) => 쿼리문 : " + stmt);
		
		// 쿼리 실행
		ResultSet rs = stmt.executeQuery();
		
		// 반환할 리스트 생성
		ArrayList<QnaMember> qnaMemberList = new ArrayList<QnaMember>();
		
		int i = 1;
		
		while(rs.next()) {
			
			// QnaMember 객체 생성
			QnaMember qnaMember = new QnaMember();
			
			// Qna 객체 생성
			Qna qna = new Qna();
			
			// Qna와 관련된 조회 결과 담기
			qna.setQnaNo(rs.getInt("qnaNo"));
			qna.setQnaTitle(rs.getString("qnaTitle"));
			qna.setQnaSecret(rs.getString("qnaSecret"));
			qna.setQnaCategory(rs.getString("qnaCategory"));
			qna.setCreateDate(rs.getString("createDate"));
			
			// 출력값 디버깅
			System.out.println("[debug] QnakDao.selectQnaList(int beginRow, int rowPerPage, String searchQnaTiitle, String searchQnaCategory) => " + i + "번째 qna 넘버 : " + qna.getQnaNo());
			System.out.println("[debug] QnakDao.selectQnaList(int beginRow, int rowPerPage, String searchQnaTiitle, String searchQnaCategory) => " + i + "번째 qna 제목 : " + qna.getQnaTitle());
			System.out.println("[debug] QnakDao.selectQnaList(int beginRow, int rowPerPage, String searchQnaTiitle, String searchQnaCategory) => " + i + "번째 qna 비밀글 여부 : " + qna.getQnaSecret());
			System.out.println("[debug] QnakDao.selectQnaList(int beginRow, int rowPerPage, String searchQnaTiitle, String searchQnaCategory) => " + i + "번째 qna 종류 : " + qna.getQnaCategory());
			System.out.println("[debug] QnakDao.selectQnaList(int beginRow, int rowPerPage, String searchQnaTiitle, String searchQnaCategory) => " + i + "번째 qna 등록날짜 : " + qna.getCreateDate());
			
			//QnaMember 객체에 Qna 객체 담기
			qnaMember.setQna(qna);
			
			// 회원 객체 생성
			Member m = new Member();
			
			// 회원 관련 조회 결과 담기
			m.setMemberNo(rs.getInt("memberNo"));
			m.setMemberName(rs.getString("memberName"));
			m.setMemberId(rs.getString("memberId"));
			
			// 출력값 디버깅
			System.out.println("[debug] QnakDao.selectQnaList(int beginRow, int rowPerPage, String searchQnaTiitle, String searchQnaCategory) => " + i + "번째 qna 작성자 넘버 :" + m.getMemberNo());
			System.out.println("[debug] QnakDao.selectQnaList(int beginRow, int rowPerPage, String searchQnaTiitle, String searchQnaCategory) => " + i + "번째 qna 작성자 이름 :" + m.getMemberName());
			System.out.println("[debug] QnakDao.selectQnaList(int beginRow, int rowPerPage, String searchQnaTiitle, String searchQnaCategory) => " + i + "번째 qna 작성자 아이디 :" + m.getMemberId());
			
			//QnaMember 객체에 회원 객체 담기
			qnaMember.setMember(m);
			
			// 반환할 리스트에 QnaMember 객체 담기
			qnaMemberList.add(qnaMember);
			
			i += 1;
			
		}
		
		// DB 자원 해제
		rs.close();
		stmt.close();
		conn.close();
		return null;
	}
	
	/*[회원, 비회원, 관리자] qna 개수 구하기*/
	// input: String => searchQnaTiitle, searchQnaCategory
	// output(success): int => CountQnaList
	// output(false): 0
	public int selectCountQnaList(String searchQnaTitle, String searchQnaCategory) throws SQLException, ClassNotFoundException {
		
		// 입력값 디버깅
		System.out.println("[debug] QnakDao.selectCountQnaList(String searchQnaTitle, String searchQnaCategory) => 입력받은 시작 행 : " + searchQnaTitle);
		System.out.println("[debug] QnakDao.selectCountQnaList(String searchQnaTitle, String searchQnaCategory) => 입력받은 페이지 당 행 : " + searchQnaCategory);
		
		// DB 자원 연결
		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();
		
		String sql = null;
		PreparedStatement stmt = null;
		
		/* 카테고리가 있고 없고에 따라(ALL or 특정 카테고리) 쿼리문을 다르게 구성 */
		// 입력받은 카테고리와 qna 제목이 없을 경우(전체검색)
		if ( (searchQnaCategory.equals("ALL")) && (searchQnaTitle.equals("ALL") || searchQnaTitle.equals("")) ) {
			sql = "SELECT COUNT(*) FROM qna q JOIN member m ON q.member_no=m.member_no";
			stmt = conn.prepareStatement(sql);
						
		// 입력받은 카테고리만 있을 경우
		} else if ( (!searchQnaCategory.equals("ALL")) && (searchQnaTitle.equals("ALL") || searchQnaTitle.equals(""))){
			sql = "SELECT COUNT(*) FROM qna q JOIN member m ON q.member_no=m.member_no WHERE q.qna_category=?";
			stmt = conn.prepareStatement(sql);
			
			// 쿼리문 세팅
			stmt.setString(1, searchQnaCategory);
		// 입력받은 qna 제목만 있을 경우	
		} else if ( (searchQnaCategory.equals("ALL")) && (!searchQnaTitle.equals("ALL") || !searchQnaTitle.equals(""))){
			sql = "SELECT COUNT(*) FROM qna q JOIN member m ON q.member_no=m.member_no WHERE q.qna_title LIKE ?";
			stmt = conn.prepareStatement(sql);
			
			// 쿼리문 세팅
			stmt.setString(1, "%" + searchQnaTitle + "%");
			
		// 입력받은 카테고리와 qna 제목이 있을 경우
		} else if ( (!searchQnaCategory.equals("ALL")) && (!searchQnaTitle.equals("ALL") || !searchQnaTitle.equals(""))){
			sql = "SELECT COUNT(*) FROM qna q JOIN member m ON q.member_no=m.member_no WHERE q.qna_category = ? AND q.qna_title LIKE ?";
			stmt = conn.prepareStatement(sql);
			
			// 쿼리문 세팅
			stmt.setString(1, searchQnaCategory);
			stmt.setString(2, "%" + searchQnaTitle + "%");
		}
		
		// 쿼리 디버깅
		System.out.println("[debug] QnakDao.selectCountQnaList(String searchQnaTitle, String searchQnaCategory) => 쿼리문 : " + stmt);
		
		// 쿼리 실행
		ResultSet rs = stmt.executeQuery();
		
		// 반환할 주문 개수를 담을 변수 생성
		int countQnaList = 0;
		
		if (rs.next()) {
			
			// 조회 결과 담기
			countQnaList = rs.getInt(1);
			
		}
		
		// DB 자원 해제
		rs.close();
		stmt.close();
		conn.close();
		
		return countQnaList;
	}
	
	/*[회원, 비회원, 관리자] qna 상세보기*/
	// input: Qna => qnaNo
	// output(success): QnaMember => Qna => qnaNo, qnaTiitle, qnaCategory, qnaContent, qna_secret,createDate, updateDate,
	//							  => Member => memberNo, memberName, memberId
	// output(false): QnaMember => null;
	public QnaMember selectQnaOne(Qna qna) {
		return null;
	}
	
	/*[회원] qna 등록*/
	// input: Qna => qnaTitle, qnaCategory, qnaContent, qna_secret, member_no
	// output(success): int => confirm
	// output(false): 0;
	public int insertQna(Qna qna) {
		return 0;
	}
	
	/*[회원] qna 수정*/
	// input: Qna => qnaNo, qnaTitle, qnaCategory, qnaContent, qna_secret, member_no
	// output(success): int => confirm
	// output(false): 0;
	public int updateQna(Qna qna) {
		return 0;
	}
	
	/* [회원] qna 삭제 */
	// input: Qna => qnaNo, member_no
	// output(success): int => confirm
	// output(false): 0;
	public int deleteQna(Qna qna) {
		return 0;
	}
	
}
