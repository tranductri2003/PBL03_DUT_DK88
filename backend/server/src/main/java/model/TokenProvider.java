package model;

import java.security.Key;
import java.util.Date;
import java.util.Map;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

public class TokenProvider {
	
	private static final String SECRET_STR = 
			"Anh thường hay vẩn vơ về một người "
			+ "Một người có ánh mắt biết cười "
			+ "Chỉ cần mơ cũng cảm thấy rụng rời.. "
			+ "Người mình yêu chưa chắc đã yêu mình "
			+ "Càng muốn hiểu càng chẳng thể hiểu "
			+ "Chạy theo ai mình muốn quan tâm nhiều "
			+ "Càng nhận lại ít bấy nhiêu "
			+ "Tìm được em người vốn đỗi tuyệt vời "
			+ "Tưởng rằng cứ thế sẽ ở bên một đời "
			+ "Nhưng chỉ anh muốn thôi biết làm sao em hỡi..";
	
	private static final long EXPIRATION_TIME = 1000 * 60 * 10; // 10 minutes
	
	public static String generateToken(Map<String, Object> data) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);
        
        return Jwts
        		.builder()
        		.setClaims(data)
        		.setIssuedAt(now)
        		.setExpiration(expiryDate)
        		.signWith(getSignKey(), SignatureAlgorithm.HS256)
        		.compact();
    }
	
	public static Map<String, Object> getDataFromToken(String token) {
		return Jwts
				.parserBuilder()
				.setSigningKey(getSignKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
	
	public static Boolean isValidToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			
		}
		return false;
	}
	
	private static Key getSignKey() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(DataHasher.hash(SECRET_STR)));
	}
	
}
