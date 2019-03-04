package JavaBeans;

import java.time.LocalDate;
//import java.util.Date;

public class Coupon {

    private String description;
    private String image;
    private String title;
    private int id;
    private int amount;
    private LocalDate start_date;
    private LocalDate end_date;
    private int company_id;
    private int category_id;
    private double price;
    
    
    //----------Setters & Getters-----------------------//
	public double getPrice() {
		return price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
		
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public LocalDate getStart_date() {
		return start_date;
	}
	public void setStart_date(LocalDate start_Date) {
		
		this.start_date = start_Date;
	}
	public LocalDate getEnd_date() {
		return end_date;
	}
	public void setEnd_date(LocalDate end_date) {
		this.end_date = end_date;
	}
	public int getCompany_id() {
		return company_id;
	}
	public void setCompany_id(int company_id) {
		this.company_id = company_id;
	}
	public int getCategory_id() {
		return category_id;
	}
	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}
    
	//-------------Constructors-----------------------------------------------//
	
	public Coupon(String description, String image, String title, int id, int amount, LocalDate start_date, LocalDate end_date,
			int company_id, int category_id, double price) {
		super();
		this.setDescription(description);
		this.setImage(image);
		this.setTitle(title);
		this.setId(id);
		this.setAmount(amount);
		this.setStart_date(start_date);
		this.setEnd_date(end_date);
		this.setCompany_id(company_id);
		this.setCategory_id(category_id);
		this.setPrice(price);
	}
    
}
