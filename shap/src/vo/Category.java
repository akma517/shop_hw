package vo;

public class Category {
	
	private String categoryName;
	private String createDate;
	private String updateDate;
	private String categoryState;
	
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public String getCategoryState() {
		return categoryState;
	}
	public void setCategoryState(String categoryState) {
		this.categoryState = categoryState;
	}
	@Override
	public String toString() {
		return "Category [categoryName=" + categoryName + ", createDate=" + createDate + ", updateDate=" + updateDate
				+ ", categoryState=" + categoryState + "]";
	}
	
}
