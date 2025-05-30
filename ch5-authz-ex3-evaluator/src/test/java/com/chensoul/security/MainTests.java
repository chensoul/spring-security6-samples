package com.chensoul.security;

import com.chensoul.security.model.Document;
import com.chensoul.security.service.DocumentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class MainTests {

    @Autowired
    private DocumentService documentService;

    @Test
    void testDocumentServiceWithNoUser() {
        assertThrows(AuthenticationException.class,
                () -> documentService.getDocument("abc123"));
    }

    @Test
    @WithMockUser(username = "emma", roles = "manager")
    void testDocumentServiceWithManagerRole() {
        assertThrows(AccessDeniedException.class,
                () -> documentService.getDocument("abc123"));
    }

    @Test
    @WithMockUser(username = "emma", roles = "manager")
    void testDocumentServiceWithManagerRoleForOwnUserDocument() {
        var result = documentService.getDocument("asd555");
        var expected = new Document("emma");

        assertEquals(expected, result);
    }

    @Test
    @WithMockUser(username = "natalie", roles = "admin")
    void testDocumentServiceWithAdminRole() {
        var result = documentService.getDocument("asd555");
        var expected = new Document("emma");

        assertEquals(expected, result);
    }
}
