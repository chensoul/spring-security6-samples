package com.chensoul.security.service;

import java.util.List;
import java.util.Map;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class NameService {

    private Map<String, List<String>> secretNames = Map.of(
            "natalie", List.of("Energico", "Perfecto"),
            "emma", List.of("Fantastico"));

    //    @PreAuthorize("hasAuthority('read')")
    @PreAuthorize("#name == authentication.principal.username")
    @PostAuthorize("returnObject.contains('emma')")
    public List<String> getSecretNames(String name) {
        return secretNames.get(name);
    }

    @PreAuthorize("hasAuthority('write')")
    public String getName() {
        return "Fantastico";
    }
}
