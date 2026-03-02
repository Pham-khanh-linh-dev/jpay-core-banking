package com.jpay.core_banking.service;

import com.jpay.core_banking.dto.request.AuthenticationRequest;
import com.jpay.core_banking.dto.request.LogoutRequest;
import com.jpay.core_banking.dto.request.RefreshRequest;
import com.jpay.core_banking.dto.response.AuthenticationResponse;
import com.jpay.core_banking.entity.InvalidatedToken;
import com.jpay.core_banking.entity.User;
import com.jpay.core_banking.exception.AppException;
import com.jpay.core_banking.exception.ErrorCode;
import com.jpay.core_banking.repository.InvalidatedTokenRepository;
import com.jpay.core_banking.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    InvalidatedTokenRepository invalidatedTokenRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    private String signerKey;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    private long REFRESHABLE_DURATION;

    public AuthenticationResponse authenticate(AuthenticationRequest request){
        System.out.println(request.getUsername());
        var user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED_ERROR));

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if(!authenticated) throw new AppException(ErrorCode.UNAUTHENTICATED_EXCEPTION);

        return AuthenticationResponse.builder().token(generateToken(user)).authenticated(true).build();
    }

    private String generateToken(User user){
        JWSHeader header = new JWSHeader( JWSAlgorithm.HS256);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("jpay.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("userID", user.getId())
                .jwtID(UUID.randomUUID().toString())
                .build();
        Payload payload  = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try{
            jwsObject.sign(new MACSigner(signerKey.getBytes()));
            return jwsObject.serialize();
        }catch(JOSEException e){
            throw new RuntimeException(e);
        }
    }

    public void logout(LogoutRequest request){
        try{
            SignedJWT signedJWT = verifyToken(request.getToken(), true);

            String jit = signedJWT.getJWTClaimsSet().getJWTID();
            Date expireTime = signedJWT.getJWTClaimsSet().getExpirationTime();


            invalidatedTokenRepository.save(InvalidatedToken.builder()
                    .jit(jit)
                    .expireTime(expireTime)
                    .build());
        } catch(AppException | ParseException | JOSEException e){
            throw new AppException(ErrorCode.UNAUTHENTICATED_EXCEPTION);
        }
    }

    public SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(signerKey.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        if(!signedJWT.verify(verifier)) throw new AppException(ErrorCode.UNAUTHENTICATED_EXCEPTION);

        Date expireTime = (isRefresh)
                ? new Date(signedJWT.getJWTClaimsSet().getIssueTime().toInstant().plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS).toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        if(expireTime.before(new Date())) throw new AppException(ErrorCode.UNAUTHENTICATED_EXCEPTION);

        if(invalidatedTokenRepository.existsByJit(signedJWT.getJWTClaimsSet().getJWTID())) throw new AppException(ErrorCode.UNAUTHENTICATED_EXCEPTION);

        return signedJWT;
    }

    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        SignedJWT signedJWT = verifyToken(request.getToken(), true);

        String jit = signedJWT.getJWTClaimsSet().getJWTID();
        Date expireTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        var invalidatedToken = InvalidatedToken.builder()
                .jit(jit)
                .expireTime(expireTime)
                .build();

        var user = userRepository.findByUsername(signedJWT.getJWTClaimsSet().getSubject()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED_ERROR));
        invalidatedTokenRepository.save(invalidatedToken);

        return AuthenticationResponse.builder()
                .authenticated(true)
                .token(generateToken(user))
                .build();
    }










//    CHuyển sang dùng oauth2 Resource Server nên không cần introspec thủ công nữa
//    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
//        var token = request.getToken();
//
//        // 1. Tạo bộ xác thực (Verifier) dựa trên khóa bí mật của bạn
//        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
//
//        // 2. Parse cái token người dùng gửi lên
//        SignedJWT signedJWT = SignedJWT.parse(token);
//
//        // 3. Kiểm tra xem token này có đúng chữ ký không (có bị sửa đổi không)
//        boolean verified = signedJWT.verify(verifier);
//
//        // 4. Kiểm tra xem token đã hết hạn chưa
//        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
//        boolean isNotExpired = expiryTime.after(new Date());
//
//        return IntrospectResponse.builder()
//                .valid(verified && isNotExpired) // Hợp lệ khi cả chữ ký đúng và chưa hết hạn
//                .build();
//    }
}
