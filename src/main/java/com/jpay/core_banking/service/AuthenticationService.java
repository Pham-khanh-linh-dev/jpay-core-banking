package com.jpay.core_banking.service;

import com.jpay.core_banking.dto.request.AuthenticationRequest;
import com.jpay.core_banking.dto.response.AuthenticationResponse;
import com.jpay.core_banking.entity.User;
import com.jpay.core_banking.exception.AppException;
import com.jpay.core_banking.exception.ErrorCode;
import com.jpay.core_banking.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.control.NoComplexMapping;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static java.rmi.server.LogStream.log;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String signerKey;

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
                .issueTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("userID", user.getId())
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
