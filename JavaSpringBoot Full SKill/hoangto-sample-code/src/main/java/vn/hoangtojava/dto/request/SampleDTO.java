package vn.hoangtojava.dto.request;

import lombok.*;

import java.io.Serializable;
import java.security.cert.CertPathBuilder;

@AllArgsConstructor(access = AccessLevel.MODULE)
public class SampleDTO implements Serializable {

    private  Integer id;
    private String name;



}
