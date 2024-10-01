package telroseApp.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import telroseApp.model.User;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service
public class JwtService {


    @Value("${token.signing.key}")
    private String secretKey;


    public String extractUsername(String token)
    {
        return extractClaim(token,Claims::getSubject);
    }



    public String generateToken(UserDetails userDetails)
    {
        Map<String,Object> claims = new HashMap<>();
        if(userDetails instanceof User customUserDetails)
        {
            claims.put("id",customUserDetails.getId());
            claims.put("email",customUserDetails.getEmail());
            claims.put("role",customUserDetails.getRole());
        }
        return generateToken(claims,userDetails);
    }

    public boolean isTokenValid(String token,UserDetails userDetails)
    {
       final String username =  extractUsername(token);
       return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


    public String generateToken(Map<String,Object> extractClaims,UserDetails userDetails)
    {
        return Jwts
                .builder()
                .claims(extractClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 100000 * 60 * 24))
                .signWith(getSignatureKey(),Jwts.SIG.HS256)
                .compact();
    }



    private boolean isTokenExpired(String token)
    {
        return extractExpiration(token).before(new Date());
    }



    private Date extractExpiration(String token)
    {
        return extractClaim(token, Claims::getExpiration);
    }



    private <T> T extractClaim(String token, Function<Claims,T> claimsResolver)
    {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }



    private Claims extractAllClaims(String token)
    {
        return Jwts
                .parser()
                .verifyWith(getSignatureKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }



    private SecretKey getSignatureKey()
    {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

















}
