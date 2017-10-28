package john.bryce89.couponiumWeb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import core.beans.Coupon;
import core.beans.CouponType;

@XmlRootElement
public class WebCoupon implements Serializable {

	@Override
	public String toString() {
		return "WebCoupon [id=" + id + ", title=" + title + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", type=" + type + ", message=" + message + ", price=" + price + "]";
	}

	private long id;
	private String title;
	private Date startDate;
	private Date endDate;
	private int amount;
	private CouponType type;
	private String message;
	private double price;
	private String image = "http://lorempixel.com/400/200";

	public WebCoupon() {
	}

	/**
	 * This Method will allow me to convert coupons from web-part to usual JDBC
	 * used coupon
	 * 
	 * @return Coupon.class coupon
	 */
	public Coupon convertToCoupon() {
		Coupon hey = new Coupon();
		hey.setAmount(this.amount);
		hey.setEndDate(this.endDate);
		hey.setId(this.id);
		hey.setTitle(this.title);
		hey.setStartDate(this.startDate);
		hey.setType(this.type);
		hey.setMessage(this.message);
		hey.setPrice(this.price);
		hey.setImage(this.image);
		return hey;
	}

	/**
	 * This method is made to provide collections of usual coupons , JDBC
	 * usable. It is done by using convertToCoupon() method;
	 * 
	 * @return Collection with Coupon.class objects;
	 */
	public static Collection<Coupon> convertAlltoCoupons(Collection<WebCoupon> webcoups) {
		Collection<Coupon> resulter = new ArrayList<>();
		for (WebCoupon webc : webcoups) {
			resulter.add(webc.convertToCoupon());
		}
		return resulter;
	}

	/**
	 * CTOR which is made with conversion purpose. Does Coupon.class to
	 * WebCoupon.class conversion.
	 * 
	 * @param Coupon.class
	 *            object;
	 */
	public WebCoupon(Coupon coupon) {
		super();
		this.title = coupon.getTitle();
		this.startDate = coupon.getStartDate();
		this.endDate = coupon.getEndDate();
		this.amount = coupon.getAmount();
		this.type = coupon.getType();
		this.message = coupon.getMessage();
		this.price = coupon.getPrice();
		this.image = coupon.getImage();
		this.id = coupon.getId();
	}

	/**
	 * This method helps to convert collections of usual Coupons to WebCoupons
	 * collections.
	 * 
	 * @param Collection<Coupon>
	 * @return Collection<WebCoupon>
	 */
	public static Collection<WebCoupon> convertAlltoWebCoupons(Collection<Coupon> cops) {
		Collection<WebCoupon> webs = new ArrayList<>();
		for (Coupon coupon : cops) {
			webs.add(new WebCoupon(coupon));
		}
		return webs;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public CouponType getType() {
		return type;
	}

	public void setType(CouponType type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

}
