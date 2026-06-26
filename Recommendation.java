package classes;

public class Recommendation {
	
	private int user_id;
	private int recommendation_id;
	private int car_recommended_id;
	private int score;
	public Recommendation(int user_id, int recommendation_id, int car_recommended_id, int score) {
		super();
		this.user_id = user_id;
		this.recommendation_id = recommendation_id;
		this.car_recommended_id = car_recommended_id;
		this.score = score;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public int getRecommendation_id() {
		return recommendation_id;
	}
	public void setRecommendation_id(int recommendation_id) {
		this.recommendation_id = recommendation_id;
	}
	public int getCar_recommended_id() {
		return car_recommended_id;
	}
	public void setCar_recommended_id(int car_recommended_id) {
		this.car_recommended_id = car_recommended_id;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}






	

}
