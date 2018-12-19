package service.calculator.utils;

import java.io.Serializable;

import org.springframework.data.redis.core.RedisHash;

import lombok.Getter;
import lombok.Setter;

@RedisHash("User")
public class User implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private Integer userid;
	
	@Getter
	@Setter
	private Long credits;
	
	@Getter
	@Setter
	private String email;

	public User() {}
	
	public User(Integer userid, Long credits, String email) {
		super();
		this.userid = userid;
		this.credits = credits;
		this.email = email;
	}
	
	@Override
    public boolean equals(final Object obj) {
       if (obj == null) {
           return false;
       }
       final User user = (User) obj;
       if (this == user) {
           return true;
       } else {
           return (this.userid.equals(user.userid) );
       }  
    }
	
    @Override
    public int hashCode() {
       int hashno = 7;
       hashno = 13 * hashno + (this.userid == null ? 0 : this.userid.hashCode());
       return hashno;
    }
	
}
