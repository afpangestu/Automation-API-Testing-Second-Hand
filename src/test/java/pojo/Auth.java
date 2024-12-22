package pojo;

public class Auth{
	private AuthItem authItem;

	public void setUser(AuthItem authItem){
		this.authItem = authItem;
	}

	public AuthItem getUser(){
		return authItem;
	}
}
