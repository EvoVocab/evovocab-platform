package com.evovocab.auth.infrastructure.adapter.in.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.evovocab.auth.application.port.in.RegisterByEmailCommand;
import com.evovocab.auth.application.port.in.RegisterUseCase;
import com.evovocab.auth.infrastructure.adapter.in.web.dto.RegisterRequest;
import com.evovocab.user.domain.exception.EmailAlreadyExistsException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.stream.Stream;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RegisterUseCase registerUseCase;

    private ResultActions performRegister(RegisterRequest request) throws Exception {
        return mockMvc.perform(
            post("/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );
    }

    private static final String VALID_FULL_NAME = "Nguyễn Văn A";
    private static final String VALID_EMAIL = "user@example.com";
    private static final String VALID_PASSWORD = "Password@123";

    static Stream<Arguments> invalidFullNames() {
        return Stream.of(
            // 1. Nhóm rỗng / khoảng trắng
            Arguments.of(null, "Họ và tên không được để trống", "null"),
            Arguments.of("", "Họ và tên không được để trống", "chuỗi rỗng"),
            Arguments.of("   ", "Họ và tên không được để trống", "chỉ chứa khoảng trắng"),
            Arguments.of("\t\n", "Họ và tên không được để trống", "chỉ chứa ký tự tab/xuống dòng"),

            // 2. Nhóm độ dài
            Arguments.of("A", "Họ và tên không hợp lệ", "1 ký tự"),
            Arguments.of("  A  ", "Họ và tên không hợp lệ", "1 ký tự kèm khoảng trắng thừa"),
            Arguments.of("A".repeat(101), "Họ và tên không hợp lệ", "vượt quá 100 ký tự (101 ký tự)"),

            // 3. Nhóm sai regex
            Arguments.of("Nguyễn Văn A1", "Họ và tên không hợp lệ", "chứa chữ số"),
            Arguments.of("12345", "Họ và tên không hợp lệ", "chỉ chứa chữ số"),
            Arguments.of("Nguyễn Văn_A", "Họ và tên không hợp lệ", "chứa dấu gạch dưới (_)"),
            Arguments.of("Nguyễn-Văn A", "Họ và tên không hợp lệ", "chứa dấu gạch nối (-)"),
            Arguments.of("Nguyễn Văn @", "Họ và tên không hợp lệ", "chứa ký tự đặc biệt (@)"),
            Arguments.of("Nguyễn Văn A.", "Họ và tên không hợp lệ", "chứa dấu chấm (.)"),
            Arguments.of("Nguyễn Văn A 😊", "Họ và tên không hợp lệ", "chứa emoji")
        );
    }

    static Stream<Arguments> invalidEmails() {
        return Stream.of(
            // 1. Nhóm rỗng / khoảng trắng
            Arguments.of(null, "Email không được để trống", "null"),
            Arguments.of("", "Email không được để trống", "chuỗi rỗng"),
            Arguments.of("   ", "Email không được để trống", "chỉ chứa khoảng trắng (sau khi trim thành rỗng)"),
            Arguments.of("\t\n", "Email không được để trống", "chỉ chứa ký tự tab/xuống dòng"),

            // 2. Nhóm sai định dạng
            Arguments.of("example.com", "Email không hợp lệ", "thiếu ký tự @"),
            Arguments.of("example@", "Email không hợp lệ", "thiếu phần domain sau @"),
            Arguments.of("@gmail.com", "Email không hợp lệ", "thiếu phần local-part trước @"),
            Arguments.of("user@@gmail.com", "Email không hợp lệ", "chứa hai ký tự @"),
            Arguments.of("user name@gmail.com", "Email không hợp lệ", "chứa khoảng trắng ở giữa"),
            Arguments.of("user@.com", "Email không hợp lệ", "domain bắt đầu bằng dấu chấm"),
            Arguments.of("user@gmail..com", "Email không hợp lệ", "domain chứa hai dấu chấm liên tiếp"),
            Arguments.of("user@-gmail.com", "Email không hợp lệ", "domain bắt đầu bằng dấu gạch nối"),
            Arguments.of("user@gmail.com-", "Email không hợp lệ", "domain kết thúc bằng dấu gạch nối"),

            // 3. Nhóm vượt quá độ dài
            Arguments.of(
                "a".repeat(64) + "@" + "b".repeat(187) + ".com",
                "Email không hợp lệ",
                "vượt quá 255 ký tự (256 ký tự, local-part 64 ký tự)"
            ),
            Arguments.of("a".repeat(244) + "@example.com", "Email không hợp lệ", "vượt quá 255 ký tự (256 ký tự)")
        );
    }

    static Stream<Arguments> invalidPasswords() {
        return Stream.of(
            // 1. Nhóm rỗng / khoảng trắng -> Khớp @NotBlank
            Arguments.of(null, "Mật khẩu không được để trống", "null"),
            Arguments.of("", "Mật khẩu không được để trống", "chuỗi rỗng"),
            Arguments.of("        ", "Mật khẩu không được để trống", "8 ký tự toàn khoảng trắng"),
            Arguments.of("\t\n\r ", "Mật khẩu không được để trống", "chỉ chứa ký tự tab/xuống dòng"),

            // 2. Nhóm độ dài -> Khớp @Size(min = 8, max = 72)
            Arguments.of("P@ssw0r", "Mật khẩu không hợp lệ", "dưới 8 ký tự (7 ký tự)"),
            Arguments.of(
                "P@ssw0rd" + "a".repeat(65),
                "Mật khẩu không hợp lệ",
                "vượt quá 72 ký tự (73 ký tự - vượt giới hạn BCrypt)"
            ),

            // 3. Nhóm thiếu thành phần ký tự bắt buộc -> Khớp @Pattern
            Arguments.of("p@ssw0rd123", "Mật khẩu không hợp lệ", "thiếu chữ cái in hoa (A-Z)"),
            Arguments.of("P@SSW0RD123", "Mật khẩu không hợp lệ", "thiếu chữ cái in thường (a-z)"),
            Arguments.of("P@sswordAbc", "Mật khẩu không hợp lệ", "thiếu chữ số (0-9)"),
            Arguments.of("Password123", "Mật khẩu không hợp lệ", "thiếu ký tự đặc biệt ([\\p{P}\\p{S}])"),

            // 4. Nhóm ký tự ngoài bảng mã ASCII (1 char != 1 byte) -> Khớp (?=\p{ASCII}+$)
            Arguments.of("P@ssw0rdÁ", "Mật khẩu không hợp lệ", "chứa ký tự tiếng Việt có dấu"),
            Arguments.of("P@ssw0rd😊", "Mật khẩu không hợp lệ", "chứa emoji"),
            Arguments.of("P@ssw0rd€", "Mật khẩu không hợp lệ", "chứa ký tự Unicode ngoài ASCII (€)"),

            // 5. Nhóm khoảng trắng ở đầu hoặc cuối -> Khớp (?!\s|.*\s$)
            Arguments.of(" P@ssw0rd", "Mật khẩu không hợp lệ", "có khoảng trắng ở đầu"),
            Arguments.of("P@ssw0rd ", "Mật khẩu không hợp lệ", "có khoảng trắng ở cuối"),
            Arguments.of("\tP@ssw0rd", "Mật khẩu không hợp lệ", "có ký tự tab ở đầu"),
            Arguments.of("P@ssw0rd\n", "Mật khẩu không hợp lệ", "có ký tự xuống dòng ở cuối")
        );
    }

    @Test
    @DisplayName("Đăng ký hợp lệ: trả 201 và chuyển dữ liệu đã chuẩn hóa tới use case")
    void registerWithRawDataReturnsNormalizedData() throws Exception {
        String rawFullName = "  nGuYễN   vĂn   a  ";
        String rawEmail = "  USER@Example.COM  ";
        String rawPassword = "Password@123";

        String expectedNormalizedFullName = "Nguyễn Văn A";
        String expectedNormalizedEmail = "user@example.com";

        given(registerUseCase.executeForEmail(any(RegisterByEmailCommand.class))).willReturn(expectedNormalizedEmail);

        performRegister(new RegisterRequest(rawFullName, rawEmail, rawPassword))
            .andExpect(status().isCreated())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.status").value(201))
            .andExpect(jsonPath("$.message").value("Đăng ký thành công"))
            .andExpect(jsonPath("$.data.email").value(expectedNormalizedEmail));

        // Được dùng để bắt lại các tham số có kiểu RegisterByEmailCommand
        ArgumentCaptor<RegisterByEmailCommand> captor = ArgumentCaptor.forClass(RegisterByEmailCommand.class);

        verify(registerUseCase).executeForEmail(captor.capture());

        RegisterByEmailCommand command = captor.getValue();

        assertEquals(expectedNormalizedFullName, command.fullName());
        assertEquals(expectedNormalizedEmail, command.email());
        // Test ở web layer chỉ kiểm tra raw password được truyền vào thành công, trong khi bcrypt được áp dụng tại service layer
        assertEquals(rawPassword, command.password());
    }

    @ParameterizedTest(name = "Từ chối fullName {2} -> \"{0}\"")
    @MethodSource("invalidFullNames")
    @DisplayName("Họ tên không hợp lệ trả về 422")
    void returnUnprocessableEntityWhenFullNameIsInvalid(
        String invalidFullName,
        String expectedMessage,
        String description
    ) throws Exception {
        ResultActions result = performRegister(new RegisterRequest(invalidFullName, VALID_EMAIL, VALID_PASSWORD));

        result
            .andExpect(status().isUnprocessableEntity())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
            .andExpect(jsonPath("$.type").value("about:blank"))
            .andExpect(jsonPath("$.title").value("Unprocessable Entity"))
            .andExpect(jsonPath("$.status").value(422))
            .andExpect(jsonPath("$.detail").value("Dữ liệu không hợp lệ."))
            .andExpect(jsonPath("$.instance").value("/v1/auth/register"))
            .andExpect(jsonPath("$.timestamp").isNumber())
            .andExpect(jsonPath("$.errors.fullName").isNotEmpty())
            .andExpect(jsonPath("$.errors.fullName").value(expectedMessage))
            .andExpect(jsonPath("$.errors", Matchers.aMapWithSize(1)));

        verifyNoInteractions(registerUseCase);
    }

    @ParameterizedTest(name = "Từ chối email {2} -> \"{0}\"")
    @MethodSource("invalidEmails")
    @DisplayName("Email không hợp lệ trả về 422")
    void returnUnprocessableEntityWhenEmailIsInvalid(String invalidEmail, String expectedMessage, String description)
        throws Exception {
        ResultActions result = performRegister(new RegisterRequest(VALID_FULL_NAME, invalidEmail, VALID_PASSWORD));

        result
            .andExpect(status().isUnprocessableEntity())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
            .andExpect(jsonPath("$.type").value("about:blank"))
            .andExpect(jsonPath("$.title").value("Unprocessable Entity"))
            .andExpect(jsonPath("$.status").value(422))
            .andExpect(jsonPath("$.detail").value("Dữ liệu không hợp lệ."))
            .andExpect(jsonPath("$.instance").value("/v1/auth/register"))
            .andExpect(jsonPath("$.timestamp").isNumber())
            .andExpect(jsonPath("$.errors.email").isNotEmpty())
            .andExpect(jsonPath("$.errors.email").value(expectedMessage))
            .andExpect(jsonPath("$.errors", Matchers.aMapWithSize(1)));

        verifyNoInteractions(registerUseCase);
    }

    @ParameterizedTest(name = "Từ chối password {2} -> \"{0}\"")
    @MethodSource("invalidPasswords")
    @DisplayName("Mật khẩu không hợp lệ trả về 422")
    void returnUnprocessableEntityWhenPasswordIsInvalid(
        String invalidPassword,
        String expectedMessage,
        String description
    ) throws Exception {
        ResultActions result = performRegister(new RegisterRequest(VALID_FULL_NAME, VALID_EMAIL, invalidPassword));

        result
            .andExpect(status().isUnprocessableEntity())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
            .andExpect(jsonPath("$.type").value("about:blank"))
            .andExpect(jsonPath("$.title").value("Unprocessable Entity"))
            .andExpect(jsonPath("$.status").value(422))
            .andExpect(jsonPath("$.detail").value("Dữ liệu không hợp lệ."))
            .andExpect(jsonPath("$.instance").value("/v1/auth/register"))
            .andExpect(jsonPath("$.timestamp").isNumber())
            .andExpect(jsonPath("$.errors.password").isNotEmpty())
            .andExpect(jsonPath("$.errors.password").value(expectedMessage))
            .andExpect(jsonPath("$.errors", Matchers.aMapWithSize(1)));

        verifyNoInteractions(registerUseCase);
    }

    @Test
    @DisplayName("Trả về 409 Problem Detail khi email đã tồn tại")
    void returnConflictWhenEmailAlreadyExists() throws Exception {
        given(registerUseCase.executeForEmail(any(RegisterByEmailCommand.class))).willThrow(
            new EmailAlreadyExistsException()
        );

        performRegister(new RegisterRequest(VALID_FULL_NAME, VALID_EMAIL, VALID_PASSWORD))
            .andExpect(status().isConflict())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
            .andExpect(jsonPath("$.type").value("about:blank"))
            .andExpect(jsonPath("$.title").value("Conflict"))
            .andExpect(jsonPath("$.status").value(409))
            .andExpect(jsonPath("$.detail").value("Email đã tồn tại"))
            .andExpect(jsonPath("$.instance").value("/v1/auth/register"))
            .andExpect(jsonPath("$.timestamp").isNumber());

        verify(registerUseCase).executeForEmail(any(RegisterByEmailCommand.class));
    }
}
