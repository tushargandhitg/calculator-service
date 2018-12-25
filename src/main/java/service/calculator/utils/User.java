package service.calculator.utils;

import java.io.Serializable;

import org.springframework.data.redis.core.RedisHash;

@RedisHash("User")
public class User implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer userid;
	
	private Double credits;
	
	private String email;

	public User() {}
	
	public User(Integer userid, Double credits, String email) {
		super();
		this.userid = userid;
		this.credits = credits;
		this.email = email;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public Double getCredits() {
		return credits;
	}

	public void setCredits(Double credits) {
		this.credits = credits;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
	
}
