package com.evovocab.auth.infrastructure.adapter.in.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.evovocab.auth.application.service.RegisterByEmailService;
import com.evovocab.auth.infrastructure.adapter.in.web.dto.RegisterRequest;
import com.evovocab.shared.filters.ProblemDetailsFilter;
import com.evovocab.user.application.service.CreateUserAccountService;
import com.evovocab.user.domain.model.UserAccount;
import com.evovocab.user.domain.repository.UserAccountRepository;
import com.evovocab.user.infrastructure.adapter.out.UserFacadeAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class AuthControllerTest {

    private static final String FULL_NAME = "Nguyễn Văn A";
    private static final String EMAIL = "test@example.com";
    private static final String PASSWORD = "P@ssword123";
    private static final String HASH = new BCryptPasswordEncoder(BCryptVersion.$2B, 10).encode(PASSWORD);

    private final ObjectMapper objectMapper = new ObjectMapper();
    private UserAccountRepository accountRepository;
    private PasswordEncoder passwordEncoder;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        accountRepository = mock(UserAccountRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);

        // Mặc định email chưa tồn tại, encoder trả hash hợp lệ và repository trả tài khoản vừa lưu.
        when(accountRepository.existsByEmail(EMAIL)).thenReturn(false);
        when(passwordEncoder.encode(PASSWORD)).thenReturn(HASH);
        when(accountRepository.save(any(UserAccount.class))).thenAnswer((invocation) -> invocation.getArgument(0));

        CreateUserAccountService userService = new CreateUserAccountService(accountRepository, passwordEncoder);
        RegisterByEmailService registerService = new RegisterByEmailService(new UserFacadeAdapter(userService));
        mockMvc = MockMvcBuilders.standaloneSetup(new AuthController(registerService))
            .setControllerAdvice(new ProblemDetailsFilter())
            .build();
    }

    @Test
    @DisplayName("Case 1: Happy path")
    void happyPath() throws Exception {
        register(new RegisterRequest(FULL_NAME, EMAIL, PASSWORD))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.email").value(EMAIL))
            .andExpect(jsonPath("$.password").doesNotExist());

        verify(passwordEncoder).encode(PASSWORD);
        verify(accountRepository).save(any(UserAccount.class));
    }

    @Test
    @DisplayName("Case 2: Trùng email")
    void duplicateEmail() throws Exception {
        when(accountRepository.existsByEmail(EMAIL)).thenReturn(true);

        register(new RegisterRequest(FULL_NAME, EMAIL, PASSWORD))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.detail").value("Email đã tồn tại"));

        verifyNoInteractions(passwordEncoder);
        verify(accountRepository, never()).save(any(UserAccount.class));
    }

    @Test
    @DisplayName("Case 3: Full name hợp lệ, trống, không hợp lệ")
    void fullNameValidation() throws Exception {
        // Hợp lệ: chứa chữ cái tiếng Việt và khoảng trắng.
        register(new RegisterRequest("Nguyễn Văn A", EMAIL, PASSWORD)).andExpect(status().isCreated());

        // Trống.
        register(new RegisterRequest("", EMAIL, PASSWORD))
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.errors.fullName").isNotEmpty());

        // Không hợp lệ: chứa chữ số.
        register(new RegisterRequest("Nguyễn Văn A1", EMAIL, PASSWORD))
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.errors.fullName").value("Họ và tên không hợp lệ"));
    }

    @Test
    @DisplayName("Case 4: Email hợp lệ, trống, không hợp lệ")
    void emailValidation() throws Exception {
        // Hợp lệ.
        register(new RegisterRequest(FULL_NAME, "test@example.com", PASSWORD)).andExpect(status().isCreated());

        // Trống.
        register(new RegisterRequest(FULL_NAME, "", PASSWORD))
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.errors.email").isNotEmpty());

        // Không hợp lệ: thiếu ký tự @.
        register(new RegisterRequest(FULL_NAME, "testexample.com", PASSWORD))
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.errors.email").value("Email không hợp lệ"));
    }

    @Test
    @DisplayName("Case 5: Password hợp lệ, trống, không hợp lệ")
    void passwordValidation() throws Exception {
        // Hợp lệ: đủ chữ hoa, chữ thường, số và ký tự đặc biệt.
        register(new RegisterRequest(FULL_NAME, EMAIL, "P@ssword123")).andExpect(status().isCreated());

        // Trống.
        register(new RegisterRequest(FULL_NAME, EMAIL, ""))
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.errors.password").isNotEmpty());

        // Không hợp lệ: thiếu ký tự đặc biệt.
        register(new RegisterRequest(FULL_NAME, EMAIL, "Password123"))
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.errors.password").value("Mật khẩu không hợp lệ"));
    }

    // Gửi JSON tới API bằng MockMvc, không cần khởi động server.
    private ResultActions register(RegisterRequest request) throws Exception {
        return mockMvc.perform(
            post("/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request))
        );
    }
}
